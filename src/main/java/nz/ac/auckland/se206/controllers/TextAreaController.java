package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GptAndTextAreaManager;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class TextAreaController {
  @FXML private Button responseSubmitButton;
  @FXML private TextArea inputBox;
  @FXML private TextArea chatDisplayBoard;
  @FXML private TextArea objectiveDisplayBoard;
  @FXML private Text typePromptText;
  @FXML private Text hintsLeftText;
  @FXML private TextField inputText;

  @FXML private CheckBox riddleSolvedObjective;
  @FXML private CheckBox codewordFoundObjective;
  @FXML private CheckBox converterFoundObjective;
  @FXML private CheckBox phoneLocatedObjective;
  @FXML private CheckBox safeLocatedObjective;
  @FXML private CheckBox guardTalkedObjective;
  private Timeline timeline;

  @FXML
  private void initialize() {
    // setting up the text area manager
    GptAndTextAreaManager.textAreaController = this;
    GptAndTextAreaManager.textAreaChatDisplayBoard = chatDisplayBoard;
    GptAndTextAreaManager.textAreaInputBox = inputBox;
    GptAndTextAreaManager.textAreaObjectiveDisplayBoard = objectiveDisplayBoard;
    GptAndTextAreaManager.textAreaTypePromptText = typePromptText;
    GptAndTextAreaManager.textAreaHintsLeftText = hintsLeftText;

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
