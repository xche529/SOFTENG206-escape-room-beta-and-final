package nz.ac.auckland.se206;

import java.io.File;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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
            return null;
          }
        };
    Thread thread = new Thread(task);
    thread.start();
  }
}
