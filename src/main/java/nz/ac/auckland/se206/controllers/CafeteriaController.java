package nz.ac.auckland.se206.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GptAndTextAreaManager;
import nz.ac.auckland.se206.GptAndTextAreaManager.Characters;
import nz.ac.auckland.se206.MovementControl;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SoundEffect;
import nz.ac.auckland.se206.reseters.RandomizationGenerator;

/** This class controls the cafeteria view. */
public class CafeteriaController {

  @FXML private Rectangle paintingWithSafe;
  @FXML private Rectangle paintingWithoutSafe;
  @FXML private Rectangle vendingMachine;
  @FXML private ImageView paintingWithSafeBig;
  @FXML private ImageView paintingWithoutSafeBig;
  @FXML private ImageView safe;
  @FXML private ImageView safeBig;
  @FXML private ImageView vendingMachineBig;
  @FXML private Pane padlockPane;
  @FXML private ImageView digitOnePlus;
  @FXML private ImageView digitOneMinus;
  @FXML private ImageView digitTwoPlus;
  @FXML private ImageView digitTwoMinus;
  @FXML private ImageView digitThreePlus;
  @FXML private ImageView digitThreeMinus;
  @FXML private ImageView digitFourPlus;
  @FXML private ImageView digitFourMinus;
  @FXML private ImageView prisonerOne;
  @FXML private ImageView prisonerTwo;
  @FXML private ImageView speechBubbleOne;
  @FXML private ImageView speechBubbleTwo;
  @FXML private ImageView cog;
  @FXML private ImageView speechBubbleThree;
  @FXML private ImageView speechBubbleOneSmall;
  @FXML private ImageView speechBubbleTwoSmall;

  @FXML private ImageView speechBubbleThreeSmall;
  @FXML private ImageView thinkingOne;
  @FXML private ImageView thinkingTwo;
  @FXML private ImageView thinkingThree;

  @FXML private Label digitOne;
  @FXML private Label digitTwo;
  @FXML private Label digitThree;
  @FXML private Label digitFour;
  @FXML private Button openButton;

  @FXML private ImageView cross;

  @FXML private Pane paperPane;
  @FXML private Label collectPaperLabel;
  @FXML private Label numberLabel;

  @FXML private ImageView paintingWithSafeArrow;
  @FXML private ImageView paintingWithoutSafeArrow;
  @FXML private ImageView vendingMachineArrow;
  @FXML private Label timerLabel;

  @FXML private Pane thoughtBubblePane;
  @FXML private Text thoughtBubbleText;

  @FXML private Pane inspectingVendingPane;
  @FXML private Pane inspectingPaintingPane;
  @FXML private Pane blurredPane;

  @FXML private Rectangle safeRectangle;

  private ImageView[] animationItems;
  private SoundEffect safeOpeningNoise;
  private Timeline timeline;
  private SoundEffect lostSound = new SoundEffect("src/main/resources/sounds/Lost.mp3");

  /** Initializes the cafeteria view, it is called when the room loads. */
  @FXML
  private void initialize() {

    // starts a thread to check if the cafeteria needs to be reset
    resetchecker();
    GptAndTextAreaManager.cafeteriaController = this;

    // creates a array of all the items that need to be animated
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

    RandomizationGenerator.createPhoneNunber();
    numberLabel.setText(GameState.phoneNumber);

    // animates all the arrows in the scene
    animateArrows(paintingWithSafeArrow);
    animateArrows(paintingWithoutSafeArrow);
    animateArrows(vendingMachineArrow);

    // plays the sound of the safe opening
    safeOpeningNoise =
        new SoundEffect("src/main/resources/sounds/door-opening-and-closing-18398.mp3");
  }

  /** This method is called when the user clicks on the vending machine. */
  @FXML
  private void onClickVendingMachine(MouseEvent event) {
    blurredPane.setVisible(true);
    inspectingVendingPane.setVisible(true);
    thoughtBubblePane.setVisible(true);
    thoughtBubbleText.setText("Hmm, there's nothing here");
    vendingMachineArrow.setVisible(false);
  }

