package nz.ac.auckland.se206.controllers;

import java.io.File;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;

public class TextAreaController {
  @FXML private Button responseSubmitButton;
  @FXML private TextArea inputBox;
  @FXML private TextArea chatDisplayBoard;
  @FXML private TextArea objectiveDisplayBoard;
  @FXML private Text typePromptText;
  @FXML private Text hintsLeftText;
  @FXML private TextField inputText;
  @FXML private VBox chatVbox;

  @FXML private CheckBox riddleSolvedObjective;
  @FXML private CheckBox codewordFoundObjective;
  @FXML private CheckBox converterFoundObjective;
  @FXML private CheckBox phoneLocatedObjective;
  @FXML private CheckBox safeLocatedObjective;
  @FXML private CheckBox guardTalkedObjective;
  private Timeline timelineTwo;

  private Timeline timeline;
  public Image guardAvatar;
  public Image playerAvatar;
  public Image prisonerOneAvatar;
  public Image prisonerTwoAvatar;

  @FXML
  private void initialize() {
    resetchecker();
    // setting up the text area manager
    chatVbox.setMaxWidth(562);
    chatVbox.setMaxHeight(195);

    inputBox.setStyle("-fx-background-color: none; -fx-text-fill: white;");

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
    playerAvatar = new Image(new File(playerAvatarImage).toURI().toString());
    guardAvatar = new Image(new File(guardAvatarImage).toURI().toString());
    prisonerOneAvatar = new Image(new File(prisonerOneAvatarImage).toURI().toString());
    prisonerTwoAvatar = new Image(new File(prisonerTwoAvatarImage).toURI().toString());

    // adding listener to update objectives
    GameState.isRiddleResolvedProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                riddleSolvedObjective.setSelected(true);
                try {
                  GptAndTextAreaManager.sendMessage(GptPromptEngineering.solvedRaddleGuardPrompt());
                } catch (ApiProxyException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                }
              }
            });
    GameState.isCodeWordFoundProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                codewordFoundObjective.setSelected(true);
              }
            });
    GameState.isConverterFoundProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                converterFoundObjective.setSelected(true);
              }
            });
    GameState.isPhoneFoundProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                phoneLocatedObjective.setSelected(true);
              }
            });
    GameState.isSafeFoundProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                safeLocatedObjective.setSelected(true);
                GptAndTextAreaManager.currentCharacter = Characters.GUARD;
                try {
                  GptAndTextAreaManager.sendMessage(GptPromptEngineering.findSafeGuardPrompt());
                } catch (ApiProxyException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                }
              }
            });
    GameState.isGuardTalkedProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                guardTalkedObjective.setSelected(true);
                GptAndTextAreaManager.sideConversationController.refreshMessages(
                    GptPromptEngineering.findGuardConversationRepond());
              }
            });

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

  @FXML
  public void onSubmitMessage() throws ApiProxyException {
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
                    GameState.resetTextArea = true;
                  }

                  if (GameState.resetTextArea) {
                    resetTicks();
                    GameState.resetTextArea = false;
                  }
                }));
    // runs the thread
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  private void resetTicks() {
    // resets the booleans
    GameState.setRiddleResolved(false);
    GameState.setCodeWordFound(false);
    GameState.setConverterFound(false);
    GameState.setPhoneFound(false);
    GameState.setSafeFound(false);
    GameState.setGuardTalked(false);

    // resets the checkboxes
    riddleSolvedObjective.setSelected(false);
    codewordFoundObjective.setSelected(false);
    converterFoundObjective.setSelected(false);
    phoneLocatedObjective.setSelected(false);
    safeLocatedObjective.setSelected(false);
    guardTalkedObjective.setSelected(false);
  }

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
              GptAndTextAreaManager.hintLeft--;
            }
            if (GptAndTextAreaManager.hintLeft == 0) {
              GptAndTextAreaManager.isHintRunning = false;
              try {
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

        avatar.setFitHeight(50);
        avatar.setFitWidth(50);
        if (messages.get(i).getRole() == "user") {
          text.setTextAlignment(TextAlignment.RIGHT);
          hbox.getChildren().add(text);
          hbox.getChildren().add(avatar);
          // hbox.setMaxWidth(562);
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
}
