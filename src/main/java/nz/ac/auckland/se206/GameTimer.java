package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.util.Duration;
import nz.ac.auckland.se206.reseters.GameEnd;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class GameTimer {

  private Timeline timeline;
  private TextToSpeech textToSpeech;

  public GameTimer() {
    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  if (GameState.stopTimer) {
                    timeline.stop();
                  }
                  GameState.secondsRemaining--;

                  // uses test to spech to tell the player how long they have left
                  if (GameState.secondsRemaining == 90 && GameState.isWon == false) {
                    textToSpeech.speak("a minute and a half remaining");
                  } else if (GameState.secondsRemaining == 60 && GameState.isWon == false) {
                    textToSpeech.speak("one minute remaining");
                  } else if (GameState.secondsRemaining == 30 && GameState.isWon == false) {
                    textToSpeech.speak("thirty seconds remaining");
                  }
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
    GameState.isGameClosed().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue) {
                    timeline.stop();
                    textToSpeech.terminate();
                }
            });

  }

  /** This method handles the event where timer reaches 0 */
  private void handleTimerExpired() {
    if (!GameState.isWon) {
      GameEnd.triggerResters();
    }
  }
}
