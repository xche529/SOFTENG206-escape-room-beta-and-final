package nz.ac.auckland.se206;

import java.util.List;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.controllers.CafeteriaController;
import nz.ac.auckland.se206.controllers.OfficeController;
import nz.ac.auckland.se206.controllers.RoomController;
import nz.ac.auckland.se206.controllers.SideConversationController;
import nz.ac.auckland.se206.controllers.TextAreaController;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/** This class is used to manage the GPT model and the text area. */
public class GptAndTextAreaManager {

  /** This enum is used to keep track of the current character. */
  public enum Characters {
    PRISONER_ONE,
    PRISONER_TWO,
    GUARD
  }

  public static Characters currentCharacter = Characters.GUARD;

  public static ChatCompletionRequest guardChatCompletionRequest =
      new ChatCompletionRequest().setN(1).setTemperature(0.1).setTopP(0.5).setMaxTokens(100);
  public static ChatCompletionRequest prisonerOneCompletionRequest =
      new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(50);
  public static ChatCompletionRequest prisonerTwoCompletionRequest =
      new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(50);

  // These fields are initialized in the initialize method of the controller
  public static RoomController roomController;
  public static CafeteriaController cafeteriaController;
  public static OfficeController officeController;
  public static TextAreaController textAreaController;
  public static SideConversationController sideConversationController;

  public static Text textAreaTypePromptText;
  public static TextArea textAreaChatDisplayBoard;
  public static TextArea textAreaInputBox;
  public static TextArea textAreaObjectiveDisplayBoard;
  public static Text textAreaHintsLeftText;

  public static boolean isGptRunning = false;
  public static boolean isHintRunning = true;
  public static boolean isNewMessage = false;
  public static int hintLeft;

  /** this method outputs MessageHistory as a string which can be put into display board. */
  public static void initialize() throws ApiProxyException {
    // send initial messages to GPT
    sendMessage(GptPromptEngineering.getGuardSetUp(GameState.itemToChoose.getId()));
    currentCharacter = Characters.PRISONER_ONE;
    sendMessage(GptPromptEngineering.getPrisonerOneSetUp());
    currentCharacter = Characters.PRISONER_TWO;
    sendMessage(GptPromptEngineering.getPrisonerTwoSetUp());
    displayTarget(Characters.PRISONER_TWO);
  }

