package nz.ac.auckland.se206;

import java.util.ArrayList;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;

/**
 * This method moves an image in the scene
 *
 * @param isToLeft       - boolean value to determine if the image is moving to
 *                       the left or right
 * @param timeToMove     - the time in seconds that the image will move for
 * @param distanceToMove - the distance in pixels that the image will move
 * @param imageView      - the image that will be moved
 */
public class MovementControl {
  public static void moveToLeft(
      boolean isToLeft, int timeToMove, int distanceToMove, ImageView imageView) {
    int timesToMove = timeToMove * 60;
    int distanceToMovePerIteration = distanceToMove / timesToMove;
    Thread timerThread = new Thread(
        () -> {
          for (int i = 0; i < timesToMove; i++) {
            try {
              Thread.sleep(50 / 3);
              if (isToLeft == true) {
                imageView.setX(imageView.getX() - distanceToMovePerIteration);
              } else {
                imageView.setX(imageView.getX() + distanceToMovePerIteration);
              }
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        });
    timerThread.start();
  }

  public static void moveToLeft(
      boolean isToLeft, double timeToMove, int distanceToMove, ImageView[] imageViews) {
    ArrayList<TranslateTransition> transitionsArray = new ArrayList<TranslateTransition>();
    for (ImageView imageView : imageViews) {
      TranslateTransition transition = new TranslateTransition();
      transition.setNode(imageView);
      transition.setDuration(javafx.util.Duration.seconds(timeToMove));
      if (isToLeft == true) {
        transition.setByX(-distanceToMove);
      } else {
        transition.setByX(distanceToMove);
      }
      transitionsArray.add(transition);
    }
    ParallelTransition parallelTransition = new ParallelTransition();
    parallelTransition.getChildren().addAll(transitionsArray);
    parallelTransition.play();
  }
}
