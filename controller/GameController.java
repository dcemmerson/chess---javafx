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

	private String chatMsg;
	private final Lock lock = new ReentrantLock();

	public GameController(Game game, AnchorPane chessBoardAnchorPane, Canvas chessCanvas, MainActions mainActions) {
		this.chessBoardAnchorPane = chessBoardAnchorPane;
		this.mainActions = mainActions;
		this.game = game;

		this.chessboard = new ChessBoard(game, chessCanvas, defineChessBoardActions(chessCanvas));

		displayInitialStartMessage();
		startThreads();

	}

	private void displayInitialStartMessage() {
		mainActions.appendToChatBox(WHITE_TURN);
	}

	private EventHandler<WorkerStateEvent> endMoveHandler(MoveService ms, boolean restart) {
		return new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent wse) {

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
					mainActions.sendMoveToRemotePlayer(mp.getFromX(), mp.getFromY(), mp.getToX(), mp.getToY());

					// remove captured piece off board, if piece was captured
					if (mp.getPiv() != null) {
						mp.getPiv().setOnBoard(false);

						chessBoardAnchorPane.getChildren().remove(mp.getPiv());
					}

					chessboard.refreshPieces(chessBoardAnchorPane);
				}

				if (!game.isEnded() && restart) {
					ms.restart();
				}
			}
		};

	}

	private EventHandler<WorkerStateEvent> moveFailHandler(MoveService ms, boolean restart) {
		return new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent wse) {

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

	public void endGame() {
		game.setEnded(true);
		player1MS.cancel();
		player2MS.cancel();
		player1MS = null;
		player2MS = null;
	}

	public Game getGame() {
		return game;
	}

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
	
	private ChessBoardAction defineChessBoardActions(Canvas chessCanvas) {
		return new ChessBoardAction() {

			public void addImage(ImageView img, int x, int y) {
				img.setLayoutX(x);
				img.setLayoutY(y);
				chessBoardAnchorPane.getChildren().add(img);
			}

			public void removeImage(PieceImageView img) {
				chessBoardAnchorPane.getChildren().remove(img);
			}

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

			@Override
			public void sendMoveToOtherPlayer(int fromX, int fromY, int toX, int toY) {
				mainActions.sendMoveToRemotePlayer(fromX, fromY, toX, toY);
			}

			public void refresh() {
				chessBoardAnchorPane.getChildren().remove(chessCanvas);
				chessBoardAnchorPane.getChildren().add(chessCanvas);
			}

		};
	}
}
