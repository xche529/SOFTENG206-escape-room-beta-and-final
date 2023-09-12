package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;
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
  private void clickVendingMachine() {
    // TODO: add code to handle click on vending machine
  }

  /**
   * This method is called when the user clicks on the picture with a safe.
   *
   * @throws ApiProxyException
   */
  @FXML
  private void clickPictureWithSafe() {
    // TODO: add code to handle click on picture with safe
  }

  /**
   * This method is called when the user clicks on the picture without a safe.
   *
   * @throws ApiProxyException
   */
  @FXML
  private void clickPictureWithoutSafe() {
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
  private void pictureWithSafeMouseEntered() {
    // TODO
  }

  @FXML
  private void pictureWithSafeMouseExited() {
    // TODO
  }

  @FXML
  private void pictureWithoutSafeMouseEntered() {
    // TODO
  }

  @FXML
  private void pictureWithoutSafeMouseExited() {
    // TODO
  }
}
