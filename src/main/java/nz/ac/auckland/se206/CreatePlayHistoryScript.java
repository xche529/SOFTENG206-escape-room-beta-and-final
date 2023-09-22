package nz.ac.auckland.se206;

public class CreatePlayHistoryScript {
  public static void main(String[] args) {
    PlayHistory playHistory = new PlayHistory(555, 1, "player1");
    playHistory.addHistory(new PlayHistory(22, 1, "player2"));
    playHistory.addHistory(new PlayHistory(35, 1, "player3"));
    playHistory.addHistory(new PlayHistory(35, 2, "player4"));
    playHistory.saveHistory();
    String history = playHistory.toString();
    System.out.println(history);
  }
}