  /** This method is called when the user clicks on the picture with a safe. */
  @FXML
  private void onClickPaintingWithSafe(MouseEvent event) {
    // set all relevant nodes to visible
    safeRectangle.setVisible(true);
    safe.setVisible(true);
    paintingWithSafe.setVisible(false);
    paintingWithSafeArrow.setVisible(false);
    // set the safe to found
    GameState.isSafeFoundProperty().set(true);
    GameState.safeFound = true;
  }

  /** This method is called when the user clicks on the picture without a safe. */
  @FXML
  private void onClickPaintingWithoutSafe(MouseEvent event) {
    blurredPane.setVisible(true);
    inspectingPaintingPane.setVisible(true);
    thoughtBubblePane.setVisible(true);
    thoughtBubbleText.setText("Hmm, there's nothing here");
    paintingWithoutSafeArrow.setVisible(false);
  }

  @FXML
  private void onClickInspectingVendingPane() {
    blurredPane.setVisible(false);
    inspectingVendingPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
  }

  @FXML
  private void onClickInspectingPaintingPane() {
    blurredPane.setVisible(false);
    inspectingPaintingPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
  }

  @FXML
  private void onSafeClick(MouseEvent event) {
    // Shows the safe pane
    blurredPane.setVisible(true);
    padlockPane.setVisible(true);
    safeBig.setVisible(false);
  }

  @FXML
  private void onBlurredPaneClicked() {
    blurredPane.setVisible(false);
    padlockPane.setVisible(false);
    inspectingVendingPane.setVisible(false);
    inspectingPaintingPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
  }

  @FXML
  private void safeMouseEntered() {
    // Shows the enlarged safe
    safeBig.setVisible(true);
  }

  @FXML
  private void safeMouseExited() {
    // Hides the enlarged safe
    safeBig.setVisible(false);
  }

  @FXML
  private void vendingMachineMouseEntered() {
    // Shows the enlarged vending machine
    vendingMachineBig.setVisible(true);
  }

  @FXML
  private void vendingMachineMouseExited() {
    // Hides the enlarged vending machine
    vendingMachineBig.setVisible(false);
  }

  @FXML
  private void paintingWithSafeMouseEntered() {
    // Shows the enlarged painting with safe
    paintingWithSafeBig.setVisible(true);
  }

  @FXML
  private void paintingWithSafeMouseExited() {
    // Hides the enlarged painting with safe
    paintingWithSafeBig.setVisible(false);
  }

  @FXML
  private void paintingWithoutSafeMouseEntered() {
    // Shows the enlarged painting without safe
    paintingWithoutSafeBig.setVisible(true);
  }

  @FXML
  private void paintingWithoutSafeMouseExited() {
    // Hides the enlarged painting without safe
    paintingWithoutSafeBig.setVisible(false);
  }

  /**
   * When the user clicks on the plus up of the first digit, the digit is incremented by one.
   *
   * @param event the mouse event from when the user clicks on the plus up of the first digit
   */
  @FXML
  private void digitOneIncrement(MouseEvent event) {
    thoughtBubblePane.setVisible(false);

    int digit = Integer.parseInt(digitOne.getText());
    digit = (digit + 1) % 10;
    digitOne.setText(Integer.toString(digit));
  }

  /** When the user clicks on the minus down of the first digit, the digit is decremented by one. */
  @FXML
  private void digitOneDecrease() {
    thoughtBubblePane.setVisible(false);
    int digit = Integer.parseInt(digitOne.getText());
    digit = (digit - 1 + 10) % 10;
    digitOne.setText(Integer.toString(digit));
  }

  /**
   * When the user clicks on the plus up of the second digit, the digit is incremented by one.
   *
   * @param event the mouse event from when the user clicks on the plus up of the second digit
   */
  @FXML
  private void digitTwoIncrement(MouseEvent event) {
    thoughtBubblePane.setVisible(false);
    int digit = Integer.parseInt(digitTwo.getText());
    digit = (digit + 1) % 10;
    digitTwo.setText(Integer.toString(digit));
  }

