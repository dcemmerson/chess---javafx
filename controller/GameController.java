/*	name: NetworkController
 *	last modified: 06/23/2020
 * 	description: Controller class for the actual chess game and the
 * 					GUI chess board.
 */

package controller;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import data.Game;
import data.Player;
import gui.ChessBoard;
import gui.ChessBoardAction;
import gui.PieceImageView;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import threading.CpuMoveService;
import threading.LocalPlayerMoveService;
import threading.MoveService;
import threading.RemoteMoveService;

public class GameController {

	static String WHITE_TURN = "White's turn\n";
	static String BLACK_TURN = "Black's turn";

	private AnchorPane chessBoardAnchorPane;
	private ChessBoard chessboard;
	private MainActions mainActions;
	private Game game;

	private MoveService player1MS;
	private MoveService player2MS;
	private boolean notifiedGameOver;

	private String chatMsg;
	private final Lock lock = new ReentrantLock();

	/*	name: GameController constructor
	 * 	arguments:	game - Game object which contains access to object where game
	 * 						data is maintained and the logic for the game is enforced.
	 * 				chessBoardAnchorPane - AnchorPane containg chess canvas as child.
	 * 						Necessary to maintain reference to this AnchorPane to
	 * 						ensure images of pieces are removed from the canvas
	 * 						overlay when they should be.
	 * 				chessCanvas - Canvas where we will draw the chessboard.
	 * 				mainActions - defined MainActions interface for passing moves and
	 * 						move information back to MainController to communicate
	 * 						to other connected hosts, display results in side panel, etc.
	 */
	public GameController(Game game, AnchorPane chessBoardAnchorPane, Canvas chessCanvas, MainActions mainActions) {
		this.chessBoardAnchorPane = chessBoardAnchorPane;
		this.mainActions = mainActions;
		this.game = game;
		this.notifiedGameOver = false;

		this.chessboard = new ChessBoard(game, chessCanvas, defineChessBoardActions(chessCanvas));

		displayInitialStartMessage();
		startThreads();

	}

	/*	name: displayInitialStartMessage
	 * 	description: Helper method called when initially creating GameController class
	 * 					to display whose turn it is.
	 */
	private void displayInitialStartMessage() {
		mainActions.appendToChatBox(WHITE_TURN);
	}

