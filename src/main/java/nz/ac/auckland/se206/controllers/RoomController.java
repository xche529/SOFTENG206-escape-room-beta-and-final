package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GptAndTextAreaManager;
import nz.ac.auckland.se206.GptAndTextAreaManager.Characters;
import nz.ac.auckland.se206.MovementControl;
import nz.ac.auckland.se206.Safe;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.speech.TextToSpeech;

/** Controller class for the room view. */
public class RoomController {
  @FXML private Pane room;
  @FXML private Pane converterPane;
  @FXML private Pane chatPane;
  @FXML private Pane startPane;
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
  @FXML private ImageView doorArrowSmall;
  @FXML private ImageView bubbleBig;
  @FXML private ImageView prisonerOne;
  @FXML private ImageView prisonerTwo;
  @FXML private ImageView speechBubbleOne;
  @FXML private ImageView speechBubbleTwo;
  @FXML private ImageView speechBubbleOneSmall;
  @FXML private ImageView speechBubbleTwoSmall;
  @FXML private ImageView thinkingOne;
  @FXML private ImageView thinkingTwo;
  @FXML private Label timerLabel;
  @FXML private Label chatProgressLabel;
  @FXML private Label overQuestionLimitLabel;
  @FXML private Label questionInfoLabel;
  @FXML private Button exitViewButton;
  @FXML private Button openButton;
  @FXML private Button sendButton;
  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private ProgressIndicator chatProgress;
  @FXML private Pane blurredPane;
  @FXML private Pane inspectingToiletPane;
  @FXML private Pane thoughtBubblePane;
  @FXML private Text thoughtBubbleText;
  @FXML private Label toiletWordLabel;
  @FXML private Label toiletPaperWordLabel;
  @FXML private Pane inspectingToiletPaperPane;
  @FXML private Pane inspectingVentPane;
  @FXML private Label ventWordLabel;
  @FXML private Pane inspectingSinkPane;
  @FXML private Label sinkWordLabel;
  @FXML private Pane inspectingMirrorPane;
  @FXML private Label mirrorWordLabel;
  @FXML private Pane inspectingTowelPane;
  @FXML private Label towelWordLabel;

  private Timeline timeline;
  private TextToSpeech textToSpeech;
  private ImageView[] animationItems = null;

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws ApiProxyException
   * @throws IOException
   */
  public void initialize() throws ApiProxyException, IOException {

    // initialize fields in the GptAndTextAreaManager class
    GptAndTextAreaManager.roomController = this;

    animationItems =
        new ImageView[] {
          prisonerOne,
          prisonerTwo,
          speechBubbleOne,
          speechBubbleTwo,
          speechBubbleOneSmall,
          speechBubbleTwoSmall,
          thinkingOne,
          thinkingTwo
        };
    resetAnimation();

    itemToChoose();
    // Setting up the timer timeline

  }

