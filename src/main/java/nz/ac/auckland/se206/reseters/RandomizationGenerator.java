package nz.ac.auckland.se206.reseters;

import java.util.Random;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameState;

/** This class is used to generate random numbers and items for the game. */
public class RandomizationGenerator {

  /**
   * This method selects a random item to hide the cypher in.
   *
   * @param cypherItems - the items that will contain the cypher
   */
  public static void hideChypher(Rectangle[] cypherItems) {
    Random randomChoose = new Random();
    int randomIndexChoose = randomChoose.nextInt(cypherItems.length);
    GameState.itemWithCypher = cypherItems[randomIndexChoose];
  }

  /** This method creates a random phone number. */
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
