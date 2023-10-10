package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;

public class EndWonController {

  @FXML private Label resultLabel;
  @FXML private Button restartButton;
  @FXML private ImageView cog;

  /**
   * Switches the scene to the start interface
   * 
   * @param event
   */
  @FXML
  private void goToStart(MouseEvent event) {
    // Switch to start interface
    Scene scene = restartButton.getScene();
    SceneManager.switchToStart(scene);
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
}
