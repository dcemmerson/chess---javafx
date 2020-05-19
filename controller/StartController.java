package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import data.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class StartController extends Controller implements Initializable {

	@FXML
	private Button startLocalButton;
	@FXML
	private Button startRemoteButton;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	@FXML
	private void handleStartCpuVsCpuButton(ActionEvent event) {
		screen.changeScreens("Chess", "cpu vs cpu", false, false);
	}
	@FXML
	private void handleStartRemoteButton(ActionEvent event) {
		screen.changeScreens("Chess", "remote", false, false);
	}
	@FXML
	private void handleStartLocal1PlayerButton(ActionEvent event) {
		screen.changeScreens("Chess", "1 player local", false, false);
	}
	@FXML
	private void handleStartLocalButton(ActionEvent event) throws IOException {		
		System.out.println("changing to chess");

		screen.changeScreens("Chess", "2 player local", false, false);
		
		
	}

	private void disableButtons() {
		startLocalButton.setDisable(true);
		startRemoteButton.setDisable(true);
	}



}
