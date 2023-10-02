package nz.ac.auckland.se206;

import java.util.Random;
import javafx.scene.shape.Rectangle;

public class RandomizationGenerator {

  public static void hideChypher(Rectangle[] cypherItems) {
    Random randomChoose = new Random();
    int randomIndexChoose = randomChoose.nextInt(cypherItems.length);
    GameState.itemWithCypher = cypherItems[randomIndexChoose];
  }

  public static void createPhoneNunber() {
    Random random = new Random();

    // Generate a random 6-digit number
    String phoneNumberInitial = Integer.toString(random.nextInt(999999 - 100000 + 1) + 100000);
    GameState.phoneNumber =
        "027" + " " + phoneNumberInitial.substring(0, 3) + " " + phoneNumberInitial.substring(3, 6);
  }

    /** This method selects a random item to be used in the riddle. */
    public static void randomiseWord(Rectangle[] items) {
        Random randomChoose = new Random();
        int randomIndexChoose = randomChoose.nextInt(items.length);
        GameState.itemToChoose = items[randomIndexChoose];
      }
}
