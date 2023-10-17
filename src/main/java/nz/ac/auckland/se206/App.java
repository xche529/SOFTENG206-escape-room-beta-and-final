package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.CafeteriaController;
import nz.ac.auckland.se206.controllers.OfficeController;
import nz.ac.auckland.se206.controllers.RoomController;
import nz.ac.auckland.se206.controllers.SettingsController;
import nz.ac.auckland.se206.controllers.SideConversationController;
import nz.ac.auckland.se206.controllers.StartInterfaceController;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/*
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {
  public static Scale scale;
  public static Scale startScale;
  public static double overallScale = 1;
  public static double centerX = 1;
  public static double centerY = 1;
  private static Scene scene;

  public static void main(final String[] args) {
    launch();
  }

  public static void setRoot(Parent root) throws IOException {
    scene.setRoot(root);
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  public static FXMLLoader loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   * @throws ApiProxyException
   */
  @Override
  public void start(final Stage stage) throws IOException, ApiProxyException {

    // set up the scene
    stage.setTitle("Prison Escape");
    stage.setMaximized(true);
    Screen screen = Screen.getPrimary();
    double width = screen.getBounds().getWidth();
    double height = screen.getBounds().getHeight();
    double ratio = width / height;

    // make it so that it fits the screen
    if (ratio < 1413.0 / 800.0) {
      overallScale = (width / 1413.0);
      System.out.println("The game start with scale:" + overallScale);
    } else {
      overallScale = (height / 800.0);
      System.out.println("The game start with scale:" + overallScale);
    }
    overallScale = overallScale * 0.8;
    centerX = (width - 1413 * overallScale) / 2;
    centerY = (height - 800 * overallScale) / 5;
    scale = new Scale(overallScale, overallScale);
    startScale = new Scale(overallScale * 1.27, overallScale  * 1.27);

    // creating loaders for all fxml files
    FXMLLoader roomLoader = loadFxml("room");
    FXMLLoader endScreenWonLoader = loadFxml("endScreenWon");
    FXMLLoader endScreenLostLoader = loadFxml("endScreenLost");
    FXMLLoader officeSceneLoader = loadFxml("officeScene");
    FXMLLoader cafeteriaLoader = loadFxml("cafeteria");
    FXMLLoader startInterfaceLoader = loadFxml("StartInterface");
    FXMLLoader textAreaLoader = loadFxml("textArea");
    FXMLLoader settingsLoader = loadFxml("settings");
    FXMLLoader sideConversationLoader = loadFxml("sideConversation");

    // creating vboxes for the scenes
    VBox settings = settingsLoader.load();
    SceneManager.settings = settings;
    VBox cafeteria = cafeteriaLoader.load();
    VBox office = officeSceneLoader.load();
    VBox room = roomLoader.load();
    VBox textArea = textAreaLoader.load();
    VBox sideConversation = sideConversationLoader.load();

    // stacks the settings page over the start interface
    StackPane startInterfaceStack = new StackPane(startInterfaceLoader.load(), settings);
    StackPane.setAlignment(settings, javafx.geometry.Pos.TOP_LEFT);
    VBox startInterfaceVBox = new VBox(startInterfaceStack);

    // creating hboxes for the scenes too add the side coverstaion
    HBox officeHbox = new HBox(office, sideConversation);
    VBox officeVbox = new VBox(officeHbox, textArea);

    HBox roomHbox = new HBox(room, sideConversation);
    VBox roomVbox = new VBox(roomHbox, textArea);

    HBox cafeteriaHbox = new HBox(cafeteria, sideConversation);
    VBox cafeteriaVbox = new VBox(cafeteriaHbox, textArea);

    // adding all the scenes to the scene manager
    SceneManager.addUi(AppUi.ROOM, roomVbox);
    SceneManager.addUi(AppUi.END_WON, endScreenWonLoader.load());
    SceneManager.addUi(AppUi.END_LOST, endScreenLostLoader.load());
    SceneManager.addUi(AppUi.START_INTERFACE, startInterfaceVBox);
    SceneManager.addUi(AppUi.OFFICE, officeVbox);
    SceneManager.addUi(AppUi.CAFETERIA, cafeteriaVbox);
    SceneManager.addUi(AppUi.SETTINGS, settings);
    SceneManager.addUi(AppUi.TEXT_AREA, textArea);
    SceneManager.addUi(AppUi.SIDE_CONVERSATION, sideConversation);

    SceneManager.getUiRoot(AppUi.START_INTERFACE).getTransforms().add(startScale);
    SceneManager.getUiRoot(AppUi.END_WON).getTransforms().add(startScale);
    SceneManager.getUiRoot(AppUi.END_LOST).getTransforms().add(startScale);

    // setting up the controllers
    CafeteriaController cafeteriaController = cafeteriaLoader.getController();
    OfficeController officeController = officeSceneLoader.getController();
    StartInterfaceController startInterfaceController = startInterfaceLoader.getController();
    RoomController roomController = roomLoader.getController();
    SideConversationController sideConversationController = sideConversationLoader.getController();
    SettingsController settingsController = settingsLoader.getController();

    // setting up the controllers
    SceneManager.cafeteriaController = cafeteriaController;
    SceneManager.officeController = officeController;
    SceneManager.roomController = roomController;
    SceneManager.settingsController = settingsController;
    GptAndTextAreaManager.sideConversationController = sideConversationController;
    startInterfaceController.setRoomController(roomController);
    startInterfaceController.setSideConversationController(sideConversationController);

    // setting up the scene and getting the random code
    Safe.getRandomCode();
    startInterfaceVBox.setLayoutX(centerX);
    startInterfaceVBox.setLayoutY(centerY);
    // make it fill the screen
    scene = new Scene(startInterfaceVBox, 1413 * overallScale, 605 * overallScale);
    scene.setFill(Color.rgb(244, 244, 244, 1));
    stage.setScene(scene);

    Platform.runLater(
        () -> {
          stage.setOnCloseRequest(
              event -> {
                Platform.exit();
                GameState.setGameClosed(true);
              });
        });

    scene.addEventFilter(
        javafx.scene.input.KeyEvent.KEY_PRESSED,
        event -> {
          if (SceneManager.currentUi == AppUi.START_INTERFACE) {
            return;
          }
          VBox up = null;
          if (event.getCode() == KeyCode.LEFT) {
            SceneManager.switchRoom(true, scene);
          } else if (event.getCode() == KeyCode.RIGHT) {
            SceneManager.switchRoom(false, scene);
          } else if (event.getCode() == KeyCode.ENTER) {
            try {
              GptAndTextAreaManager.onSubmitMessage();
              GptAndTextAreaManager.removeInputTextAreaFocus();
              GptAndTextAreaManager.onSubmitMessage();
            } catch (ApiProxyException e) {
              e.printStackTrace();
            }
          }
          if (up == null) {
            return;
          }
        });
    stage.show();
  }
}
