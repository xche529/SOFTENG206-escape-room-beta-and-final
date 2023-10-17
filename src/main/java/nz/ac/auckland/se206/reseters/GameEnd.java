package nz.ac.auckland.se206.reseters;

import nz.ac.auckland.se206.GameState;

/** This class is used to reset the game when the player wins or loses. */
public class GameEnd {

  /** This method is used to reset the game when the player wins or loses. */
  public static void triggerResters() {
    // resets the game
    GameState.secondsRemaining = 0;
    GameState.resetCafeteria = true;
    GameState.resetOffice = true;
    GameState.resetRoom = true;
    GameState.resetTextArea = true;
  }
}
