package nz.ac.auckland.se206;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PlayHistory implements Serializable {
  private double score;
  private int timeTook;
  private int difficulty;
  private String name;
  private PlayHistory childPlayHistory = null;
  private PlayHistory parentPlayHistory = null;

  public PlayHistory(int time, int difficulty, String name) {
    this.name = name;
    this.score = time / difficulty;
    this.timeTook = time;
    this.difficulty = difficulty;
  }

  public void addHistory(PlayHistory playHistory) {

    if (playHistory.score > this.score) {
      if (this.parentPlayHistory == null) {
        playHistory.childPlayHistory = this;
        this.parentPlayHistory = playHistory;
        return;
      } else {
        if (this.parentPlayHistory.score > playHistory.score) {
          playHistory.parentPlayHistory = this.parentPlayHistory;
          playHistory.childPlayHistory = this;
          this.parentPlayHistory.childPlayHistory = playHistory;
          this.parentPlayHistory = playHistory;
          return;
        } else {
          this.parentPlayHistory.addHistory(playHistory);
          return;
        }
      }
    } else {
      if (this.childPlayHistory == null) {
        this.childPlayHistory = playHistory;
        playHistory.parentPlayHistory = this;
        return;
      }
      if (this.childPlayHistory.score < playHistory.score) {
        playHistory.childPlayHistory = this.childPlayHistory;
        playHistory.parentPlayHistory = this;
        this.childPlayHistory.parentPlayHistory = playHistory;
        this.childPlayHistory = playHistory;
        return;
      } else {
        this.childPlayHistory.addHistory(playHistory);
        return;
      }
    }
  }

  public String toString() {
    PlayHistory playHistory = this;
    String result = "";
    if (this.childPlayHistory != null) {
      result = this.childPlayHistory.toString();
    } else {
      int rank = 1;
      do {
        result +=
            "Rank"
                + rank
                + ":\n "
                + playHistory.name
                + "\n"
                + " Time: "
                + playHistory.timeTook
                + "\n Difficulty: "
                + playHistory.difficulty
                + "\n\n";
        playHistory = playHistory.parentPlayHistory;
        rank++;
      } while (playHistory != null);
    }
    return result;
  }

  public void saveHistory() {
    try (ObjectOutputStream oos =
        new ObjectOutputStream(new FileOutputStream("player_history.dat"))) {
      oos.writeObject(this);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
