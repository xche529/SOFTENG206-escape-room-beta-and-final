package nz.ac.auckland.se206.reseters;

import nz.ac.auckland.se206.GameState;

public class GameEnd {

  public static void triggerResters() {
    // resets the game
    GameState.secondsRemaining = -1;
    GameState.resetCafeteria = true;
    GameState.resetOffice = true;
    GameState.resetRoom = true;
    GameState.resetTextArea = true;
  }
}
