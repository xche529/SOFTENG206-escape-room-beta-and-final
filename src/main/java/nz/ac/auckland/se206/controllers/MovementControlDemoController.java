package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.MovementControl;

public class MovementControlDemoController {

  @FXML private Button moveLeftButton;
  @FXML private Button moveRightButton;
  @FXML private ImageView imageView;

  // you can change these values to change the speed and distance of the arrow
  int timeToMove = 1;

  int distanceToMove = 200;

  /* Call this method when you want to move the character to the left */
  @FXML
  private void moveLeft() {
    MovementControl.moveToLeft(true, timeToMove, distanceToMove, imageView);
  }

  @FXML
  private void moveRight() {
    MovementControl.moveToLeft(false, timeToMove, distanceToMove, imageView);
  }
}
