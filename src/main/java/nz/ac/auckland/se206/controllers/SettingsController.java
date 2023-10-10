package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.reseters.GameEnd;

public class SettingsController {

  @FXML private Slider audioSlider;
  @FXML private Slider musicSlider;
  @FXML private Slider sfxSlider;
  @FXML private Label audioValue;
  @FXML private Label musicValue;
  @FXML private Label sfxValue;
  @FXML private ImageView cross;
  @FXML private ImageView abandonGame;
  @FXML private ImageView help;
  @FXML private VBox settingsBox;

  public void initialize() {

    // set the initial values of the sliders
    audioSlider.setValue(100);
    audioSlider.setMin(0);
    audioSlider.setMax(100);

    musicSlider.setValue(100);
    musicSlider.setMin(0);
    musicSlider.setMax(100);

    sfxSlider.setValue(100);
    sfxSlider.setMin(0);
    sfxSlider.setMax(100);

    // updates the volumes when the sliders are moved
    audioSlider
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              audioValue.setText(String.valueOf(newValue.intValue()));
              float audioVolume = newValue.intValue() / 100f;
              GameState.audioVolume = audioVolume;
            });

    musicSlider
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              musicValue.setText(String.valueOf(newValue.intValue()));
              // float musicVolume = newValue.intValue() / 100f;
              // GameState.musicVolume = musicVolume;
            });

    sfxSlider
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              sfxValue.setText(String.valueOf(newValue.intValue()));
              // float sfxVolume = newValue.intValue() / 100f;
              // GameState.sfxVolume = sfxVolume;
            });

    GameState.isSettingsVisableProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                settingsBox.setVisible(true);
              }
            });
  }

  @FXML
  private void onCrossClicked(MouseEvent Event) {
    settingsBox.setVisible(false);
    GameState.setSettingsVisable(false);
  }

  @FXML
  private void crossMouseEntered() {
    cross.scaleYProperty().setValue(1.2);
    cross.scaleXProperty().setValue(1.2);
  }

  @FXML
  private void crossMouseExited() {
    cross.scaleYProperty().setValue(1);
    cross.scaleXProperty().setValue(1);
  }

  @FXML
  private void onClickAbandonGame(MouseEvent Event) {
    if (SceneManager.currentUi == SceneManager.AppUi.ROOM
        || SceneManager.currentUi == SceneManager.AppUi.OFFICE
        || SceneManager.currentUi == SceneManager.AppUi.CAFETERIA) {
      // ends the game
      GameState.stopTimer = true;
      GameEnd.triggerResters();
      SceneManager.switchToEndLost(abandonGame.getScene());
      settingsBox.setVisible(false);
      GameState.setSettingsVisable(false);
    }
  }

  @FXML
  private void abandonGameMouseEntered() {
    // shows the enlarged text
    abandonGame.setScaleX(1.1);
    abandonGame.setScaleY(1.1);
  }

  @FXML
  private void abandonGameMouseExited() {
    // hides the enlarged text
    abandonGame.setScaleX(1);
    abandonGame.setScaleY(1);
  }

  @FXML
  private void onClickHelp(MouseEvent Event) {
    // opens the help window

  }

  @FXML
  private void helpMouseEntered() {
    // shows the enlarged text
    help.setScaleX(1.1);
    help.setScaleY(1.1);
  }

  @FXML
  private void helpMouseExited() {
    // hides the enlarged text
    help.setScaleX(1);
    help.setScaleY(1);
  }
}
