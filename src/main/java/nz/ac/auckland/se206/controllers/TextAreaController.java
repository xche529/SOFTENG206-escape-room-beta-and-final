package nz.ac.auckland.se206.controllers;

import java.io.File;

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
import javafx.util.Duration;
import javafx.scene.text.TextAlignment;
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
  private Timeline timeline;

  public Image guardAvatar;
  public Image playerAvatar;
  public Image prisonerOneAvatar;
  public Image prisonerTwoAvatar;

  @FXML
  private void initialize() {
    // setting up the text area manager
    chatVbox.setMaxWidth(562);
    chatVbox.setMaxHeight(195);

    GptAndTextAreaManager.textAreaController = this;
    GptAndTextAreaManager.textAreaChatDisplayBoard = chatDisplayBoard;
    GptAndTextAreaManager.textAreaInputBox = inputBox;
    GptAndTextAreaManager.textAreaObjectiveDisplayBoard = objectiveDisplayBoard;
    GptAndTextAreaManager.textAreaTypePromptText = typePromptText;
    GptAndTextAreaManager.textAreaHintsLeftText = hintsLeftText;
    String image = "src/main/resources/images/prisonerOneAvatar.png";
    String guardAvatarImage = "src/main/resources/images/guardAvatar.png";
    String prisonerOneAvatarImage = "src/main/resources/images/prisonerTwoAvatar.png";
    String prisonerTwoAvatarImage = "src/main/resources/images/prisonerOneAvatar.png";
    guardAvatar = new Image(new File(guardAvatarImage).toURI().toString()); 
    playerAvatar = new Image(new File(image).toURI().toString()); 
    prisonerOneAvatar =
        new Image(new File(prisonerOneAvatarImage).toURI().toString()); 
    prisonerTwoAvatar = 
        new Image(new File(prisonerTwoAvatarImage).toURI().toString()); 

    resetchecker();

    // adding listener to update objectives
    GameState.isRiddleResolvedProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                riddleSolvedObjective.setSelected(true);
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
              }
            });
    GameState.isGuardTalkedProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                guardTalkedObjective.setSelected(true);
              }
            });
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
    //resets the booleans
    GameState.setRiddleResolved(false);
    GameState.setCodeWordFound(false);
    GameState.setConverterFound(false);
    GameState.setPhoneFound(false);
    GameState.setSafeFound(false);
    GameState.setGuardTalked(false);

    //resets the checkboxes
    riddleSolvedObjective.setSelected(false);
    codewordFoundObjective.setSelected(false);
    converterFoundObjective.setSelected(false);
    phoneLocatedObjective.setSelected(false);
    safeLocatedObjective.setSelected(false);
    guardTalkedObjective.setSelected(false);
    
  }
}
