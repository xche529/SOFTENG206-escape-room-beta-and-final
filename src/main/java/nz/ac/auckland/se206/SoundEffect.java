package nz.ac.auckland.se206;

import java.io.File;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/** This class plays the sound effects in the game. */
public class SoundEffect {

  private String path;

  public SoundEffect(String path) {
    this.path = path;
  }

  /** Plays the sound effect in a separate thread to avoid lag. */
  public void playSfx() {
    Media media = new Media(new File(path).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setVolume(GameState.sfxVolume);
    // plays the sound effect in a separate thread to avoid lag
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            mediaPlayer.play();
            pauseSafeNoise(mediaPlayer);
            return null;
          }
        };
    Thread thread = new Thread(task);
    thread.start();
  }

  /**
   * Pauses the safe opening sound effect after 3 seconds and resumes it when the safe is closed.
   *
   * @param mediaPlayer the media player that plays the sound effect
   * @throws InterruptedException if the thread is interrupted
   */
  private void pauseSafeNoise(MediaPlayer mediaPlayer) throws InterruptedException {
    if (path == "src/main/resources/sounds/door-opening-and-closing-18398.mp3") {
      // plays the first 3 seconds then pauses the sound effect
      Thread.sleep(3000);
      mediaPlayer.pause();
      Duration length = mediaPlayer.getCurrentTime();
      // Checks if the safe is closed and resumes the sound effect
      GameState.isSafeClosed()
          .addListener(
              (observable, oldValue, newValue) -> {
                if (newValue) {
                  mediaPlayer.seek(length);
                  mediaPlayer.play();
                  GameState.isSafeClosed().set(false);
                }
              });
    }
  }
}
