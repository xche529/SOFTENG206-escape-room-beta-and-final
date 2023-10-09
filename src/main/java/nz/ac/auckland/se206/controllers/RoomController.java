package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import nz.ac.auckland.se206.reseters.RandomizationGenerator;
import nz.ac.auckland.se206.speech.TextToSpeech;

/** Controller class for the room view. */
public class RoomController {
  @FXML private Pane room;
  @FXML private Pane converterPane;
  @FXML private Pane chatPane;
  @FXML private Pane startPane;
  @FXML private Rectangle vent;
  @FXML private Rectangle toiletPaper;
  @FXML private Rectangle toilet;
  @FXML private Rectangle sink;
  @FXML private Rectangle tap;
  @FXML private Rectangle mirror;
  @FXML private Rectangle towel;
  @FXML private ImageView toiletBig;
  @FXML private ImageView toiletPaperBig;
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
  private Rectangle[] items;

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws ApiProxyException
   * @throws IOException
   */
  public void initialize() throws ApiProxyException, IOException {

    items = new Rectangle[] {vent, toiletPaper, toilet, sink, tap, mirror, towel};

    RandomizationGenerator.randomiseWord(items);

    // initialize fields in the GptAndTextAreaManager class
    GptAndTextAreaManager.roomController = this;

    // initializes all the animation items into an array

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
        
        // moves the prisoners to their starting point
        resetAnimation();

        // runs a thread that is always checking if the room needs to be reset
        resetchecker();
        doorArrow.setVisible(false);
      }

  /**
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

                  // uses test to spech to tell the player how long they have left
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

    // wlecomes the player to the room
    textToSpeech = new TextToSpeech();
    textToSpeech.speak("Welcome to the room");
    animateArrows(doorArrow);
    // animates all the arrows if the riddle is resolved
    GameState.isRiddleResolvedProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                animateAllArrows();
              }
            });
    timeline.play();

    // runs the test to speech in its iwn thread to avoid lag
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

  }

  /**
   * This method resets the room if the player has lost.
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
                    if (SceneManager.curretUi == SceneManager.AppUi.ROOM) {

                      // prevents the reset from being called multipul times
                      GameState.secondsRemaining = -1;
                      GameState.resetCafeteria = true;
                      GameState.resetOffice = true;
                      GameState.resetRoom = true;
                      GameState.resetTextArea = true;
                      try {
                        // switches to the lost screen
                        Scene scene = sink.getScene();
                        Parent parent = SceneManager.getUiRoot(SceneManager.AppUi.END_LOST);
                        parent.setLayoutX(App.centerX);
                        parent.setLayoutY(App.centerY);
                        scene.setRoot(parent);
                      } catch (NullPointerException e) {
                        System.out.println("Null pointer exception");
                      }
                    }
                  }

                  if (GameState.resetRoom) {
                    try {
                      resetRoom();
                    } catch (ApiProxyException e) {
                      e.printStackTrace();
                    }
                  }
                }));

    // starts the thread
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  /**
   * resets the room to it's origanial state with new randomised variables.
   *
   * @throws IOException if there is an error loading the start view
   */
  private void resetRoom() throws ApiProxyException {
    // randomises the word for the riddle
    RandomizationGenerator.randomiseWord(items);

    // resets the code for the safe
    Safe.getRandomCode();

    // sets all the little arrows to invisible
    toiletArrow.setOpacity(0);
    toiletPaperArrow.setOpacity(0);
    ventArrow.setOpacity(0);
    sinkArrow.setOpacity(0);
    mirrorArrow.setOpacity(0);
    towelArrow.setOpacity(0);
    doorArrowSmall.setOpacity(0);
    // sets the required boolean values to false
    GameState.setRiddleResolved(false);
    GameState.wordFound = false;
    GameState.resetRoom = false;

    // resets the text area
    GptAndTextAreaManager.reset();
    System.out.println("room reseted");
  }

  /** This method animates all of the arrows in the scene */
  public void animateAllArrows() {
    animateArrows(toiletArrow);
    animateArrows(toiletPaperArrow);
    animateArrows(ventArrow);
    animateArrows(sinkArrow);
    animateArrows(mirrorArrow);
    animateArrows(towelArrow);
  }

