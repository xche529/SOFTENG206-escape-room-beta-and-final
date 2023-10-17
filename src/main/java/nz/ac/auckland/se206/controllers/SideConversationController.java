package nz.ac.auckland.se206.controllers;

import java.util.List;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.GptAndTextAreaManager;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/**
 * Controller for the side conversation view. This view is used to display the conversation between
 * the two prisoners.
 */
public class SideConversationController {

  @FXML private TextArea prisonerOneTextArea;
  @FXML private TextArea prisonerTwoTextArea;

  private String prisonerOneText = "";
  private String prisonerTwoText = "";

  private ChatCompletionRequest groupChatCompletionRequest =
      new ChatCompletionRequest().setN(1).setTemperature(0.1).setTopP(0.8).setMaxTokens(100);
  private String playerName = "player";

  /**
   * This method is invoked when the application starts. It loads and shows the intial prompt for
   * the side conversation.
   */
  public void start() {
    // set the initial prompt
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
    if (isRecursion) {
      Task<Void> backgroundTask =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              try {
                GptAndTextAreaManager.setPrisonerOneThinkUp();
                System.out.println("GPT for conversation running");
                // executes a new chat completion request
                ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
                Choice result = chatCompletionResult.getChoices().iterator().next();
                chatCompletionRequest.addMessage(result.getChatMessage());
                List<ChatMessage> messages = chatCompletionRequest.getMessages();
                int size = messages.size();
                ChatMessage message = messages.get(size - 1);
                prisonerOneText = message.getContent();
                displayPrisonerOneMessage();
                // stops the thinking animation
                GptAndTextAreaManager.setPrisonerOneThinkDown();
                chatCompletionRequest.addMessage(
                    new ChatMessage("user", GptPromptEngineering.getConversationRespond()));
                runGpt(chatCompletionRequest, false);
                return null;
              } catch (ApiProxyException e) {
                // displays that an errorhas occured
                ChatMessage error = new ChatMessage("assistant", "Error: \n" + "GPT not working");
                chatCompletionRequest.addMessage(error);
                e.printStackTrace();
                return null;
              }
            }
          };
      Thread gptThread = new Thread(null, backgroundTask);
      gptThread.start();
    } else {
      // executes a new chat completion request
      GptAndTextAreaManager.setPrisonerTwoThinkUp();
      System.out.println("GPT for conversation running");
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      // creates a list of the messages received from the GPT model
      List<ChatMessage> messages = chatCompletionRequest.getMessages();
      int size = messages.size();
      ChatMessage message = messages.get(size - 1);
      prisonerTwoText = message.getContent();
      displayPrisonerTwoMessage();
      GptAndTextAreaManager.setPrisonerTwoThinkDown();
    }
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

  /**
   * This method is invoked when the user clicks the send button. It sends the user's message to the
   * GPT model and displays the response.
   *
   * @param message The message to send to the GPT model.
   */
  public void refreshMessages(String prompt) {
    groupChatCompletionRequest.addMessage(new ChatMessage("user", prompt));
    try {
      runGpt(groupChatCompletionRequest, true);
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }
}
