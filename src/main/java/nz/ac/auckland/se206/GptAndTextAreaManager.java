package nz.ac.auckland.se206;

import java.util.List;

import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.controllers.CafeteriaController;
import nz.ac.auckland.se206.controllers.OfficeController;
import nz.ac.auckland.se206.controllers.RoomController;
import nz.ac.auckland.se206.controllers.TextAreaController;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class GptAndTextAreaManager {
    public enum Characters {
        PRISONER_ONE,
        PRISONER_TWO,
        GUARD
    }

    static Characters currentCharacter = Characters.GUARD;

    public static ChatCompletionRequest guardChatCompletionRequest = new ChatCompletionRequest().setN(1)
            .setTemperature(0.2).setTopP(0.5).setMaxTokens(50);
    public static ChatCompletionRequest prisonerOneCompletionRequest = new ChatCompletionRequest().setN(1)
            .setTemperature(0.2).setTopP(0.5).setMaxTokens(50);
    public static ChatCompletionRequest prisonerTwoCompletionRequest = new ChatCompletionRequest().setN(1)
            .setTemperature(0.2).setTopP(0.5).setMaxTokens(50);

    // These fields are initialized in the initialize method of the controller
    public static RoomController roomController;
    public static CafeteriaController cafeteriaController;
    public static OfficeController officeController;
    public static TextAreaController textAreaController;

    public static Text textAreaTypePromptText;
    public static TextArea textAreaChatDisplayBoard;
    public static TextArea textAreaInputBox;
    public static TextArea textAreaObjectiveDisplayBoard;

    public static boolean isGptRunning = false;

    /*
     * this method outputs MessageHistory as a string which can be put into display
     * board
     * 
     * @param chat the ChatCompletionRequest for the chat history
     */
    public static void initialize() throws ApiProxyException {
        sendMessage(GptPromptEngineering.getGuardSetUp(GameState.itemToChoose.getId(), GameState.numHints));
        currentCharacter = Characters.PRISONER_ONE;
        sendMessage(GptPromptEngineering.getPrisonerOneSetUp());
        currentCharacter = Characters.PRISONER_TWO;
        sendMessage(GptPromptEngineering.getPrisonerTwoSetUp());
        displayTarget(Characters.GUARD);
    }

    private static String getMessageHistory(ChatCompletionRequest chat) {
        String result = "";
        List<ChatMessage> messages = chat.getMessages();
        // IMPORTANT: increase i here to filtout the prompt Engineering content

        if (messages.size() > 1) {
          for (int i = 1; i < messages.size(); i++) {
            if (messages.get(i).getRole().equals("assistant") && messages.get(i).getContent().contains("Correct")) {
                GameState.setRiddleResolved(true);
            }
              result += messages.get(i).getRole() + ": " + chat.getMessages().get(i).getContent() + "\n\n";
          }

        }
        return result;
    }

    /*
     * this method is called when the user switches the target character or the
     * message needs to be update
     * 
     * @param character the character to display
     */
    public static void displayTarget(Characters character) {
        String prompt = null;
        String chatHistory = null;
        if (character == Characters.GUARD) {
            currentCharacter = Characters.GUARD;
            prompt = "Type here to talk to the guard";
            chatHistory = getMessageHistory(guardChatCompletionRequest);
            System.out.println("display Guard history");
        } else if (character == Characters.PRISONER_ONE) {
            currentCharacter = Characters.PRISONER_ONE;
            prompt = "Type here to talk to prisoner1";
            chatHistory = getMessageHistory(prisonerOneCompletionRequest);
            System.out.println("display Prisoner1 history");
        } else {
            currentCharacter = Characters.PRISONER_TWO;
            prompt = "Type here to talk to prisoner2";
            chatHistory = getMessageHistory(prisonerTwoCompletionRequest);
            System.out.println("display Prisoner2 history");
        }
        textAreaTypePromptText.setText(prompt);
        textAreaChatDisplayBoard.setText(chatHistory);
    }

    public static void sendMessage(String message) throws ApiProxyException {
        
        if (currentCharacter == Characters.GUARD) {
            if (GameState.isRiddleResolved() == false) {
                message = message + "? " + "riddle unsolved";    
            } else if (GameState.wordFound == false) {
                message = message + "? " + "riddle solved";
            } else if (GameState.cypherFound == false) {
                message = message + "? " + "word found";
            } else if (GameState.safeFound == false) {
                message = message + "? " + "cypher found";
            } else if (GameState.safeUnlocked == false) {
                message = message + "? " + "safe found";
            }
            guardChatCompletionRequest.addMessage(new ChatMessage("user", message));
            runGpt(guardChatCompletionRequest);
        } else if (currentCharacter == Characters.PRISONER_ONE) {
            prisonerOneCompletionRequest.addMessage(new ChatMessage("user", message));
            runGpt(prisonerOneCompletionRequest);
        } else {
            prisonerTwoCompletionRequest.addMessage(new ChatMessage("user", message));
            runGpt(prisonerTwoCompletionRequest);
        }
    }

    private static void runGpt(ChatCompletionRequest chatCompletionRequest) throws ApiProxyException {
        // run the GPT model in a background thread
        Task<Void> backgroundTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
                    Choice result = chatCompletionResult.getChoices().iterator().next();
                    chatCompletionRequest.addMessage(result.getChatMessage());
                    displayTarget(currentCharacter);
                    return null;
                } catch (ApiProxyException e) {
                    ChatMessage error = new ChatMessage(
                            "assistant",
                            "Error: \n"
                                    + "GPT not working");
                    chatCompletionRequest.addMessage(error);
                    displayTarget(currentCharacter);
                    e.printStackTrace();
                    return null;
                }

            }
        };
        Thread gptThread = new Thread(null, backgroundTask);
        gptThread.start();
    }
}
