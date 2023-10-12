package nz.ac.auckland.se206.controllers;

import java.io.File;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
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
  @FXML private ImageView avatarImageView;
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
  @FXML private ImageView cog;

  private RoomController roomController;
  private SideConversationController sideConversationController;
  private int currentAvatar = 1;

  public Image playerAvatarOne;
  public Image playerAvatarTwo;
  public Image playerAvatarThree;
  public Image playerAvatarFour;
  public Image playerAvatarFive;
  public Image currentAvatarImage;

  @FXML
  private void initialize() {
    playerAvatarOne =
        new Image(new File("src/main/resources/images/PlayerAvatarOne.png").toURI().toString());
    playerAvatarTwo =
        new Image(new File("src/main/resources/images/PlayerAvatarTwo.png").toURI().toString());
    playerAvatarThree =
        new Image(new File("src/main/resources/images/PlayerAvatarThree.png").toURI().toString());
    playerAvatarFour =
        new Image(new File("src/main/resources/images/PlayerAvatarFour.png").toURI().toString());
    playerAvatarFive =
        new Image(new File("src/main/resources/images/PlayerAvatarFive.png").toURI().toString());
    avatarImageView.setImage(playerAvatarOne);
    avatarImageView.setFitHeight(100);
    avatarImageView.setFitWidth(100);
    System.out.println("StartInterfaceController initialized");
    // load play history
    loadPlayHistory();

    GameState.updatePlayHistory()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                loadPlayHistory();
                GameState.setUpdatePlayHistory(false);
              }
            });
  }

  private void loadPlayHistory() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("player_history.dat"))) {
      // load the play history
      PlayHistory playHistory = (PlayHistory) ois.readObject();
      System.out.println(playHistory.toString());
      playHistoryTextArea.setText(playHistory.toString());
    } catch (IOException | ClassNotFoundException e) {
      // otherwise there is none found
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

  public void setSideConversationController(SideConversationController sideConversationController) {
    this.sideConversationController = sideConversationController;
  }

  @FXML
  private void onStartGame(Event event) throws IOException, ApiProxyException {
    if (!twoMin.isSelected() && !fourMin.isSelected() && !sixMin.isSelected()) {
      showDialog("Invaild Inputs", "Please select a difficulty and time limit", "");
      return;
    }
    GptAndTextAreaManager.setPlayerAvatar(currentAvatarImage);
    GameState.playerName = playerName.getText();
    Scene sceneButtonIsIn = twoTick.getScene();
    sideConversationController.setPlayerName(playerName.getText());
    sideConversationController.initialize();
    SceneManager.switchToFirstRoom(sceneButtonIsIn);
    new GameTimer();
    roomController.start();
    GptAndTextAreaManager.initialize();
    System.out.println(
        "Game started with difficulty "
            + GameState.difficulty
            + " and play time "
            + GameState.secondsRemaining
            + " seconds");
    // unselects all of the choices
    twoMin.setSelected(false);
    fourMin.setSelected(false);
    sixMin.setSelected(false);
    easy.setSelected(false);
    medium.setSelected(false);
    hard.setSelected(false);
    easyTick.setVisible(false);
    mediumTick.setVisible(false);
    hardTick.setVisible(false);
    twoTick.setVisible(false);
    fourTick.setVisible(false);
    sixTick.setVisible(false);
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

  @FXML
  private void onSwitchAvatar(Event e) {
    currentAvatar++;
    if (currentAvatar == 1) {
      currentAvatarImage = playerAvatarOne;
    }
    if (currentAvatar == 2) {
      currentAvatarImage = playerAvatarTwo;
    }
    if (currentAvatar == 3) {
      currentAvatarImage = playerAvatarThree;
    }
    if (currentAvatar == 4) {
      currentAvatarImage = playerAvatarFour;
    }
    if (currentAvatar == 5) {
      currentAvatarImage = playerAvatarFive;
      currentAvatar = 0;
    }
    avatarImageView.setImage(currentAvatarImage);
    avatarImageView.setFitHeight(100);
    avatarImageView.setFitWidth(100);
  }

  /**
   * This method is invoked when the user clicks any of the difficulty checkboxes.
   *
   * @param event
   */
  @FXML
  private void onSwitchDifficulty(Event event) {
    CheckBox checkBox = (CheckBox) event.getSource();
    if (checkBox.getId().equals("easy")) {
      // uncheck other checkboxes
      hard.setSelected(false);
      medium.setSelected(false);
      easy.setSelected(true);
      GameState.difficulty = GameState.Difficulty.EASY;
      System.out.println("Difficulty change: easy");
    }
    if (checkBox.getId().equals("medium")) {
      // uncheck other checkboxes
      easy.setSelected(false);
      hard.setSelected(false);
      medium.setSelected(true);
      // set number of hints
      GameState.difficulty = GameState.Difficulty.MEDIUM;
      System.out.println("Difficulty change: medium");
    }
    if (checkBox.getId().equals("hard")) {
      // uncheck other checkboxes
      easy.setSelected(false);
      medium.setSelected(false);
      hard.setSelected(true);
      GameState.difficulty = GameState.Difficulty.HARD;
      System.out.println("Difficulty change: hard");
    }
  }

  @FXML
  private void onClickEasy() {
    // set check boxes
    hard.setSelected(false);
    medium.setSelected(false);
    easy.setSelected(true);
    easyTick.setVisible(true);
    mediumTick.setVisible(false);
    hardTick.setVisible(false);
    // set gamestate to easy
    GameState.difficulty = GameState.Difficulty.EASY;
    System.out.println("Difficulty change: easy");
  }

  @FXML
  private void onClickMedium() {
    // set required check boxes
    easy.setSelected(false);
    hard.setSelected(false);
    medium.setSelected(true);
    easyTick.setVisible(false);
    mediumTick.setVisible(true);
    hardTick.setVisible(false);
    // set difficulty to medium
    GameState.difficulty = GameState.Difficulty.MEDIUM;
    System.out.println("Difficulty change: medium");
  }

  @FXML
  private void onClickHard() {
    // set required check boxes
    easy.setSelected(false);
    medium.setSelected(false);
    hard.setSelected(true);
    easyTick.setVisible(false);
    mediumTick.setVisible(false);
    hardTick.setVisible(true);
    // set to hard difficulty
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
      // check if two minutes is selected, if so then uncheck the other two
      fourMin.setSelected(false);
      sixMin.setSelected(false);
      twoMin.setSelected(true);
      GameState.secondsRemaining = 120;
      GameState.totalSeconds = 120;
      System.out.println("Play time change: 2 minutes");
    }
    if (checkBox.getId().equals("fourMin")) {
      // check if four minutes is selected, if so then uncheck the other two
      twoMin.setSelected(false);
      sixMin.setSelected(false);
      fourMin.setSelected(true);
      GameState.secondsRemaining = 240;
      GameState.totalSeconds = 240;
      System.out.println("Play time change: 4 minutes");
    }
    if (checkBox.getId().equals("sixMin")) {
      // check if six minutes is selected, if so then uncheck the other two
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
    // set checkboxes so two is checked
    fourMin.setSelected(false);
    sixMin.setSelected(false);
    twoMin.setSelected(true);
    fourTick.setVisible(false);
    sixTick.setVisible(false);
    twoTick.setVisible(true);
    // set timer to two minutes
    GameState.secondsRemaining = 120;
    GameState.totalSeconds = 120;
    System.out.println("Play time change: 2 minutes");
  }

  @FXML
  private void onClickFour() {
    // set checkboxes so four is checked
    twoMin.setSelected(false);
    sixMin.setSelected(false);
    fourMin.setSelected(true);
    twoTick.setVisible(false);
    fourTick.setVisible(true);
    sixTick.setVisible(false);
    // set timer to four minutes
    GameState.secondsRemaining = 240;
    GameState.totalSeconds = 240;
    System.out.println("Play time change: 4 minutes");
  }

  @FXML
  private void onClickSix() {
    // set checkboxes so six is checked
    twoMin.setSelected(false);
    fourMin.setSelected(false);
    sixMin.setSelected(true);
    twoTick.setVisible(false);
    fourTick.setVisible(false);
    sixTick.setVisible(true);
    // set timer to six minutes
    GameState.secondsRemaining = 360;
    GameState.totalSeconds = 360;
    System.out.println("Play time change: 6 minutes");
  }

  // handling exiting the play history pane
  @FXML
  private void onGoBack() {
    historyPane.setVisible(false);
  }

  // viewing the play history pane
  @FXML
  private void onViewPlayHistory() {
    historyPane.setVisible(true);
  }

  // handling hovering over the buttons
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