  /**
   * When the user clicks on the minus down of the second digit, the digit is decremented by one.
   *
   * @param event the mouse event from when the user clicks on the minus down of the second digit
   */
  @FXML
  private void digitTwoDecrease(MouseEvent event) {
    thoughtBubblePane.setVisible(false);
    int digit = Integer.parseInt(digitTwo.getText());
    digit = (digit - 1 + 10) % 10;
    digitTwo.setText(Integer.toString(digit));
  }

  /**
   * When the user clicks on the plus up of the third digit, the digit is incremented by one.
   *
   * @param event the mouse event from when the user clicks on the plus up of the third digit
   */
  @FXML
  private void digitThreeIncrement(MouseEvent event) {
    thoughtBubblePane.setVisible(false);
    int digit = Integer.parseInt(digitThree.getText());
    digit = (digit + 1) % 10;
    digitThree.setText(Integer.toString(digit));
  }

  /**
   * When the user clicks on the minus down of the third digit, the digit is decremented by one.
   *
   * @param event the mouse event from when the user clicks on the minus down of the third digit
   */
  @FXML
  private void digitThreeDecrease(MouseEvent event) {
    thoughtBubblePane.setVisible(false);
    int digit = Integer.parseInt(digitThree.getText());
    digit = (digit - 1 + 10) % 10;
    digitThree.setText(Integer.toString(digit));
  }

  /**
   * When the user clicks on the plus up of the fourth digit, the digit is incremented by one.
   *
   * @param event the mouse event from when the user clicks on the plus up of the fourth digit
   */
  @FXML
  private void digitFourIncrement(MouseEvent event) {
    thoughtBubblePane.setVisible(false);
    int digit = Integer.parseInt(digitFour.getText());
    digit = (digit + 1) % 10;
    digitFour.setText(Integer.toString(digit));
  }

  /**
   * When the user clicks on the minus down of the fourth digit, the digit is decremented by one.
   *
   * @param event the mouse event from when the user clicks on the minus down of the fourth digit
   */
  @FXML
  private void digitFourDecrease(MouseEvent event) {
    thoughtBubblePane.setVisible(false);
    int digit = Integer.parseInt(digitFour.getText());
    digit = (digit - 1 + 10) % 10;
    digitFour.setText(Integer.toString(digit));
  }

  /**
   * When the user clicks on the open button, the padlock is opened if the combination is correct.
   */
  @FXML
  private void onClickOpenPadlock() {
    int digitOneInt = Integer.parseInt(digitOne.getText());
    int digitTwoInt = Integer.parseInt(digitTwo.getText());
    int digitThreeInt = Integer.parseInt(digitThree.getText());
    int digitFourInt = Integer.parseInt(digitFour.getText());
    int code = digitOneInt * 1000 + digitTwoInt * 100 + digitThreeInt * 10 + digitFourInt;
    if (code == Integer.parseInt(GameState.code)) {
      // Gives the player the next clue
      padlockPane.setVisible(false);
      paperPane.setVisible(true);
      GameState.safeUnlocked = true;

      // plays the sound of the safe opening
      safeOpeningNoise.playSfx();

    } else {
      thoughtBubblePane.setVisible(true);
      thoughtBubbleText.setText("Hmm, that code didn't work");
    }
  }

  /**
   * This method enlarges the speech bubble for prisoner one when the mouse enters the speech
   * bubble.
   */
  @FXML
  private void onSetSpeechBubbleOneUp() {
    speechBubbleOne.setVisible(true);
  }

  /**
   * This method shrinks the speech bubble for prisoner one when the mouse exits the speech bubble.
   */
  @FXML
  private void onSetSpeechBubbleOneDown() {
    speechBubbleOne.setVisible(false);
  }