	/*	name: endMoveHandler
	 * 	arguments: 	ms - MoveService object is either CpuMoveService LocalPlayerMoveService,
	 * 						or RemoteMoveService instance from completed service.
	 * 				localPlayer - boolean flag indicating if this player is playing on 
	 * 						local machine or not.
	 * 	description: Handles successful move event. MoveProperties instance extracted from
	 * 					ms, and string str is built up with information that we need to
	 * 					display to user, including whose turn is next, what pieces were
	 * 					captured, etc. This information is also sent to other host. Captured
	 * 					pieces are removed from GUI, and service is restarted.
	 */
	private EventHandler<WorkerStateEvent> endMoveHandler(MoveService ms, boolean localPlayer) {
		return new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent wse) {
				MoveProperties mp = (MoveProperties) wse.getSource().getValue();
				String str = "";

				if (mp != null) {
					if (mp.getMsg() != null) {
						str += mp.getMsg();
					}

					// remove captured piece off board, if piece was captured
					if (mp.getPiv() != null) {
						mp.getPiv().setOnBoard(false);

						chessBoardAnchorPane.getChildren().remove(mp.getPiv());
					}
				
					if(localPlayer) {
						mainActions.sendMoveToRemotePlayer(mp.getFromX(), mp.getFromY(), mp.getToX(), mp.getToY());
					}
				}
				
				// If game has not ended, display whose turn is next, else, display who won.
				if (!game.isEnded()) {
					if (game.getPlayerWhite().isTurn()) {
						str += "White's turn\n";
					} else {
						str += "Black's turn\n";
					}
				} else if (!notifiedGameOver) {
					notifiedGameOver = true;
					str += "Game over!\n";
					if (game.getPlayerWhite().isTurn()) {
						str += "Black wins\n";
					} else {
						str += "White wins\n";
					}
				}

				//If str is not empty, then we have text we need to append to chat box area.
				if(str != "") {
					mainActions.appendToChatBox(str);
				}
				

				chessboard.refreshPieces(chessBoardAnchorPane);
				
				if (!game.isEnded() && localPlayer) {
					System.out.println("restarting thread");
					ms.restart();
				}
			}
		};

	}

	/*	name: moveFailHandler
	 * 	arguments: 	ms - MoveService object is either CpuMoveService LocalPlayerMoveService,
	 * 						or RemoteMoveService instance from completed service.
	 * 				restart - boolean flag passed to indicate if we should attempt to
	 * 						restart service.
	 * 	description: Handles failed move service. This method shouldn't be called,
	 * 					but is still implemented in the case something fails, we can
	 * 					attempt to restart move service. Display to user whose turn it is,
	 * 					remove any pieces from GUI if necessary and restart service
	 * 					depending on restart flag.
	 */
	private EventHandler<WorkerStateEvent> moveFailHandler(MoveService ms, boolean restart) {
		return new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent wse) {
				System.out.println("fail event handler");
				MoveProperties mp = (MoveProperties) wse.getSource().getValue();

				if (mp != null) {
					String str = "";

					if (mp.getMsg() != null) {
						str += mp.getMsg();
					}

					if (!game.isEnded()) {
						if (game.getPlayerWhite().isTurn()) {
							str += "White's turn\n";
						} else {
							str += "Black's turn\n";
						}
					}

					mainActions.appendToChatBox(str);
					if (mp != null) {
						mainActions.sendMoveToRemotePlayer(mp.getFromX(), mp.getFromY(), mp.getToX(), mp.getToY());
						// remove captured piece off board, if piece was captured
						if (mp.getPiv() != null) {
							mp.getPiv().setOnBoard(false);

							chessBoardAnchorPane.getChildren().remove(mp.getPiv());
						}
					}

					chessboard.refreshPieces(chessBoardAnchorPane);
				}

				if (!game.isEnded() && restart) {
					ms.restart();
				}
			}
		};

	}

	/*	name: receiveMoveFromRemotePlayer
	 * 	arguments: 	fromX/fromY/toX/toY - ints representing from (y, x) to (y, x)
	 * 				location on board where remote player made move and has now 
	 * 				sent us the move to update our local copy of the current game.
	 * 	description: Determine who is making the move presented in the arguments,
	 * 					and make move using thread controlled by move service.
	 */
	public void receiveMoveFromRemotePlayer(int fromX, int fromY, int toX, int toY) {
		Player p;

		if (!game.getPlayerWhite().isLocal()) {
			p = game.getPlayerWhite();
			player1MS = new RemoteMoveService(game, chessboard, p, lock, fromX, fromY, toX, toY);
			player1MS.setOnSucceeded(endMoveHandler(player1MS, false));
			player1MS.start();
		} else {
			p = game.getPlayerBlack();
			player2MS = new RemoteMoveService(game, chessboard, p, lock, fromX, fromY, toX, toY);
			player2MS.setOnSucceeded(endMoveHandler(player2MS, false));
			player2MS.start();
		}

	}

	/*	name: endGame
	 * 	description: Set game ended flag to true in Game and cancel any running 
	 * 					MoveServices.
	 */
	public void endGame() {
		game.setEnded(true);
		if(player1MS != null) {
			player1MS.cancel();
		}
		if(player2MS != null) {
			player2MS.cancel();
		}
		
		player1MS = null;
		player2MS = null;
	}

	/*	name: getGame
	 * 	description: Getter method.
	 */
	public Game getGame() {
		return game;
	}

	/*	name: startThreads
	 * 	description: Fire off local player threads, which include cpus. A lock is passed
	 * 					to each thread to coordinate moves and ensure no one moves out
	 * 					of order.
	 */
	private void startThreads() {
		if (game.getPlayerWhite().isLocal() && !game.getPlayerWhite().isCpu()) {
			player1MS = new LocalPlayerMoveService(game, chessboard, game.getPlayerWhite(), lock);

		} else if (game.getPlayerWhite().isLocal() && game.getPlayerWhite().isCpu()) {
			player1MS = new CpuMoveService(game, chessboard, game.getPlayerWhite(), lock);

		} else { // remote
			if (game.getPlayerWhite().isLocal()) {
				player1MS = new LocalPlayerMoveService(game, chessboard, game.getPlayerWhite(), lock);
			} else {
				player1MS = null;
			}
		}

		if (game.getPlayerBlack().isLocal() && !game.getPlayerBlack().isCpu()) {
			player2MS = new LocalPlayerMoveService(game, chessboard, game.getPlayerBlack(), lock);
		} else if (game.getPlayerBlack().isCpu()) {
			player2MS = new CpuMoveService(game, chessboard, game.getPlayerBlack(), lock);
		} else { // remote
			if (game.getPlayerBlack().isLocal()) {
				player2MS = new LocalPlayerMoveService(game, chessboard, game.getPlayerBlack(), lock);
			} else {
				player2MS = null;
			}
		}

		//Set on success / on failed
		if (player1MS != null) {
			player1MS.setOnSucceeded(endMoveHandler(player1MS, true));
			player1MS.setOnFailed(moveFailHandler(player1MS, true));
			player1MS.start();
		}
		if (player2MS != null) {
			player2MS.setOnSucceeded(endMoveHandler(player2MS, true));
			player2MS.setOnFailed(moveFailHandler(player2MS, true));
			player2MS.start();
		}
	}

	/*	name: defineChessBoardActions
	 * 	description: Provide an interface for ChessBoard class to communicate back to
	 * 					GameController.
	 */
	private ChessBoardAction defineChessBoardActions(Canvas chessCanvas) {
		return new ChessBoardAction() {

			/*	name: addImage
			 * 	arguments: img - ImageView object to place on board
			 * 				x/y - ints representing square location on board
			 */
			public void addImage(ImageView img, int x, int y) {
				img.setLayoutX(x);
				img.setLayoutY(y);
				chessBoardAnchorPane.getChildren().add(img);
			}

			/*	name: removeImage
			 * 	description: Remove image off AnchorPane container.
			 */
			public void removeImage(PieceImageView img) {
				chessBoardAnchorPane.getChildren().remove(img);
			}

			/*	name: switchTurns
			 * 	description: Allows ChessBoard class non-JavaFX thread to place message
			 * 					in GameController class that will be consumed on thread
			 * 					success (or fail), before the other player thread finishes.
			 */
			@Override
			public void switchTurns(boolean isWhiteTurn, String captureMessage) {
				String str = captureMessage;

				if (isWhiteTurn) {
					str += WHITE_TURN;
				} else {
					str += WHITE_TURN;
				}

				chatMsg = str;
			}

			/* name: sendMoveToOtherPlayer
			 * arguments: fromX/fromY/toX/toY - ints representing squares on board where move
			 * 				was made.
			 * description:	Call mainActions interface to send move to MainController.
			 */
			@Override
			public void sendMoveToOtherPlayer(int fromX, int fromY, int toX, int toY) {
				mainActions.sendMoveToRemotePlayer(fromX, fromY, toX, toY);
			}

		};
	}
}
