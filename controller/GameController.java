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
			System.out.println("creating white player");
			player1MS = new LocalPlayerMoveService(game, chessboard, game.getPlayerWhite(), lock);

		} else if (game.getPlayerWhite().isLocal() && game.getPlayerWhite().isCpu()) {
			System.out.println("creating white cpu");

			player1MS = new CpuMoveService(game, chessboard, game.getPlayerWhite(), lock);

		}

		if (game.getPlayerBlack().isLocal() && !game.getPlayerBlack().isCpu()) {
			System.out.println("creating black player");
			player2MS = new LocalPlayerMoveService(game, chessboard, game.getPlayerBlack(), lock);
		} else if (game.getPlayerBlack().isCpu()) {
			System.out.println("creating black cpu");

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
						mainActions.appendToChatBox(str, game.getPlayerWhite().isTurn());
					} else {
						mainActions.appendToChatBox(str, game.getPlayerWhite().isTurn());
					}

					// remove captured piece off board, if piece was captured
					if (mp.getPiv() != null) {
						mp.getPiv().setOnBoard(false);

						chessBoardAnchorPane.getChildren().remove(mp.getPiv());
					}

					chessboard.refreshPieces(chessBoardAnchorPane);
				}

				if (!game.isEnded()) {
					System.out.println("restarting thread");
					ms.restart();
				}
			}
		};

	}

	/*
	 * public void appendBoardResultsToChat() { try {
	 * mainActions.appendToChatBox(chatMsgs, game.getPlayerWhite().isTurn()); }
	 * finally {
	 * 
	 * } }
	 */

	public void startGameController(Player player) {
		Thread t = new Thread(new Cpu(player));
		t.start();

	}

	public void receiveMoveFromOtherPlayer(int fromX, int fromY, int toX, int toY) {
//		chessboard.receiveMoveFromOtherPlayer(fromX, fromY, toX, toY);
	}

	private synchronized void cpuMove(Player player) throws InterruptedException {

		// wait for other player to make turn
		while (!player.isTurn()) {
			System.out.println("player.isTurn(): " + player.isTurn());

			System.out.println(Thread.currentThread().getName() + ": wait");
			wait();
			Thread.sleep(1000);
			System.out.println(Thread.currentThread().getName() + ": continuing");

		}

		// Now check if GameController is ready for next move to take place.
		// Moves occur in following manner:
		// Player 1 makes move and Player 1 thread set this.chatMsgs with whatever
		// needs to be appended to the chat pane.
		// Player 1 thread goes into wait state. Main thread is notify(ed) and appends
		// msgs to chat pane, then notify(s) player 2 it is their turn. Process repeats.

		try {
			chessboard.cpuMakeMove(player);
		} finally {
		}
	}
	
	public void endGame() {
		game.setEnded(true);
		player1MS.cancel();
		player2MS.cancel();
		player1MS = null;
		player2MS = null;
	}

	private class Cpu implements Runnable {
		Player player;

		public Cpu(Player player) {
			System.out.println("cpu constructor");
			this.player = player;
		}

		@Override
		public void run() {

			while (!game.isEnded()) {
				try {
					cpuMove(player);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
