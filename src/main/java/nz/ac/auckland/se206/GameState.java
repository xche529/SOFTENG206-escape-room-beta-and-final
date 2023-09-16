package nz.ac.auckland.se206;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.shape.Rectangle;

/** Represents the state of the game. */
public class GameState {

  public static enum Difficulty {
    EASY,
    MEDIUM,
    HARD
  }

  public static Difficulty difficulty = Difficulty.EASY;

  /** Indicates whether the riddle has been resolved. */
  private static final BooleanProperty isRiddleResolvedProperty = new SimpleBooleanProperty(false);

  public static boolean isWon = false;

  public static int secondsRemaining = 1000;

  public static int totalSeconds = 120;

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