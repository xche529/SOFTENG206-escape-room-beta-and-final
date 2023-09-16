package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class CafeteriaController {

  @FXML private Rectangle paintingWithSafe;
  @FXML private Rectangle paintingWithoutSafe;
  @FXML private Rectangle vendingMachine;
  @FXML private ImageView paintingWithSafeBig;
  @FXML private ImageView paintingWithoutSafeBig;
  @FXML private ImageView paintingWithSafeRotated;
  @FXML private ImageView safe;
  @FXML private ImageView vendingMachineBig;

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
}
