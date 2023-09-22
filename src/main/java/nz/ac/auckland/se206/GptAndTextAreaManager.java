package nz.ac.auckland.se206;

import java.io.File;
import java.util.List;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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

  public static Text textAreaTypePromptText;
  public static TextArea textAreaChatDisplayBoard;
  public static TextArea textAreaInputBox;
  public static TextArea textAreaObjectiveDisplayBoard;
  public static Text textAreaHintsLeftText;

  public static boolean isGptRunning = false;
  public static boolean isHintRunning = true;
  public static int hintLeft;

  /*
   * this method outputs MessageHistory as a string which can be put into display
   * board
   *
   * @param chat the ChatCompletionRequest for the chat history
   */
  public static void initialize() throws ApiProxyException {
    // send initial messages to GPT
    sendMessage(GptPromptEngineering.getGuardSetUp(GameState.itemToChoose.getId()));
    currentCharacter = Characters.PRISONER_ONE;
    sendMessage(GptPromptEngineering.getPrisonerOneSetUp());
    currentCharacter = Characters.PRISONER_TWO;
    sendMessage(GptPromptEngineering.getPrisonerTwoSetUp());
    displayTarget(Characters.PRISONER_TWO);
  }

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
        if(GameState.difficulty == GameState.Difficulty.MEDIUM){
          textAreaHintsLeftText.setText("5");
        }
  }

  private static String getMessageHistory(ChatCompletionRequest chat) {
    String result = "";
    List<ChatMessage> messages = chat.getMessages();
    // IMPORTANT: increase i here to filtout the prompt Engineering content

    if (messages.size() > 1) {
      if (isHintRunning) {
        hintLeft = GameState.hints;
      } else{
        hintLeft = 0;
      }
      // filter parentheses out so we can send messages without player seeing
      for (int i = messages.size() - 1; i >= 0; i--) {
        String check = messages.get(i).getContent();
        if (check.charAt(0) == ('(') && check.charAt(check.length() - 1) == (')')) {
          System.out.println(
              messages.get(i).getRole() + ": " + chat.getMessages().get(i).getContent() + "\n\n");
          continue;
        }
        if (currentCharacter == Characters.GUARD) {
          if (messages.get(i).getRole().equals("assistant")
              && messages.get(i).getContent().contains("Correct")) {
            GameState.setRiddleResolved(true);
          }
          if (isHintRunning && GameState.difficulty == GameState.Difficulty.MEDIUM) {
            if (messages.get(i).getRole().equals("assistant")
                && (messages.get(i).getContent().contains("HINT") || messages.get(i).getContent().contains("Hint:") || messages.get(i).getContent().contains("hint:")) && i != 1) {
              hintLeft--;
            }
            if (hintLeft == 0) {
              isHintRunning = false;
              try {
                sendMessage(GptPromptEngineering.stopGivingHint());
              } catch (ApiProxyException e) {
                System.err.println("Error sending message: " + e.getMessage());
              }
            }
          }
        }
        // replace "assistant" with "Guard:" for immersion
        String name = messages.get(i).getRole();
        if (name.trim().equals("assistant")) {
          if (currentCharacter == Characters.GUARD) {
            name = "Guard: ";
          } else if (currentCharacter == Characters.PRISONER_ONE) {
            name = "Prisoner1: ";
          } else {
            name = "Prisoner2: ";
          }
        } else if (name.trim().equals("user")) {
          name = GameState.playerName + ": ";
        }
        // get filtered messages for display
        String content = chat.getMessages().get(i).getContent();
        result += name + parenthesesFilter(content) + "\n\n";
        System.out.println("parenthesesFilter passed");
        System.out.println(
            messages.get(i).getRole() + ": " + chat.getMessages().get(i).getContent() + "\n\n");
      }
      GameState.hintsLeft = hintLeft;
      System.out.println("Hints left: " + hintLeft);
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
    String prompt;
    String chatHistory;
    if (character == Characters.GUARD) {
      // set different recommended text depending on who they want to talk to
      currentCharacter = Characters.GUARD;
      prompt = "Type here to talk to the guard";
      chatHistory = getMessageHistory(guardChatCompletionRequest);
      System.out.println("display Guard history");
    } else if (character == Characters.PRISONER_ONE) {
      // logic for talking to prisoner
      currentCharacter = Characters.PRISONER_ONE;
      prompt = "Type here to talk to prisoner1";
      chatHistory = getMessageHistory(prisonerOneCompletionRequest);
      System.out.println("display Prisoner1 history");
    } else {
      currentCharacter = Characters.PRISONER_TWO;
      // logic for chatting to prisoner 2
      prompt = "Type here to talk to prisoner2";
      chatHistory = getMessageHistory(prisonerTwoCompletionRequest);
      System.out.println("display Prisoner2 history");
    }
    // setting the text for fxml objects
    textAreaTypePromptText.setText(prompt);
    textAreaChatDisplayBoard.setText(chatHistory);
    textAreaChatDisplayBoard.setScrollTop(0);
    if (GameState.difficulty == GameState.Difficulty.MEDIUM) {
      textAreaHintsLeftText.setText(Integer.toString(GameState.hintsLeft));
    } else if (GameState.difficulty == GameState.Difficulty.HARD) {
      textAreaHintsLeftText.setText("No hints available");
    } else {
      textAreaHintsLeftText.setText("Unlimited");
    }
  }

  private static String parenthesesFilter(String input) {
    // Filtering partheses out of the message
    String result = "";
    if (input.contains("(") && input.contains(")")) {
      System.out.println("parenthesesFilter Stage 1 passed");
      result += input.substring(0, input.indexOf("("));
      if (!(input.indexOf(")") + 1 < input.length() - 1)) {
        result += input.substring(input.indexOf(")") + 1);
      }
      // check if passed
      System.out.println("parenthesesFilter Stage 2 passed");
      System.out.println("parenthesesFilter result: " + result);
    } else {
      result = input;
    }
  
    // return the filtered message
    return result;
  }


  public static void sendMessage(String message) throws ApiProxyException {

    boolean ifSpeak = false;

    if (currentCharacter == Characters.GUARD) {
      // append message depending on where the player is in the game
      if (GameState.isRiddleResolved() == false) {
        message = message + "(riddle unsolved)";
      } else if (GameState.wordFound == false) {
        message = message + "(riddle solved)";
      } else if (GameState.cypherFound == false) {
        message = message + "(word found)";
      } else if (GameState.safeFound == false) {
        message = message + "(cypher found)";
      } else if (GameState.safeUnlocked == false) {
        message = message + "(safe found)";
      }
      // make new message and apppend
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
    if (ifSpeak) {
      // play sound effect, hmm sound
      String soundEffect;
      if (currentCharacter == Characters.GUARD) {
        soundEffect = "src/main/resources/sounds/HmmSoundEffect1.mp3";
      } else if (currentCharacter == Characters.PRISONER_ONE) {
        soundEffect = "src/main/resources/sounds/HmmSoundEffect2.mp3";
      } else {
        soundEffect = "src/main/resources/sounds/HmmSoundEffect3.mp3";
      }
      // sound effect configuration
      Media media = new Media(new File(soundEffect).toURI().toString());
      MediaPlayer mediaPlayer = new MediaPlayer(media);
      mediaPlayer.play();
    }
  }

  private static void runGpt(ChatCompletionRequest chatCompletionRequest) throws ApiProxyException {
    // run the GPT model in a background thread
    Task<Void> backgroundTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            if (currentCharacter == Characters.GUARD) {
              // make guard thinking animation go
              cafeteriaController.setThinkingThreeUp();
            } else if (currentCharacter == Characters.PRISONER_ONE) {
              // make prisoner one thinking animation start
              roomController.setThinkingOneUp();
              cafeteriaController.setThinkingOneUp();
              officeController.setThinkingOneUp();
            } else if (currentCharacter == Characters.PRISONER_TWO) {
              // make prisoner two thinking animation start
              roomController.setThinkingTwoUp();
              cafeteriaController.setThinkingTwoUp();
              officeController.setThinkingTwoUp();
            }
            try {
              ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              chatCompletionRequest.addMessage(result.getChatMessage());
              displayTarget(currentCharacter);
              // setting the thinking animations in all of the scenes
              roomController.setThinkingTwoDown();
              cafeteriaController.setThinkingTwoDown();
              officeController.setThinkingTwoDown();
              roomController.setThinkingOneDown();
              cafeteriaController.setThinkingOneDown();
              officeController.setThinkingOneDown();
              cafeteriaController.setThinkingThreeDown();
              return null;
            } catch (ApiProxyException e) {
              ChatMessage error = new ChatMessage("assistant", "Error: \n" + "GPT not working");
              chatCompletionRequest.addMessage(error);
              displayTarget(currentCharacter);
              e.printStackTrace();
              // setting the thinking animations in all of the scenes
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
