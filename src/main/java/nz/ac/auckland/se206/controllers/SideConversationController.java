package nz.ac.auckland.se206.controllers;

import java.util.List;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class SideConversationController {

  @FXML private TextArea prisonerOneTextArea;
  @FXML private TextArea prisonerTwoTextArea;

  public String prisonerOneText = "";
  public String prisonerTwoText = "";

  public ChatCompletionRequest groupChatCompletionRequest =
      new ChatCompletionRequest().setN(1).setTemperature(0.1).setTopP(0.8).setMaxTokens(100);
  private String playerName = "player";

  public void initialize() {
    String message = GptPromptEngineering.groupConversationPrompt(playerName);
    groupChatCompletionRequest.addMessage(new ChatMessage("user", message));
    try {
      runGpt(groupChatCompletionRequest, true);
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }

  private void runGpt(ChatCompletionRequest chatCompletionRequest, boolean isRecursion)
      throws ApiProxyException {
    // run the GPT model in a background thread
    Task<Void> backgroundTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            try {
                System.out.println("GPT for conversation running");
              ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              chatCompletionRequest.addMessage(result.getChatMessage());
              List<ChatMessage> messages = chatCompletionRequest.getMessages();
              int size = messages.size();

              if (isRecursion) {
                ChatMessage message = messages.get(size - 1);
                prisonerOneText = message.getContent();
                displayPrisonerOneMessage();
                chatCompletionRequest.addMessage(
                    new ChatMessage("user", GptPromptEngineering.getConversationRespond()));
                runGpt(chatCompletionRequest, false);
              }else{
                ChatMessage message = messages.get(size - 1);
                prisonerTwoText = message.getContent();
                displayPrisonerTwoMessage();
              }

              return null;
            } catch (ApiProxyException e) {
              ChatMessage error = new ChatMessage("assistant", "Error: \n" + "GPT not working");
              chatCompletionRequest.addMessage(error);
              e.printStackTrace();
              return null;
            }
          }
        };
    Thread gptThread = new Thread(null, backgroundTask);
    gptThread.start();
  }

  private void displayPrisonerOneMessage() {
    prisonerOneTextArea.setText(prisonerOneText);
  }

  private void displayPrisonerTwoMessage() {
    prisonerTwoTextArea.setText(prisonerTwoText);
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  public void refreshMessages(String prompt) {
    groupChatCompletionRequest.addMessage(new ChatMessage("user", prompt));
    try {
      runGpt(groupChatCompletionRequest, true);
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }
}
