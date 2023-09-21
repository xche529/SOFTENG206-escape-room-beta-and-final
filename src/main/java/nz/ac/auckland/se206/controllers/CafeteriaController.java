package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;

import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GptAndTextAreaManager;
import nz.ac.auckland.se206.GptAndTextAreaManager.Characters;
import nz.ac.auckland.se206.MovementControl;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class CafeteriaController {

  @FXML

  private Rectangle paintingWithSafe;
  @FXML

  private Rectangle paintingWithoutSafe;
  @FXML

  private Rectangle vendingMachine;
  @FXML

  private ImageView paintingWithSafeBig;
  @FXML

  private ImageView paintingWithoutSafeBig;
  @FXML

  private ImageView safe;
  @FXML

  private ImageView safeBig;
  @FXML

  private ImageView vendingMachineBig;
  @FXML

  private Pane padlockPane;
  @FXML

  private ImageView digitOnePlus;
  @FXML

  private ImageView digitOneMinus;
  @FXML

  private ImageView digitTwoPlus;
  @FXML

  private ImageView digitTwoMinus;
  @FXML

  private ImageView digitThreePlus;
  @FXML

  private ImageView digitThreeMinus;
  @FXML

  private ImageView digitFourPlus;
  @FXML

  private ImageView digitFourMinus;
  @FXML

  private ImageView prisonerOne;
  @FXML

  private ImageView prisonerTwo;
  @FXML

  private ImageView speechBubbleOne;
  @FXML

  private ImageView speechBubbleTwo;
  @FXML

  private Label digitOne;
  @FXML

  private Label digitTwo;
  @FXML

  private Label digitThree;
  @FXML

  private Label digitFour;
  @FXML
  private Button openButton;

  @FXML
  private Pane paperPane;
  @FXML
  private Label collectPaperLabel;
  @FXML
  private Label numberLabel;

  @FXML private ImageView paintingWithSafeArrow;
  @FXML private ImageView paintingWithoutSafeArrow;
  @FXML private ImageView vendingMachineArrow;

  private OfficeController officeController;
  private RoomController roomController;
  private ImageView[] animationItems;

  public void setOfficeController(OfficeController officeController) {
    this.officeController = officeController;
  }

  public void setRoomController(RoomController roomController) {
    this.roomController = roomController;
  }

  private Timeline timeline;

  /**
   * Initializes the cafeteria view, it is called when the room loads.
   * 
   * @throws IOException
   *
   * @throws ApiProxyException
   */
  @FXML
  private void initialize() {

    resetchecker();
    GptAndTextAreaManager.cafeteriaController = this;

    animationItems = new ImageView[] { prisonerOne, prisonerTwo, speechBubbleOne, speechBubbleTwo };

    Random random = new Random();

    // Generate a random 6-digit number
    String phoneNumberInitial = Integer.toString(random.nextInt(999999 - 100000 + 1) + 100000);
    GameState.phoneNumber = "027" + " " + phoneNumberInitial.substring(0, 3) + " " + phoneNumberInitial.substring(3, 6);
    numberLabel.setText(GameState.phoneNumber);

  }

  /**
   * This method is called when the user clicks on the vending machine.
   *
   * @throws ApiProxyException
   */
  @FXML
  private void onClickVendingMachine(MouseEvent event) {
    // TODO: add code to handle click on vending machine
    vendingMachineArrow.setVisible(false);
  }

  /**
   * This method is called when the user clicks on the picture with a safe.
   *
   * @throws ApiProxyException
   */
  @FXML
  private void onClickPaintingWithSafe(MouseEvent event) {
    safe.setVisible(true);
    paintingWithSafe.setVisible(false);
    paintingWithSafeArrow.setVisible(false);
  }

  /**
   * This method is called when the user clicks on the picture without a safe.
   *
   * @throws ApiProxyException
   */
  @FXML
  private void onClickPaintingWithoutSafe(MouseEvent event) {
    // TODO: add code to handle click on picture without safe
    paintingWithoutSafeArrow.setVisible(false);
  }

  @FXML
  private void onSafeClick(MouseEvent event) {
    padlockPane.setVisible(true);
    safeBig.setVisible(false);
    
  }

  @FXML
  private void safeMouseEntered() {
    safeBig.setVisible(true);
  }

  @FXML
  private void safeMouseExited() {
    safeBig.setVisible(false);
  }

  @FXML
  private void vendingMachineMouseEntered() {
    vendingMachineBig.setVisible(true);
  }

  @FXML
  private void vendingMachineMouseExited() {
    vendingMachineBig.setVisible(false);
  }

  @FXML
  private void paintingWithSafeMouseEntered() {
    paintingWithSafeBig.setVisible(true);
  }

  @FXML
  private void paintingWithSafeMouseExited() {
    paintingWithSafeBig.setVisible(false);
  }

  @FXML
  private void paintingWithoutSafeMouseEntered() {
    paintingWithoutSafeBig.setVisible(true);
  }

  @FXML
  private void paintingWithoutSafeMouseExited() {
    paintingWithoutSafeBig.setVisible(false);
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
    int code = digitOneInt * 1000 + digitTwoInt * 100 + digitThreeInt * 10 + digitFourInt;
    if (code == Integer.parseInt(GameState.code)) {

      padlockPane.setVisible(false);
      paperPane.setVisible(true);
    } else {
      showDialog(
          "Wrong combination",
          "Try again",
          "The padlock did not open.");
    }
  }

  @FXML
  private void onClickExitPadlock() {
    padlockPane.setVisible(false);
  }

  @FXML
  private void onClickCollectPaper() {
    GameState.hasPaperProperty().set(true);
    paperPane.setVisible(false);
  }

  @FXML
  private void collectPaperMouseEntered() {
    collectPaperLabel.setStyle("-fx-text-fill: blue;");
  }

  @FXML
  private void collectPaperMouseExited() {
    collectPaperLabel.setStyle("-fx-text-fill: black;");
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

  private void showDialog(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
  }

  private void resetCafeteria() {
    padlockPane.setVisible(false);
    safe.setVisible(false);
    paintingWithSafe.setVisible(true);
    paintingWithoutSafe.setVisible(true);
    vendingMachine.setVisible(true);
    paintingWithSafeBig.setVisible(false);
    paintingWithoutSafeBig.setVisible(false);
    safeBig.setVisible(false);
    vendingMachineBig.setVisible(false);
    digitOne.setText("0");
    digitTwo.setText("0");
    digitThree.setText("0");
    digitFour.setText("0");
    paintingWithSafeArrow.setVisible(true);
    paintingWithoutSafeArrow.setVisible(true);
    vendingMachineArrow.setVisible(true);
  }

      public void animateArrows(ImageView arrow) {
    double startY = 0;

    TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), arrow);
    translateTransition.setFromY(startY);
    translateTransition.setToY(startY + 5);
    translateTransition.setAutoReverse(true);
    translateTransition.setCycleCount(Animation.INDEFINITE);
    translateTransition.play();
  }

  /*
   * This method starts a thread that checks if the cafeteria needs to be reset.
   */
  private void resetchecker() {
    timeline = new Timeline(
        new KeyFrame(
            Duration.seconds(1),
            event -> {
              if (GameState.secondsRemaining >= 0) {
                updateTimerLabel();
              }
              if (GameState.secondsRemaining == 0) {
                if (GameState.gameFinishedCafeteria) {
                  GameState.gameFinishedCafeteria = false;
                  GameState.resetCafeteria = true;
                  GameState.resetOffice = true;
                  GameState.resetRoom = true;
                  try {
                    Scene scene = vendingMachine.getScene();
                    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.END_LOST));
                  } catch (NullPointerException e) {
                  }
                }

              }
              updateTimerLabel();
              if (GameState.resetCafeteria) {
                resetCafeteria();
                GameState.resetCafeteria = false;
              }
            }));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  private void updateTimerLabel() {
    // TODO: add code to update timer label
  }

  public void walkInAnimation() {
    MovementControl.moveToLeft(true, 1, 500, animationItems);
  }

  public void resetAnimation() {
    for (ImageView item : animationItems) {
      item.setTranslateX(500);
    }

  }
}
