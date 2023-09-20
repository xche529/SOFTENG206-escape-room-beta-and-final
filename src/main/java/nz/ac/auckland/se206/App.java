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
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.CafeteriaController;
import nz.ac.auckland.se206.controllers.OfficeController;
import nz.ac.auckland.se206.controllers.StartInterfaceController;

/**
 * This is the entry point of the JavaFX application, while you can change this
 * class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {
  public static Scale scale;

  private static Scene scene;

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
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    double overallScale = 1;
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
    overallScale = overallScale * 0.9;
    scale = new Scale(overallScale, overallScale);
    FXMLLoader endScreenWonLoader = loadFxml("endScreenWon");
    FXMLLoader endScreenLostLoader = loadFxml("endScreenLost");
    FXMLLoader officeSceneLoader = loadFxml("officeScene");
    FXMLLoader cafeteriaLoader = loadFxml("cafeteria");
    FXMLLoader StartInterfaceLoader = loadFxml("StartInterface");

    SceneManager.addUi(AppUi.END_WON, endScreenWonLoader.load());
    SceneManager.addUi(AppUi.END_LOST, endScreenLostLoader.load());
    SceneManager.addUi(AppUi.OFFICE, officeSceneLoader.load());
    SceneManager.addUi(AppUi.CAFETERIA, cafeteriaLoader.load());
    SceneManager.addUi(AppUi.START_INTERFACE, StartInterfaceLoader.load());
    SceneManager.getUiRoot(AppUi.CAFETERIA).getTransforms().add(scale);
    SceneManager.getUiRoot(AppUi.OFFICE).getTransforms().add(scale);
    SceneManager.getUiRoot(AppUi.START_INTERFACE).getTransforms().add(scale);
    SceneManager.getUiRoot(AppUi.END_WON).getTransforms().add(scale);
    SceneManager.getUiRoot(AppUi.END_LOST).getTransforms().add(scale);

    CafeteriaController cafeteriaController = cafeteriaLoader.getController();
    OfficeController officeController = officeSceneLoader.getController();
    StartInterfaceController startInterfaceController = StartInterfaceLoader.getController();

    SceneManager.cafeteriaController = cafeteriaController;
    SceneManager.officeController = officeController;
    cafeteriaController.setOfficeController(officeController);
    officeController.setCafeteriaController(cafeteriaController);
    startInterfaceController.setOfficeController(officeController);
    startInterfaceController.setCafeteriaController(cafeteriaController);

    Safe.getRandomCode();

    scene = new Scene(SceneManager.getUiRoot(AppUi.START_INTERFACE), 1113 * overallScale, 800 * overallScale);
    stage.setScene(scene);
    scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
      if (event.getCode() == KeyCode.LEFT) {
        scene.setRoot(SceneManager.switchRoom(true));
      } else if (event.getCode() == KeyCode.RIGHT) {
        scene.setRoot(SceneManager.switchRoom(false));
      }
    });
    stage.show();
    SceneManager.getUiRoot(AppUi.START_INTERFACE).requestFocus();
  }
}
