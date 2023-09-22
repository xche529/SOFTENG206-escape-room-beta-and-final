package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class EndWonController {

  @FXML
  private Label resultLabel;
  @FXML
  private Button restartButton;

  @FXML
  private void goToStart() {
    Scene scene = restartButton.getScene();
    scene.setRoot(SceneManager.getUiRoot(AppUi.START_INTERFACE));
  }
}
