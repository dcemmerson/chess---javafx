/*	name: Controller.java
 * 	last modified: 06/23/2020
 * 	description: Parent class for all other controller classes in this app.
 * 					Using a parent Controller class for all other controller
 * 					classes allows for simple scene changes, and to change 
 * 					the controller class at the same time, without needing
 * 					to rewrite new ScreenController.changeScreen methods
 * 					for every page.
 */

package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.stage.Stage;
import network.ChessHost;

public class Controller implements Initializable{
	
	protected ChangeScreen screen;
	protected Stage stage;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	

	public void initialize(Stage stage, ChangeScreen screen) {
		this.screen = screen;
		this.stage = stage;
	}
	
	public void initialize(Stage stage, ChangeScreen screen, GameType args) {
		this.screen = screen;
		this.stage = stage;
	}
	
	public void initialize(Stage stage, ChangeScreen screen, GameType args, ChessHost ch) {	
		this.screen = screen;
		this.stage = stage;
	}
}
