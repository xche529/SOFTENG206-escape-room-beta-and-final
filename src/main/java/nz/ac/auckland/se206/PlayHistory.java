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

    if (playHistory.getScore() > this.score) {
      if (this.parentPlayHistory == null) {
        playHistory.setChildPlayHistory(this); 
        this.parentPlayHistory = playHistory;
        return;
      } else {
        if (getParentPlayHistory().getScore() > playHistory.getScore()) {
          playHistory.setParentPlayHistory(this.parentPlayHistory);
          playHistory.setChildPlayHistory(this);
          getParentPlayHistory().setChildPlayHistory(playHistory);
          this.setParentPlayHistory(playHistory);
          return;
        } else {
          this.parentPlayHistory.addHistory(playHistory);
          return;
        }
      }
    } else {
      if (this.childPlayHistory == null) {
        this.childPlayHistory = playHistory;
        playHistory.setParentPlayHistory(this);
        return;
      }
      if (this.childPlayHistory.getScore() < playHistory.getScore()) {
        playHistory.setChildPlayHistory(this.childPlayHistory);
        playHistory.setParentPlayHistory(this);
        this.childPlayHistory.setParentPlayHistory(playHistory);
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
        result+=(
            "Rank"
                + rank
                + ":\n "
                + playHistory.getName()
                + "\n"
                + " Time: "
                + playHistory.getTimeTook()
                + "\n Difficulty: "
                + playHistory.getDifficulty()
                + "\n\n");
        playHistory = playHistory.getParentPlayHistory();
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

  public double getScore() {
    return score;
  }

  public int getTimeTook() {
    return timeTook;
  }

  public int getDifficulty() {
    return difficulty;
  }

  public String getName() {
    return name;
  }

  public PlayHistory getParentPlayHistory() {
    return parentPlayHistory;
  }

  public PlayHistory getChildPlayHistory() {
    return childPlayHistory;
  }

  public void setParentPlayHistory(PlayHistory parentPlayHistory) {
    this.parentPlayHistory = parentPlayHistory;
  }

  public void setChildPlayHistory(PlayHistory childPlayHistory) {
    this.childPlayHistory = childPlayHistory;
  }
}
