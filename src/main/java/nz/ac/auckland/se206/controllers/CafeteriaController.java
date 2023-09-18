package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
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
  @FXML private ImageView paintingWithSafeRotated;
  @FXML private ImageView safe;
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

  /**
   * Initializes the cafeteria view, it is called when the room loads.
   *
   * @throws ApiProxyException
   */
  @FXML
  private void initialize() {
    // TODO: set visability of all required items
    System.out.println("cafeteriaController initialized");
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
    paintingWithSafeRotated.setVisible(true);
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
    if (digitOneInt == 0 && digitTwoInt == 1 && digitThreeInt == 9 && digitFourInt == 2) {
      Scene scene = openButton.getScene();
      GameState.isWon = true;
      scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.END_WON));
    } else {
      showDialog("Wrong combination", "Try again", "The padlock did not open.");
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
}
