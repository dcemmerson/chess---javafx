package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import data.Game;
import gui.ChatBoxTypeArea;
import gui.ChatScrollPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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
	

	//custom parts
	private ChatScrollPane chatBox;
	@FXML
	private ChatBoxTypeArea chatBoxTypeArea;
	
	
	@FXML	
	private MenuBar menuBar;
	@FXML
	private SplitPane chatSplitPane;
	@FXML
	private SplitPane chessSplitPane;
	@FXML
	private MenuItem close;
	
	
	private GameController gameController;
	
	//data
//	private Game game;
	
	public void initialize(Stage primaryStage, ChangeScreen screen, String arg) {
		MainActions mainActions = defineMainActions();
		this.screen = screen;
		this.stage = stage;
		
		this.chatBox = new ChatScrollPane();
		this.chatSplitPane.getItems().add(chatBox);
		this.chatBoxTypeArea = new ChatBoxTypeArea(mainActions);
		this.chatSplitPane.getItems().add(chatBoxTypeArea);

		Game game = null;
		if(arg == "remote") {
			System.out.println("remote game starting");
			game = new Game(true, false, false, false);
		}
		else if(arg == "1 player local") { //vs cpu is black
			game = new Game(true, true, false, true);
		}
		else if(arg == "cpu vs cpu") {
			game = new Game(true, true, true, true);
		}
		else {
			//2 player local
			game = new Game(true, true, false, false);
		}

		this.gameController = new GameController(game, chessBoardAnchorPane, chessCanvas, mainActions);
		
		primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
			double width = (primaryStage.getWidth() - chessBoardAnchorPane.getWidth() - chatBox.getWidth()) / 2;
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
//			chatSplitPane.setDividerPosition(0, 1 - (chatBoxTypeArea.getHeight() / chatSplitPane.getHeight()));
		});
		

		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	@FXML
	public void quitGame() {
		gameController.endGame();
		screen.changeScreens("start", null, true, false);
	}
	@FXML
	public void closeGame() {
		System.exit(0);
	}
	
	private MainActions defineMainActions() {
		return new MainActions() {

			@Override
			public void appendToChatBox(ArrayList<String> str, boolean white) {
				
				str.forEach(new Consumer<String>() {

					public void accept(String s) {
						Text t = new Text(s);
						if(s != null && s == str.get(str.size() - 1)) {
							if(white) {
								t.setFill(Color.DARKRED);
							}
							else {
								t.setFill(Color.MEDIUMSLATEBLUE);
							}
							chatBox.appendText(t);
						}
						else if(s != null) {
							t.setFill(Color.DARKGRAY);
							chatBox.appendText(t);
						}

						
						
					}

				});

			}

			@Override
			public void sendMoveToOtherPlayer(int fromX, int fromY, int toX, int toY) {
				System.out.println("send move");
//				cpuGuiController.receiveMoveFromOtherPlayer(fromX, fromY, toX, toY);
				
				
			}
			
		};
	}


}
