package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import data.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;

public class StartController extends Controller implements Initializable {

	@FXML
	private Button startLocalButton;
	@FXML
	private Button startRemoteButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	@FXML
	private void handleStartCpuVsCpuButton(ActionEvent event) {
		GameType gt = new GameType(true, true, true, true);
		screen.changeScreens("Chess", gt, false, false);
	}
	@FXML
	private void handleStartRemoteButton(ActionEvent event) {
//		GameType gt = new GameType(false, false, false, false, true);
		screen.changeScreens("Network", false, false);
	}
	@FXML
	private void handleStartLocal1PlayerButton(ActionEvent event) {
		GameType gt = new GameType(true, true, false, true);
		screen.changeScreens("Chess", gt, false, false);
	}
	@FXML
	private void handleStartLocalButton(ActionEvent event) throws IOException {		
		GameType gt = new GameType(true, true, false, false);
		screen.changeScreens("Chess", gt, false, false);
		
		
	}

	private void disableButtons() {
		startLocalButton.setDisable(true);
		startRemoteButton.setDisable(true);
	}



}
