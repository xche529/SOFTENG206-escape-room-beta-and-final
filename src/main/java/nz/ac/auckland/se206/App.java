package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.CafeteriaController;
import nz.ac.auckland.se206.controllers.EndWonController;
import nz.ac.auckland.se206.controllers.OfficeController;
import nz.ac.auckland.se206.controllers.RoomController;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

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
   */
  @Override
  public void start(final Stage stage) throws IOException {

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

    CafeteriaController cafeteriaController = cafeteriaLoader.getController();
    EndWonController endWonController = endScreenWonLoader.getController();
    OfficeController officeController = officeSceneLoader.getController();

    cafeteriaController.setOfficeController(officeController);
    officeController.setCafeteriaController(cafeteriaController);


    Safe.getRandomCode();

    scene = new Scene(SceneManager.getUiRoot(AppUi.START_INTERFACE), 1113, 605);

    stage.setScene(scene);
    stage.show();
    SceneManager.getUiRoot(AppUi.START_INTERFACE).requestFocus();
  }
}
