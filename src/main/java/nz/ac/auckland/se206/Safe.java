package nz.ac.auckland.se206;

import java.util.Random;

public class Safe {

  private static String[] fourLetterWords = {
    "back", "test", "idea", "star", "fire",
    "hope", "moon", "work", "luck", "book",
    "time", "game", "play", "hand", "code",
    "true", "fail", "life", "good", "home",
    "keep", "mind", "park", "road", "bear",
    "food", "rain", "snow", "song", "view",
    "tree", "lake", "walk", "blue", "pink",
    "dark", "rich", "cool", "warm", "wine",
    "fast", "slow", "love", "hate", "high",
    "rope", "open", "flow", "rich", "poor"
  };

  public static void getRandomCode() {

    Random random = new Random();
    int randomIndex = random.nextInt(fourLetterWords.length);

    GameState.code = getCodeFromWord(fourLetterWords[randomIndex]);
    GameState.codeWord = fourLetterWords[randomIndex];
  }

  private static String getCodeFromWord(String string) {
    String code = "";
    String converterValues[] = {"jt", "aku", "bvl", "cmw", "dnx", "eoy", "fpz", "gq", "hr", "is"};
    int digits[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    for (int i = 0; i < 4; i++) {
      char currentChar = string.charAt(i);
      for (int j = 0; j < 9; j++) {
        if (converterValues[j].contains(String.valueOf(currentChar))) {
          code = code + digits[j];
          break;
        }
      }
    }

    return code;
  }
}