  /**
   * this method is called a new game is started.
   *
   * @throws ApiProxyException if the GPT model is not working
   */
  public static void reset() throws ApiProxyException {
    isHintRunning = true;
    // function for replayability and resetting conversations
    currentCharacter = Characters.GUARD;
    guardChatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.1).setTopP(0.5).setMaxTokens(100);
    prisonerOneCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(50);
    prisonerTwoCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(50);
    if (GameState.difficulty == GameState.Difficulty.MEDIUM) {
      GameState.hintsLeft = 5;
      hintLeft = 5;
    }
  }

  /**
   * this method is called when the user switches the target character or the message needs to be
   * updated.
   */
  public static void removeInputTextAreaFocus() {
    textAreaInputBox.getParent().requestFocus();
  }

  public static void displayTarget() {
    textAreaController.displayTarget(currentCharacter);
  }

  public static void displayTarget(Characters character) {
    textAreaController.displayTarget(character);
  }

  public static void setMessageHistory(ChatCompletionRequest chatCompletionRequest) {
    textAreaController.setMessageHistory(chatCompletionRequest);
  }

  public static void onSubmitMessage() throws ApiProxyException {
    textAreaController.submitMessage();
  }

  public static void setPlayerAvatar(Image image) {
    textAreaController.setPlayerAvatar(image);
  }

  public static String getRiddle() throws ApiProxyException {
    List<ChatMessage> messages = guardChatCompletionRequest.getMessages();
    return messages.get(1).getContent();
  }

  /**
   * This method sends a message to the GPT model and plays the sound effect.
   *
   * @param message the message to send to GPT
   * @throws ApiProxyException if the GPT model is not working
   */
  public static void sendMessage(String message) throws ApiProxyException {

    boolean ifSpeak = false;

    if (currentCharacter == Characters.GUARD) {

      // make new message and append
      guardChatCompletionRequest.addMessage(new ChatMessage("user", message));
      runGpt(guardChatCompletionRequest);
      if (guardChatCompletionRequest.getMessages().size() > 2) {
        ifSpeak = true;
      }
    } else if (currentCharacter == Characters.PRISONER_ONE) {
      // send to prisoner one
      prisonerOneCompletionRequest.addMessage(new ChatMessage("user", message));
      runGpt(prisonerOneCompletionRequest);
      if (prisonerOneCompletionRequest.getMessages().size() > 2) {
        ifSpeak = true;
      }
    } else {
      // send to prisoner two
      prisonerTwoCompletionRequest.addMessage(new ChatMessage("user", message));
      runGpt(prisonerTwoCompletionRequest);
      if (prisonerTwoCompletionRequest.getMessages().size() > 2) {
        ifSpeak = true;
      }
    }
    displayTarget(currentCharacter);
    if (ifSpeak) {
      if (currentCharacter
          == Characters.GUARD) { // plays the sound effect for the corresponding character
        SoundEffect guardNoise = new SoundEffect("src/main/resources/sounds/HmmSoundEffect1.mp3");
        guardNoise.playSfx();
      } else if (currentCharacter == Characters.PRISONER_ONE) {
        SoundEffect prisonerOneNoise =
            new SoundEffect("src/main/resources/sounds/HmmSoundEffect2.mp3");
        prisonerOneNoise.playSfx();
      } else {
        SoundEffect prisonerTwoNoise =
            new SoundEffect("src/main/resources/sounds/HmmSoundEffect3.mp3");
        prisonerTwoNoise.playSfx();
      }
    }
  }

  /**
   * This method runs the GPT model in a background thread.
   *
   * @param chatCompletionRequest the ChatCompletionRequest for the chat history
   * @throws ApiProxyException if the GPT model is not working
   */
  private static void runGpt(ChatCompletionRequest chatCompletionRequest) throws ApiProxyException {
    // run the GPT model in a background thread
    Task<Void> backgroundTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            if (currentCharacter == Characters.GUARD) {
              // make guard thinking animation go
              setGuardThinkUp();
            } else if (currentCharacter == Characters.PRISONER_ONE) {
              // make prisoner one thinking animation start
              setPrisonerOneThinkUp();
            } else if (currentCharacter == Characters.PRISONER_TWO) {
              // make prisoner two thinking animation start
              setPrisonerTwoThinkUp();
            }
            try {
              ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              chatCompletionRequest.addMessage(result.getChatMessage());
              setAllThinkDown();
              isNewMessage = true;
              // setting the thinking animations in all of the scenes
              return null;
            } catch (ApiProxyException e) {
              ChatMessage error = new ChatMessage("assistant", "Error: \n" + "GPT not working");
              chatCompletionRequest.addMessage(error);
              displayTarget(currentCharacter);
              e.printStackTrace();
              // setting the thinking animations in all of the scenes
              setAllThinkDown();
              isNewMessage = true;
              return null;
            }
          }
        };
    Thread gptThread = new Thread(null, backgroundTask);
    gptThread.start();
  }

  /** This method sets the thinking animations for prisoner one in all of the scenes to up. */
  public static void setPrisonerOneThinkUp() {
    roomController.setThinkingOneUp();
    cafeteriaController.setThinkingOneUp();
    officeController.setThinkingOneUp();
  }

  /** This method sets the thinking animations for prisoner one in all of the scenes to down. */
  public static void setPrisonerOneThinkDown() {
    roomController.setThinkingOneDown();
    cafeteriaController.setThinkingOneDown();
    officeController.setThinkingOneDown();
  }

  /** This method sets the thinking animations for prisoner two in all of the scenes to up. */
  public static void setPrisonerTwoThinkUp() {
    roomController.setThinkingTwoUp();
    cafeteriaController.setThinkingTwoUp();
    officeController.setThinkingTwoUp();
  }

  /** This method sets the thinking animations for prisoner two in all of the scenes to down. */
  public static void setPrisonerTwoThinkDown() {
    roomController.setThinkingTwoDown();
    cafeteriaController.setThinkingTwoDown();
    officeController.setThinkingTwoDown();
  }

  public static void setGuardThinkUp() {
    cafeteriaController.setThinkingThreeUp();
  }

  public static void setGuardThinkDown() {
    cafeteriaController.setThinkingThreeDown();
  }

  /** This method sets the thinking animations in all of the scenes to down. */
  public static void setAllThinkDown() {
    // setting the thinking animations in all of the scenes
    setGuardThinkDown();
    setPrisonerOneThinkDown();
    setPrisonerTwoThinkDown();
  }
}
