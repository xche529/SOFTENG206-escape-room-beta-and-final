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
  @FXML private Rectangle posters;
  @FXML private Rectangle sink;
  @FXML private Rectangle bedsideTable;
  @FXML private Rectangle tap;
  @FXML private Rectangle mirror;
  @FXML private Rectangle picture;
  @FXML private Rectangle towel;
  @FXML private Rectangle window;
  @FXML private ImageView toiletBig;
  @FXML private ImageView toiletPaperBig;
  @FXML private ImageView doorBig;
  @FXML private ImageView ventBig;
  @FXML private ImageView postersBig;
  @FXML private ImageView bedsideTableBig;
  @FXML private ImageView sinkBig;
  @FXML private ImageView mirrorBig;
  @FXML private ImageView towelBig;
  @FXML private ImageView toiletArrow;
  @FXML private ImageView postersArrow;
  @FXML private ImageView toiletPaperArrow;
  @FXML private ImageView ventArrow;
  @FXML private ImageView bedsideTableArrow;
  @FXML private ImageView sinkArrow;
  @FXML private ImageView mirrorArrow;
  @FXML private ImageView towelArrow;
  @FXML private ImageView windowArrow;
  @FXML private ImageView doorArrow;
  @FXML private ImageView pictureArrow;
  @FXML private ImageView windowBig;
  @FXML private ImageView pictureBig;
  @FXML private Label timerLabel;
  @FXML private Pane converterPane;
  @FXML private Button exitViewButton;
  @FXML private Button openButton;
  @FXML private ImageView digitOnePlus;
  @FXML private ImageView digitOneMinus;
  @FXML private ImageView digitTwoPlus;
  @FXML private ImageView digitTwoMinus;
  @FXML private ImageView digitThreePlus;
  @FXML private ImageView digitThreeMinus;
  @FXML private ImageView digitFourPlus;
  @FXML private ImageView digitFourMinus;
  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private Button sendButton;
  @FXML private Label digitOne;
  @FXML private Label digitTwo;
  @FXML private Label digitThree;
  @FXML private Label digitFour;
  @FXML private Pane padlockPane;
  @FXML private Pane chatPane;
  @FXML private Pane startPane;
  @FXML private ImageView doorArrowSmall;
  @FXML private ProgressIndicator chatProgress;
  @FXML private Label chatProgressLabel;
  @FXML private Rectangle textBubble;
  @FXML private ImageView bubbleBig;
  @FXML private Pane guardSpeechPane;
  @FXML private ImageView textBubbleArrow;
  @FXML private Label overQuestionLimitLabel;
  @FXML private Label questionInfoLabel;

  private Timeline timeline;
  private Rectangle itemCode;
  private ChatCompletionRequest chatCompletionRequest;
  private TextToSpeech textToSpeech;

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws ApiProxyException
   */
  public void initialize() throws ApiProxyException {

    // Getting random item to be used in the riddle
    Rectangle[] itemsForWord = new Rectangle[] {bedsideTable, window, picture};
    Random random = new Random();
    int randomIndex = random.nextInt(itemsForWord.length);
    this.itemCode = itemsForWord[randomIndex];

    // Getting a random item to hide the chart behind
    Rectangle[] items = new Rectangle[] {vent, toiletPaper, toilet, mirror, towel, sink};
    Random randomChoose = new Random();
    int randomIndexChoose = randomChoose.nextInt(items.length);
    GameState.itemToChoose = items[randomIndexChoose];

    // Sending the initial request so the riddle is ready when the player enters the chat
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(100);
    ChatMessage userChatMessage =
        new ChatMessage(
            "user", GptPromptEngineering.getRiddleWithGivenWord(GameState.itemToChoose.getId()));
    runGpt(userChatMessage, lastMsg -> {});

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
    timeline.setCycleCount(120);
    timeline.setOnFinished(event -> handleTimerExpired());
    updateTimerLabel();
  }

  public void animateAllArrows() {
    animateArrows(toiletArrow);
    animateArrows(postersArrow);
    animateArrows(toiletPaperArrow);
    animateArrows(ventArrow);
    animateArrows(bedsideTableArrow);
    animateArrows(sinkArrow);
    animateArrows(mirrorArrow);
    animateArrows(towelArrow);
    animateArrows(windowArrow);
    animateArrows(pictureArrow);
    animateArrows(doorArrowSmall);
    animateArrows(textBubbleArrow);
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

  @FXML
  private void onClickStartGame() {
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
    startPane.setVisible(false);
    Stage stage = (Stage) startPane.getScene().getWindow();
    stage.setOnCloseRequest(
        event -> {
          timeline.stop();
          textToSpeech.terminate();
          Platform.exit();
        });
  }

  @FXML
  private void digitOneIncrement() {
    int digit = Integer.parseInt(digitOne.getText());
    digit = (digit + 1) % 10;
    digitOne.setText(Integer.toString(digit));
  }

  @FXML
  private void digitOneDecrease() {
    int digit = Integer.parseInt(digitOne.getText());
    digit = (digit - 1 + 10) % 10;
    digitOne.setText(Integer.toString(digit));
  }

  @FXML
  private void digitTwoIncrement() {
    int digit = Integer.parseInt(digitTwo.getText());
    digit = (digit + 1) % 10;
    digitTwo.setText(Integer.toString(digit));
  }

  @FXML
  private void digitTwoDecrease() {
    int digit = Integer.parseInt(digitTwo.getText());
    digit = (digit - 1 + 10) % 10;
    digitTwo.setText(Integer.toString(digit));
  }

  @FXML
  private void digitThreeIncrement() {
    int digit = Integer.parseInt(digitThree.getText());
    digit = (digit + 1) % 10;
    digitThree.setText(Integer.toString(digit));
  }

  @FXML
  private void digitThreeDecrease() {
    int digit = Integer.parseInt(digitThree.getText());
    digit = (digit - 1 + 10) % 10;
    digitThree.setText(Integer.toString(digit));
  }

  @FXML
  private void digitFourIncrement() {
    int digit = Integer.parseInt(digitFour.getText());
    digit = (digit + 1) % 10;
    digitFour.setText(Integer.toString(digit));
  }

  @FXML
  private void digitFourDecrease() {
    int digit = Integer.parseInt(digitFour.getText());
    digit = (digit - 1 + 10) % 10;
    digitFour.setText(Integer.toString(digit));
  }

  @FXML
  private void onClickOpenPadlock() {
    int digitOneInt = Integer.parseInt(digitOne.getText());
    int digitTwoInt = Integer.parseInt(digitTwo.getText());
    int digitThreeInt = Integer.parseInt(digitThree.getText());
    int digitFourInt = Integer.parseInt(digitFour.getText());
    if (digitOneInt == 0 && digitTwoInt == 1 && digitThreeInt == 9 && digitFourInt == 2) {
      Scene scene = openButton.getScene();
      GameState.isWon = true;
      scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.ENDWON));
    } else {
      showDialog("Wrong combination", "Try again", "The padlock did not open.");
    }
  }

  @FXML
  private void onClickExitPadlock() {
    padlockPane.setVisible(false);
  }

  private void updateTimerLabel() {
    int minutes = GameState.secondsRemaining / 60;
    int seconds = GameState.secondsRemaining % 60;
    timerLabel.setText(String.format("%d:%02d", minutes, seconds));
  }

  private void handleTimerExpired() {
    if (!GameState.isWon) {
      Scene scene = door.getScene();
      scene.setRoot(SceneManager.getUiRoot(AppUi.END_LOST));
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
  private void onClickExitConverterView() {
    converterPane.setVisible(false);
  }

  @FXML
  public void toiletMouseEntered() {
    if (GameState.isRiddleResolved()) {
      toiletBig.setOpacity(1);
    }
  }

  @FXML
  public void toiletMouseExit() {
    toiletBig.setOpacity(0);
  }

  @FXML
  public void toiletPaperMouseEntered() {
    if (GameState.isRiddleResolved()) {
      toiletPaperBig.setOpacity(1);
    }
  }

  @FXML
  public void toiletPaperMouseExit() {
    toiletPaperBig.setOpacity(0);
  }

  @FXML
  public void doorMouseEntered() {
    doorBig.setOpacity(1);
  }

  @FXML
  public void doorMouseExit() {
    doorBig.setOpacity(0);
  }

  @FXML
  public void ventMouseEntered() {
    if (GameState.isRiddleResolved()) {
      ventBig.setOpacity(1);
    }
  }

  @FXML
  public void ventMouseExit() {
    ventBig.setOpacity(0);
  }

  @FXML
  public void postersMouseEntered() {
    if (GameState.isRiddleResolved()) {
      postersBig.setOpacity(1);
    }
  }

  @FXML
  public void postersMouseExit() {
    postersBig.setOpacity(0);
  }

  @FXML
  public void bedsideTableMouseEntered() {
    if (GameState.isRiddleResolved()) {
      bedsideTableBig.setOpacity(1);
    }
  }

  @FXML
  public void bedsideTableMouseExit() {
    bedsideTableBig.setOpacity(0);
  }

  @FXML
  public void sinkMouseEntered() {
    if (GameState.isRiddleResolved()) {
      sinkBig.setOpacity(1);
    }
  }

  @FXML
  public void sinkMouseExit() {
    sinkBig.setOpacity(0);
  }

  @FXML
  public void mirrorMouseEntered() {
    if (GameState.isRiddleResolved()) {
      mirrorBig.setOpacity(1);
    }
  }

  @FXML
  public void mirrorMouseExit() {
    mirrorBig.setOpacity(0);
  }

  @FXML
  public void towelMouseEntered() {
    if (GameState.isRiddleResolved()) {
      towelBig.setOpacity(1);
    }
  }

  @FXML
  public void towelMouseExit() {
    towelBig.setOpacity(0);
  }

  @FXML
  public void windowMouseEntered() {
    if (GameState.isRiddleResolved()) {
      windowBig.setOpacity(1);
    }
  }

  @FXML
  public void windowMouseExit() {
    windowBig.setOpacity(0);
  }

  @FXML
  public void pictureMouseEntered() {
    if (GameState.isRiddleResolved()) {
      pictureBig.setOpacity(1);
    }
  }

  @FXML
  public void pictureMouseExit() {
    pictureBig.setOpacity(0);
  }

  @FXML
  public void textBubbleMouseEntered() {
    bubbleBig.setOpacity(1);
  }

  @FXML
  public void textBubbleMouseExit() {
    bubbleBig.setOpacity(0);
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
      showDialog("Info", "The door is padlocked shut!", "You must find the passcode to escape!");
      padlockPane.setVisible(true);
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
            "On the inside of the toilet is written the word " + "JAIL");
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
            "Nice Job", "You found the item!", "On the toilet paper is written the word " + "JAIL");
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
            "In the vent you notice a piece of paper, scribbled on one side is the word " + "JAIL");
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
            "In the sink you notice scribbled on the side is the word " + "JAIL");
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
            "On the mirror you notice a word written in ink: " + "JAIL");
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
            "You notice that scribbled on one side of the towel is the word " + "JAIL");
      } else {
        showDialog("Nothing!", "Towel", "Just a normal towel");
      }
    }
  }

  @FXML
  public void clickBedsideTable(MouseEvent event) {
    bedsideTableArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (itemCode == bedsideTable) {
        converterPane.setVisible(true);

      } else {
        showDialog("Nothing!", "Bedside Table", "Nothing inside...");
      }
    }
  }

  @FXML
  public void clickWindow(MouseEvent event) {
    windowArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (itemCode == window) {
        converterPane.setVisible(true);
      } else {
        showDialog("Nothing!", "Window", "Just a normal window");
      }
    }
  }

  @FXML
  public void clickPicture(MouseEvent event) {
    pictureArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (itemCode == picture) {
        converterPane.setVisible(true);
      } else {
        showDialog("Nothing!", "Picture", "Just a normal picture");
      }
    }
  }

  @FXML
  public void clickPosters(MouseEvent event) {
    postersArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (itemCode == posters) {
        converterPane.setVisible(true);
      } else {
        showDialog("Nothing!", "Posters", "Just some posters");
      }
    }
  }

  @FXML
  private void onTextBubbleClicked() throws ApiProxyException {
    textBubbleArrow.setOpacity(0);
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
}
