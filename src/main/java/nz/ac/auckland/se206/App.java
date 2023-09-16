package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.Random;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.AppUi;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

  private static Scene scene;

  public static void main(final String[] args) {
    launch();
  }

  public static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFxml(fxml));
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  public static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    Parent root = loadFxml("room");

    SceneManager.addUi(AppUi.END_WON, loadFxml("endScreenWon"));
    SceneManager.addUi(AppUi.END_LOST, App.loadFxml("endScreenLost"));
    SceneManager.addUi(AppUi.ROOM, App.loadFxml("room"));
    SceneManager.addUi(AppUi.OFFICE, App.loadFxml("officeScene"));
    SceneManager.addUi(AppUi.CAFETERIA, App.loadFxml("cafeteria"));

    String[] fourLetterWords = {
      "back", "test", "idea", "star", "fire",
      "hope", "moon", "work", "luck", "book",
      "time", "game", "play", "hand", "code",
      "true", "false", "life", "good", "home",
      "keep", "mind", "park", "road", "bear",
      "food", "rain", "snow", "song", "view",
      "tree", "lake", "walk", "blue", "pink",
      "dark", "rich", "cool", "warm", "wine",
      "fast", "slow", "love", "hate", "high",
      "low", "open", "close", "rich", "poor"
    };

    Random random = new Random();
    int randomIndex = random.nextInt(fourLetterWords.length);

    GameState.codeWord = getCode(fourLetterWords[randomIndex]);

    scene = new Scene(SceneManager.getUiRoot(AppUi.ROOM), 742, 403);
    stage.setScene(scene);
    stage.show();
    root.requestFocus();
  }

  private int getCode(String string) {
    for (int i = 1; i < 4; i++) {
      if ("aku".contains(String.valueOf(string.charAt(i))))
        ;
    }
  }
}
