package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class Controller implements Initializable{
	
	protected ChangeScreen screen;
	protected Stage stage;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	public void initialize(Stage stage, ChangeScreen screen, GameType args) {
		this.screen = screen;
		this.stage = stage;
		
	}
	public void initialize(Stage stage, ChangeScreen screen) {
		this.screen = screen;
		this.stage = stage;
		
	}
}
