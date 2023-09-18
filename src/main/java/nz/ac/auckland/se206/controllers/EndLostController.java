package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class EndLostController {

    @FXML private Button restartButton;

    void goToStart() {
        Scene scene = restartButton.getScene();
        scene.setRoot(SceneManager.getUiRoot(AppUi.CAFETERIA));
    }
    
}
