package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.GptAndTextAreaManager;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class TextAreaController {
  @FXML private Button responseSubmitButton;
  @FXML private TextArea inputBox;
  @FXML private TextArea chatDisplayBoard;
  @FXML private TextArea objectiveDisplayBoard;
  @FXML private Text typePromptText;
  @FXML private TextField inputText;

  @FXML
  private void initialize() {
    GptAndTextAreaManager.textAreaController = this;
    GptAndTextAreaManager.textAreaChatDisplayBoard = chatDisplayBoard;
    GptAndTextAreaManager.textAreaInputBox = inputBox;
    GptAndTextAreaManager.textAreaObjectiveDisplayBoard = objectiveDisplayBoard;
    GptAndTextAreaManager.textAreaTypePromptText = typePromptText;
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
}
