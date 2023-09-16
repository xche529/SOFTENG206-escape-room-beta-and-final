package nz.ac.auckland.se206;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class PlayHistory {
  int score;
  int timeTook;
  int difficulty;
  String name;
  PlayHistory childPlayHistory = null;
  PlayHistory parentPlayHistory = null;

  public PlayHistory(int time, int difficulty, String name) {
    this.name = name;
    this.score = difficulty * time;
    this.timeTook = time;
    this.difficulty = difficulty;
  }

  public void addHistory(PlayHistory playHistory) {
    if (this.parentPlayHistory != null) {
      if (this.parentPlayHistory.score > playHistory.score && this.score < playHistory.score) {
        playHistory.childPlayHistory = this;
        this.parentPlayHistory = playHistory;
        this.parentPlayHistory.childPlayHistory = playHistory;
        return;
      }
    } else if (this.parentPlayHistory == null && this.score < playHistory.score) {
      playHistory.childPlayHistory = this;
      this.parentPlayHistory = playHistory;
      return;
    } else if (this.childPlayHistory == null && this.score > playHistory.score) {
      playHistory.parentPlayHistory = this;
      this.childPlayHistory = playHistory;
      return;
    } else if (this.score == playHistory.score) {
      if (this.childPlayHistory != null) {
        this.childPlayHistory.parentPlayHistory = playHistory;
      }
      this.childPlayHistory = playHistory;
      playHistory.parentPlayHistory = this;

    } else {
      if (playHistory.score > this.score) {
        this.parentPlayHistory.addHistory(playHistory);
      } else if (playHistory.score < this.score) {
        this.childPlayHistory.addHistory(playHistory);
      }
    }
  }

  public String toString() {
    PlayHistory playHistory = this;
    String result = "";
    if (this.parentPlayHistory != null) {
      result = this.parentPlayHistory.toString();
    } else {
      do {
        result =
            "Name: "
                + playHistory.name
                + " Score: "
                + playHistory.score
                + " Time: "
                + playHistory.timeTook
                + " Difficulty: "
                + playHistory.difficulty
                + "\n";
        playHistory = playHistory.childPlayHistory;
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
