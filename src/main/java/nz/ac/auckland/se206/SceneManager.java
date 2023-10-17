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

/** This class is used to switch between the different scenes in the game. */
public class SceneManager {

  /** This enum is used to keep track of the scenes */
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

  /**
   * Switches the scene to the next room.
   *
   * @param isToLeft true if the player is moving to the left, false if the player is moving to the
   *     right
   * @param scene the scene to switch
   */
  public static void switchRoom(boolean isToLeft, Scene scene) {
    VBox roomToSwitch;

    if (currentUi == AppUi.START_INTERFACE) {
      // goes to the starting room
      roomToSwitch = (VBox) getUiRoot(AppUi.CAFETERIA);
      currentUi = AppUi.CAFETERIA;
      // starts the characters animations
      cafeteriaController.resetAnimation();
      cafeteriaController.walkInAnimation();
    } else {
      // prints the action being taken in the terminal for debugging purposes
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
      if (index == 0) { // creating a playing walk animation in the new room
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
    // stacks the settings directly above the current room
    StackPane newRoomStack = new StackPane(roomToSwitch, settings);
    StackPane.setAlignment(settings, javafx.geometry.Pos.TOP_LEFT);
    HBox hbox = new HBox(newRoomStack, getUiRoot(AppUi.SIDE_CONVERSATION));
    VBox vbox = new VBox(hbox, getUiRoot(AppUi.TEXT_AREA));

    // setting up the vbox
    vbox.getTransforms().add(App.scale);
    vbox.setLayoutX(App.centerX);
    vbox.setLayoutY(App.centerY);

    // switches to the new room
    scene.setRoot(vbox);
  }

  /**
   * Switches the scene to the start interface.
   *
   * @param scene the scene that we are currently on
   */
  public static void switchToStart(Scene scene) {
    // get the start interface
    VBox endLost = (VBox) getUiRoot(AppUi.START_INTERFACE);
    GameState.setUpdatePlayHistory(true);
    // get the end lost pane
    StackPane endLostStack = new StackPane(endLost, settings);
    StackPane.setAlignment(getUiRoot(AppUi.SETTINGS), javafx.geometry.Pos.TOP_LEFT);
    VBox vbox = new VBox(endLostStack);
    // get the position of the vbox
    vbox.setLayoutX(App.centerX);
    vbox.setLayoutY(App.centerY);
    scene.setRoot(vbox);
    currentUi = AppUi.END_LOST;
  }

  /**
   * Switches the scene to the end won interface.
   *
   * @param scene the scene that we are currently on
   */
  public static void switchToEndWon(Scene scene) {
    // get the end won interface
    VBox endWon = (VBox) getUiRoot(AppUi.END_WON);
    StackPane endWonStack = new StackPane(endWon, settings);
    // add the scale
    settings.getTransforms().add(App.scale);
    StackPane.setAlignment(getUiRoot(AppUi.SETTINGS), javafx.geometry.Pos.TOP_LEFT);
    // add the vbox
    VBox vbox = new VBox(endWonStack);
    // set the layout
    vbox.setLayoutX(App.centerX);
    vbox.setLayoutY(App.centerY);
    scene.setRoot(vbox);
    // set the current ui
    currentUi = AppUi.END_WON;
  }

  /**
   * Switches the scene to the end lost interface.
   *
   * @param scene the scene that we are currently on
   */
  public static void switchToEndLost(Scene scene) {
    // get the end lost interface
    VBox endLost = (VBox) getUiRoot(AppUi.END_LOST);
    // add the scale
    settings.getTransforms().add(App.scale);
    StackPane endLostStack = new StackPane(endLost, settings);
    // set the alignment of the stackpane
    StackPane.setAlignment(getUiRoot(AppUi.SETTINGS), javafx.geometry.Pos.TOP_LEFT);
    VBox vbox = new VBox(endLostStack);
    // set the layout
    vbox.setLayoutX(App.centerX);
    vbox.setLayoutY(App.centerY);
    scene.setRoot(vbox);
    // set the current ui
    currentUi = AppUi.END_LOST;
  }

  /**
   * Switches the scene to the office.
   *
   * @param scene the scene that we are currently on
   */
  public static void switchToFirstRoom(Scene scene) {
    VBox room = (VBox) getUiRoot(AppUi.CAFETERIA);
    if (isFirstRound) {
      isFirstRound = false;
    } else { // This code is used to scale the room to the correct size, as it shrinks on every
      // round without it
      Scale settingsScale = new Scale(1.39, 1.39);
      settings.getTransforms().add(settingsScale);
    }
    cafeteriaController.resetAnimation();
    cafeteriaController.walkInAnimation();
    StackPane endLostStack = new StackPane(room, settings);
    StackPane.setAlignment(getUiRoot(AppUi.SETTINGS), javafx.geometry.Pos.TOP_LEFT);
    VBox vbox = new VBox(endLostStack, getUiRoot(AppUi.TEXT_AREA));
    vbox.getTransforms().add(App.scale);
    vbox.setLayoutX(App.centerX);
    vbox.setLayoutY(App.centerY);
    scene.setRoot(vbox);
    currentUi = AppUi.CAFETERIA;
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
