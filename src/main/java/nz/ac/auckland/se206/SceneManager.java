package nz.ac.auckland.se206;

import java.util.HashMap;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.controllers.CafeteriaController;
import nz.ac.auckland.se206.controllers.OfficeController;
import nz.ac.auckland.se206.controllers.RoomController;

public class SceneManager {

  public static RoomController roomController;
  public static OfficeController officeController;
  public static CafeteriaController cafeteriaController;

  public enum AppUi {
    ROOM,
    OFFICE,
    CAFETERIA,
    END_WON,
    END_LOST,
    START_INTERFACE,
    TEXT_AREA
  }

  public static AppUi curretUi = AppUi.START_INTERFACE;

  static AppUi[] appUis = {
      AppUi.ROOM,
      AppUi.OFFICE,
      AppUi.CAFETERIA,
  };

/*
 * Switches the scene to the next room
 * @param isToLeft true if the player is moving to the left, false if the player is moving to the right
 * @param scene the scene to switch
 */
  public static void switchRoom(boolean isToLeft, Scene scene) {
    VBox roomToSwitch = null;

    if (curretUi == AppUi.START_INTERFACE) {
      roomToSwitch = (VBox) getUiRoot(AppUi.ROOM);
      curretUi = AppUi.ROOM;
      roomController.resetAnimation();
      roomController.walkInAnimation();
    } else {
      if (isToLeft) {
        System.out.println("Moving to left");
      } else {
        System.out.println("Moving to right");
      }
      int index = 0;
      for (int i = 0; i < appUis.length; i++) {
        if (curretUi == appUis[i]) {
          index = i;
        }
      }
      if (index == 0 && isToLeft) {
        index = appUis.length - 1;
      } else if (isToLeft) {
        index--;
      } else if (index == appUis.length - 1) {
        index = 0;
      } else {
        index++;
      }
      if (index == 0) {
        roomController.resetAnimation();
        roomController.walkInAnimation();
      } else if (index == 1) {
        officeController.resetAnimation();
        officeController.walkInAnimation();
      } else {
        cafeteriaController.resetAnimation();
        cafeteriaController.walkInAnimation();
      }
      System.out.println("Index: " + index);
      curretUi = appUis[index];
      roomToSwitch = (VBox) getUiRoot(appUis[index]);
    }
    VBox vBox = new VBox(roomToSwitch, getUiRoot(AppUi.TEXT_AREA));


    vBox.getTransforms().add(App.scale);
    vBox.setLayoutX(App.centerX);
    vBox.setLayoutY(App.centerY);

    scene.setRoot(vBox);
  }

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();

  public static void addUi(AppUi ui, VBox uiRoot) {
    sceneMap.put(ui, uiRoot);
  }

  public static Parent getUiRoot(AppUi ui) {
    return sceneMap.get(ui);
  }

  public static Object getInstance() {
    return null;
  }
}
