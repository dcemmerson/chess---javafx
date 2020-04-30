package controller;

import java.net.URL;
import java.util.ResourceBundle;

import data.Game;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainController implements Initializable {

		
//	@FXML
//	private Canvas chessCanvas;
	
	private Parent root;
	private Stage primaryStage;
	
	@FXML
	private Canvas chessCanvas;
	
	@FXML
	private AnchorPane chessBoardAnchorPane;


	private GUIController guiController;
	
	//data
	private Game game;
	
	public void initialize(Stage primaryStage, Parent root) {
		this.primaryStage = primaryStage;
		this.root = root;
		
		this.game = new Game();
		this.guiController = new GUIController(game, chessBoardAnchorPane, chessCanvas);
		
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}


}
