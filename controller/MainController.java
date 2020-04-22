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

	final int SQUARES_WIDE = 8;
	final int SQUARES_HIGH = 8;
	
	private int num = 0;
	
//	@FXML
//	private Canvas chessCanvas;
	
	private Parent root;
	private Stage primaryStage;
	
	@FXML
	private Canvas chessCanvas;
	
	@FXML
	private AnchorPane chessAnchorPane;

	private GUIController guiController;
	
	//data
	private Game game;
	
	public void initialize(Stage primaryStage, Parent root) {
		this.primaryStage = primaryStage;
		this.root = root;
		
		this.game = new Game(SQUARES_WIDE, SQUARES_HIGH);
		this.guiController = new GUIController(game, chessAnchorPane, chessCanvas, SQUARES_WIDE, SQUARES_HIGH);
		
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}


}
