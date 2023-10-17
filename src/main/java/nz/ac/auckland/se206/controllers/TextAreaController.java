package nz.ac.auckland.se206.controllers;

import java.io.File;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GptAndTextAreaManager;
import nz.ac.auckland.se206.GptAndTextAreaManager.Characters;
import nz.ac.auckland.se206.SoundEffect;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;

/**
 * Controller for the text area view. This view is used to display the conversation between the
 * player and the guard.
 */
public class TextAreaController {

  /**
   * This method filters any words in parentheses out of the message.
   *
   * @param input the message to be displayed
   * @return the message without the words in parentheses
   */
  public static String parenthesesFilter(String input) {
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

  @FXML private Button responseSubmitButton;
  @FXML private TextArea inputBox;
  @FXML private TextArea chatDisplayBoard;
  @FXML private TextArea objectiveDisplayBoard;
  @FXML private Text typePromptText;
  @FXML private Text hintsLeftText;
  @FXML private TextField inputText;
  @FXML private VBox chatVbox;
  @FXML private Text riddleSolvedObjective;
  @FXML private Text codewordFoundObjective;
  @FXML private Text converterFoundObjective;
  @FXML private Text phoneLocatedObjective;
  @FXML private Text safeLocatedObjective;
  @FXML private Text guardTalkedObjective;

  private Timeline timelineTwo;
  private Timeline timeline;
  private Image guardAvatar;
  private Image playerAvatar;
  private Image prisonerOneAvatar;
  private Image prisonerTwoAvatar;
  private SoundEffect writingSfx = new SoundEffect("src/main/resources/sounds/pencil.mp3");

  @FXML
  private void initialize() {
    resetchecker();
    // setting up the text area manager
    chatVbox.setMaxWidth(562);
    chatVbox.setMaxHeight(195);

    // setting up the text area manager
    GptAndTextAreaManager.textAreaController = this;
    GptAndTextAreaManager.textAreaChatDisplayBoard = chatDisplayBoard;
    GptAndTextAreaManager.textAreaInputBox = inputBox;
    GptAndTextAreaManager.textAreaObjectiveDisplayBoard = objectiveDisplayBoard;
    GptAndTextAreaManager.textAreaTypePromptText = typePromptText;
    GptAndTextAreaManager.textAreaHintsLeftText = hintsLeftText;
    String playerAvatarImage = "src/main/resources/images/PlayerAvatarOne.png";
    String guardAvatarImage = "src/main/resources/images/guardAvatar.png";
    String prisonerOneAvatarImage = "src/main/resources/images/prisonerTwoAvatar.png";
    String prisonerTwoAvatarImage = "src/main/resources/images/prisonerOneAvatar.png";

    // setting up the images for the characters
    playerAvatar = new Image(new File(playerAvatarImage).toURI().toString());
    guardAvatar = new Image(new File(guardAvatarImage).toURI().toString());
    prisonerOneAvatar = new Image(new File(prisonerOneAvatarImage).toURI().toString());
    prisonerTwoAvatar = new Image(new File(prisonerTwoAvatarImage).toURI().toString());

    // adding listener to update the riddle objective
    GameState.isRiddleResolvedProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                // sets the objective to be strikethrough
                riddleSolvedObjective.setStrikethrough(true);
                writingSfx.playSfx();
                try {
                  // gives the guard the next hint
                  GptAndTextAreaManager.sendMessage(GptPromptEngineering.solvedRaddleGuardPrompt());
                  GptAndTextAreaManager.sideConversationController.refreshMessages(
                      GptPromptEngineering.solvedRiddlePrisonerPrompt());
                } catch (ApiProxyException e) {
                  e.printStackTrace();
                }
              }
            });
    // observes the property of the code word being found
    GameState.isCodeWordFoundProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                // sets the objective to be strikethrough
                codewordFoundObjective.setStrikethrough(true);
                writingSfx.playSfx();
              }
            });
    // observes the property of the converter being found
    GameState.isConverterFoundProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                // sets the objective to be strikethrough
                converterFoundObjective.setStrikethrough(true);
                writingSfx.playSfx();

                GptAndTextAreaManager.sideConversationController.refreshMessages(
                    GptPromptEngineering.converterFindPrisonerPrompt());
              }
            });
    // observes the property of the phone being
    GameState.isPhoneFoundProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                // sets the objective to be strikethrough
                phoneLocatedObjective.setStrikethrough(true);
                // plays the writing sound effect
                writingSfx.playSfx();
                // sets the current character to be the guard
                GptAndTextAreaManager.sideConversationController.refreshMessages(
                    GptPromptEngineering.phoneFindPrisonerPrompt());
              }
            });
    // observes the property of the safe being found
    GameState.isSafeFoundProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                // sets the objective to be strikethrough
                safeLocatedObjective.setStrikethrough(true);
                writingSfx.playSfx();

                GptAndTextAreaManager.currentCharacter = Characters.GUARD;
                try {
                  // sends the guard the next hint
                  GptAndTextAreaManager.sendMessage(GptPromptEngineering.findSafeGuardPrompt());
                  GptAndTextAreaManager.sideConversationController.refreshMessages(
                      GptPromptEngineering.safeFindPrisonerPrompt());
                } catch (ApiProxyException e) {
                  e.printStackTrace();
                }
              }
            });
    // observes the property of the guard being talked to
    GameState.isGuardTalkedProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                // sets the objective to be strikethrough
                guardTalkedObjective.setStrikethrough(true);
                writingSfx.playSfx();

                try {
                  GptAndTextAreaManager.sideConversationController.refreshMessages(
                      GptPromptEngineering.findGuardConversationRepond());
                } catch (ApiProxyException e) {
                  e.printStackTrace();
                }
              }
            });

    // setting up the timeline to display the target
    timelineTwo =
        new Timeline(
            new KeyFrame(
                Duration.seconds(0.2),
                event -> {
                  if (GptAndTextAreaManager.isNewMessage) {
                    GptAndTextAreaManager.displayTarget();
                    GptAndTextAreaManager.isNewMessage = false;
                  }
                }));
    timelineTwo.setCycleCount(Timeline.INDEFINITE);
    timelineTwo.play();
  }

  @FXML
  public void onSetPromptTextFalse() {
    typePromptText.setVisible(false);
  }

  /**
   * This method calls the submit message method.
   *
   * @throws ApiProxyException - if the message cannot be sent
   */
  @FXML
  private void onSubmitMessage() throws ApiProxyException {
    submitMessage();
  }

  /**
   * This method submits the message to the GPT.
   *
   * @throws ApiProxyException - if the message cannot be sent
   */
  public void submitMessage() throws ApiProxyException {
    // submit message to GPT
    String message = inputBox.getText();
    inputBox.clear();
    typePromptText.setVisible(true);
    if (message.trim().isEmpty()) {
      typePromptText.setVisible(true);
      return;
    } else {
      GptAndTextAreaManager.sendMessage(message);
    }
  }

  public void setPlayerAvatar(Image avatar) {
    playerAvatar = avatar;
  }

  private void resetchecker() {
    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  if (GameState.secondsRemaining == 0) {
                    try {
                      resetTicks();
                    } catch (ApiProxyException e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
                    }
                  }
                }));
    // runs the thread
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  private void resetTicks() throws ApiProxyException {
    // resets the booleans
    GameState.setRiddleResolved(false);
    GameState.setCodeWordFound(false);
    GameState.setConverterFound(false);
    GameState.setPhoneFound(false);
    GameState.setSafeFound(false);
    GameState.setGuardTalked(false);
    GptAndTextAreaManager.reset();

    // resets the checkboxes
    riddleSolvedObjective.setStrikethrough(false);
    codewordFoundObjective.setStrikethrough(false);
    converterFoundObjective.setStrikethrough(false);
    phoneLocatedObjective.setStrikethrough(false);
    safeLocatedObjective.setStrikethrough(false);
    guardTalkedObjective.setStrikethrough(false);
  }

  /**
   * This method displays the message in the text area.
   *
   * @param chat the message to be displayed
   */
  public void setMessageHistory(ChatCompletionRequest chat) {
    chatVbox.getChildren().clear();
    System.out.println("Vbox cleared");

    List<ChatMessage> messages = chat.getMessages();
    // IMPORTANT: increase i here to filtout the prompt Engineering content

    if (messages.size() > 1) {
      if (GptAndTextAreaManager.currentCharacter == Characters.GUARD) {
        if (GptAndTextAreaManager.isHintRunning) {
          GptAndTextAreaManager.hintLeft = GameState.hints;
        } else {
          GptAndTextAreaManager.hintLeft = 0;
        }
      }
      // filter parentheses out so we can send messages without player seeing

      for (int i = messages.size() - 1; i >= 0; i--) {

        String result = "";
        String check = messages.get(i).getContent();
        if (check.charAt(0) == ('(') && check.charAt(check.length() - 1) == (')')) {
          System.out.println(
              messages.get(i).getRole() + ": " + chat.getMessages().get(i).getContent() + "\n\n");
          continue;
        }

        if (GptAndTextAreaManager.currentCharacter == Characters.GUARD) {
          if (messages.get(i).getRole().equals("assistant")
              && (messages.get(i).getContent().contains("Correct"))) {
            GameState.setRiddleResolved(true);
          }
          if (GptAndTextAreaManager.isHintRunning
              && GameState.difficulty == GameState.Difficulty.MEDIUM) {
            if (messages.get(i).getRole().equals("assistant")
                && (messages.get(i).getContent().contains("HINT")
                    || messages.get(i).getContent().contains("Hint:")
                    || messages.get(i).getContent().contains("hint:"))
                && i != 1) {
              // if the hint is the first message, then it is a hint
              GptAndTextAreaManager.hintLeft--;
            }
            if (GptAndTextAreaManager.hintLeft == 0) {
              GptAndTextAreaManager.isHintRunning = false;
              try {
                // tells gpt to stop giving hints
                GptAndTextAreaManager.sendMessage(GptPromptEngineering.stopGivingHint());
              } catch (ApiProxyException e) {
                System.err.println("Error sending message: " + e.getMessage());
              }
            }
          }
        }

        // replace "assistant" with "Guard:" for immersion
        String name = messages.get(i).getRole();
        ImageView avatar = null;
        if (name.trim().equals("assistant")) {
          if (GptAndTextAreaManager.currentCharacter == Characters.GUARD) {
            name = "Guard: ";
            avatar = new ImageView(guardAvatar);
          } else if (GptAndTextAreaManager.currentCharacter == Characters.PRISONER_ONE) {
            name = "Prisoner1: ";
            avatar = new ImageView(prisonerOneAvatar);
          } else {
            name = "Prisoner2: ";
            avatar = new ImageView(prisonerTwoAvatar);
          }
        } else if (name.trim().equals("user")) {
          name = GameState.playerName + ": ";
          avatar = new ImageView(playerAvatar);
        }
        // get filtered messages for display
        String content = chat.getMessages().get(i).getContent();
        result += name + "\n" + parenthesesFilter(content) + "\n\n";
        System.out.println("parenthesesFilter passed");
        System.out.println(
            messages.get(i).getRole() + ": " + chat.getMessages().get(i).getContent() + "\n\n");
        System.out.println("creating new Hbox");

        HBox hbox = new HBox();
        Text text = new Text(result);
        text.setWrappingWidth(200);

        // setting up the avatar
        avatar.setFitHeight(50);
        avatar.setFitWidth(50);
        if (messages.get(i).getRole() == "user") {
          text.setTextAlignment(TextAlignment.RIGHT);
          hbox.getChildren().add(text);
          hbox.getChildren().add(avatar);
          hbox.setAlignment(Pos.CENTER_RIGHT);
        } else {
          hbox.getChildren().add(avatar);
          hbox.getChildren().add(text);
        }
        chatVbox.getChildren().add(hbox);
        System.out.println("Vbox updated");
      }
      GameState.hintsLeft = GptAndTextAreaManager.hintLeft;
      System.out.println("Hints left: " + GptAndTextAreaManager.hintLeft);
    }
  }

  /**
   * This method displays the target character in the text area.
   *
   * @param character the character that the player is talking to
   */
  public void displayTarget(Characters character) {
    String prompt;
    if (character == Characters.GUARD) {
      // set different recommended text depending on who they want to talk to
      GptAndTextAreaManager.currentCharacter = Characters.GUARD;
      prompt = "Type here to talk to the guard";
      setMessageHistory(GptAndTextAreaManager.guardChatCompletionRequest);
      System.out.println("display Guard history");
    } else if (character == Characters.PRISONER_ONE) {
      // logic for talking to prisoner
      GptAndTextAreaManager.currentCharacter = Characters.PRISONER_ONE;
      prompt = "Type here to talk to prisoner1";
      setMessageHistory(GptAndTextAreaManager.prisonerOneCompletionRequest);
      System.out.println("display Prisoner1 history");
    } else {
      GptAndTextAreaManager.currentCharacter = Characters.PRISONER_TWO;
      // logic for chatting to prisoner 2
      prompt = "Type here to talk to prisoner2";
      setMessageHistory(GptAndTextAreaManager.prisonerTwoCompletionRequest);
      System.out.println("display Prisoner2 history");
    }
    // setting the text for fxml objects
    typePromptText.setText(prompt);
    typePromptText.setVisible(true);
    if (GameState.difficulty == GameState.Difficulty.MEDIUM) {
      hintsLeftText.setText(Integer.toString(GameState.hintsLeft));
    } else if (GameState.difficulty == GameState.Difficulty.HARD) {
      hintsLeftText.setText("No hints available");
    } else {
      hintsLeftText.setText("Unlimited");
    }
  }
}
