package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GptAndTextAreaManager;
import nz.ac.auckland.se206.GptAndTextAreaManager.Characters;
import nz.ac.auckland.se206.MovementControl;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class OfficeController {

  @FXML

  private Rectangle deskDrawers;
  @FXML

  private Rectangle bin;
  @FXML

  private Rectangle blackBoard;
  @FXML

  private Rectangle phone;
  @FXML

  private ImageView binBig;
  @FXML

  private ImageView blackBoardBig;
  @FXML

  private ImageView phoneBig;
  @FXML

  private ImageView deskDrawersBig;
  @FXML

  private ImageView prisonerOne;
  @FXML

  private ImageView prisonerTwo;
  @FXML

  private ImageView speechBubbleOne;
  @FXML

  private ImageView speechBubbleTwo;

  @FXML
  private TextArea inputBox;
  @FXML
  private TextArea chatDisplayBoard;
  @FXML
  private TextArea objectiveDisplayBoard;
  @FXML
  private Text typePromptText;
  private Label digitOne;

  @FXML
  private Label digitTwo;
  @FXML
  private Label digitThree;
  @FXML
  private Label digitFour;
  @FXML
  private Label digitFive;
  @FXML
  private Label digitSix;
  @FXML
  private Label digitSeven;
  @FXML
  private Label digitEight;
  @FXML
  private Label digitNine;
  @FXML
  private Button exitVeiwButton;
  @FXML
  private Pane cypherPane;
  @FXML
  private Pane paperPane;
  @FXML
  private Pane phonePane;
  @FXML
  private ImageView crossBig;
  @FXML
  private Label numberLabel;

  private Timeline timeline;
  private CafeteriaController cafeteriaController;
  private RoomController roomController;
  private ImageView[] animationItems;
  private Label[] digits;
  private int currentDigit = 0;

  @FXML
  private void initialize() throws IOException {

    resetOffice();
    resetchecker();

    GptAndTextAreaManager.officeController = this;

    //GptAndTextAreaManager.officeChatDisplayBoard = chatDisplayBoard;
    //GptAndTextAreaManager.officeTypePromptText = typePromptText;
    //GptAndTextAreaManager.officeInputBox = inputBox;
    //GptAndTextAreaManager.officeObjectiveDisplayBoard = objectiveDisplayBoard;

    animationItems = new ImageView[] { prisonerOne, prisonerTwo, speechBubbleOne, speechBubbleTwo };


    digits = new Label[] {
        digitOne, digitTwo, digitThree, digitFour, digitFive, digitSix, digitSeven, digitEight,
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


  public void setCafeteriaController(CafeteriaController cafeteriaController) {
    this.cafeteriaController = cafeteriaController;
  }

  public void setRoomController(RoomController roomController) {
    this.roomController = roomController;
  }

  public void walkInAnimation() {
    MovementControl.moveToLeft(false, 1, 500, animationItems);
  }

  public void resetAnimation() {
    for (ImageView item : animationItems) {
      item.setTranslateX(-500);
    }
  }

  @FXML
  private void onClickExitConverterView() {
    cypherPane.setVisible(false);
  }

  @FXML
  private void clickDeskDrawers(MouseEvent event) {
    if (GameState.itemWithCypher == deskDrawers) {
      cypherPane.setVisible(true);
    } else {
      System.out.println("deskDrawersClicked");
    }
  }

  @FXML
  private void clickBin(MouseEvent event) {
    if (GameState.itemWithCypher == bin) {
      cypherPane.setVisible(true);
    } else {
      System.out.println("binClicked");
    }
  }

  @FXML
  private void clickBlackboard(MouseEvent event) {
    if (GameState.itemWithCypher == blackBoard) {
      cypherPane.setVisible(true);
    } else {
      System.out.println("blackBoardClicked");
    }
  }

  @FXML
  private void clickPhone(MouseEvent event) {
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
      String phoneNumber = digitOne.getText() + digitTwo.getText() + digitThree.getText() + " "
          + digitFour.getText() + digitFive.getText() + digitSix.getText() + " " + digitSeven.getText()
          + digitEight.getText() + digitNine.getText();
      if (phoneNumber.equals(GameState.phoneNumber)) {
        GameState.isWon = true;
        Scene scene = phonePane.getScene();
        GameState.isWon = true;
        GameState.resetCafeteria = true;
        GameState.resetOffice = true;
        GameState.resetRoom = true;
        scene.setRoot(SceneManager.getUiRoot(AppUi.END_WON));
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
    timeline = new Timeline(
        new KeyFrame(
            Duration.seconds(1),
            event -> {
              if (GameState.secondsRemaining >= 0) {
                updateTimerLabel();
              }
              if (GameState.secondsRemaining == 0) {
                if (GameState.gameFinishedOffice) {
                  GameState.gameFinishedOffice = false;
                  GameState.resetCafeteria = true;
                  GameState.resetOffice = true;
                  GameState.resetRoom = true;
                  try {
                    Scene scene = phone.getScene();
                    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.END_LOST));
                  } catch (NullPointerException e) {
                  }

                }
              }
              updateTimerLabel();
              if (GameState.resetOffice) {
                resetOffice();
                GameState.resetOffice = false;
              }
            }));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  private void updateTimerLabel() {
  }

  private void resetOffice() {
    // Getting random item to be used to hide the cypher
    Rectangle[] items = new Rectangle[] {
        bin, blackBoard, deskDrawers,
    };
    Random randomChoose = new Random();
    int randomIndexChoose = randomChoose.nextInt(items.length);
    GameState.itemWithCypher = items[randomIndexChoose];
  }
}
