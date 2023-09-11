package nz.ac.auckland.se206;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.shape.Rectangle;

/** Represents the state of the game. */
public class GameState {

  /** Indicates whether the riddle has been resolved. */
  private static final BooleanProperty isRiddleResolvedProperty = new SimpleBooleanProperty(false);

  public static boolean isWon = false;

  public static int secondsRemaining = 120;

  public static Rectangle itemToChoose;

  public static int questionsAsked = 0;

  public static boolean isRiddleResolved() {
    return isRiddleResolvedProperty.get();
  }

  public static void setRiddleResolved(boolean value) {
    isRiddleResolvedProperty.set(value);
  }

  public static BooleanProperty isRiddleResolvedProperty() {
    return isRiddleResolvedProperty;
  }
}
