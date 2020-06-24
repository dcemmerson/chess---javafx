/*	name: MainController
 *	last modified: 06/23/2020
 * 	description: Main controller used on chess board page. Provides 
 * 					interface for various components to communicate without
 * 					directly relying on each other. Contains references to
 * 					components on chess board.
 */

package controller;

import java.net.URL;
import java.util.ResourceBundle;

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
import network.ChessDataPacket;
import network.ChessHost;
import utility.ChessMove;

public class MainController extends Controller implements Initializable {

	public static String OPPONENT_RESIGNED_MESSAGE = "Opponent has resigned.\nGame over.";
	public static String CONNECTION_LOST_MESSAGE = "Remote connection lost.\nGame over.";

	private Parent root;

	@FXML
	private Canvas chessCanvas;
	@FXML
	private AnchorPane chessBoardAnchorPane;
	@FXML
	private Region chessboardTopArea;
	@FXML
	private Region chessboardLeftArea;

	// custom parts
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
	@FXML
	private AnchorPane connectionLostPane;
	
	private GameController gameController;
	private NetworkController networkController;

	private String username;

	/*	name: initialize
	 * 	description: Stage initialize method called upon user selecting any local
	 * 					game type - local vs local, local vs cpu, etc. This version
	 * 					of initialize is also called by the remote version of intialize
	 * 					after we create and set the NetworkController.
	 */
	public void initialize(Stage primaryStage, ChangeScreen screen, GameType args) {
		MainActions mainActions = defineMainActions();
		this.screen = screen;
		this.stage = primaryStage;

		this.chatBox = new ChatScrollPane();
		this.chatSplitPane.getItems().add(chatBox);
		this.chatBoxTypeArea = new ChatBoxTypeArea(mainActions, args.isP1Local(), username);
		this.chatSplitPane.getItems().add(chatBoxTypeArea);
		
		Game game = new Game(args.isP1Local(), args.isP2Local(), args.isP1IsCpu(), args.isP2IsCpu());

		this.gameController = new GameController(game, chessBoardAnchorPane, chessCanvas, mainActions);

		setStageBehavior();
		
		//disable chat type area unless this is a remote 2 player game
		if(args.isP1Local() && args.isP2Local()) {
			this.chatBoxTypeArea.setDisable(true);
			this.chatBoxTypeArea.getTextArea().setText("Chat type area disabled in local mode.");;
		}

	}

	/*	name: initialize
	 * 	description: Stage initialize method called only when playing
	 * 					remote game. Sets MainController's NetworkController,
	 * 					but the connected ChessHost object must be passed in.
	 */
	public void initialize(Stage primaryStage, ChangeScreen screen, GameType args, ChessHost ch) {
		this.networkController = new NetworkController(ch, defineMainActions());
		this.username = ch.getUserName();
		initialize(primaryStage, screen, args);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	/*	name: quiteGame
	 * 	description: Handle event when user select quit game from top bar.
	 */
	@FXML
	public void quitGame() {
		gameController.endGame();
		if(networkController != null) {
			networkController.endNetworkServices();
		}
		screen.changeScreens("Start", null, true, false);
	}
	
	/*	name: closeGame
	 * 	description: Handle event when user select exit game from top bar.
	 */
	@FXML
	public void closeGame() {
		System.exit(0);
	}

	/*	name: defineMainActions
	 * 	description: Return MainActions interface. General interface used
	 * 					for communication between components on chess
	 * 					board screen.
	 */
	private MainActions defineMainActions() {
		return new MainActions() {

			@Override
			public void appendToChatBox(String str, boolean white) {

				if (str != null) {
					Text t = new Text(str);
					if (white) {
						t.setFill(Color.DARKRED);
					} else {
						t.setFill(Color.MEDIUMSLATEBLUE);
					}
					chatBox.appendText(t);
				}
			}
			
			@Override
			public void appendToChatBox(String str) {

				if (str != null) {
					Text t = new Text(str);
					t.setFill(Color.DARKGRAY);

					chatBox.appendText(t);
				}
			}

			@Override
			public void sendMoveToRemotePlayer(int fromX, int fromY, int toX, int toY) {
				if (networkController != null) {
					ChessDataPacket cdp = new ChessDataPacket(fromX, fromY, toX, toY);
					networkController.write(cdp);
				}
			}

			@Override
			public void sendText(String str) {
				if (networkController != null) {
					ChessDataPacket cdp = new ChessDataPacket(username + ": " + str );
					networkController.write(cdp);
				}
			}

			@Override
			public void receiveText(String str) {
				Text t = new Text(str + "\n");

				if (gameController.getGame().getPlayerBlack().isLocal()) {
					t.setFill(Color.DARKRED);
				} else {
					t.setFill(Color.MEDIUMSLATEBLUE);
				}
				chatBox.appendText(t);
			}
			
			@Override
			public void receiveChessDataPacket(ChessDataPacket cdp) {
				if(cdp != null) {
					if(cdp.isMessage()) {
						receiveText(cdp.getMessage());
					}
					else if(cdp.isMove()) {
						ChessMove cm = cdp.getChessMove();
						gameController.receiveMoveFromRemotePlayer(cm.getFromX(), cm.getFromY(), cm.getToX(), cm.getToY());
					}
					else if(cdp.connectionLost() || cdp.isOpponentResigned()) {
						receiveText(CONNECTION_LOST_MESSAGE);
						chatBoxTypeArea.getTextArea().setText(CONNECTION_LOST_MESSAGE);
						chatBoxTypeArea.getTextArea().setDisable(true);
						connectionLostPane.toFront();
					}
				}
			}
			@Override
			public void startGame(boolean player1IsRemote, boolean player2IsRemote) {
				Game game = new Game(player1IsRemote, player2IsRemote, false, false);

				gameController = new GameController(game, chessBoardAnchorPane, chessCanvas, this);
			}

		};
	}

	/*	name: setStageBehavior
	 * 	description: Called in class constructor to set responsive layout
	 * 				of chess board and other components on chess board page.
	 */
	public void setStageBehavior() {
		connectionLostPane.toBack();
		
		stage.widthProperty().addListener((obs, oldVal, newVal) -> {
			double width = (stage.getWidth() - chessBoardAnchorPane.getWidth() - chatBox.getWidth()) / 2;
			if (width > 100) {
				width = 100;
			}
			chessboardLeftArea.setMaxWidth(width);

		});
		stage.heightProperty().addListener((obs, oldVal, newVal) -> {

			double height = (stage.getHeight() - chessBoardAnchorPane.getHeight() - menuBar.getHeight()) / 2;

			if (height > 100) {
				height = 100;
			}
			chessboardTopArea.setMaxHeight(height);
		});
	}
}
