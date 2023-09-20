package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Random;
import java.util.function.Consumer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Safe;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;
import nz.ac.auckland.se206.speech.TextToSpeech;

/** Controller class for the room view. */
public class RoomController {
  @FXML private Pane room;
  @FXML private Rectangle door;
  @FXML private Rectangle vent;
  @FXML private Rectangle toiletPaper;
  @FXML private Rectangle toilet;
  @FXML private Rectangle sink;
  @FXML private Rectangle tap;
  @FXML private Rectangle mirror;
  @FXML private Rectangle towel;
  @FXML private ImageView toiletBig;
  @FXML private ImageView toiletPaperBig;
  @FXML private ImageView doorBig;
  @FXML private ImageView ventBig;
  @FXML private ImageView sinkBig;
  @FXML private ImageView mirrorBig;
  @FXML private ImageView towelBig;
  @FXML private ImageView toiletArrow;
  @FXML private ImageView toiletPaperArrow;
  @FXML private ImageView ventArrow;
  @FXML private ImageView sinkArrow;
  @FXML private ImageView mirrorArrow;
  @FXML private ImageView towelArrow;
  @FXML private ImageView doorArrow;
  @FXML private Label timerLabel;
  @FXML private Pane converterPane;
  @FXML private Button exitViewButton;
  @FXML private Button openButton;
  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private Button sendButton;
  @FXML private Pane chatPane;
  @FXML private Pane startPane;
  @FXML private ImageView doorArrowSmall;
  @FXML private ProgressIndicator chatProgress;
  @FXML private Label chatProgressLabel;
  @FXML private Rectangle textBubble;
  @FXML private ImageView bubbleBig;
  @FXML private Pane guardSpeechPane;
  @FXML private Label overQuestionLimitLabel;
  @FXML private Label questionInfoLabel;