  /** This method enlarges the speech bubble for prisoner two when the mouse enters the speech. */
  @FXML
  private void onSetSpeechBubbleTwoUp() {
    speechBubbleTwo.setVisible(true);
  }

  /**
   * This method shrinks the speech bubble for prisoner two when the mouse exits the speech bubble.
   */
  @FXML
  private void onSetSpeechBubbleTwoDown() {
    speechBubbleTwo.setVisible(false);
  }

  /**
   * This method enlarges the speech bubble for the guard when the mouse enters the speech bubble.
   */
  @FXML
  private void onSetSpeechBubbleThreeUp() {
    speechBubbleThree.setVisible(true);
  }

  /** This method shrinks the speech bubble for the guard when the mouse exits the speech bubble. */
  @FXML
  private void onSetSpeechBubbleThreeDown() {
    speechBubbleThree.setVisible(false);
  }

  /** This method allows the player to leave the view with the padlock. */
  @FXML
  private void onClickExitPadlock() {
    thoughtBubblePane.setVisible(false);
    padlockPane.setVisible(false);
    if (paperPane.isVisible()) {
      GameState.setSafeClosed(true);
    }
  }

  /** This method collects the phone number and makes the navigates back to the room. */
  @FXML
  private void onClickCollectPaper() {
    GameState.hasPaperProperty().set(true);
    GameState.setSafeClosed(true);
    blurredPane.setVisible(false);
    paperPane.setVisible(false);
  }

  /** This method makes the test blue when the mouse enters the text. */
  @FXML
  private void collectPaperMouseEntered() {
    collectPaperLabel.setStyle("-fx-text-fill: blue;");
  }

  /** This method makes the test black when the mouse exits the text. */
  @FXML
  private void collectPaperMouseExited() {
    collectPaperLabel.setStyle("-fx-text-fill: black;");
  }

  /**
   * This method switchs the conversation to the prisoner one when the player clicks on the speech
   * bubble.
   *
   * @param event the mouse event from when the user clicks on the speech bubble
   */
  @FXML
  private void onSpeechBubbleOneClicked(MouseEvent event) {
    GptAndTextAreaManager.displayTarget(Characters.PRISONER_ONE);
    System.out.println("Speech bubble one clicked");
  }

  /**
   * This method switchs the conversation to the prisoner two when the player clicks on the speech
   * bubble.
   *
   * @param event the mouse event from when the user clicks on the speech bubble
   */
  @FXML
  private void onSpeechBubbleTwoClicked(MouseEvent event) {
    GptAndTextAreaManager.displayTarget(Characters.PRISONER_TWO);
    System.out.println("Speech bubble two clicked");
  }

  /**
   * This method switchs the conversation to the guard when the player clicks on the speech bubble.
   *
   * @param event the mouse event from when the user clicks on the speech bubble
   */
  @FXML
  private void onSpeechBubbleThreeClicked(MouseEvent event) {
    GameState.setGuardTalked(true);
    GptAndTextAreaManager.displayTarget(Characters.GUARD);
    System.out.println("Speech bubble three clicked");
  }

  /** This method enlages the cross when the mouse enters the cross. */
  @FXML
  private void crossMouseEntered() {
    cross.scaleXProperty().set(1.15);
    cross.scaleYProperty().set(1.15);
  }

  /** This method shrinks the cross when the mouse exits the cross. */
  @FXML
  private void crossMouseExited() {
    cross.scaleXProperty().set(1);
    cross.scaleYProperty().set(1);
  }

