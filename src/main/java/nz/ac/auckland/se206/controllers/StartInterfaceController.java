package nz.ac.auckland.se206.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class StartInterfaceController {
  @FXML private CheckBox hard;
  @FXML private CheckBox medium;
  @FXML private CheckBox easy;

  @FXML
  private void initialize() {
    System.out.println("StartInterfaceController initialized");
  }

  @FXML
  private void startGame(Event event) {
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.ROOM));
  }

  @FXML
  private void exitGame(Event event) {
    System.exit(0);
  }

  @FXML
  private void switchHard(Event event) {
    medium.setSelected(false);
    easy.setSelected(false);
    GameState.difficulty = GameState.Difficulty.HARD;
  }

  @FXML
  private void switchMedium(Event event) {
    hard.setSelected(false);
    easy.setSelected(false);
    GameState.difficulty = GameState.Difficulty.MEDIUM;
  }

  @FXML
  private void switchEasy(Event event) {
    hard.setSelected(false);
    medium.setSelected(false);
    GameState.difficulty = GameState.Difficulty.EASY;
  }
}
