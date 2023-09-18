package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;

public class SceneManager {

  public enum AppUi {
    ROOM,
    OFFICE,
    CAFETERIA,
    END_WON,
    END_LOST,
    START_INTERFACE
  }

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();

  public static void addUi(AppUi ui, Parent uiRoot) {
    sceneMap.put(ui, uiRoot);
  }

  public static Parent getUiRoot(AppUi ui) {
    return sceneMap.get(ui);
  }

public static Object getInstance() {
    return null;
}
}