  /** This method resets the cafeteria to its initial state. */
  private void resetCafeteria() {
    // sets visablity of the images
    padlockPane.setVisible(false);
    inspectingPaintingPane.setVisible(false);
    blurredPane.setVisible(false);
    inspectingVendingPane.setVisible(false);
    safe.setVisible(false);
    paintingWithSafe.setVisible(true);
    paintingWithoutSafe.setVisible(true);
    vendingMachine.setVisible(true);
    paintingWithSafeBig.setVisible(false);
    paintingWithoutSafeBig.setVisible(false);
    safeBig.setVisible(false);
    vendingMachineBig.setVisible(false);

    // resets the digits of the padlock
    digitOne.setText("0");
    digitTwo.setText("0");
    digitThree.setText("0");
    digitFour.setText("0");

    // resets the arrows
    paintingWithSafeArrow.setVisible(true);
    paintingWithoutSafeArrow.setVisible(true);
    vendingMachineArrow.setVisible(true);
    GameState.safeFound = false;
    GameState.safeUnlocked = false;
    GameState.resetCafeteria = false;
    System.out.println("cafeteria reseted");

    // chooses a new phone number
    RandomizationGenerator.createPhoneNunber();
    numberLabel.setText(GameState.phoneNumber);
  }

  /**
   * This method animates the arrows in the scene.
   *
   * @param arrow the arrow to animate
   */
  public void animateArrows(ImageView arrow) {

    // sets the start position of the arrow
    double startY = 0;

    // makes the arrow bounce up and down
    TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), arrow);
    translateTransition.setFromY(startY);
    translateTransition.setToY(startY + 5);
    translateTransition.setAutoReverse(true);
    translateTransition.setCycleCount(Animation.INDEFINITE);
    translateTransition.play();
  }

  /**
   * This method starts a thread that checks if the cafeteria needs to be reset. It also changes to
   * the end lost scene if the timer ran out in the cafeteria.
   */
  private void resetchecker() {
    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(0.1),
                event -> {
                  if (GameState.secondsRemaining >= 0) {
                    // updates the timer label
                    updateTimerLabel();
                  }
                  if (GameState.secondsRemaining == 0) {
                    if (SceneManager.currentUi == SceneManager.AppUi.CAFETERIA) {
                      GameState.resetCafeteria = true;
                      try {
                        // switches to the lost screen
                        Scene scene = cog.getScene();
                        SceneManager.switchToEndLost(scene);

                      } catch (NullPointerException e) {
                        e.printStackTrace();
                      }
                    }
                  }
                  updateTimerLabel();
                  if (GameState.resetCafeteria) {
                    resetCafeteria();
                  }
                }));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  /** This method updates the timer label. */
  private void updateTimerLabel() {
    int minutes = (GameState.secondsRemaining) / 60;
    int seconds = (GameState.secondsRemaining) % 60;
    timerLabel.setText(String.format("%d:%02d", minutes, seconds));
  }

  @FXML
  private void onClickCog(MouseEvent event) {
    GameState.setSettingsVisable(true);
  }

  @FXML
  private void cogMouseEntered() {
    // shows the enlarged cog
    cog.setScaleX(1.2);
    cog.setScaleY(1.2);
  }

  @FXML
  private void cogMouseExited() {
    // hides the enlarged cog
    cog.setScaleX(1);
    cog.setScaleY(1);
  }

  /** This method animates the items in the scene. */
  public void walkInAnimation() {
    MovementControl.moveToLeft(true, 1, 500, animationItems);
  }

  /** This method resets the start location of the items in the scene. */
  public void resetAnimation() {
    for (ImageView item : animationItems) {
      item.setTranslateX(500);
    }
  }

  /** This method shows the thinking emoji for the prisoner one. */
  public void setThinkingOneUp() {
    thinkingOne.setVisible(true);
  }

  /** This method hides the thinking emoji for the prisoner one. */
  public void setThinkingOneDown() {
    thinkingOne.setVisible(false);
  }

  /** This method shows the thinking emoji for the prisoner two. */
  public void setThinkingTwoUp() {
    thinkingTwo.setVisible(true);
  }

  /** This method hides the thinking emoji for the prisoner two. */
  public void setThinkingTwoDown() {
    thinkingTwo.setVisible(false);
  }

  /** This method shows the thinking emoji for the guard. */
  public void setThinkingThreeUp() {
    thinkingThree.setVisible(true);
  }

  /** This method hides the thinking emoji for the guard. */
  public void setThinkingThreeDown() {
    thinkingThree.setVisible(false);
  }
}
