package gui;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

import controller.MoveProperties;
import controller.PlayerInterface;
import data.Board;
import data.Game;
import data.Piece;
import data.Player;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import testing.BoardMovement;

public class ChessBoard {
	private final Color COLOR_1 = Color.BLUE;
	private final Color COLOR_2 = Color.GREEN;

	public static int SQUARE_SIZE;

	private Random random;

	private Game game;
	private Board board;
	private Piece[][] gameboard;
	private ArrayList<PieceImageView> pieceImagesViews;

	private Canvas chessCanvas;
	private GraphicsContext gc;

	private ChessBoardAction chessBoardAction;

	private double clickOffsetX;
	private double clickOffsetY;

	private PlayerInterface playerInterface;

//	private ChessBoardCanvas chessBoardCanvas;


	public ChessBoard(Game game, Canvas canvas, ChessBoardAction cba) {
		this.random = new Random();

		this.game = game;
		this.board = game.getBoard();
		this.gameboard = board.getBoard();

		this.chessCanvas = canvas;
		this.gc = chessCanvas.getGraphicsContext2D();
		this.chessBoardAction = cba;
		this.pieceImagesViews = new ArrayList<PieceImageView>();

		ChessBoard.SQUARE_SIZE = (int) (chessCanvas.getHeight() / Board.SQUARES_HIGH);

		drawBoard();
//		addPieceActionListeners();

//		cpuStartMove();

//		initializeCanvasActionListener();
	}

//	public void setChessBoardCanvas(ChessBoardCanvas cbc) {
//		this.chessBoardCanvas = cbc;
//	}

	public void drawBoard() {
		Color color;
		Piece[][] gameboard = board.getBoard();

		for (int y = 0; y < Board.SQUARES_HIGH; y++) {
			for (int x = 0; x < Board.SQUARES_WIDE; x++) {
				if ((x + y) % 2 == 0) {
					color = COLOR_1;
				} else {
					color = COLOR_2;
				}
				gc.setFill(color);
				gc.fillRect(x * SQUARE_SIZE, y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
				if (gameboard[y][x] != null) {
					addPieceToBoard(gameboard[y][x], x, y);
				}
			}
		}
	}

	public void addPieceToBoard(Piece piece, int xSquare, int ySquare) {

		Image img = new Image(piece.getImgLocation(), SQUARE_SIZE, SQUARE_SIZE, false, false);
		PieceImageView pieceImgView = new PieceImageView(xSquare * SQUARE_SIZE, ySquare * SQUARE_SIZE, xSquare,
				ySquare);
		pieceImgView.setImage(img);
		chessBoardAction.addImage(pieceImgView, 0, 0);
		pieceImgView.setX(xSquare * SQUARE_SIZE);
		pieceImgView.setY(ySquare * SQUARE_SIZE);

		this.pieceImagesViews.add(pieceImgView);
	}

	public void enablePieceActionListeners() {
		pieceImagesViews.forEach(piv -> {
			piv.addEventHandler(MouseEvent.MOUSE_PRESSED, clickEventHandler(piv));
			piv.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragEventHandler(piv));
			piv.addEventHandler(MouseEvent.MOUSE_RELEASED, releasedEventHandler(piv));
		});
	}

	public void disablePieceActionListeners() {
		pieceImagesViews.forEach(piv -> {
			piv.removeEventHandler(MouseEvent.MOUSE_PRESSED, clickEventHandler(piv));
			piv.removeEventHandler(MouseEvent.MOUSE_DRAGGED, dragEventHandler(piv));
			piv.removeEventHandler(MouseEvent.MOUSE_RELEASED, releasedEventHandler(piv));
		});
	}

