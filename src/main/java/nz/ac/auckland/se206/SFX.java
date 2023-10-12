package nz.ac.auckland.se206;

import java.io.File;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class SFX {

  private String path;

  public SFX(String path) {
    this.path = path;
  }

  public void playSFX() {

    // runs the safe opening sound effect
    Media media = new Media(new File(path).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setVolume(GameState.sfxVolume);
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

private void pauseSafeNoise(MediaPlayer mediaPlayer) throws InterruptedException {
      if (path == "src/main/resources/sounds/door-opening-and-closing-18398.mp3") {
        //plays the first 3 seconds then pauses the sound effect
        Thread.sleep(3000);
        mediaPlayer.pause();
        Duration length = mediaPlayer.getCurrentTime();
        // animates all the arrows if the riddle is resolved
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
