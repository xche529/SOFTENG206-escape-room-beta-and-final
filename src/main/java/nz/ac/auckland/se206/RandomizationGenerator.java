package nz.ac.auckland.se206;

import java.util.Random;
import javafx.scene.shape.Rectangle;

public class RandomizationGenerator {

  public static void hideChypher(Rectangle[] cypherItems) {
    Random randomChoose = new Random();
    int randomIndexChoose = randomChoose.nextInt(cypherItems.length);
    GameState.itemWithCypher = cypherItems[randomIndexChoose];
  }
}
