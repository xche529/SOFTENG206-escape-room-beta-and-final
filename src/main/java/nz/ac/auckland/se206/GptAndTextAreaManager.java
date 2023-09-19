package nz.ac.auckland.se206;

import java.util.List;

import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.controllers.CafeteriaController;
import nz.ac.auckland.se206.controllers.OfficeController;
import nz.ac.auckland.se206.controllers.RoomController;
import nz.ac.auckland.se206.gpt.ChatMessage;
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

    Characters currentCharacter = Characters.GUARD;

    public static ChatCompletionRequest guardChatCompletionRequest;
    public static ChatCompletionRequest prisonerOneCompletionRequest;
    public static ChatCompletionRequest prisonerTwoCompletionRequest;

    // These fields are initialized in the initialize method of the controller
    public static RoomController roomController;
    public static CafeteriaController cafeteriaController;
    public static OfficeController officeController;

    public static Text roomTypePromptText;
    public static Text officeTypePromptText;
    public static Text cafeteriaTypePromptText;

    public static TextArea roomChatDisplayBoard;
    public static TextArea officeChatDisplayBoard;
    public static TextArea cafeteriaChatDisplayBoard;

    public static TextArea roomInputBox;
    public static TextArea officeInputBox;
    public static TextArea cafeteriaInputBox;

    public static TextArea roomObjectiveDisplayBoard;
    public static TextArea officeObjectiveDisplayBoard;
    public static TextArea cafeteriaObjectiveDisplayBoard;

    public static boolean isGptRunning = false;

    /*
     * this method outputs MessageHistory as a string which can be put into display
     * board
     * 
     * @param chat the ChatCompletionRequest for the chat history
     */
    private static String getMessageHistory(ChatCompletionRequest chat) {
        String result = "";
        List<ChatMessage> messages = chat.getMessages();
        for (int i = 0; i < messages.size(); i++) {
            result += messages.get(i).getRole() + ": " + chat.getMessages().get(i).getContent() + "\n\n";
        }
        return result;
    }

    /*
     * this method is called when the user switches the target character or the
     * message needs to be update
     * 
     * @param character the character to display
     */
    public void displayTarget(Characters character) {
        String prompt = null;
        String chatHistory = null;
        if (character == Characters.GUARD) {
            currentCharacter = Characters.GUARD;
            prompt = "Type here to talk to the guard";
            chatHistory = getMessageHistory(guardChatCompletionRequest);
            roomChatDisplayBoard.setText(chatHistory);
            officeChatDisplayBoard.setText(chatHistory);
            cafeteriaChatDisplayBoard.setText(chatHistory);
        } else if (character == Characters.PRISONER_ONE) {
            currentCharacter = Characters.PRISONER_ONE;
            prompt = "Type here to talk to prisoner1";
            chatHistory = getMessageHistory(prisonerOneCompletionRequest);
        } else {
            currentCharacter = Characters.PRISONER_TWO;
            prompt = "Type here to talk to prisoner2";
            chatHistory = getMessageHistory(prisonerTwoCompletionRequest);
        }
        roomTypePromptText.setText(prompt);
        officeTypePromptText.setText(prompt);
        cafeteriaTypePromptText.setText(prompt);
    }

    public void sendMessage(String message) throws ApiProxyException {
        if (currentCharacter == Characters.GUARD) {
            guardChatCompletionRequest.addMessage(new ChatMessage("You", message));
            runGpt(guardChatCompletionRequest);
        } else if (currentCharacter == Characters.PRISONER_ONE) {
            prisonerOneCompletionRequest.addMessage(new ChatMessage("You", message));
            runGpt(prisonerOneCompletionRequest);
        } else {
            prisonerTwoCompletionRequest.addMessage(new ChatMessage("You", message));
            runGpt(prisonerTwoCompletionRequest);
        }
    }

    private void runGpt(ChatCompletionRequest chatCompletionRequest) throws ApiProxyException {
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
