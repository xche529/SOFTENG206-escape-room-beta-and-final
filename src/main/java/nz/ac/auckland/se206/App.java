package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.CafeteriaController;
import nz.ac.auckland.se206.controllers.OfficeController;
import nz.ac.auckland.se206.controllers.RoomController;
import nz.ac.auckland.se206.controllers.StartInterfaceController;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/**
 * This is the entry point of the JavaFX application, while you can change this
 * class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {
  public static Scale scale;
  public static double overallScale = 1;
  public static double centerX = 1;
  public static double centerY = 1;
  private static Scene scene;
  Boolean isFirstSwitch = false;

  public static void main(final String[] args) {
    launch();
  }

  public static void setRoot(Parent root) throws IOException {
    scene.setRoot(root);
  }

  /**
   * Returns the node associated to the input file. The method expects that the
   * file is located in
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
   * This method is invoked when the application starts. It loads and shows the
   * "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException       If "src/main/resources/fxml/canvas.fxml" is not
   *                           found.
   * @throws ApiProxyException
   */
  @Override
  public void start(final Stage stage) throws IOException, ApiProxyException {
    stage.setTitle("Prison Escape");
    stage.setMaximized(true);
    Screen screen = Screen.getPrimary();
    double width = screen.getBounds().getWidth();
    double height = screen.getBounds().getHeight();
    double ratio = width / height;
    if (ratio < 1113.0 / 800.0) {
      overallScale = (width / 1113.0);
      System.out.println("The game start with scale:" + overallScale);
    } else {
      overallScale = (height / 800.0);
      System.out.println("The game start with scale:" + overallScale);
    }
    overallScale = overallScale * 0.8;
    centerX = (width - 1113 * overallScale) / 2;
    centerY = (height - 800 * overallScale) / 5;
    scale = new Scale(overallScale, overallScale);
    FXMLLoader endScreenWonLoader = loadFxml("endScreenWon");
    FXMLLoader endScreenLostLoader = loadFxml("endScreenLost");
    FXMLLoader officeSceneLoader = loadFxml("officeScene");
    FXMLLoader cafeteriaLoader = loadFxml("cafeteria");
    FXMLLoader StartInterfaceLoader = loadFxml("StartInterface");
    FXMLLoader textAreaLoader = loadFxml("textArea");
    FXMLLoader roomLoader = loadFxml("room");
    VBox cafeteria = cafeteriaLoader.load();
    VBox office = officeSceneLoader.load();
    VBox room = roomLoader.load();
    VBox textArea = textAreaLoader.load();

    VBox cafeteriaVBox = new VBox(cafeteria, textArea);
    VBox officeVBox = new VBox(office, textArea);
    VBox roomVBox = new VBox(room, textArea);

    SceneManager.addUi(AppUi.END_WON, endScreenWonLoader.load());
    SceneManager.addUi(AppUi.END_LOST, endScreenLostLoader.load());
    SceneManager.addUi(AppUi.START_INTERFACE, StartInterfaceLoader.load());
    SceneManager.addUi(AppUi.OFFICE, officeVBox);
    SceneManager.addUi(AppUi.CAFETERIA, cafeteriaVBox);
    SceneManager.addUi(AppUi.ROOM, roomVBox);
    SceneManager.addUi(AppUi.TEXT_AREA, textArea);
    SceneManager.getUiRoot(AppUi.START_INTERFACE).getTransforms().add(scale);
    SceneManager.getUiRoot(AppUi.END_WON).getTransforms().add(scale);
    SceneManager.getUiRoot(AppUi.END_LOST).getTransforms().add(scale);

    CafeteriaController cafeteriaController = cafeteriaLoader.getController();
    OfficeController officeController = officeSceneLoader.getController();
    StartInterfaceController startInterfaceController = StartInterfaceLoader.getController();
    RoomController roomController = roomLoader.getController();

    SceneManager.cafeteriaController = cafeteriaController;
    SceneManager.officeController = officeController;
    startInterfaceController.setRoomController(roomController);
    SceneManager.roomController = roomController;

    Safe.getRandomCode();
    GptAndTextAreaManager.initialize();
    VBox root = (VBox) SceneManager.getUiRoot(AppUi.START_INTERFACE);
    root.setLayoutX(centerX);
    root.setLayoutY(centerY);
    scene = new Scene(root, 1113 * overallScale, 600 * overallScale);
    scene.setFill(Color.rgb(244, 244, 244, 1));
    stage.setScene(scene);
    scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
      VBox up = null;
      if (event.getCode() == KeyCode.LEFT) {
        SceneManager.switchRoom(true, scene);
      } else if (event.getCode() == KeyCode.RIGHT) {
        SceneManager.switchRoom(false, scene);
      }
      if (up == null) {
        return;
      }
    });
    stage.show();
    // SceneManager.getUiRoot(AppUi.START_INTERFACE).requestFocus();
  }
}
