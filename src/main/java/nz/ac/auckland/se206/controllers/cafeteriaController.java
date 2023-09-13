package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class cafeteriaController {

  @FXML private Rectangle pictureWithSafe;
  @FXML private Rectangle pictureWithoutSafe;
  @FXML private Rectangle vendingMachine;

  /**
   * Initializes the cafeteria view, it is called when the room loads.
   *
   * @throws ApiProxyException
   */
  private void initialize() {
    // TODO: set visability of all required items
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
    // TODO: add code to handle click on picture with safe
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
    // TODO
  }

  @FXML
  private void vendingMachineMouseExited() {
    // TODO
  }

  @FXML
  private void paintingWithSafeMouseEntered() {
    // TODO
  }

  @FXML
  private void paintingWithSafeMouseExited() {
    // TODO
  }

  @FXML
  private void paintingWithoutSafeMouseEntered() {
    // TODO
  }

  @FXML
  private void paintingWithoutSafeMouseExited() {
    // TODO
  }

    @FXML
  private void goToOffice() {
    Scene scene = vendingMachine.getScene();
    scene.setRoot(SceneManager.getUiRoot(AppUi.OFFICE));
  }

    @FXML
  private void goToRoom() {
    Scene scene = vendingMachine.getScene();
    scene.setRoot(SceneManager.getUiRoot(AppUi.ROOM));
  }
}
