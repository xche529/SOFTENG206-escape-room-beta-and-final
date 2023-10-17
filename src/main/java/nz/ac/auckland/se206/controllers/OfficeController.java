package nz.ac.auckland.se206.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameState.Difficulty;
import nz.ac.auckland.se206.GptAndTextAreaManager;
import nz.ac.auckland.se206.GptAndTextAreaManager.Characters;
import nz.ac.auckland.se206.MovementControl;
import nz.ac.auckland.se206.PlayHistory;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SoundEffect;
import nz.ac.auckland.se206.reseters.GameEnd;
import nz.ac.auckland.se206.reseters.RandomizationGenerator;

/** Controller for the office scene. */
public class OfficeController {

  @FXML private Rectangle deskDrawers;
  @FXML private Rectangle bin;
  @FXML private Rectangle blackBoard;
  @FXML private Rectangle phone;
  @FXML private ImageView binBig;
  @FXML private ImageView blackBoardBig;
  @FXML private ImageView phoneBig;
  @FXML private ImageView deskDrawersBig;
  @FXML private ImageView prisonerOne;
  @FXML private ImageView prisonerTwo;
  @FXML private ImageView speechBubbleOne;
  @FXML private ImageView speechBubbleTwo;
  @FXML private ImageView speechBubbleOneSmall;
  @FXML private ImageView speechBubbleTwoSmall;
  @FXML private ImageView thinkingOne;
  @FXML private ImageView thinkingTwo;
  @FXML private ImageView cog;
  @FXML private ImageView binArrow;
  @FXML private ImageView blackBoardArrow;
  @FXML private ImageView drawArrow;
  @FXML private ImageView phoneArrow;

  @FXML private TextArea inputBox;
  @FXML private TextArea chatDisplayBoard;
  @FXML private TextArea objectiveDisplayBoard;
  @FXML private Text typePromptText;
  @FXML private Label digitOne;

  @FXML private Label digitTwo;
  @FXML private Label digitThree;
  @FXML private Label digitFour;
  @FXML private Label digitFive;
  @FXML private Label digitSix;
  @FXML private Label digitSeven;
  @FXML private Label digitEight;
  @FXML private Label digitNine;
  @FXML private Button exitVeiwButton;
  @FXML private Pane paperPane;
  @FXML private Pane phonePane;
  @FXML private ImageView cross;
  @FXML private Label numberLabel;
  @FXML private Label timerLabel;
  @FXML private Pane inspectingBinPane;
  @FXML private Pane blurringPane;
  @FXML private Pane thoughtBubblePane;
  @FXML private Text thoughtBubbleText;
  @FXML private ImageView binConverter;
  @FXML private Pane inspectingBlackBoardPane;
  @FXML private ImageView inspectingBlackBoardConverter;
  @FXML private ImageView inspectingBlackBoardEmpty;
  @FXML private Pane inspectingDrawerPane;
  @FXML private ImageView drawerConverter;

  private Timeline timeline;
  private ImageView[] animationItems;
  private Label[] digits;
  private int currentDigit = 0;
  private Rectangle[] items;
  private SoundEffect winningSound = new SoundEffect("src/main/resources/sounds/Win.mp3");
  private SoundEffect lostSound = new SoundEffect("src/main/resources/sounds/Lost.mp3");