  private Timeline timeline;
  private ChatCompletionRequest chatCompletionRequest;
  private TextToSpeech textToSpeech;

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws ApiProxyException
   * @throws IOException
   */
  public void initialize() throws ApiProxyException, IOException {

    itemToChoose();
    prepareRiddle();

    // Setting up the timer timeline
    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  GameState.secondsRemaining--;
                  updateTimerLabel();
                  if (GameState.secondsRemaining == 90 && GameState.isWon == false) {
                    textToSpeech.speak("a minute and a half remaining");
                  } else if (GameState.secondsRemaining == 60 && GameState.isWon == false) {
                    textToSpeech.speak("one minute remaining");
                  } else if (GameState.secondsRemaining == 30 && GameState.isWon == false) {
                    textToSpeech.speak("thirty seconds remaining");
                  }
                }));
    timeline.setCycleCount(GameState.totalSeconds);
    timeline.setOnFinished(event -> handleTimerExpired());
    updateTimerLabel();

    textToSpeech = new TextToSpeech();
    textToSpeech.speak("Welcome to the room");
    animateArrows(doorArrow);
    GameState.isRiddleResolvedProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                animateAllArrows();
                guardSpeechPane.setVisible(true);
              }
            });
    timeline.play();
    Platform.runLater(
        () -> {
          Stage stage = (Stage) room.getScene().getWindow();
          stage.setOnCloseRequest(
              event -> {
                timeline.stop();
                textToSpeech.terminate();
                Platform.exit();
              });
        });

    resetchecker();
  }

  /*
   * This method starts a thread that checks if the cafeteria needs to be reset.
   * 
   * @throws IOException
   */

  private void resetchecker() throws IOException {
    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  if (GameState.secondsRemaining >= 0) {
                    updateTimerLabel();
                  }
                  if (GameState.secondsRemaining == 0) {
                    if (GameState.gameFinishedRoom){

                      GameState.resetCafeteria = true;
                      GameState.resetOffice = true;
                      GameState.resetRoom = true;
                      try {
                        Scene scene = sink.getScene();
                        scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.END_LOST));
                      } catch (NullPointerException e) {
                      }
                    }
                  }
                  updateTimerLabel();
                  if (GameState.resetRoom) {
                      try {
                        resetRoom();
                      } catch (ApiProxyException e) {
                      }
                    GameState.resetRoom = false;
                  }
                }));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  private void resetRoom() throws ApiProxyException {
    itemToChoose();
    prepareRiddle();
    Safe.getRandomCode();
    toiletArrow.setOpacity(0);
    toiletPaperArrow.setOpacity(0);
    ventArrow.setOpacity(0);
    sinkArrow.setOpacity(0);
    mirrorArrow.setOpacity(0);
    towelArrow.setOpacity(0);
    doorArrowSmall.setOpacity(0);
    guardSpeechPane.setVisible(false);
    animateArrows(doorArrow);
    GameState.setRiddleResolved(false);
  }

  /*
   * This method prepares the riddle for the player to solve.
   *
   * @throws ApiProxyException
   */
  private void prepareRiddle() throws ApiProxyException {
    // Sending the initial request so the riddle is ready when the player enters the chat
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(100);
    ChatMessage userChatMessage =
        new ChatMessage(
            "user", GptPromptEngineering.getRiddleWithGivenWord(GameState.itemToChoose.getId()));
    runGpt(userChatMessage, lastMsg -> {});
  }

  /*
   * This method selects a random item to be used in the riddle.
   */
  private void itemToChoose() {
    Rectangle[] items = new Rectangle[] {vent, toiletPaper, toilet, mirror, towel, sink};
    Random randomChoose = new Random();
    int randomIndexChoose = randomChoose.nextInt(items.length);
    GameState.itemToChoose = items[randomIndexChoose];
  }

  public void animateAllArrows() {
    animateArrows(toiletArrow);
    animateArrows(toiletPaperArrow);
    animateArrows(ventArrow);
    animateArrows(sinkArrow);
    animateArrows(mirrorArrow);
    animateArrows(towelArrow);
    animateArrows(doorArrowSmall);
  }

  public void animateArrows(ImageView arrow) {
    arrow.setOpacity(1);

    double startY = 0;

    TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), arrow);
    translateTransition.setFromY(startY);
    translateTransition.setToY(startY + 5);
    translateTransition.setAutoReverse(true);
    translateTransition.setCycleCount(Animation.INDEFINITE);
    translateTransition.play();
  }

  private void updateTimerLabel() {
    int minutes = GameState.secondsRemaining / 60;
    int seconds = GameState.secondsRemaining % 60;
    timerLabel.setText(String.format("%d:%02d", minutes, seconds));
  }

  private void handleTimerExpired() {
    if (!GameState.isWon) {
      GameState.resetCafeteria = true;
      GameState.resetOffice = true;
      GameState.resetRoom = true;
      GameState.gameFinishedCafeteria = true;
      GameState.gameFinishedOffice = true;
      GameState.gameFinishedRoom = true;
      // TODO: fix the error
    }
  }

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("key " + event.getCode() + " released");
  }

  /**
   * Displays a dialog box with the given title, header text, and message.
   *
   * @param title the title of the dialog box
   * @param headerText the header text of the dialog box
   * @param message the message content of the dialog box
   */
  private void showDialog(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
  }

  @FXML
  public void toiletMouseEntered() {
    if (GameState.isRiddleResolved()) {
      toiletBig.setVisible(true);
    }
  }

  @FXML
  public void toiletMouseExit() {
    toiletBig.setVisible(false);
  }

  @FXML
  public void toiletPaperMouseEntered() {
    if (GameState.isRiddleResolved()) {
      toiletPaperBig.setVisible(true);
    }
  }

  @FXML
  public void toiletPaperMouseExit() {
    toiletPaperBig.setVisible(false);
  }

  @FXML
  public void doorMouseEntered() {
    doorBig.setVisible(true);
  }

  @FXML
  public void doorMouseExit() {
    doorBig.setVisible(false);
  }

  @FXML
  public void ventMouseEntered() {
    if (GameState.isRiddleResolved()) {
      ventBig.setVisible(true);
    }
  }

  @FXML
  public void ventMouseExit() {
    ventBig.setVisible(false);
  }

  @FXML
  public void sinkMouseEntered() {
    if (GameState.isRiddleResolved()) {
      sinkBig.setVisible(true);
    }
  }

  @FXML
  public void sinkMouseExit() {
    sinkBig.setVisible(false);
  }

  @FXML
  public void mirrorMouseEntered() {
    if (GameState.isRiddleResolved()) {
      mirrorBig.setVisible(true);
    }
  }

  @FXML
  public void mirrorMouseExit() {
    mirrorBig.setVisible(false);
  }

  @FXML
  public void towelMouseEntered() {
    if (GameState.isRiddleResolved()) {
      towelBig.setVisible(true);
    }
  }

  @FXML
  public void towelMouseExit() {
    towelBig.setVisible(false);
  }

  @FXML
  public void textBubbleMouseEntered() {
    bubbleBig.setVisible(true);
  }

  @FXML
  public void textBubbleMouseExit() {
    bubbleBig.setVisible(false);
  }

  /**
   * Handles the click event on the door.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   * @throws ApiProxyException
   */
  @FXML
  public void clickDoor(MouseEvent event) throws IOException, ApiProxyException {
    doorArrow.setOpacity(0);
    doorArrowSmall.setOpacity(0);
    if (!GameState.isRiddleResolved()) {
      showDialog(
          "Info",
          "A guard approaches...",
          "\"Are you ready to break out of here? Then listen to this riddle:\" he says");
      chatPane.setVisible(true);
      return;
    } else {
      showDialog("Info", "Locked!", "Find the next clue elsewhere");
    }
  }

  @FXML
  public void clickToilet(MouseEvent event) {
    toiletArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (GameState.itemToChoose == toilet) {
        showDialog(
            "Nice Job",
            "You found the item!",
            "On the inside of the toilet is written the word " + GameState.codeWord);
      } else {
        showDialog("Nothing!", "Toilet", "Just a normal toilet.");
      }
    }
  }

  @FXML
  public void clickToiletPaper(MouseEvent event) {
    toiletPaperArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (GameState.itemToChoose == toiletPaper) {
        showDialog(
            "Nice Job",
            "You found the item!",
            "On the toilet paper is written the word " + GameState.codeWord);
      } else {
        showDialog("Nothing!", "Toilet Paper", "Just a normal roll of toilet paper.");
      }
    }
  }

  @FXML
  public void clickVent(MouseEvent event) {
    ventArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (GameState.itemToChoose == vent) {
        showDialog(
            "Nice Job",
            "You found the item!",
            "In the vent you notice a piece of paper, scribbled on one side is the word "
                + GameState.codeWord);
      } else {
        showDialog("Nothing!", "Vent", "Just an empty vent");
      }
    }
  }

  @FXML
  public void clickSink(MouseEvent event) {
    sinkArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (GameState.itemToChoose == sink) {
        showDialog(
            "Nice Job",
            "You found the item!",
            "In the sink you notice scribbled on the side is the word " + GameState.codeWord);
      } else {
        showDialog("Nothing!", "Sink", "Just an empty sink");
      }
    }
  }

  @FXML
  public void clickMirror(MouseEvent event) {
    mirrorArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (GameState.itemToChoose == mirror) {
        showDialog(
            "Nice Job",
            "You found the item!",
            "On the mirror you notice a word written in ink: " + GameState.codeWord);
      } else {
        showDialog("Nothing!", "Mirror", "Just a normal mirror");
      }
    }
  }

  @FXML
  public void clickTowel(MouseEvent event) {
    towelArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (GameState.itemToChoose == towel) {
        showDialog(
            "Nice Job",
            "You found the item!",
            "You notice that scribbled on one side of the towel is the word " + GameState.codeWord);
      } else {
        showDialog("Nothing!", "Towel", "Just a normal towel");
      }
    }
  }

  @FXML
  private void onTextBubbleClicked() throws ApiProxyException {
    chatPane.setVisible(true);
    questionInfoLabel.setVisible(true);
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(1).setTopP(0.5).setMaxTokens(100);
    ChatMessage userChatMessage = new ChatMessage("user", GptPromptEngineering.getGuardSetUp());
    runGpt(userChatMessage, lastMsg -> {});
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    if (msg.getRole().equals("assistant")) {
      chatTextArea.appendText("Guard: " + msg.getContent() + "\n\n");
    } else if (msg.getRole().equals("user")) {
      chatTextArea.appendText("You: " + msg.getContent() + "\n\n");
    }
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private void runGpt(ChatMessage msg, Consumer<ChatMessage> completionCallback)
      throws ApiProxyException {
    // define a new task to create threading
    Task<Void> callGpt =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            try {
              // add the message to the request
              chatCompletionRequest.addMessage(msg);
              ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              chatCompletionRequest.addMessage(result.getChatMessage());
              Platform.runLater(
                  () -> {
                    // update gui components later
                    appendChatMessage(result.getChatMessage());
                    completionCallback.accept(result.getChatMessage());
                    chatProgress.setVisible(false);
                    chatProgressLabel.setVisible(false);
                  });
            } catch (ApiProxyException e) {
              // TODO handle exception appropriately
              e.printStackTrace();
            }
            return null;
          }
        };
    // start thread
    Thread thread = new Thread(callGpt);
    thread.start();
    // set progress circle to loading
    chatProgress.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
    chatProgress.setVisible(true);
    chatProgressLabel.setVisible(true);
  }

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    if (GameState.isRiddleResolved()) {
      GameState.questionsAsked++;
      if (GameState.questionsAsked >= 2) {
        sendButton.disableProperty().set(true);
        overQuestionLimitLabel.setVisible(true);
        inputText.disableProperty().set(true);
      }
    }
    String message = inputText.getText();
    if (message.trim().isEmpty()) {
      return;
    }
    inputText.clear();
    ChatMessage msg = new ChatMessage("user", message);
    appendChatMessage(msg);

    runGpt(
        msg,
        lastMsg -> {
          if (lastMsg.getRole().equals("assistant") && lastMsg.getContent().startsWith("Correct")) {
            GameState.setRiddleResolved(true);
          }
          System.out.println(chatCompletionRequest.toString());
        });
  }

  /**
   * Navigates back to the previous view.
   *
   * @param event the action event triggered by the go back button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    chatPane.setVisible(false);
  }

  @FXML
  private void goToCafeteria() {
    Scene scene = mirror.getScene();
    scene.setRoot(SceneManager.getUiRoot(AppUi.CAFETERIA));
  }

  @FXML
  private void goToOffice() {
    Scene scene = mirror.getScene();
    scene.setRoot(SceneManager.getUiRoot(AppUi.OFFICE));
  }
}
