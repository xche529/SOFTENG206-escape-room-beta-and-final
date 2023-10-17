package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.speech.TextToSpeech;

/** This class is used to create a timer for the game. */
public class GameTimer {

  private Timeline timeline;
  private TextToSpeech textToSpeech;

  /** This method creates a timer that runs the timer in the game by updating every second */
  public GameTimer() {
    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  if (GameState.stopTimer) {
                    timeline.stop();
                    GameState.stopTimer = false;
                  }
                  GameState.secondsRemaining--;

                  // uses text to spech to tell the player how long they have left
                  if (GameState.secondsRemaining == 90 && GameState.isWon == false) {
                    textToSpeech.speak("a minute and a half remaining");
                    GptAndTextAreaManager.sideConversationController.refreshMessages(
                        GptPromptEngineering.noTimeLeftPrisonerPrompt());
                  } else if (GameState.secondsRemaining == 60 && GameState.isWon == false) {
                    textToSpeech.speak("one minute remaining");
                  } else if (GameState.secondsRemaining == 30 && GameState.isWon == false) {
                    textToSpeech.speak("thirty seconds remaining");
                  }
                  // displays the message sender if there is a new message
                  if (GptAndTextAreaManager.isNewMessage) {
                    GptAndTextAreaManager.displayTarget();
                    GptAndTextAreaManager.isNewMessage = false;
                  }
                }));

    timeline.setCycleCount(GameState.totalSeconds);
    timeline.setOnFinished(event -> handleTimerExpired());
    // wlecomes the player to the room
    textToSpeech = new TextToSpeech();
    textToSpeech.speak("Welcome to the room");
    timeline.play();

    // animates all the arrows if the riddle is resolved
    GameState.isGameClosed()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                timeline.stop();
                textToSpeech.terminate();
              }
            });
  }

  /* This method handles the event where timer reaches 0 */
  private void handleTimerExpired() {
    if (!GameState.isWon) {
      GameState.secondsRemaining = 0;
    }
  }
}
