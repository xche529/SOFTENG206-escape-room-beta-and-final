package nz.ac.auckland.se206;

import java.util.Random;

public class Safe {

  // defining list of random words to use
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

  /** This method generates a random codeword and sets it to the GameState */
  public static void getRandomCode() {

    // getting a random string and turning into code

    Random random = new Random();
    int randomIndex = random.nextInt(fourLetterWords.length);

    GameState.code = getCodeFromWord(fourLetterWords[randomIndex]);
    GameState.codeWord = fourLetterWords[randomIndex];
  }

  /**
   * This method converts the codeword into to a string of integers coresponding to the numbers on
   * the cypher
   *
   * @param string
   * @return a 4 didgit string of integers
   */
  private static String getCodeFromWord(String string) {
    // getting code from word
    String[] converterValues = {"jt", "aku", "bvl", "cmw", "dnx", "eoy", "fpz", "gq", "hr", "is"};
    int[] digits = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    StringBuilder codeBuilder = new StringBuilder();
    // iterating through all characters
    for (int i = 0; i < 4; i++) {
      char currentChar = string.charAt(i);
      for (int j = 0; j < 10; j++) {
        // appending number to code string
        if (converterValues[j].contains(String.valueOf(currentChar))) {
          codeBuilder.append(digits[j]);
          break;
        }
      }
    }
    return codeBuilder.toString();
  }
}
