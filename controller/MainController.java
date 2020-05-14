package controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.ChangeScreen;
import data.Game;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class MainController extends Controller implements Initializable {

		
//	@FXML
//	private Canvas chessCanvas;
	
	private Parent root;
	
	@FXML
	private Canvas chessCanvas;	
	@FXML
	private AnchorPane chessBoardAnchorPane;
	@FXML
	private Region chessboardTopArea;
	@FXML
	private Region chessboardLeftArea;
	@FXML
	private TextArea chatbox;
	@FXML
	private TextArea chatboxTypeArea;
	@FXML
	private MenuBar menuBar;
	@FXML
	private SplitPane chatSplitPane;
	@FXML
	private SplitPane chessSplitPane;
	@FXML
	private MenuItem close;
	
	
	private GUIController guiController;
	
	//data
	private Game game;

	
	public void initialize(Stage primaryStage, ChangeScreen screen) {
		this.screen = screen;
		this.stage = stage;
		
		this.game = new Game(true, true);
		this.guiController = new GUIController(game, chessBoardAnchorPane, chessCanvas);
		
		primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
			double width = (primaryStage.getWidth() - chessBoardAnchorPane.getWidth() - chatbox.getWidth()) / 2;
			if(width > 100) {
				width = 100;
			}
			chessboardLeftArea.setMaxWidth(width);
			
		});
		primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
				
			double height = (primaryStage.getHeight() - chessBoardAnchorPane.getHeight() - menuBar.getHeight()) / 2;
			
			if(height > 100) {
				height = 100;
			}
			chessboardTopArea.setMaxHeight(height);
			chatSplitPane.setDividerPosition(0, 1 - (chatboxTypeArea.getHeight() / chatSplitPane.getHeight()));
		});
		
//		close.setOnAction(e -> System.exit(0));
//		close.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));

		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	@FXML
	public void quitGame() {
		screen.changeScreens("start", true, true);
	}
	@FXML
	public void closeGame() {
		System.exit(0);
	}


}
