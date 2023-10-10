package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.GameState;;

public class SettingsController {

    @FXML private Slider audioSlider;
    @FXML private Slider musicSlider;
    @FXML private Slider sfxSlider;
    @FXML private Label audioValue;
    @FXML private Label musicValue;
    @FXML private Label sfxValue;
    @FXML private ImageView cross;
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

        //updates the volumes when the sliders are moved
        audioSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            audioValue.setText(String.valueOf(newValue.intValue()));
            float audioVolume = newValue.intValue() / 100f;
            GameState.audioVolume = audioVolume;
        });

        musicSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            musicValue.setText(String.valueOf(newValue.intValue()));
            // float musicVolume = newValue.intValue() / 100f;
            // GameState.musicVolume = musicVolume;
        });

        sfxSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            sfxValue.setText(String.valueOf(newValue.intValue()));
            // float sfxVolume = newValue.intValue() / 100f;
            // GameState.sfxVolume = sfxVolume;
        });
    }

    @FXML
    private void onCrossClicked() {
        settingsBox.setVisible(false);
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
    
}