	public EventHandler<MouseEvent> clickEventHandler(PieceImageView piv) {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (!game.isEnded()) {
					piv.toFront();
					clickOffsetX = e.getX() - piv.getSquareX() * SQUARE_SIZE;
					clickOffsetY = e.getY() - piv.getSquareY() * SQUARE_SIZE;
				}
			}
		};
	}

	public EventHandler<MouseEvent> dragEventHandler(PieceImageView piv) {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (!game.isEnded()) {
					Player white = game.getPlayerWhite();
					Player black = game.getPlayerBlack();

					if ((white.isLocal() && white.isTurn() && !white.isCpu()
							&& gameboard[piv.getSquareY()][piv.getSquareX()].isWhite())
							|| (black.isLocal() && black.isTurn() && !black.isCpu()
									&& !gameboard[piv.getSquareY()][piv.getSquareX()].isWhite())) {
						System.out.println("inside dragging");
						piv.setX(e.getX() - clickOffsetX);
						piv.setY(e.getY() - clickOffsetY);
					}
				}
			}
		};
	}

	public EventHandler<MouseEvent> releasedEventHandler(PieceImageView piv) {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (!game.isEnded()) {
					Player white = game.getPlayerWhite();
					Player black = game.getPlayerBlack();
					if (white.isTurn() == gameboard[piv.getSquareY()][piv.getSquareX()].isWhite()
							&& ((white.isTurn() && white.isLocal() && !white.isCpu())
									|| (black.isTurn() && black.isLocal() && !black.isCpu()))) {
						boolean isCaptureMove = false;
						String captureMessage = null;
						String capturedPieceName = null;
						String capturingPieceName = null;

						int fromX = piv.getSquareX();
						int fromY = piv.getSquareY();
						int toX = (int) (e.getX() / SQUARE_SIZE);
						int toY = (int) (e.getY() / SQUARE_SIZE);

						if (board.getBoard()[toY][toX] != null) {
							isCaptureMove = true;
							capturedPieceName = board.getBoard()[toY][toX].getName();
							capturingPieceName = board.getBoard()[fromY][fromX].getName();
						}

						if (game.moveFullTurn(fromX, fromY, toX, toY)) {
							// successful move
							// 1. check if any piece image needs to be removed
							// 2. move image on board
							// 3. set capturing piece name and set string equal to display to user
							// 4. update whose turn it is on the gui using cba interface
							PieceImageView capturedPiece = getPieceImageView(toX, toY);

							// removeCapturedPiece(toX, toY);

							piv.updateSquareLocation(toX, toY);

							// if now player white turn is true, that means black just made move. Else white
							// just made move
							if (isCaptureMove && game.getPlayerWhite().isTurn()) {
								captureMessage = "Black " + capturingPieceName + " capture white " + capturedPieceName
										+ "\n";
							} else if (isCaptureMove && !game.getPlayerWhite().isTurn()) {
								captureMessage = "White " + capturingPieceName + " capture black " + capturedPieceName
										+ "\n";
							}


							chessBoardAction.switchTurns(game.getPlayerWhite().isTurn(), captureMessage);

							if (capturedPiece != null) {
								pieceImagesViews.remove(capturedPiece);
							}
							if(playerInterface != null) {
								playerInterface.signalMoveMade(new MoveProperties(captureMessage, capturedPiece));
							}
						} else {
							piv.updateSquareLocation(fromX, fromY);
						}
					}
				}
			}
		};
	}
	/*
	 * private void addPieceActionListener(PieceImageView pieceImgView) {
	 * 
	 * // clicked pieceImgView.setOnMousePressed(new EventHandler<MouseEvent>() {
	 * 
	 * @Override public void handle(MouseEvent e) { if (!game.isEnded()) {
	 * pieceImgView.toFront(); clickOffsetX = e.getX() - pieceImgView.getSquareX() *
	 * SQUARE_SIZE; clickOffsetY = e.getY() - pieceImgView.getSquareY() *
	 * SQUARE_SIZE; } } });
	 * 
	 * pieceImgView.setOnMouseDragged(new EventHandler<MouseEvent>() {
	 * 
	 * @Override public void handle(MouseEvent e) { if (!game.isEnded()) { Player
	 * white = game.getPlayerWhite(); Player black = game.getPlayerBlack();
	 * 
	 * if ((white.isLocal() && white.isTurn() && !white.isCpu() &&
	 * gameboard[pieceImgView.getSquareY()][pieceImgView.getSquareX()].isWhite()) ||
	 * (black.isLocal() && black.isTurn() && !black.isCpu() &&
	 * !gameboard[pieceImgView.getSquareY()][pieceImgView.getSquareX()].isWhite()))
	 * { System.out.println("inside dragging"); pieceImgView.setX(e.getX() -
	 * clickOffsetX); pieceImgView.setY(e.getY() - clickOffsetY); } } } });
	 * 
	 * // end drag pieceImgView.setOnMouseReleased(new EventHandler<MouseEvent>() {
	 * 
	 * @Override public void handle(MouseEvent e) { if (!game.isEnded()) { Player
	 * white = game.getPlayerWhite(); Player black = game.getPlayerBlack(); if
	 * (white.isTurn() ==
	 * gameboard[pieceImgView.getSquareY()][pieceImgView.getSquareX()].isWhite() &&
	 * ((white.isTurn() && white.isLocal() && !white.isCpu()) || (black.isTurn() &&
	 * black.isLocal() && !black.isCpu()))) { boolean isCaptureMove = false; String
	 * captureMessage = null; String capturedPieceName = null; String
	 * capturingPieceName = null;
	 * 
	 * int fromX = pieceImgView.getSquareX(); int fromY = pieceImgView.getSquareY();
	 * int toX = (int) (e.getX() / SQUARE_SIZE); int toY = (int) (e.getY() /
	 * SQUARE_SIZE);
	 * 
	 * if (board.getBoard()[toY][toX] != null) { isCaptureMove = true;
	 * capturedPieceName = board.getBoard()[toY][toX].getName(); capturingPieceName
	 * = board.getBoard()[fromY][fromX].getName(); }
	 * 
	 * if (game.moveFullTurn(fromX, fromY, toX, toY)) { // successful move // 1.
	 * check if any piece image needs to be removed // 2. move image on board // 3.
	 * set capturing piece name and set string equal to display to user // 4. update
	 * whose turn it is on the gui using cba interface removeCapturedPiece(toX,
	 * toY); pieceImgView.updateSquareLocation(toX, toY);
	 * 
	 * // if now player white turn is true, that means black just made move. Else
	 * white // just made move if (isCaptureMove && game.getPlayerWhite().isTurn())
	 * { captureMessage = "Black " + capturingPieceName + " capture white " +
	 * capturedPieceName + "\n"; } else if (isCaptureMove &&
	 * !game.getPlayerWhite().isTurn()) { captureMessage = "White " +
	 * capturingPieceName + " capture black " + capturedPieceName + "\n"; }
	 * 
	 * // notify the other play that this move was made, if they are a cpu or a
	 * remote // player if (!game.getPlayerWhite().isLocal() ||
	 * !game.getPlayerBlack().isLocal()) {
	 * chessBoardAction.sendMoveToOtherPlayer(fromX, fromY, toX, toY); }
	 * chessBoardAction.switchTurns(game.getPlayerWhite().isTurn(), captureMessage);
	 * chessBoardAction.displayMessage(captureMessage); } else {
	 * pieceImgView.updateSquareLocation(fromX, fromY); } cpuStartMove(); } } } });
	 * }
	 */

	/*
	public MoveProperties localMakeMove(Player player, int fromX, int fromY, int toX, int toY) {

		if (!game.isEnded() && player != null) {

			boolean moveMade = false;

			if (gameboard[fromY][fromX] != null && player.isWhite() == gameboard[fromY][fromX].isWhite()) {
				boolean isCaptureMove = false;
				String captureMessage = null;
				String capturedPieceName = null;
				String capturingPieceName = null;

				if (gameboard[toY][toX] != null) {
					isCaptureMove = true;
					capturedPieceName = board.getBoard()[toY][toX].getName();
					capturingPieceName = board.getBoard()[fromY][fromX].getName();
				}

				PieceImageView movingPiece = getPieceImageView(fromX, fromY);

				if (moveMade = game.moveFullTurn(fromX, fromY, toX, toY)) {
					// successful move
					// 1. check if any piece image needs to be removed
					// 2. move image on board
					// 3. set capturing piece name and set string equal to display to user
					// 4. update whose turn it is on the gui using cba interface
					PieceImageView capturedPiece = getPieceImageView(toX, toY);

					movingPiece.updateSquareLocation(toX, toY);

					// if now player white turn is true, that means black just made move. Else white
					// just made move
					if (isCaptureMove && game.getPlayerWhite().isTurn()) {
						captureMessage = "Black " + capturingPieceName + " capture white " + capturedPieceName + "\n";
					} else if (isCaptureMove && !game.getPlayerWhite().isTurn()) {
						captureMessage = "White " + capturingPieceName + " capture black " + capturedPieceName + "\n";
					}

					// notify the other player that this move was made, if they are a cpu or a
					// remote
					// player
					if (!game.getPlayerWhite().isLocal() || !game.getPlayerBlack().isLocal()) {
						chessBoardAction.sendMoveToOtherPlayer(fromX, fromY, toX, toY);
					}
					chessBoardAction.switchTurns(game.getPlayerWhite().isTurn(), captureMessage);

					if (capturedPiece != null) {
						pieceImagesViews.remove(capturedPiece);
					}

					if(playerInterface != null) {
						playerInterface.signalMoveMade(new MoveProperties(captureMessage, capturedPiece));
					}
					
					return new MoveProperties(captureMessage, capturedPiece);
				} else {
					movingPiece.updateSquareLocation(fromX, fromY);
					isCaptureMove = false;
					capturedPieceName = null;
					capturingPieceName = null;
				}
			}
		}

		return null;
	}
*/
	
	public MoveProperties cpuMakeMove(Player player) {

		if (!game.isEnded() && player != null) {

			int fromX = Math.abs(random.nextInt()) % Board.SQUARES_WIDE;
			int fromY = Math.abs(random.nextInt()) % Board.SQUARES_HIGH;
			boolean moveMade = false;
			
			if (player.isWhite()) {
				System.out.println("White trying to go");
			} else {
				System.out.println("Black trying to go");
			}


			while (!moveMade && player.isTurn() && !game.isEnded()) {
				
				if (player.isWhite()) {
					System.out.println("White is going");
				} else {
					System.out.println("Black is going");
				}
				
				fromX = Math.abs(random.nextInt()) % Board.SQUARES_WIDE;
				fromY = Math.abs(random.nextInt()) % Board.SQUARES_HIGH;
				if (gameboard[fromY][fromX] != null && player.isWhite() == gameboard[fromY][fromX].isWhite()) {
					boolean isCaptureMove = false;
					String captureMessage = null;
					String capturedPieceName = null;
					String capturingPieceName = null;

					int toX;
					int toY;

					for (int y = 0; y < Board.SQUARES_HIGH; y++) {
						toY = y;
						for (int x = 0; x < Board.SQUARES_WIDE; x++) {

							toX = x;

							if (gameboard[toY][toX] != null) {
								isCaptureMove = true;
								capturedPieceName = gameboard[toY][toX].getName();
								capturingPieceName = gameboard[fromY][fromX].getName();
							}

							PieceImageView movingPiece = getPieceImageView(fromX, fromY);

							
							moveMade = game.moveFullTurn(fromX, fromY, toX, toY);
							if (moveMade) {
								// successful move
								// 1. check if any piece image needs to be removed
								// 2. move image on board
								// 3. set capturing piece name and set string equal to display to user
								// 4. update whose turn it is on the gui using cba interface
								PieceImageView capturedPiece = getPieceImageView(toX, toY);

//								removeCapturedPiece(toX, toY);
								movingPiece.updateSquareLocation(toX, toY);

								// if now player white turn is true, that means black just made move. Else white
								// just made move
								if (isCaptureMove && game.getPlayerWhite().isTurn()) {
									captureMessage = "Black " + capturingPieceName + " capture white "
											+ capturedPieceName + "\n";
								} else if (isCaptureMove && !game.getPlayerWhite().isTurn()) {
									captureMessage = "White " + capturingPieceName + " capture black "
											+ capturedPieceName + "\n";
								}


								chessBoardAction.switchTurns(game.getPlayerWhite().isTurn(), captureMessage);

								if (capturedPiece != null) {
									pieceImagesViews.remove(capturedPiece);
								}
								return new MoveProperties(captureMessage, capturedPiece);
								
							} else {
								movingPiece.updateSquareLocation(fromX, fromY);

								isCaptureMove = false;
								capturedPieceName = null;
								capturingPieceName = null;
							}

						}
					}
				}
			}
			
/*			if(game.isEnded()) {
				System.out.println("checkmate from chessboard");
				return new MoveProperties("Checkmate", null);
			}
			*/
		}

		if (player.isWhite()) {
			System.out.println("White isnt going");
		} else {
			System.out.println("Black isnt going");
		}
		return null;
	}

	public PieceImageView getPieceImageView(int x, int y) {
		for (int i = 0; i < pieceImagesViews.size(); i++) {
			if (pieceImagesViews.get(i).getSquareX() == x && pieceImagesViews.get(i).getSquareY() == y) {
				return pieceImagesViews.get(i);
			}
		}
		return null;
	}

	public void removeCapturedPiece(int squareX, int squareY) {

		pieceImagesViews.forEach(new Consumer<PieceImageView>() {

			@Override
			public void accept(PieceImageView piece) {
				if (piece.getSquareX() == squareX && piece.getSquareY() == squareY) {
					chessBoardAction.removeImage(piece);
				}
			}

		});
	}

	public void movePieceImageView(int fromX, int fromY, int toX, int toY) {
		pieceImagesViews.forEach(new Consumer<PieceImageView>() {

			@Override
			public void accept(PieceImageView piece) {
				if (piece.getSquareX() == fromX && piece.getSquareY() == fromY) {
					piece.updateSquareLocation(toX, toY);
				}
			}
		});
	}

	public void refreshPieces(AnchorPane parent) {

		pieceImagesViews.forEach(piv -> {
		
			String imgLocation = (gameboard[piv.getSquareY()][piv.getSquareX()]).getImgLocation();	
			
			parent.getChildren().remove(piv);
			
			if(!piv.getImage().getUrl().matches("^.*" + imgLocation + "$")) {
				Image img = new Image(imgLocation, SQUARE_SIZE, SQUARE_SIZE, false, false);
				piv.setImage(img);
						
			}
			
			parent.getChildren().add(piv);

		});
	}
	
	public void setPlayerInterface(PlayerInterface playerInterface) {
		this.playerInterface = playerInterface;
	}
}

