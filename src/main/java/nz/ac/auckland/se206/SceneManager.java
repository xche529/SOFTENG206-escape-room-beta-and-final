package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import nz.ac.auckland.se206.controllers.CafeteriaController;
import nz.ac.auckland.se206.controllers.OfficeController;
import nz.ac.auckland.se206.controllers.RoomController;

public class SceneManager {

  public enum AppUi {
    ROOM,
    OFFICE,
    CAFETERIA,
    END_WON,
    END_LOST,
    START_INTERFACE,
    TEXT_AREA,
    SETTINGS,
    SIDE_CONVERSATION
  }

  public static AppUi currentUi = AppUi.START_INTERFACE;
  public static RoomController roomController;
  public static OfficeController officeController;
  public static CafeteriaController cafeteriaController;
  private static boolean isFirstRound = true;

  public static VBox settings;

  static AppUi[] appUis = {
    AppUi.ROOM, AppUi.OFFICE, AppUi.CAFETERIA,
  };

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();

  /*
   * Switches the scene to the next room
   *
   * @param isToLeft true if the player is moving to the left, false if the player
   * is moving to the right
   *
   * @param scene the scene to switch
   */
  public static void switchRoom(boolean isToLeft, Scene scene) {
    VBox roomToSwitch;

    if (currentUi == AppUi.START_INTERFACE) {
      // switching room sto the left
      roomToSwitch = (VBox) getUiRoot(AppUi.ROOM);

      currentUi = AppUi.ROOM;
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
        if (currentUi == appUis[i]) {
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
        // creating a playing walk animations
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
      currentUi = appUis[index];
      roomToSwitch = (VBox) getUiRoot(appUis[index]);
    }
    StackPane newRoomStack = new StackPane(roomToSwitch, settings);
    StackPane.setAlignment(settings, javafx.geometry.Pos.TOP_LEFT);
    HBox hbox = new HBox(newRoomStack, getUiRoot(AppUi.SIDE_CONVERSATION));
    VBox vbox = new VBox(hbox, getUiRoot(AppUi.TEXT_AREA));

    // setting up the vbox

    vbox.getTransforms().add(App.scale);
    vbox.setLayoutX(App.centerX);
    vbox.setLayoutY(App.centerY);

    scene.setRoot(vbox);
  }

  public static void switchToStart(Scene scene) {
    VBox endLost = (VBox) getUiRoot(AppUi.START_INTERFACE);
    GameState.setUpdatePlayHistory(true);
    // settings.getTransforms().add(App.scale);
    StackPane endLostStack = new StackPane(endLost, settings);
    StackPane.setAlignment(getUiRoot(AppUi.SETTINGS), javafx.geometry.Pos.TOP_LEFT);
    VBox vbox = new VBox(endLostStack);
    vbox.setLayoutX(App.centerX);
    vbox.setLayoutY(App.centerY);
    scene.setRoot(vbox);
    currentUi = AppUi.END_LOST;
  }

  public static void switchToEndWon(Scene scene) {
    VBox endWon = (VBox) getUiRoot(AppUi.END_WON);
    StackPane endWonStack = new StackPane(endWon, settings);
    settings.getTransforms().add(App.scale);
    StackPane.setAlignment(getUiRoot(AppUi.SETTINGS), javafx.geometry.Pos.TOP_LEFT);
    VBox vbox = new VBox(endWonStack);
    vbox.setLayoutX(App.centerX);
    vbox.setLayoutY(App.centerY);
    scene.setRoot(vbox);
    currentUi = AppUi.END_WON;
  }

  public static void switchToEndLost(Scene scene) {
    VBox endLost = (VBox) getUiRoot(AppUi.END_LOST);
    settings.getTransforms().add(App.scale);
    StackPane endLostStack = new StackPane(endLost, settings);
    StackPane.setAlignment(getUiRoot(AppUi.SETTINGS), javafx.geometry.Pos.TOP_LEFT);
    VBox vbox = new VBox(endLostStack);
    vbox.setLayoutX(App.centerX);
    vbox.setLayoutY(App.centerY);
    scene.setRoot(vbox);
    currentUi = AppUi.END_LOST;
  }

  public static void switchToFirstRoom(Scene scene) {
    VBox room = (VBox) getUiRoot(AppUi.ROOM);
    if (isFirstRound) {
      isFirstRound = false;
    } else {
      Scale settingsScale = new Scale(1.39, 1.39);
      settings.getTransforms().add(settingsScale);
    }
    roomController.resetAnimation();
    roomController.walkInAnimation();
    StackPane endLostStack = new StackPane(room, settings);
    StackPane.setAlignment(getUiRoot(AppUi.SETTINGS), javafx.geometry.Pos.TOP_LEFT);
    VBox vbox = new VBox(endLostStack, getUiRoot(AppUi.TEXT_AREA));
    vbox.getTransforms().add(App.scale);
    vbox.setLayoutX(App.centerX);
    vbox.setLayoutY(App.centerY);
    scene.setRoot(vbox);
    currentUi = AppUi.ROOM;
  }

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
