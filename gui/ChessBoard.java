/*	filename: ChessBoard.java
 * 	description: ChessBoard class is responsible for drawing the GUI chessboard
 * 					and handling move events on the board from local player,
 * 					cpus, or remote players.
 */
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

	/*	name: ChessBoard
	 * 	arguments: 	game - Game class instance. Contains and maintains the data
	 * 						for the chess game.
	 * 				canvas - Empty Canvas object already placed on scene.
	 * 				cba - ChessBoardAction interface with methods already defined
	 * 						in calling class.
	 */
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
	}

	/*	name: drawBoard
	 * 	description: Draws chess board on canvas, alternating square colors.
	 */
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

	/*	name: addPieceToBoard
	 * 	arguments: 	piece - Piece class object to be added onto gameboard
	 * 				xSquare/ySquare - ints representing piece's square location
	 * 					on game board.
	 * description:	Create an Image object and put image into new PieceImageView class, 
	 * 					use the ChessBoard interface to append image view to 
	 * 					pane. Maintain a list of PieceImageViews within this
	 * 					ChessBoard class so we can easily enable and disable
	 * 					click action events (for user clicking and dragging pieces).
	 */
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

	/*	name: enablePieceActionListeners
	 * 	description: Iterate through our list of PieceImageViews and enable mouse click,
	 * 					drag, and release event listeners on these. Used when local player
	 * 					turn begins.
	 */
	public void enablePieceActionListeners() {
		pieceImagesViews.forEach(piv -> {
			piv.addEventHandler(MouseEvent.MOUSE_PRESSED, clickEventHandler(piv));
			piv.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragEventHandler(piv));
			piv.addEventHandler(MouseEvent.MOUSE_RELEASED, releasedEventHandler(piv));
		});
	}

	/*	name: disablePieceActionListeners
	 * 	description: Iterate through our list of PieceImageViews and disable mouse click,
	 * 					drag, and release event listeners on these. Typically used
	 * 					after local player's turn is over.
	 */
	public void disablePieceActionListeners() {
		pieceImagesViews.forEach(piv -> {
			piv.removeEventHandler(MouseEvent.MOUSE_PRESSED, clickEventHandler(piv));
			piv.removeEventHandler(MouseEvent.MOUSE_DRAGGED, dragEventHandler(piv));
			piv.removeEventHandler(MouseEvent.MOUSE_RELEASED, releasedEventHandler(piv));
		});
	}
	
	/*	name: clickHandler
	 * 	arguments: piv - PieceImageView which we are assigning the click event handler on.
	 * 	description: Check the game has not ended. If not, make sure this ImageView is in
	 * 					front of all other pieces and update the clickOffsetX/Y values, 
	 * 					which will be used as user drags piece around on board, to
	 * 					make sure if user clicked on side of piece, the mouse's relative
	 * 					position to the actual piece image stays correct.
	 */
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

	/*	name: dragEventHandler
	 * 	arguments: piv - PieceImageView which we are assigning the drag event handler on.
	 * 	description: Check the game has not ended. Then check if the piece dragged 
	 * 					correctly corresponds to the player whose turn it is. If so,
	 * 					begin updating the ImageView's location on chess canvas. This
	 * 					will allow user to correctly drag around piece.
	 */
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
						piv.setX(e.getX() - clickOffsetX);
						piv.setY(e.getY() - clickOffsetY);
					}
				}
			}
		};
	}

	/*	name: releasedEventHandler
	 * 	arguments: piv - PieceImageView which we are assigning the released event handler on.
	 * 	description: Check the game has not ended. Then check if the piece released on 
	 * 					correctly corresponds to the player whose turn it is. If so,
	 * 					we now need to check if this is a valid move for the local player
	 * 					to make. Get the to/from coordinates, and first check to see if the
	 * 					attempted move is on the game board, then use the
	 * 					game.moveFullTurn method to either make the move, or determine
	 * 					this is an invalid move and place piece back at starting location.
	 * 					We then return a MoveProperties object which contains any capture
	 * 					messages, as well as any pieces that were captured.
	 */
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

						// Set capture move before calling moveFullTurn, otherwise we will not know
						// if moving piece is trying to capture other player's piece.
						if (toX >= 0 && toX <= Board.SQUARES_WIDE && toY >= 0 && toY <= Board.SQUARES_HIGH && board.getBoard()[toY][toX] != null) {
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
							if (playerInterface != null) {
								playerInterface.signalMoveMade(
										new MoveProperties(captureMessage, capturedPiece, fromX, fromY, toX, toY));
							}
						} else {
							piv.updateSquareLocation(fromX, fromY);
						}
					}
				}
			}
		};
	}

	/*	name: remotePlayerMakeMove
	 * 	arguments:	player - Player object who is the remote player making the move
	 * 				fromX/Y/toX/Y - ints representing move from square, to square on board.
	 * 	description: Checks that the game isn't over, and that the remote player making
	 * 					a move is actually their turn. Then, check if we are going to be
	 * 					capturing any pieces with this move, followed by validating the
	 * 					move using game.moveFullTurn. When a remote player sends us 
	 * 					their move, the move has already been validated on the remote player's
	 * 					end, so there is no reason any of these checks should fail.
	 * 					We then return a MoveProperties object which contains any capture
	 * 					messages, as well as any pieces that were captured.
	 * 
	 * 				This method would normally be called in a separate thread from JavaFX
	 * 				thread.
	 */
	public MoveProperties remotePlayerMakeMove(Player player, int fromX, int fromY, int toX, int toY) {

		if (!game.isEnded() && player != null) {

			boolean moveMade = false;
				
			if (gameboard[fromY][fromX] != null && player.isWhite() == gameboard[fromY][fromX].isWhite()) {
				boolean isCaptureMove = false;
				String captureMessage = null;
				String capturedPieceName = null;
				String capturingPieceName = null;


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
					System.out.println("Something bad happened while remote player trying to make move...");
					System.out.println("From (" + fromX + ", " + fromY + ") to (" + toX + ", " + toY + ")");
					isCaptureMove = false;
					capturedPieceName = null;
					capturingPieceName = null;
				}

			}
		}

		return null;
	}

	/*	name: cpuMakeMove
	 * 	arguments: player - Player object who is making move (a cpu in this case)
	 * 	description: This method is used to make cpu move when a local player is
	 * 					playing against the computer. Currently, the move made
	 * 					by the computer is completely random, but I hope to improve
	 * 					upon this at some point. We randomly choose fromX and fromY
	 * 					coordinates to move a piece. If the random piece selected is
	 * 					the correct color, then we begin iterating through the entire
	 * 					board looking for a valid square to move. The move is then 
	 * 					executed, and any captured pieces are removed from board. We
	 * 					then return a MoveProperties object which contains any capture
	 * 					messages, as well as any pieces that were captured.
	 * 
	 * 				This method would normally be called in a separate thread from JavaFX
	 * 				thread.
	 */
	public MoveProperties cpuMakeMove(Player player) {

		if (!game.isEnded() && player != null) {

			int fromX = Math.abs(random.nextInt()) % Board.SQUARES_WIDE;
			int fromY = Math.abs(random.nextInt()) % Board.SQUARES_HIGH;
			boolean moveMade = false;

			while (!moveMade && player.isTurn() && !game.isEnded()) {

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

		}
		
		return null;
	}

	/*	name: getPieceImageView
	 * 	arguments: x/y - ints representing location where we need associated PieceImageView
	 * 	description: Iterates through the list of PieceImageView until we find the
	 * 					PieceImage view matching the x/y coordinates.
	 */
	public PieceImageView getPieceImageView(int x, int y) {
		for (int i = 0; i < pieceImagesViews.size(); i++) {
			if (pieceImagesViews.get(i).getSquareX() == x && pieceImagesViews.get(i).getSquareY() == y) {
				return pieceImagesViews.get(i);
			}
		}
		return null;
	}

	/*	name:	removeCapturedPiece
	 * 	arguments: squareX/squareY - ints representing location where we need
	 * 				to remove associated PieceImageView 
	 * 	description: Iterates through list of PieceImageViews and calls the ChessBoardAction
	 * 					removeImage interface method to remove ImageView off board.
	 */
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

	/*	name:	movePieceImageView
	 * 	arguments: fromX/fromY/toX/toY - ints representing location where we
	 * 					need to move associated PieceImageView 
	 * 	description: Iterates through list of PieceImageViews and update
	 * 					PieceImageView instance location on board.
	 */
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

	/*	name:	refreshPieces
	 * 	arguments: parent - AnchorPane in scene where we are attaching ImageViews
	 * 	description: Iterates through list of PieceImageViews and removes then
	 * 					places image back on parent.
	 */
	public void refreshPieces(AnchorPane parent) {

		pieceImagesViews.forEach(piv -> {

			String imgLocation = (gameboard[piv.getSquareY()][piv.getSquareX()]).getImgLocation();

			parent.getChildren().remove(piv);

			if (!piv.getImage().getUrl().matches("^.*" + imgLocation + "$")) {
				Image img = new Image(imgLocation, SQUARE_SIZE, SQUARE_SIZE, false, false);
				piv.setImage(img);

			}

			parent.getChildren().add(piv);

		});
	}

	/*	name: setPlayerInterface
	 * 	description: Setter method.
	 */
	public void setPlayerInterface(PlayerInterface playerInterface) {
		this.playerInterface = playerInterface;
	}
}
