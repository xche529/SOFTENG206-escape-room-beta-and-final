package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class EndLostController {

  @FXML private Button restartButton;

  /**
   * Switches the scene to the start interface
   *
   * @param event
   */
  @FXML
  private void onTryAgain(MouseEvent event) {
    // Switch to start interface
    Scene scene = restartButton.getScene();
    scene.setRoot(SceneManager.getUiRoot(AppUi.START_INTERFACE));
  }
}
