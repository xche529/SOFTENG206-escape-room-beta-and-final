package nz;

public class PlayHistory {
    int score;
    int timeTook;
    int difficulty;
    PlayHistory childPlayHistory;

    public PlayHistory(int score, int time, int difficulty) {
        this.score = score;
        this.timeTook = time;
        this.difficulty = difficulty;
    }
}
