package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

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
  @FXML private Label digitOne;
  @FXML private Label digitTwo;
  @FXML private Label digitThree;
  @FXML private Label digitFour;
  @FXML private Button openButton;

  private Timeline timeline;

  /**
   * Initializes the cafeteria view, it is called when the room loads.
   * @throws IOException
   *
   * @throws ApiProxyException
   */
  @FXML
  private void initialize() {
    resetchecker();
  }

  /**
   * This method is called when the user clicks on the vending machine.
   *
   * @throws ApiProxyException
   */
  @FXML
  private void onClickVendingMachine(MouseEvent event) {
    // TODO: add code to handle click on vending machine
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
  }

  /**
   * This method is called when the user clicks on the picture without a safe.
   *
   * @throws ApiProxyException
   */
  @FXML
  private void onClickPaintingWithoutSafe(MouseEvent event) {
    // TODO: add code to handle click on picture without safe
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
  private void goToOffice() {
    Scene scene = paintingWithSafe.getScene();
    scene.setRoot(SceneManager.getUiRoot(AppUi.OFFICE));
  }

  @FXML
  private void goToRoom() {
    Scene scene = paintingWithSafe.getScene();
    scene.setRoot(SceneManager.getUiRoot(AppUi.ROOM));
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
      Scene scene = openButton.getScene();
      GameState.isWon = true;
      GameState.resetCafeteria = true;
      GameState.resetOffice = true;
      GameState.resetRoom = true;
      scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.END_WON));
    } else {
      showDialog(
          "Wrong combination",
          "Try again",
          "The padlock did not open. " + GameState.codeWord + " " + GameState.code + " " + code);
    }
  }

  @FXML
  private void onClickExitPadlock() {
    padlockPane.setVisible(false);
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
  }

  /*
   * This method starts a thread that checks if the cafeteria needs to be reset.
   */
  private void resetchecker() {
    timeline =
        new Timeline(
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
}
