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

  private static final BooleanProperty hasPaper = new SimpleBooleanProperty(false);

  private static final BooleanProperty isPhoneFound = new SimpleBooleanProperty(false);

  private static final BooleanProperty isCodeWordFound = new SimpleBooleanProperty(false);

  private static final BooleanProperty isConverterFound = new SimpleBooleanProperty(false);

  private static final BooleanProperty isSafeFound = new SimpleBooleanProperty(false);

  public static boolean isWon = false;

  public static String phoneNumber;

  public static String code;

  public static String codeWord;

  public static int secondsRemaining = 120;

  public static boolean wordFound = false;

  public static boolean cypherFound = false;

  public static boolean safeFound = false;

  public static boolean safeUnlocked = false;

  public static int totalSeconds = 120;

  public static Rectangle itemToChoose;

  public static Rectangle itemWithCypher;

  public static int questionsAsked = 0;

  public static boolean resetCafeteria = false;

  public static boolean resetOffice = false;

  public static boolean resetRoom = false;

  public static int hints = 5;

  public static int hintsLeft = 5;

  public static boolean stopTimer = false;

  public static String playerName = "Player";

  public static boolean isRiddleResolved() {
    return isRiddleResolvedProperty.get();
  }

  public static void setRiddleResolved(boolean value) {
    isRiddleResolvedProperty.set(value);
  }

  public static BooleanProperty isRiddleResolvedProperty() {
    return isRiddleResolvedProperty;
  }

  public static boolean hasPaper() {
    return hasPaper.get();
  }

  public static void setHasPaper(boolean value) {
    hasPaper.set(value);
  }

  public static BooleanProperty hasPaperProperty() {
    return hasPaper;
  }

  public static boolean isCodeWordFound() {
    return isCodeWordFound.get();
  }

  public static void setCodeWordFound(boolean value) {
    isCodeWordFound.set(value);
  }

  public static BooleanProperty isCodeWordFoundProperty() {
    return isCodeWordFound;
  }

  public static boolean isConverterFound() {
    return isConverterFound.get();
  }

  public static void setConverterFound(boolean value) {
    isConverterFound.set(value);
  }

  public static BooleanProperty isConverterFoundProperty() {
    return isConverterFound;
  }

  public static boolean isSafeFound() {
    return isSafeFound.get();
  }

  public static void setSafeFound(boolean value) {
    isSafeFound.set(value);
  }

  public static BooleanProperty isSafeFoundProperty() {
    return isSafeFound;
  }

  public static boolean isPhoneFound() {
    return isPhoneFound.get();
  }

  public static void setPhoneFound(boolean value) {
    isPhoneFound.set(value);
  }

  public static BooleanProperty isPhoneFoundProperty() {
    return isPhoneFound;
  }
}
