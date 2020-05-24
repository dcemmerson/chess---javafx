package controller;

import java.util.ArrayList;
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

public class GameController {

	private AnchorPane chessBoardAnchorPane;
	private ChessBoard chessboard;
	private MainActions mainActions;
	private Game game;

	private MoveService player1MS;
	private MoveService player2MS;
	
	private ArrayList<String> chatMsgs;
	private final Lock lock = new ReentrantLock();

	public GameController(Game game, AnchorPane chessBoardAnchorPane, Canvas chessCanvas, MainActions mainActions) {
		this.chessBoardAnchorPane = chessBoardAnchorPane;
		this.mainActions = mainActions;
		this.game = game;

		this.chessboard = new ChessBoard(game, chessCanvas, new ChessBoardAction() {

			public void addImage(ImageView img, int x, int y) {
				img.setLayoutX(x);
				img.setLayoutY(y);
				chessBoardAnchorPane.getChildren().add(img);
			}

			public void movePiece(ImageView img, int x, int y) {
				System.out.println("abc");
			}

			public void removeImage(PieceImageView img) {
				chessBoardAnchorPane.getChildren().remove(img);
			}

			@Override
			public void switchTurns(boolean isWhiteTurn, String captureMessage) {
				ArrayList<String> str = new ArrayList<String>();

				str.add(captureMessage);

				if (isWhiteTurn) {
					str.add("White's turn\n");
				} else {
					str.add("Black's turn\n");
				}

				chatMsgs = str;
//				mainActions.appendToChatBox(str, isWhiteTurn);
			}

			@Override
			public void sendMoveToOtherPlayer(int fromX, int fromY, int toX, int toY) {
				mainActions.sendMoveToOtherPlayer(fromX, fromY, toX, toY);
			}

			public void refresh() {
				chessBoardAnchorPane.getChildren().remove(chessCanvas);
				chessBoardAnchorPane.getChildren().add(chessCanvas);
			}

			@Override
			public void displayMessage(String captureMessage) {
				// TODO Auto-generated method stub

			}

		});

		if (game.getPlayerWhite().isLocal() && !game.getPlayerWhite().isCpu()) {
			player1MS = new LocalPlayerMoveService(game, chessboard, game.getPlayerWhite(), lock);

		} else if (game.getPlayerWhite().isLocal() && game.getPlayerWhite().isCpu()) {
			player1MS = new CpuMoveService(game, chessboard, game.getPlayerWhite(), lock);

		}

		if (game.getPlayerBlack().isLocal() && !game.getPlayerBlack().isCpu()) {
			player2MS = new LocalPlayerMoveService(game, chessboard, game.getPlayerBlack(), lock);
		} else if (game.getPlayerBlack().isCpu()) {
			player2MS = new CpuMoveService(game, chessboard, game.getPlayerBlack(), lock);
		}
		
		player1MS.setOnSucceeded(cpuEventHandler(player1MS));
		player1MS.start();
		player2MS.setOnSucceeded(cpuEventHandler(player2MS));
		player2MS.start();
	}

	private EventHandler<WorkerStateEvent> cpuEventHandler(MoveService ms) {
		return new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent wse) {

				MoveProperties mp = (MoveProperties) wse.getSource().getValue();

				if (mp != null) {
					ArrayList<String> str = new ArrayList<String>();

					str.add(mp.getMsg());

					if (!game.isEnded()) {
						if (game.getPlayerWhite().isTurn()) {
							str.add("White's turn\n");
						} else {
							str.add("Black's turn\n");
						}
					}
					
					mainActions.appendToChatBox(str, game.getPlayerWhite().isTurn());

					// remove captured piece off board, if piece was captured
					if (mp.getPiv() != null) {
						mp.getPiv().setOnBoard(false);

						chessBoardAnchorPane.getChildren().remove(mp.getPiv());
					}

					chessboard.refreshPieces(chessBoardAnchorPane);
				}

				if (!game.isEnded()) {
					ms.restart();
				}
			}
		};

	}

	public void receiveMoveFromOtherPlayer(int fromX, int fromY, int toX, int toY) {
//		chessboard.receiveMoveFromOtherPlayer(fromX, fromY, toX, toY);
	}
	
	public void endGame() {
		game.setEnded(true);
		player1MS.cancel();
		player2MS.cancel();
		player1MS = null;
		player2MS = null;
	}



}
