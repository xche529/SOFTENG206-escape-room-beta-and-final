package nz.ac.auckland.se206.controllers;

import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class OfficeController {

  @FXML private Rectangle deskDrawers;
  @FXML private Rectangle bin;
  @FXML private Rectangle blackBoard;
  @FXML private Rectangle phone;
  @FXML private ImageView binBig;
  @FXML private ImageView blackBoardBig;
  @FXML private ImageView phoneBig;
  @FXML private ImageView deskDrawersBig;
  private CafeteriaController cafeteriaController;
  private RoomController roomController;

  public void setCafeteriaController(CafeteriaController cafeteriaController) {
    this.cafeteriaController = cafeteriaController;
  }

  public void setRoomController(RoomController roomController) {
    this.roomController = roomController;
  }
  @FXML private Button exitVeiwButton;
  @FXML private Pane cypherPane;

  @FXML
  private void initialize() {
     // Getting random item to be used to hide the cypher
    Rectangle[] items = new Rectangle[] {bin, phone, blackBoard, deskDrawers,};
    Random randomChoose = new Random();
    int randomIndexChoose = randomChoose.nextInt(items.length);
    GameState.itemWithCypher = items[randomIndexChoose];
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
    if (GameState.itemWithCypher == phone) {
      cypherPane.setVisible(true);
    } else {
      System.out.println("phoneClicked");
    }
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
  private void goToRoom() {
    roomController.resetAnimation();
    Scene scene = bin.getScene();
    scene.setRoot(SceneManager.getUiRoot(AppUi.ROOM));
    roomController.walkInAnimation();
  }

  @FXML
  private void goToCafeteria() {
    Scene scene = bin.getScene();
    scene.setRoot(SceneManager.getUiRoot(AppUi.CAFETERIA));
  }
}