  /**
   * This method is called by the FXMLLoader when initialization is complete.
   *
   * @throws IOException for the reset checker
   */
  @FXML
  private void initialize() throws IOException {

    // creates an array with all of the items that can hide the cypher
    items =
        new Rectangle[] {
          bin, blackBoard, deskDrawers,
        };

    // sets all the variables and randomises the cypher location
    resetOffice();

    // starts a thread that constantly checks if the game is over
    resetchecker();

    // animates all the arrows
    animateArrows(binArrow);
    animateArrows(blackBoardArrow);
    animateArrows(drawArrow);
    animateArrows(phoneArrow);

    GptAndTextAreaManager.officeController = this;

    // creates an array with all of the animation items
    animationItems =
        new ImageView[] {
          prisonerOne,
          prisonerTwo,
          speechBubbleOne,
          speechBubbleTwo,
          speechBubbleOneSmall,
          speechBubbleTwoSmall,
          thinkingOne,
          thinkingTwo
        };

    // creates an array with all of the digits
    digits =
        new Label[] {
          digitOne,
          digitTwo,
          digitThree,
          digitFour,
          digitFive,
          digitSix,
          digitSeven,
          digitEight,
          digitNine
        };

    // if the user finds the paper, the phone number will be displayed
    GameState.hasPaperProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                paperPane.setVisible(true);
                numberLabel.setText(GameState.phoneNumber);
              }
            });
  }

  /**
   * animates the arrow so that it bounces up and down.
   *
   * @param arrow the arrow to be animates
   */
  public void animateArrows(ImageView arrow) {

    // sets the starting position of the arrows
    double startY = 0;

    // makes the arrow bounce up and down
    TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), arrow);
    translateTransition.setFromY(startY);
    translateTransition.setToY(startY + 5);
    translateTransition.setAutoReverse(true);
    translateTransition.setCycleCount(Animation.INDEFINITE);
    translateTransition.play();
  }

  /** animates the prisoner and the speech bubble */
  public void walkInAnimation() {
    MovementControl.moveToLeft(false, 1, 500, animationItems);
  }

  /** this method resets the loction of the prisoner and the speech bubble. */
  public void resetAnimation() {
    for (ImageView item : animationItems) {
      item.setTranslateX(-500);
    }
  }

  /** shows the enlarged speech bubble for the prisoner one. */
  public void setThinkingOneUp() {
    thinkingOne.setVisible(true);
  }

  /** hides the enlarged speech bubble for the prisoner one. */
  public void setThinkingOneDown() {
    thinkingOne.setVisible(false);
  }

  /** shows the enlarged speech bubble for the prisoner two. */
  public void setThinkingTwoUp() {
    thinkingTwo.setVisible(true);
  }

  /** hides the enlarged speech bubble for the prisoner two. */
  public void setThinkingTwoDown() {
    thinkingTwo.setVisible(false);
  }

  /**
   * exits the veiw of the drawer
   *
   * @param event the mouse event where the character clicks anywhare on the screen.
   */
  @FXML
  void onClickInspectingDrawerPane(MouseEvent event) {
    inspectingDrawerPane.setVisible(false);
    blurringPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
  }

  /**
   * shows the blown up drawer and if it has the cypher.
   *
   * @param event when the drawer is clicked
   */
  @FXML
  private void clickDeskDrawers(MouseEvent event) {
    drawArrow.setVisible(false);
    if (GameState.itemWithCypher == deskDrawers) {
      GameState.isConverterFoundProperty().set(true);
      // shows the drawer with the cypher
      inspectingDrawerPane.setVisible(true);
      drawerConverter.setVisible(true);
      thoughtBubblePane.setVisible(true);
      blurringPane.setVisible(true);
      // makes thecypher visable
      GameState.cypherFound = true;
      thoughtBubbleText.setText("What's that piece of paper???");

    } else {
      // shows an empty drawer
      inspectingDrawerPane.setVisible(true);
      blurringPane.setVisible(true);
      thoughtBubblePane.setVisible(true);
      thoughtBubbleText.setText("Hmm.. Nothing here...");
    }
  }

  /**
   * exits the veiw of the bin.
   *
   * @param event the mouse event wher the character clicks anywhare on the screen
   */
  @FXML
  void onClickInspectingBinPane(MouseEvent event) {
    inspectingBinPane.setVisible(false);
    blurringPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
    binConverter.setVisible(false);
  }

  /**
   * shows the blown up bin and if it has the cypher.
   *
   * @param event when the bin is clicked
   */
  @FXML
  private void clickBin(MouseEvent event) {
    binArrow.setVisible(false);
    if (GameState.itemWithCypher == bin) {
      GameState.isConverterFoundProperty().set(true);
      // shows the bin with the cypher
      GameState.cypherFound = true;
      inspectingBinPane.setVisible(true);
      binConverter.setVisible(true);
      thoughtBubblePane.setVisible(true);
      blurringPane.setVisible(true);
      thoughtBubbleText.setText("What's that piece of paper???");
    } else {
      // shows an empty bin
      inspectingBinPane.setVisible(true);
      blurringPane.setVisible(true);
      thoughtBubblePane.setVisible(true);
      thoughtBubbleText.setText("Hmm.. Nothing here...");
    }
  }

  /**
   * exits the view of the blackboard.
   *
   * @param event the mouse event wher the character clicks anywhare on the screen
   */
  @FXML
  void onClickInspectingBlackBoardPane(MouseEvent event) {
    inspectingBlackBoardPane.setVisible(false);
    blurringPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
  }

  /**
   * shows the blown up blackboard and if it has the cypher.
   *
   * @param event when the blackboard is clicked
   */
  @FXML
  private void clickBlackboard(MouseEvent event) {
    blackBoardArrow.setVisible(false);
    if (GameState.itemWithCypher == blackBoard) {
      GameState.isConverterFoundProperty().set(true);
      // shows the blackboard with the cypher
      GameState.cypherFound = true;
      inspectingBlackBoardPane.setVisible(true);
      inspectingBlackBoardConverter.setVisible(true);
      blurringPane.setVisible(true);
      thoughtBubblePane.setVisible(true);
      thoughtBubbleText.setText("What's that chart???");
    } else {
      // shows an empty blackboardS
      inspectingBlackBoardPane.setVisible(true);
      blurringPane.setVisible(true);
      thoughtBubblePane.setVisible(true);
      thoughtBubbleText.setText("Hmm.. Nothing here...");
    }
  }

  /**
   * enters the veiw of the phone.
   *
   * @param event the mouse event wher the character clicks on the phone
   */
  @FXML
  private void clickPhone(MouseEvent event) {
    // set the gamestate property to true
    GameState.isPhoneFoundProperty().set(true);
    // make relevant panes visible
    phoneArrow.setVisible(false);
    phonePane.setVisible(true);
    blurringPane.setVisible(true);
    // if the user has the paper, the phone number will be displayed
    if (GameState.hasPaperProperty().get()) {
      paperPane.setVisible(true);
      numberLabel.setText(GameState.phoneNumber);
    }
  }

  /** shows the enlarged desk drawer. */
  @FXML
  private void deskDrawersMouseEntered() {
    deskDrawersBig.setVisible(true);
  }

  /** hides the enlarged desk drawer. */
  @FXML
  private void deskDrawersMouseExited() {
    deskDrawersBig.setVisible(false);
  }

  /** shows the enlarged bin. */
  @FXML
  private void binMouseEntered() {
    binBig.setVisible(true);
  }

  /** hides the enlarged bin. */
  @FXML
  private void binMouseExited() {
    binBig.setVisible(false);
  }

  /** shows the enlarged blackboard when the mouse enters the blackboard. */
  @FXML
  private void blackBoardMouseEntered() {
    blackBoardBig.setVisible(true);
  }

  /** hides the enlarged blackboard when the mouse exits the blackboard. */
  @FXML
  private void blackBoardMouseExited() {
    blackBoardBig.setVisible(false);
  }

  /** shows the enlarged phone when the mouse enters the phone. */
  @FXML
  private void phoneMouseEntered() {
    phoneBig.setVisible(true);
  }

  /** hides the enlarged phone when the mouse exits the phone. */
  @FXML
  private void phoneMouseExited() {
    phoneBig.setVisible(false);
  }

  /**
   * changes the text in the text area to the prisoner ones conversation.
   *
   * @param event the mouse event where the character clicks on the prisoner
   */
  @FXML
  private void onSpeechBubbleOneClicked(MouseEvent event) {
    GptAndTextAreaManager.displayTarget(Characters.PRISONER_ONE);
    System.out.println("Speech bubble one clicked");
  }

  /**
   * changes the text in the text area to the prisoner twos conversation.
   *
   * @param event the mouse event where the character clicks on the prisoner
   */
  @FXML
  private void onSpeechBubbleTwoClicked(MouseEvent event) {
    GptAndTextAreaManager.displayTarget(Characters.PRISONER_TWO);
    System.out.println("Speech bubble two clicked");
  }

  @FXML
  private void onSetSpeechBubbleOneUp() {
    // shows the enlarged speech bubble for the prisoner one
    speechBubbleOne.setVisible(true);
  }

  @FXML
  private void onSetSpeechBubbleOneDown() {
    // hides the enlarged speech bubble for the prisoner one
    speechBubbleOne.setVisible(false);
  }

  @FXML
  private void onSetSpeechBubbleTwoUp() {
    // shows the enlarged speech bubble for the prisoner two
    speechBubbleTwo.setVisible(true);
  }

  @FXML
  private void onSetSpeechBubbleTwoDown() {
    // hides the enlarged speech bubble for the prisoner two
    speechBubbleTwo.setVisible(false);
  }

  @FXML
  private void oneMouseEntered() {
    // shows the image of the number one in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("1");
    }
  }

  @FXML
  private void oneMouseExited() {
    // hides the image of the number one in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  /**
   * sets the image of the number one in the next digit slot.
   *
   * @param event the mouse event where the character clicks on the number one
   */
  @FXML
  private void onClickOne(MouseEvent event) {
    // sets the image of the number one in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("1");
      currentDigit++;
    }
  }

  @FXML
  private void twoMouseEntered() {
    // shows the image of the number two in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("2");
    }
  }

  @FXML
  private void twoMouseExited() {
    // hides the image of the number two in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  /**
   * sets the image of the number two in the next digit slot.
   *
   * @param event the mouse event where the character clicks on the number two
   */
  @FXML
  private void onClickTwo(MouseEvent event) {
    // sets the image of the number two in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("2");
      currentDigit++;
    }
  }

  @FXML
  private void threeMouseEntered() {
    // shows the image of the number three in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("3");
    }
  }

  @FXML
  private void threeMouseExited() {
    // hides the image of the number three in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  /**
   * sets the image of the number three in the next digit slot.
   *
   * @param event the mouse event where the character clicks on the number three
   */
  @FXML
  private void onClickThree(MouseEvent event) {
    // sets the image of the number three in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("3");
      currentDigit++;
    }
  }

  @FXML
  private void fourMouseEntered() {
    // shows the image of the number four in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("4");
    }
  }

  @FXML
  private void fourMouseExited() {
    // hides the image of the number four in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  /**
   * sets the image of the number four in the next digit slot.
   *
   * @param event the mouse event where the character clicks on the number four
   */
  @FXML
  private void onClickFour(MouseEvent event) {
    // sets the image of the number four in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("4");
      currentDigit++;
    }
  }

  @FXML
  private void fiveMouseEntered() {
    // shows the image of the number five in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("5");
    }
  }

  @FXML
  private void fiveMouseExited() {
    // hides the image of the number five in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  /**
   * sets the image of the number five in the next digit slot.
   *
   * @param event the mouse event where the character clicks on the number five
   */
  @FXML
  private void onClickFive(MouseEvent event) {
    // sets the image of the number five in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("5");
      currentDigit++;
    }
  }

  @FXML
  private void sixMouseEntered() {
    // shows the image of the number six in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("6");
    }
  }

  @FXML
  private void sixMouseExited() {
    // hides the image of the number six in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  /**
   * sets the image of the number six in the next digit slot.
   *
   * @param event the mouse event where the character clicks on the number six
   */
  @FXML
  private void onClickSix(MouseEvent event) {
    // sets the image of the number six in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("6");
      currentDigit++;
    }
  }

  @FXML
  private void sevenMouseEntered() {
    // shows the image of the number seven in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("7");
    }
  }

  @FXML
  private void sevenMouseExited() {
    // hides the image of the number seven in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  /**
   * sets the image of the number seven in the next digit slot.
   *
   * @param event the mouse event where the character clicks on the number seven
   */
  @FXML
  private void onClickSeven(MouseEvent event) {
    // sets the image of the number seven in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("7");
      currentDigit++;
    }
  }

  @FXML
  private void eightMouseEntered() {
    // shows the image of the number eight in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("8");
    }
  }

  @FXML
  private void eightMouseExited() {
    // hides the image of the number eight in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  /**
   * sets the image of the number eight in the next digit slot.
   *
   * @param event the mouse event where the character clicks on the number eight
   */
  @FXML
  private void onClickEight(MouseEvent event) {
    // sets the image of the number eight in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("8");
      currentDigit++;
    }
  }

  @FXML
  private void nineMouseEntered() {
    // shows the image of the number nine in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("9");
    }
  }

  @FXML
  private void nineMouseExited() {
    // hides the image of the number nine in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  /**
   * sets the image of the number nine in the next digit slot.
   *
   * @param event the mouse event where the character clicks on the number nine
   */
  @FXML
  private void onClickNine(MouseEvent event) {
    // sets the image of the number nine in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("9");
      currentDigit++;
    }
  }

  @FXML
  private void zeroMouseEntered() {
    // shows the image of the number zero in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("0");
    }
  }

  @FXML
  private void zeroMouseExited() {
    // hides the image of the number zero in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  /**
   * sets the image of the number zero in the next digit slot.
   *
   * @param event the mouse event where the character clicks on the number zero
   */
  @FXML
  private void onClickZero(MouseEvent event) {
    // sets the image of the number zero in the next digit slot
    if (currentDigit < 9) {
      digits[currentDigit].setText("0");
      currentDigit++;
    }
  }

  /**
   * clears all the digits.
   *
   * @param event the mouse event where the character clicks on the clear button
   */
  @FXML
  private void onClickClear(MouseEvent event) {
    clearAll();
  }

  private void clearAll() {
    // clears all the digits
    digitOne.setText("_");
    digitTwo.setText("_");
    digitThree.setText("_");
    digitFour.setText("_");
    digitFive.setText("_");
    digitSix.setText("_");
    digitSeven.setText("_");
    digitEight.setText("_");
    digitNine.setText("_");
    // resets the current digit
    currentDigit = 0;
  }

  /**
   * deletes the last digit
   *
   * @param event the mouse event where the character clicks on the delete button
   */
  @FXML
  private void onClickDelete(MouseEvent event) {
    if (currentDigit > 0) {
      digits[currentDigit - 1].setText("_");
      currentDigit--;
    }
  }

  /**
   * checks if the number is correct and declares the game won if it is.
   *
   * @param event the mouse event where the character clicks on the call button
   */
  @FXML
  private void onClickCall(MouseEvent event) {
    if (currentDigit == 9) {
      // creates a string of the phone number
      String phoneNumber =
          digitOne.getText()
              + digitTwo.getText()
              + digitThree.getText()
              + " "
              + digitFour.getText()
              + digitFive.getText()
              + digitSix.getText()
              + " "
              + digitSeven.getText()
              + digitEight.getText()
              + digitNine.getText();
      if (phoneNumber.equals(GameState.phoneNumber)) {
        // if the strings match, the game is won
        GameState.isWon = true;
        Scene scene = phonePane.getScene();
        int timeTook = GameState.totalSeconds - GameState.secondsRemaining;
        // updates the play history
        int difficulty = 1;
        if (GameState.difficulty == Difficulty.MEDIUM) {
          difficulty = 2;
        } else if (GameState.difficulty == Difficulty.HARD) {
          difficulty = 3;
        }
        PlayHistory playHistory =
            new PlayHistory(
                timeTook, difficulty, GameState.playerName, GameState.getPlayerAvatar());
        try (ObjectInputStream ois =
            new ObjectInputStream(new FileInputStream("player_history.dat"))) {
          PlayHistory oldPlayHistory = (PlayHistory) ois.readObject();
          oldPlayHistory.addHistory(playHistory);
          playHistory.saveHistory();
        } catch (IOException | ClassNotFoundException e) {
          playHistory.saveHistory();
        }
        GameEnd.triggerResters();

        // switches to the end screen
        SceneManager.switchToEndWon(scene);
        SceneManager.settingsController.stopMusic();
        winningSound.playSFX();
      } else {
        // do nothing if the phone number is wrong
        System.out.println("Wrong number");
      }
    } else {
      // do nothing if the phone nuber is not long enough
      System.out.println("Number is invalid");
    }
  }

  /**
   * exits the veiw of the phone.
   *
   * @param event the mouse event where the character clicks on the exit button
   */
  @FXML
  private void onClickCross(MouseEvent event) {
    phonePane.setVisible(false);
    blurringPane.setVisible(false);
  }

  @FXML
  private void crossMouseEntered() {
    // shows the enlarged cross
    cross.setScaleX(1.15);
    cross.setScaleY(1.15);
  }

  @FXML
  private void crossMouseExited() {
    // hides the enlarged cross
    cross.setScaleX(1);
    cross.setScaleY(1);
  }

  @FXML
  private void onClickCog(MouseEvent event) {
    GameState.setSettingsVisable(true);
  }

  @FXML
  private void cogMouseEntered() {
    // shows the enlarged cog
    cog.setScaleX(1.2);
    cog.setScaleY(1.2);
  }

  @FXML
  private void cogMouseExited() {
    // hides the enlarged cog
    cog.setScaleX(1);
    cog.setScaleY(1);
  }

  private void resetchecker() throws IOException {
    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(0.1),
                event -> {
                  if (GameState.secondsRemaining >= 0) {
                    // updates the timer
                    updateTimerLabel();
                  }
                  if (GameState.secondsRemaining == 0) {
                    if (SceneManager.currentUi == SceneManager.AppUi.OFFICE) {
                      GameEnd.triggerResters();
                      try {
                        // changes to the end screen
                        Scene scene = phone.getScene();
                        SceneManager.switchToEndLost(scene);
                        SceneManager.settingsController.stopMusic();
                        lostSound.playSFX();

                      } catch (NullPointerException e) {
                        System.out.println("Scene not found");
                      }
                    }
                  }

                  if (GameState.resetOffice) {
                    resetOffice();
                  }
                }));
    // runs the thread
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  private void updateTimerLabel() {
    // updates the timer
    int minutes = (GameState.secondsRemaining) / 60;
    int seconds = (GameState.secondsRemaining) % 60;
    timerLabel.setText(String.format("%d:%02d", minutes, seconds));
  }

  /** restores the office to its original state. */
  private void resetOffice() {

    // Getting random item to be used to hide the cypher
    RandomizationGenerator.hideChypher(items);

    // resets the neccesary booleans
    GameState.cypherFound = false;
    GameState.resetOffice = false;
    GameState.isConverterFoundProperty().set(false);
    System.out.println("office reseted");

    // resets the visability of the items
    binArrow.setVisible(true);
    blackBoardArrow.setVisible(true);
    drawArrow.setVisible(true);
    phoneArrow.setVisible(true);
    phonePane.setVisible(false);
    paperPane.setVisible(false);

    // resets the digits
    clearAll();
  }
}