  /** This method animates the arrows */
  public void animateArrows(ImageView arrow) {
    arrow.setOpacity(1);
    // sets the starting position of the arrow
    double startY = 0;

    // makes the arrow move up and down
    TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), arrow);
    translateTransition.setFromY(startY);
    translateTransition.setToY(startY + 5);
    translateTransition.setAutoReverse(true);
    translateTransition.setCycleCount(Animation.INDEFINITE);
    translateTransition.play();
  }

  /** This method updates the timer label */
  private void updateTimerLabel() {
    int minutes = GameState.secondsRemaining / 60;
    int seconds = GameState.secondsRemaining % 60;
    timerLabel.setText(String.format("%d:%02d", minutes, seconds));
  }

  /** This method handles the event where timer reaches 0 */
  private void handleTimerExpired() {
    if (!GameState.isWon) {
      GameState.resetCafeteria = true;
      GameState.resetOffice = true;
      GameState.resetRoom = true;
    }
  }

  /** This methos animates the prioners walking into the room */
  public void walkInAnimation() {
    MovementControl.moveToLeft(true, 1, 500, animationItems);
  }

  /** Moves the prisoners to their starting position. */
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

  @FXML
  public void toiletMouseEntered() {
    if (GameState.isRiddleResolved()) {
      // shows the enlarged image of the toilet
      toiletBig.setVisible(true);
    }
  }

  @FXML
  public void toiletMouseExit() {
    // hides the enlarged image of the toilet
    toiletBig.setVisible(false);
  }

  @FXML
  public void toiletPaperMouseEntered() {
    if (GameState.isRiddleResolved()) {
      // shows the enlarged image of the toilet paper
      toiletPaperBig.setVisible(true);
    }
  }

  @FXML
  public void toiletPaperMouseExit() {
    // hides the enlarged image of the toilet paper
    toiletPaperBig.setVisible(false);
  }

  @FXML
  public void ventMouseEntered() {
    if (GameState.isRiddleResolved()) {
      // shows the enlarged image of the vent
      ventBig.setVisible(true);
    }
  }

  @FXML
  public void ventMouseExit() {
    // hides the enlarged image of the vent
    ventBig.setVisible(false);
  }

  @FXML
  public void sinkMouseEntered() {
    if (GameState.isRiddleResolved()) {
      // shows the enlarged image of the sink
      sinkBig.setVisible(true);
    }
  }

  @FXML
  public void sinkMouseExit() {
    // hides the enlarged image of the sink
    sinkBig.setVisible(false);
  }

  @FXML
  public void mirrorMouseEntered() {
    if (GameState.isRiddleResolved()) {
      // shows the enlarged image of the mirror
      mirrorBig.setVisible(true);
    }
  }

  @FXML
  public void mirrorMouseExit() {
    // hides the enlarged image of the mirror
    mirrorBig.setVisible(false);
  }

  @FXML
  public void towelMouseEntered() {
    if (GameState.isRiddleResolved()) {
      // shows the enlarged image of the towel
      towelBig.setVisible(true);
    }
  }

  @FXML
  public void towelMouseExit() {
    // hides the enlarged image of the towel
    towelBig.setVisible(false);
  }

  @FXML
  public void textBubbleMouseEntered() {
    // shows the enlarged image of the text bubble
    bubbleBig.setVisible(true);
  }

  @FXML
  public void textBubbleMouseExit() {
    // hides the enlarged image of the text bubble
    bubbleBig.setVisible(false);
  }

  /** This method lets the user exit the blown up view of the toilet */
  @FXML
  public void onClickInspectingToiletPane() {
    // hides a blown up view of the toilet
    blurredPane.setVisible(false);
    inspectingToiletPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
  }

  /**
   * This method shows the user a blown up view of the toilet when it is clicked on
   *
   * @param event
   */
  @FXML
  public void clickToilet(MouseEvent event) {
    toiletArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (GameState.itemToChoose == toilet) {
        GameState.isCodeWordFoundProperty().set(true);
        blurredPane.setVisible(true);
        inspectingToiletPane.setVisible(true);
        toiletWordLabel.setText(GameState.codeWord);
        // shows the text bubble
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

  /** This methao allows the user to exit the blown up view of the toilet paper */
  @FXML
  public void onClickInspectingToiletPaperPane() {
    // sets the pane and its features to invisible
    blurredPane.setVisible(false);
    inspectingToiletPaperPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
  }

  /**
   * This method shows the user a blown up view of the toilet paper when it is clicked on
   *
   * @param event
   */
  @FXML
  public void clickToiletPaper(MouseEvent event) {
    toiletPaperArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (GameState.itemToChoose == toiletPaper) {
        GameState.isCodeWordFoundProperty().set(true);
        blurredPane.setVisible(true);
        inspectingToiletPaperPane.setVisible(true);
        toiletPaperWordLabel.setText(GameState.codeWord);
        // shows the text bubble
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

  /** This method allows the user to exit the blown up view of the vent */
  @FXML
  public void onClickInspectingVentPane() {
    blurredPane.setVisible(false);
    inspectingVentPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
  }

  /**
   * This method shows the user a blown up view of the vent when it is clicked on
   *
   * @param event
   */
  @FXML
  public void clickVent(MouseEvent event) {
    ventArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (GameState.itemToChoose == vent) {
        GameState.isCodeWordFoundProperty().set(true);
        // shows the blown up view of the vent with the word on it
        blurredPane.setVisible(true);
        inspectingVentPane.setVisible(true);
        ventWordLabel.setText(GameState.codeWord);
        // shows the text bubble
        thoughtBubblePane.setVisible(true);
        thoughtBubbleText.setText("What's that scratched onto the vent?");
        GameState.wordFound = true;

      } else {
        // shows the blown up view of the vent without the word on it
        blurredPane.setVisible(true);
        inspectingVentPane.setVisible(true);
        thoughtBubblePane.setVisible(true);
        thoughtBubbleText.setText("Hmm... Can't see anything here");
      }
    }
  }

  /** This method allows the user to exit the blown up view of the sink */
  @FXML
  public void onClickInspectingSinkPane() {
    blurredPane.setVisible(false);
    inspectingSinkPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
  }

  /**
   * This method shows the user a blown up view of the sink when it is clicked on
   *
   * @param event
   */
  @FXML
  public void clickSink(MouseEvent event) {
    sinkArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (GameState.itemToChoose == sink) {
        GameState.isCodeWordFoundProperty().set(true);
        blurredPane.setVisible(true);
        inspectingSinkPane.setVisible(true);
        sinkWordLabel.setText(GameState.codeWord);
        // shows the text bubble
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

  /** This method allows the user to exit the blown up view of the mirror */
  @FXML
  public void onClickInspectingMirrorPane() {
    blurredPane.setVisible(false);
    inspectingMirrorPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
  }

  /**
   * This method shows the user a blown up view of the mirror when it is clicked on
   *
   * @param event
   */
  @FXML
  public void clickMirror(MouseEvent event) {
    mirrorArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (GameState.itemToChoose == mirror) {
        GameState.isCodeWordFoundProperty().set(true);
        blurredPane.setVisible(true);
        inspectingMirrorPane.setVisible(true);
        mirrorWordLabel.setText(GameState.codeWord);
        // shows the text bubble
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

  /** This method allows the user to exit the blown up view of the towel */
  @FXML
  public void onClickInspectingTowelPane() {
    blurredPane.setVisible(false);
    inspectingTowelPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
  }

  /**
   * This method shows the user a blown up view of the towel when it is clicked on
   *
   * @param event
   */
  @FXML
  public void clickTowel(MouseEvent event) {
    towelArrow.setOpacity(0);
    if (GameState.isRiddleResolved()) {
      if (GameState.itemToChoose == towel) {
        GameState.isCodeWordFoundProperty().set(true);
        blurredPane.setVisible(true);
        inspectingTowelPane.setVisible(true);
        towelWordLabel.setText(GameState.codeWord);
        // shows the text bubble
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

  /** This methos allows the user to speak to prisoner one */
  @FXML
  private void onSpeechBubbleOneClicked() {
    GptAndTextAreaManager.displayTarget(Characters.PRISONER_ONE);
    System.out.println("Speech bubble one clicked");
  }

  /** This method allows the user to speak to prisoner two */
  @FXML
  private void onSpeechBubbleTwoClicked() {
    GptAndTextAreaManager.displayTarget(Characters.PRISONER_TWO);
    System.out.println("Speech bubble two clicked");
  }

  /** This method enlarges the prisoner one's speech bubble */
  @FXML
  private void onSetSpeechBubbleOneUp() {
    speechBubbleOne.setVisible(true);
  }

  /** This method shrinks the prisoner one's speech bubble */
  @FXML
  private void onSetSpeechBubbleOneDown() {
    speechBubbleOne.setVisible(false);
  }

  /** This method enlarges the prisoner two's speech bubble */
  @FXML
  private void onSetSpeechBubbleTwoUp() {
    speechBubbleTwo.setVisible(true);
  }

  /** This method shrinks the prisoner two's speech bubble */
  @FXML
  private void onSetSpeechBubbleTwoDown() {
    speechBubbleTwo.setVisible(false);
  }
}
