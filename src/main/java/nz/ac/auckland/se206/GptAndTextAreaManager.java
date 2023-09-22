package nz.ac.auckland.se206;

import java.io.File;
import java.util.List;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
import nz.ac.auckland.se206.speech.TextToSpeech;

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
    private static TextToSpeech textToSpeech;

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
        displayTarget(Characters.PRISONER_TWO);
    }

    private static String getMessageHistory(ChatCompletionRequest chat) {
        String result = "";
        List<ChatMessage> messages = chat.getMessages();
        // IMPORTANT: increase i here to filtout the prompt Engineering content

        if (messages.size() > 1) {

            for (int i = 1; i < messages.size(); i++) {
                if (messages.get(i).getRole().equals("assistant")
                        && messages.get(i).getContent().contains("Correct")) {
                    GameState.setRiddleResolved(true);
                }
                String name = messages.get(i).getRole();
                if (name.trim().equals("assistant")) {
                    if (currentCharacter == Characters.GUARD) {
                        name = "";
                    } else if (currentCharacter == Characters.PRISONER_ONE) {
                        name = "Prisoner1: ";
                    } else {
                        name = "Prisoner2: ";
                    }
                } else if (name.trim().equals("user")) {
                    name = GameState.playerName + ": ";
                }
                result += name + parenthesesFilter(chat.getMessages().get(i).getContent()) + "\n\n";
                System.out.println(messages.get(i).getRole() + ": " + chat.getMessages().get(i).getContent() + "\n\n");
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
        textAreaChatDisplayBoard.setScrollTop(Double.MAX_VALUE);
    }

    private static String parenthesesFilter(String input){
        String result = "";
        if (input.contains("(") && input.contains(")")) {
            result += result.substring(0, input.indexOf("("));
            result += result.substring(input.indexOf(")") + 1);
        }
        return result;
    }
    public static void sendMessage(String message) throws ApiProxyException {

        boolean ifSpeak = false;

        if (currentCharacter == Characters.GUARD) {
            if (GameState.isRiddleResolved() == false) {
                message = message + "? " + "(riddle unsolved)";    
            } else if (GameState.wordFound == false) {
                message = message + "? " + "(riddle solved)";
            } else if (GameState.cypherFound == false) {
                message = message + "? " + "(word found)";
            } else if (GameState.safeFound == false) {
                message = message + "? " + "(cypher found)";
            } else if (GameState.safeUnlocked == false) {
                message = message + "? " + "(safe found)";
            }
            guardChatCompletionRequest.addMessage(new ChatMessage("user", message));
            runGpt(guardChatCompletionRequest);
            if (guardChatCompletionRequest.getMessages().size() > 2) {
                ifSpeak = true;
            }
        } else if (currentCharacter == Characters.PRISONER_ONE) {
            prisonerOneCompletionRequest.addMessage(new ChatMessage("user", message));
            runGpt(prisonerOneCompletionRequest);
            if (prisonerOneCompletionRequest.getMessages().size() > 2) {
                ifSpeak = true;
            }
        } else {
            prisonerTwoCompletionRequest.addMessage(new ChatMessage("user", message));
            runGpt(prisonerTwoCompletionRequest);
            if (prisonerTwoCompletionRequest.getMessages().size() > 2) {
                ifSpeak = true;
            }
        }
        if (ifSpeak) {
            // textToSpeech = new TextToSpeech();
            // textToSpeech.speak(message);
            String soundEffect;
            if (currentCharacter == Characters.GUARD) {
                soundEffect = "src/main/resources/sounds/HmmSoundEffect1.mp3";
            } else if (currentCharacter == Characters.PRISONER_ONE) {
                soundEffect = "src/main/resources/sounds/HmmSoundEffect2.mp3";
            } else {
                soundEffect = "src/main/resources/sounds/HmmSoundEffect3.mp3";
            }
            Media media = new Media(new File(soundEffect).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        }
    }

    private static void runGpt(ChatCompletionRequest chatCompletionRequest) throws ApiProxyException {
        // run the GPT model in a background thread
        Task<Void> backgroundTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (currentCharacter == Characters.GUARD) {
                    cafeteriaController.setThinkingThreeUp();
                } else if (currentCharacter == Characters.PRISONER_ONE) {
                    roomController.setThinkingOneUp();
                    cafeteriaController.setThinkingOneUp();
                    officeController.setThinkingOneUp();
                } else if (currentCharacter == Characters.PRISONER_TWO) {
                    roomController.setThinkingTwoUp();
                    cafeteriaController.setThinkingTwoUp();
                    officeController.setThinkingTwoUp();
                }
                try {
                    ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
                    Choice result = chatCompletionResult.getChoices().iterator().next();
                    chatCompletionRequest.addMessage(result.getChatMessage());
                    displayTarget(currentCharacter);
                    roomController.setThinkingTwoDown();
                    cafeteriaController.setThinkingTwoDown();
                    officeController.setThinkingTwoDown();
                    roomController.setThinkingOneDown();
                    cafeteriaController.setThinkingOneDown();
                    officeController.setThinkingOneDown();
                    cafeteriaController.setThinkingThreeDown();
                    return null;
                } catch (ApiProxyException e) {
                    ChatMessage error = new ChatMessage(
                            "assistant",
                            "Error: \n"
                                    + "GPT not working");
                    chatCompletionRequest.addMessage(error);
                    displayTarget(currentCharacter);
                    e.printStackTrace();
                    roomController.setThinkingTwoDown();
                    cafeteriaController.setThinkingTwoDown();
                    officeController.setThinkingTwoDown();
                    roomController.setThinkingOneDown();
                    cafeteriaController.setThinkingOneDown();
                    officeController.setThinkingOneDown();
                    cafeteriaController.setThinkingThreeDown();
                    return null;
                }

            }

        };
        Thread gptThread = new Thread(null, backgroundTask);
        gptThread.start();
    }
}
