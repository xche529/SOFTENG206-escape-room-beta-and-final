package nz.ac.auckland.se206;

import javafx.scene.image.ImageView;

/**
 * This method moves an image in the scene
 *
 * @param isToLeft - boolean value to determine if the image is moving to the left or right
 * @param timeToMove - the time in seconds that the image will move for
 * @param distanceToMove - the distance in pixels that the image will move
 * @param imageView - the image that will be moved
 */
public class MovementControl {
  public static void moveToLeft(
      boolean isToLeft, int timeToMove, int distanceToMove, ImageView imageView) {
    int timesToMove = timeToMove * 60;
    int distanceToMovePerIteration = distanceToMove / timesToMove;
    Thread timerThread =
        new Thread(
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
      boolean isToLeft, int timeToMove, int distanceToMove, ImageView[] imageViews) {
    int timesToMove = timeToMove * 120;
    int distanceToMovePerIteration = distanceToMove / timesToMove;
    Thread timerThread =
        new Thread(
            () -> {
              for (int i = 0; i < timesToMove; i++) {
                try {
                  Thread.sleep(25 / 3);
                  for (int j = 0; j < imageViews.length; j++)
                    if (isToLeft == true) {
                      imageViews[j].setX(imageViews[j].getX() - distanceToMovePerIteration);
                    } else {
                      imageViews[j].setX(imageViews[j].getX() + distanceToMovePerIteration);
                    }
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            });
    timerThread.start();
  }
}
