package nz.ac.auckland.se206.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GptAndTextAreaManager;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class StartInterfaceController {
  @FXML private CheckBox hard;
  @FXML private CheckBox medium;
  @FXML private CheckBox easy;
  @FXML private CheckBox twoMin;
  @FXML private CheckBox fourMin;
  @FXML private CheckBox sixMin;
  @FXML private MenuButton difficulty;
  private RoomController roomController;

  @FXML
  private void initialize() {
    System.out.println("StartInterfaceController initialized");
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("player_history.dat"))) {
      PlayHistory playHistory = (PlayHistory) ois.readObject();
      System.out.println(playHistory.toString());
      playHistoryTextArea.setText(playHistory.toString());
    } catch (IOException | ClassNotFoundException e) {
      playHistoryTextArea.setText("No play history found");
      e.printStackTrace();
    }

  }

  /*
   * This method is invoked when the user clicks the "Start" button. It starts the
   * game.
   * It loads the room scene with user selected difficulty and play time.
   */
  public void setRoomController(RoomController roomController) {
    this.roomController = roomController;
  }


  @FXML
  private void onStartGame(Event event) throws IOException, ApiProxyException {
      if (!twoMin.isSelected() && !fourMin.isSelected() && !sixMin.isSelected()) {
      showDialog("Invaild Inputs", "Please select a difficulty and time limit", "");
      return;
    }
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    // // initialize the characters with prompt
    // //GptAndTextAreaManager.initialize();
    SceneManager.switchRoom(false, sceneButtonIsIn);
    roomController.start();
    System.out.println("Game started");
    twoMin.setSelected(false);
    fourMin.setSelected(false);
    sixMin.setSelected(false);
  }

  /*
   * This method is invoked when the user clicks the "Exit" button. It exits the
   * application.
   */
  @FXML
  private void onExitGame(Event event) {
    System.out.println("Goodbye!");
    System.exit(0);
  }

  /**
   * Displays a dialog box with the given title, header text, and message.
   *
   * @param title the title of the dialog box
   * @param headerText the header text of the dialog box
   * @param message the message content of the dialog box
   */
  private void showDialog(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /*
   * This method is invoked when the user clicks any of the difficulty checkboxes.
   */
  @FXML
  private void onSwitchDifficulty(Event event) {
    CheckBox checkBox = (CheckBox) event.getSource();
    if (checkBox.getId().equals("easy")) {
      hard.setSelected(false);
      medium.setSelected(false);
      easy.setSelected(true);
      GameState.difficulty = GameState.Difficulty.EASY;
      System.out.println("Difficulty change: easy");
    }
    if (checkBox.getId().equals("medium")) {
      easy.setSelected(false);
      hard.setSelected(false);
      medium.setSelected(true);
      GameState.difficulty = GameState.Difficulty.MEDIUM;
      System.out.println("Difficulty change: medium");
    }
    if (checkBox.getId().equals("hard")) {
      easy.setSelected(false);
      medium.setSelected(false);
      hard.setSelected(true);
      GameState.difficulty = GameState.Difficulty.HARD;
      System.out.println("Difficulty change: hard");
    }
  }

  /*
   * This method is invoked when the user clicks any of the play time checkboxes.
   */
  @FXML
  private void onSetPlayTime(Event event) {
    CheckBox checkBox = (CheckBox) event.getSource();
    if (checkBox.getId().equals("twoMin")) {
      fourMin.setSelected(false);
      sixMin.setSelected(false);
      twoMin.setSelected(true);
      GameState.secondsRemaining = 120;
      GameState.totalSeconds = 120;
      System.out.println("Play time change: 2 minutes");
    }
    if (checkBox.getId().equals("fourMin")) {
      twoMin.setSelected(false);
      sixMin.setSelected(false);
      fourMin.setSelected(true);
      GameState.secondsRemaining = 240;
      GameState.totalSeconds = 240;
      System.out.println("Play time change: 4 minutes");
    }
    if (checkBox.getId().equals("sixMin")) {
      twoMin.setSelected(false);
      fourMin.setSelected(false);
      sixMin.setSelected(true);
      GameState.secondsRemaining = 360;
      GameState.totalSeconds = 360;
      System.out.println("Play time change: 6 minutes");
    }
  }
}
