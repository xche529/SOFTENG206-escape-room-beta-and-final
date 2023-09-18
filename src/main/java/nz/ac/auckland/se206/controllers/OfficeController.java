package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
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
  @FXML private Button exitVeiwButton;
  @FXML private Pane cypherPane;

  @FXML
  private void initialize() {
    System.out.println("OfficeController initialized");
  }

  @FXML
  private void onClickExitConverterView() {
    cypherPane.setVisible(false);
  }

  @FXML
  private void clickDeskDrawers(MouseEvent event) {
    System.out.println("deskDrawersClicked");
  }

  @FXML
  private void clickBin(MouseEvent event) {
    System.out.println("binClicked");
  }

  @FXML
  private void clickBlackboard(MouseEvent event) {
    System.out.println("blackBoardClicked");
  }

  @FXML
  private void clickPhone(MouseEvent event) {
    System.out.println("phoneClicked");
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
    Scene scene = bin.getScene();
    scene.setRoot(SceneManager.getUiRoot(AppUi.ROOM));
  }

  @FXML
  private void goToCafeteria() {
    Scene scene = bin.getScene();
    scene.setRoot(SceneManager.getUiRoot(AppUi.CAFETERIA));
  }
}
