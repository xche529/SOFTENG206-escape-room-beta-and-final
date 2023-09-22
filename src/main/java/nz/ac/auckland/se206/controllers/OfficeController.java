package nz.ac.auckland.se206.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Parent;
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
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameState.Difficulty;
import nz.ac.auckland.se206.GptAndTextAreaManager;
import nz.ac.auckland.se206.GptAndTextAreaManager.Characters;
import nz.ac.auckland.se206.MovementControl;
import nz.ac.auckland.se206.PlayHistory;
import nz.ac.auckland.se206.SceneManager;

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
  @FXML private ImageView crossBig;
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

  @FXML
  private void initialize() throws IOException {

    resetOffice();
    resetchecker();

    animateArrows(binArrow);
    animateArrows(blackBoardArrow);
    animateArrows(drawArrow);
    animateArrows(phoneArrow);

    GptAndTextAreaManager.officeController = this;

    animationItems =
        new ImageView[] {
          prisonerOne,
          prisonerTwo,
          speechBubbleOne,
          speechBubbleTwo,
          speechBubbleOneSmall,
          speechBubbleTwoSmall
        };

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

    GameState.hasPaperProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                paperPane.setVisible(true);
                numberLabel.setText(GameState.phoneNumber);
              }
            });
  }

  public void animateArrows(ImageView arrow) {
    double startY = 0;

    TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), arrow);
    translateTransition.setFromY(startY);
    translateTransition.setToY(startY + 5);
    translateTransition.setAutoReverse(true);
    translateTransition.setCycleCount(Animation.INDEFINITE);
    translateTransition.play();
  }

  public void walkInAnimation() {
    MovementControl.moveToLeft(false, 1, 500, animationItems);
  }

  public void resetAnimation() {
    for (ImageView item : animationItems) {
      item.setTranslateX(-500);
    }
  }

  public void setThinkingOneUp() {
    thinkingOne.setVisible(true);
  }

  public void setThinkingOneDown() {
    thinkingOne.setVisible(false);
  }

  public void setThinkingTwoUp() {
    thinkingTwo.setVisible(true);
  }

  public void setThinkingTwoDown() {
    thinkingTwo.setVisible(false);
  }

  @FXML
  void onClickInspectingDrawerPane() {
    inspectingDrawerPane.setVisible(false);
    blurringPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
  }

  @FXML
  private void clickDeskDrawers(MouseEvent event) {
    if (GameState.itemWithCypher == deskDrawers) {

      //makes thecypher visable
      GameState.cypherFound = true;
      inspectingDrawerPane.setVisible(true);
      drawerConverter.setVisible(true);
      thoughtBubblePane.setVisible(true);
      blurringPane.setVisible(true);
      thoughtBubbleText.setText("What's that piece of paper???");

    } else {
      //shows an empty drawer
      inspectingDrawerPane.setVisible(true);
      blurringPane.setVisible(true);
      thoughtBubblePane.setVisible(true);
      thoughtBubbleText.setText("Hmm.. Nothing here...");
    }
  }

  @FXML
  void onClickInspectingBinPane() {
    inspectingBinPane.setVisible(false);
    blurringPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
    binConverter.setVisible(false);
  }

  @FXML
  private void clickBin(MouseEvent event) {
    if (GameState.itemWithCypher == bin) {
      GameState.cypherFound = true;
      inspectingBinPane.setVisible(true);
      binConverter.setVisible(true);
      thoughtBubblePane.setVisible(true);
      blurringPane.setVisible(true);
      thoughtBubbleText.setText("What's that piece of paper???");
    } else {
      inspectingBinPane.setVisible(true);
      blurringPane.setVisible(true);
      thoughtBubblePane.setVisible(true);
      thoughtBubbleText.setText("Hmm.. Nothing here...");
    }
  }

  @FXML
  void onClickInspectingBlackBoardPane() {
    inspectingBlackBoardPane.setVisible(false);
    blurringPane.setVisible(false);
    thoughtBubblePane.setVisible(false);
  }

  @FXML
  private void clickBlackboard(MouseEvent event) {
    if (GameState.itemWithCypher == blackBoard) {
      GameState.cypherFound = true;
      inspectingBlackBoardPane.setVisible(true);
      inspectingBlackBoardConverter.setVisible(true);
      blurringPane.setVisible(true);
      thoughtBubblePane.setVisible(true);
      thoughtBubbleText.setText("What's that chart???");
    } else {
      inspectingBlackBoardPane.setVisible(true);
      blurringPane.setVisible(true);
      thoughtBubblePane.setVisible(true);
      thoughtBubbleText.setText("Hmm.. Nothing here...");
    }
  }

  @FXML
  private void clickPhone(MouseEvent event) {
    phoneArrow.setVisible(false);
    phonePane.setVisible(true);
  }

  @FXML
  private void deskDrawersMouseEntered() {
    deskDrawersBig.setVisible(true);
  }

  @FXML
  private void deskDrawersMouseExited() {
    deskDrawersBig.setVisible(false);
  }

  @FXML
  private void binMouseEntered() {
    binBig.setVisible(true);
  }

  @FXML
  private void binMouseExited() {
    binBig.setVisible(false);
  }

  @FXML
  private void blackBoardMouseEntered() {
    blackBoardBig.setVisible(true);
  }

  @FXML
  private void blackBoardMouseExited() {
    blackBoardBig.setVisible(false);
  }

  @FXML
  private void phoneMouseEntered() {
    phoneBig.setVisible(true);
  }

  @FXML
  private void phoneMouseExited() {
    phoneBig.setVisible(false);
  }

  @FXML
  private void onSpeechBubbleOneClicked() {
    GptAndTextAreaManager.displayTarget(Characters.PRISONER_ONE);
    System.out.println("Speech bubble one clicked");
  }

  @FXML
  private void onSpeechBubbleTwoClicked() {
    GptAndTextAreaManager.displayTarget(Characters.PRISONER_TWO);
    System.out.println("Speech bubble two clicked");
  }

  @FXML
  private void onSetSpeechBubbleOneUp() {
    speechBubbleOne.setVisible(true);
  }

  @FXML
  private void onSetSpeechBubbleOneDown() {
    speechBubbleOne.setVisible(false);
  }

  @FXML
  private void onSetSpeechBubbleTwoUp() {
    speechBubbleTwo.setVisible(true);
  }

  @FXML
  private void onSetSpeechBubbleTwoDown() {
    speechBubbleTwo.setVisible(false);
  }

  @FXML
  private void oneMouseEntered() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("1");
    }
  }

  @FXML
  private void oneMouseExited() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  @FXML
  private void onClickOne() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("1");
      currentDigit++;
    }
  }

  @FXML
  private void twoMouseEntered() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("2");
    }
  }

  @FXML
  private void twoMouseExited() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  @FXML
  private void onClickTwo() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("2");
      currentDigit++;
    }
  }

  @FXML
  private void threeMouseEntered() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("3");
    }
  }

  @FXML
  private void threeMouseExited() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  @FXML
  private void onClickThree() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("3");
      currentDigit++;
    }
  }

  @FXML
  private void fourMouseEntered() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("4");
    }
  }

  @FXML
  private void fourMouseExited() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  @FXML
  private void onClickFour() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("4");
      currentDigit++;
    }
  }

  @FXML
  private void fiveMouseEntered() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("5");
    }
  }

  @FXML
  private void fiveMouseExited() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  @FXML
  private void onClickFive() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("5");
      currentDigit++;
    }
  }

  @FXML
  private void sixMouseEntered() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("6");
    }
  }

  @FXML
  private void sixMouseExited() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  @FXML
  private void onClickSix() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("6");
      currentDigit++;
    }
  }

  @FXML
  private void sevenMouseEntered() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("7");
    }
  }

  @FXML
  private void sevenMouseExited() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  @FXML
  private void onClickSeven() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("7");
      currentDigit++;
    }
  }

  @FXML
  private void eightMouseEntered() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("8");
    }
  }

  @FXML
  private void eightMouseExited() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  @FXML
  private void onClickEight() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("8");
      currentDigit++;
    }
  }

  @FXML
  private void nineMouseEntered() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("9");
    }
  }

  @FXML
  private void nineMouseExited() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  @FXML
  private void onClickNine() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("9");
      currentDigit++;
    }
  }

  @FXML
  private void zeroMouseEntered() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("0");
    }
  }

  @FXML
  private void zeroMouseExited() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("_");
    }
  }

  @FXML
  private void onClickZero() {
    if (currentDigit < 9) {
      digits[currentDigit].setText("0");
      currentDigit++;
    }
  }

  @FXML
  private void onClickClear() {
    digitOne.setText("_");
    digitTwo.setText("_");
    digitThree.setText("_");
    digitFour.setText("_");
    digitFive.setText("_");
    digitSix.setText("_");
    digitSeven.setText("_");
    digitEight.setText("_");
    digitNine.setText("_");
    currentDigit = 0;
  }

  @FXML
  private void onClickDelete() {
    if (currentDigit > 0) {
      digits[currentDigit - 1].setText("_");
      currentDigit--;
    }
  }

  @FXML
  private void onClickCall() {
    if (currentDigit == 9) {
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
        GameState.isWon = true;
        Scene scene = phonePane.getScene();
        int timeTook = GameState.totalSeconds - GameState.secondsRemaining;
        int difficulty = 1;
        if (GameState.difficulty == Difficulty.MEDIUM) {
          difficulty = 2;
        } else if (GameState.difficulty == Difficulty.HARD) {
          difficulty = 3;
        }
        PlayHistory playHistory = new PlayHistory(timeTook, difficulty, GameState.playerName);
        try (ObjectInputStream ois =
            new ObjectInputStream(new FileInputStream("player_history.dat"))) {
          PlayHistory oldPlayHistory = (PlayHistory) ois.readObject();
          oldPlayHistory.addHistory(playHistory);
          playHistory.saveHistory();
        } catch (IOException | ClassNotFoundException e) {
          playHistory.saveHistory();
        }
        GameState.resetCafeteria = true;
        GameState.resetOffice = true;
        GameState.resetRoom = true;

        Parent parent = SceneManager.getUiRoot(SceneManager.AppUi.END_WON);
        parent.setLayoutX(App.centerX);
        parent.setLayoutY(App.centerY);
        scene.setRoot(parent);
      } else {
        System.out.println("Wrong number");
      }
    } else {
      System.out.println("Number is invalid");
    }
  }

  @FXML
  private void onClickCross() {
    phonePane.setVisible(false);
  }

  @FXML
  private void crossMouseEntered() {
    crossBig.setVisible(true);
  }

  @FXML
  private void crossMouseExited() {
    crossBig.setVisible(false);
  }

  private void resetchecker() throws IOException {
    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  if (GameState.secondsRemaining >= 0) {
                    updateTimerLabel();
                  }
                  if (GameState.secondsRemaining == 0) {
                    if (SceneManager.curretUi == SceneManager.AppUi.OFFICE) {
                      GameState.secondsRemaining = -1;
                      GameState.resetCafeteria = true;
                      GameState.resetOffice = true;
                      GameState.resetRoom = true;
                      try {
                        Scene scene = phone.getScene();
                        Parent parent = SceneManager.getUiRoot(SceneManager.AppUi.END_LOST);
                        parent.setLayoutX(App.centerX);
                        parent.setLayoutY(App.centerY);
                        scene.setRoot(parent);
                      } catch (NullPointerException e) {
                        System.out.println("Null pointer exception");
                      }
                    }
                  }
                  updateTimerLabel();
                  if (GameState.resetOffice) {
                    resetOffice();
                  }
                }));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  private void updateTimerLabel() {
    int minutes = (GameState.secondsRemaining) / 60;
    int seconds = (GameState.secondsRemaining) % 60;
    timerLabel.setText(String.format("%d:%02d", minutes, seconds));
  }

  /*
   * restores the office to its original state
   */
  private void resetOffice() {
    // Getting random item to be used to hide the cypher
    Rectangle[] items =
        new Rectangle[] {
          bin, blackBoard, deskDrawers,
        };
    Random randomChoose = new Random();
    int randomIndexChoose = randomChoose.nextInt(items.length);
    GameState.itemWithCypher = items[randomIndexChoose];

    binArrow.setVisible(true);
    blackBoardArrow.setVisible(true);
    drawArrow.setVisible(true);
    phoneArrow.setVisible(true);
    GameState.cypherFound = false;
    GameState.resetOffice = false;
    System.out.println("office reseted");
  }
}
