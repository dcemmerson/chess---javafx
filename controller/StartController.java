package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
	private void handleStartRemoteButton(ActionEvent event) {
		System.out.println("start remote");
	}
	
	@FXML
	private void handleStartLocalButton(ActionEvent event) throws IOException {		
		
		screen.changeScreens("Chess", false, false);
		
		
	}

	private void disableButtons() {
		startLocalButton.setDisable(true);
		startRemoteButton.setDisable(true);
	}



}