  /*
   * This method starts a thread that checks if the room needs to be reset.
   *
   * @throws IOException
   */
  public void start() throws IOException {

    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  if (GameState.stopTimer) {
                    timeline.stop();
                  }
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
    doorArrow.setVisible(false);
  }

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
                    if (SceneManager.curretUi == SceneManager.AppUi.ROOM) {
                      GameState.secondsRemaining = -1;
                      GameState.resetCafeteria = true;
                      GameState.resetOffice = true;
                      GameState.resetRoom = true;
                      try {
                        Scene scene = sink.getScene();
                        Parent parent = SceneManager.getUiRoot(SceneManager.AppUi.END_LOST);
                        parent.setLayoutX(App.centerX);
                        parent.setLayoutY(App.centerY);
                        scene.setRoot(parent);
                      } catch (NullPointerException e) {
                      }
                    }
                  }

                  if (GameState.resetRoom) {
                    try {
                      resetRoom();
                    } catch (ApiProxyException e) {
                    }
                  }
                }));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  private void resetRoom() throws ApiProxyException {
    itemToChoose();
    Safe.getRandomCode();
    toiletArrow.setOpacity(0);
    toiletPaperArrow.setOpacity(0);
    ventArrow.setOpacity(0);
    sinkArrow.setOpacity(0);
    mirrorArrow.setOpacity(0);
    towelArrow.setOpacity(0);
    doorArrowSmall.setOpacity(0);
    // animateArrows(doorArrow);
    GameState.setRiddleResolved(false);
    GptAndTextAreaManager.reset();
    GameState.wordFound = false;
    GameState.resetRoom = false;
    System.out.println("room reseted");
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
    // animateArrows(doorArrowSmall);
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
    }
  }

  public void walkInAnimation() {
    MovementControl.moveToLeft(true, 1, 500, animationItems);
  }

  public void resetAnimation() {
    for (ImageView item : animationItems) {
      item.setTranslateX(500);
    }
  }

  public void setThinkingOneUp() {
    thinkingOne.setVisible(true);
  }

  public void setThinkingOneDown() {
    thinkingOne.setVisible(false);
  }

  public void setThinkingTwoUp() {
    thinkingTwo.setVisible(true);
  }

  public void setThinkingTwoDown() {
    thinkingTwo.setVisible(false);
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
  public void onClickInspectingToiletPane() {
    blurredPane.setVisible(false);
    inspectingToiletPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
  }

  @FXML
  public void clickToilet(MouseEvent event) {
    toiletArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (GameState.itemToChoose == toilet) {
        blurredPane.setVisible(true);
        inspectingToiletPane.setVisible(true);
        toiletWordLabel.setText(GameState.codeWord);
        thoughtBubblePane.setVisible(true);
        thoughtBubbleText.setText("What's that scratched onto the rim?");
        GameState.wordFound = true;
      } else {
        blurredPane.setVisible(true);
        inspectingToiletPane.setVisible(true);
        thoughtBubblePane.setVisible(true);
        thoughtBubbleText.setText("Hmm... Can't see anything here");
      }
    }
  }

  @FXML
  public void onClickInspectingToiletPaperPane() {
    blurredPane.setVisible(false);
    inspectingToiletPaperPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
  }

  @FXML
  public void clickToiletPaper(MouseEvent event) {
    toiletPaperArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (GameState.itemToChoose == toiletPaper) {
        blurredPane.setVisible(true);
        inspectingToiletPaperPane.setVisible(true);
        toiletPaperWordLabel.setText(GameState.codeWord);
        thoughtBubblePane.setVisible(true);
        thoughtBubbleText.setText("What's that scratched onto the rim?");
        GameState.wordFound = true;
      } else {
        blurredPane.setVisible(true);
        inspectingToiletPaperPane.setVisible(true);
        thoughtBubblePane.setVisible(true);
        thoughtBubbleText.setText("Hmm... Can't see anything here");
      }
    }
  }

  @FXML
  public void onClickInspectingVentPane() {
    blurredPane.setVisible(false);
    inspectingVentPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
  }

  @FXML
  public void clickVent(MouseEvent event) {
    ventArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (GameState.itemToChoose == vent) {

        blurredPane.setVisible(true);
        inspectingVentPane.setVisible(true);
        ventWordLabel.setText(GameState.codeWord);
        thoughtBubblePane.setVisible(true);
        thoughtBubbleText.setText("What's that scratched onto the vent?");
        GameState.wordFound = true;

      } else {
        blurredPane.setVisible(true);
        inspectingVentPane.setVisible(true);
        thoughtBubblePane.setVisible(true);
        thoughtBubbleText.setText("Hmm... Can't see anything here");
      }
    }
  }

  @FXML
  public void onClickInspectingSinkPane() {
    blurredPane.setVisible(false);
    inspectingSinkPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
  }

  @FXML
  public void clickSink(MouseEvent event) {
    sinkArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (GameState.itemToChoose == sink) {

        blurredPane.setVisible(true);
        inspectingSinkPane.setVisible(true);
        sinkWordLabel.setText(GameState.codeWord);
        thoughtBubblePane.setVisible(true);
        thoughtBubbleText.setText("What's that scratched onto the rim?");
        GameState.wordFound = true;

      } else {
        blurredPane.setVisible(true);
        inspectingSinkPane.setVisible(true);
        thoughtBubblePane.setVisible(true);
        thoughtBubbleText.setText("Hmm... Can't see anything here");
      }
    }
  }

  @FXML
  public void onClickInspectingMirrorPane() {
    blurredPane.setVisible(false);
    inspectingMirrorPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
  }

  @FXML
  public void clickMirror(MouseEvent event) {
    mirrorArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (GameState.itemToChoose == mirror) {

        blurredPane.setVisible(true);
        inspectingMirrorPane.setVisible(true);
        mirrorWordLabel.setText(GameState.codeWord);
        thoughtBubblePane.setVisible(true);
        thoughtBubbleText.setText("What's that on the mirror?");
        GameState.wordFound = true;

      } else {
        blurredPane.setVisible(true);
        inspectingMirrorPane.setVisible(true);
        thoughtBubblePane.setVisible(true);
        thoughtBubbleText.setText("Hmm... Can't see anything here");
      }
    }
  }

  @FXML
  public void onClickInspectingTowelPane() {
    blurredPane.setVisible(false);
    inspectingTowelPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
  }

  @FXML
  public void clickTowel(MouseEvent event) {
    towelArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (GameState.itemToChoose == towel) {

        blurredPane.setVisible(true);
        inspectingTowelPane.setVisible(true);
        towelWordLabel.setText(GameState.codeWord);
        thoughtBubblePane.setVisible(true);
        thoughtBubbleText.setText("What's that written on the towel?");
        GameState.wordFound = true;

      } else {
        blurredPane.setVisible(true);
        inspectingTowelPane.setVisible(true);
        thoughtBubblePane.setVisible(true);
        thoughtBubbleText.setText("Hmm... Can't see anything here");
      }
    }
  }

  @FXML
  private void onSpeechBubbleOneClicked() {
    GptAndTextAreaManager.displayTarget(Characters.PRISONER_ONE);
    System.out.println("Speech bubble one clicked");
  }

  @FXML
  private void onSpeechBubbleTwoClicked() {
    GptAndTextAreaManager.displayTarget(Characters.PRISONER_TWO);
    System.out.println("Speech bubble two clicked");
  }

  @FXML
  private void onSetSpeechBubbleOneUp() {
    speechBubbleOne.setVisible(true);
  }

  @FXML
  private void onSetSpeechBubbleOneDown() {
    speechBubbleOne.setVisible(false);
  }

  @FXML
  private void onSetSpeechBubbleTwoUp() {
    speechBubbleTwo.setVisible(true);
  }

  @FXML
  private void onSetSpeechBubbleTwoDown() {
    speechBubbleTwo.setVisible(false);
  }
}
