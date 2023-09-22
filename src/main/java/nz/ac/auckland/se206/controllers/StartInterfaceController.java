package nz.ac.auckland.se206.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GptAndTextAreaManager;
import nz.ac.auckland.se206.PlayHistory;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class StartInterfaceController {
  @FXML private CheckBox hard;
  @FXML private CheckBox medium;
  @FXML private CheckBox easy;
  @FXML private CheckBox twoMin;
  @FXML private CheckBox fourMin;
  @FXML private CheckBox sixMin;
  @FXML private MenuButton difficulty;
  @FXML private TextArea playHistoryTextArea;
  @FXML private TextField playerName;
  @FXML private ImageView easyTick;
  @FXML private ImageView mediumTick;
  @FXML private ImageView hardTick;
  @FXML private ImageView twoTick;
  @FXML private ImageView fourTick;
  @FXML private ImageView sixTick;
  @FXML private Pane historyPane;
  @FXML private ImageView twoMinText;
  @FXML private ImageView fourMinText;
  @FXML private ImageView sixMinText;
  @FXML private ImageView easyText;
  @FXML private ImageView mediumText;
  @FXML private ImageView hardText;
  @FXML private ImageView startGameText;
  @FXML private ImageView exitText;
  @FXML private ImageView playHistoryText;

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
    GameState.playerName = playerName.getText();
    Scene sceneButtonIsIn = twoTick.getScene();
    GptAndTextAreaManager.initialize();
    SceneManager.switchRoom(false, sceneButtonIsIn);
    roomController.start();
    System.out.println("Game started with difficulty " + GameState.difficulty + " and play time " + GameState.secondsRemaining + " seconds");
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

  @FXML
  private void onClickEasy() {
    hard.setSelected(false);
    medium.setSelected(false);
    easy.setSelected(true);
    easyTick.setVisible(true);
    mediumTick.setVisible(false);
    hardTick.setVisible(false);
    GameState.difficulty = GameState.Difficulty.EASY;
    System.out.println("Difficulty change: easy");
  }

  @FXML
  private void onClickMedium() {
    easy.setSelected(false);
    hard.setSelected(false);
    medium.setSelected(true);
    easyTick.setVisible(false);
    mediumTick.setVisible(true);
    hardTick.setVisible(false);
    GameState.difficulty = GameState.Difficulty.MEDIUM;
    System.out.println("Difficulty change: medium");
  }

  @FXML
  private void onClickHard() {
    easy.setSelected(false);
    medium.setSelected(false);
    hard.setSelected(true);
    easyTick.setVisible(false);
    mediumTick.setVisible(false);
    hardTick.setVisible(true);
    GameState.difficulty = GameState.Difficulty.HARD;
    System.out.println("Difficulty change: hard");
  }

  /*
   * This method is invoked when the user clicks any of the play time
   * checkboxes.
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

  @FXML
  private void onClickTwo() {
    fourMin.setSelected(false);
    sixMin.setSelected(false);
    twoMin.setSelected(true);
    fourTick.setVisible(false);
    sixTick.setVisible(false);
    twoTick.setVisible(true);
    GameState.secondsRemaining = 120;
    GameState.totalSeconds = 120;
    System.out.println("Play time change: 2 minutes");
  }

  @FXML
  private void onClickFour() {
    twoMin.setSelected(false);
    sixMin.setSelected(false);
    fourMin.setSelected(true);
    twoTick.setVisible(false);
    fourTick.setVisible(true);
    sixTick.setVisible(false);
    GameState.secondsRemaining = 240;
    GameState.totalSeconds = 240;
    System.out.println("Play time change: 4 minutes");
  }

  @FXML
  private void onClickSix() {
    twoMin.setSelected(false);
    fourMin.setSelected(false);
    sixMin.setSelected(true);
    twoTick.setVisible(false);
    fourTick.setVisible(false);
    sixTick.setVisible(true);
    GameState.secondsRemaining = 360;
    GameState.totalSeconds = 360;
    System.out.println("Play time change: 6 minutes");
  }

  @FXML
  private void onGoBack() {
    historyPane.setVisible(false);
  }

  @FXML
  private void onViewPlayHistory() {
    historyPane.setVisible(true);
  }

  @FXML
  private void twoMinEntered() {
    twoMinText.scaleXProperty().set(1.15);
    twoMinText.scaleYProperty().set(1.15);
  }

  @FXML
  private void twoMinExited() {
    twoMinText.scaleXProperty().set(1);
    twoMinText.scaleYProperty().set(1);
  }

  @FXML
  private void fourMinEntered() {
    fourMinText.scaleXProperty().set(1.15);
    fourMinText.scaleYProperty().set(1.15);
  }

  @FXML
  private void fourMinExited() {
    fourMinText.scaleXProperty().set(1);
    fourMinText.scaleYProperty().set(1);
  }

  @FXML
  private void sixMinEntered() {
    sixMinText.scaleXProperty().set(1.15);
    sixMinText.scaleYProperty().set(1.15);
  }

  @FXML
  private void sixMinExited() {
    sixMinText.scaleXProperty().set(1);
    sixMinText.scaleYProperty().set(1);
  }

  @FXML
  private void easyEntered() {
    easyText.scaleXProperty().set(1.15);
    easyText.scaleYProperty().set(1.15);
  }

  @FXML
  private void easyExited() {
    easyText.scaleXProperty().set(1);
    easyText.scaleYProperty().set(1);
  }

  @FXML
  private void mediumEntered() {
    mediumText.scaleXProperty().set(1.15);
    mediumText.scaleYProperty().set(1.15);
  }

  @FXML
  private void mediumExited() {
    mediumText.scaleXProperty().set(1);
    mediumText.scaleYProperty().set(1);
  }

  @FXML
  private void hardEntered() {
    hardText.scaleXProperty().set(1.15);
    hardText.scaleYProperty().set(1.15);
  }

  @FXML
  private void hardExited() {
    hardText.scaleXProperty().set(1);
    hardText.scaleYProperty().set(1);
  }

  @FXML
  private void startEntered() {
    startGameText.scaleXProperty().set(1.15);
    startGameText.scaleYProperty().set(1.15);
  }

  @FXML
  private void startExited() {
    startGameText.scaleXProperty().set(1);
    startGameText.scaleYProperty().set(1);
  }

  @FXML
  private void exitEntered() {
    exitText.scaleXProperty().set(1.15);
    exitText.scaleYProperty().set(1.15);
  }

  @FXML
  private void exitExited() {
    exitText.scaleXProperty().set(1);
    exitText.scaleYProperty().set(1);
  }

  @FXML
  private void viewHistoryEntered() {
    playHistoryText.scaleXProperty().set(1.15);
    playHistoryText.scaleYProperty().set(1.15);
  }

  @FXML
  private void viewHistoryExited() {
    playHistoryText.scaleXProperty().set(1);
    playHistoryText.scaleYProperty().set(1);
  }
}
