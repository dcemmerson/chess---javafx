package gui;


import data.Piece;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class ChessBoard {
	private final Color COLOR_1 = Color.BLUE;
	private final Color COLOR_2 = Color.GREEN;

//	private final int SQUARE_SIZE;
	final int SQUARES_WIDE;
	final int SQUARES_HIGH;
	final int SQUARE_SIZE;
	
	private Piece[][] board;

	
	private Canvas chessCanvas;
	private GraphicsContext gc;
	private BoardPoint prevLocation;
	private ImageView testImage;
	
	private ChessBoardAction chessBoardAction;
	
//	private ChessBoardCanvas chessBoardCanvas;
		
	public ChessBoard(Piece[][] board, Canvas canvas, int SQUARES_WIDE, int SQUARES_HIGH, ChessBoardAction cba) {
		this.board = board;
		this.SQUARES_WIDE = SQUARES_WIDE;
		this.SQUARES_HIGH = SQUARES_HIGH;
		this.chessCanvas = canvas;
		this.gc = chessCanvas.getGraphicsContext2D();
		this.SQUARE_SIZE = (int)(chessCanvas.getHeight() / SQUARES_HIGH);
		this.chessBoardAction = cba;
		
		drawBoard();

//		canvasActionListener();
	}

//	public void setChessBoardCanvas(ChessBoardCanvas cbc) {
//		this.chessBoardCanvas = cbc;
//	}
	
	public void drawBoard() {
		Color color;
		for(int y = 0; y < SQUARES_HIGH; y++) {
			for(int x = 0; x < SQUARES_WIDE; x++) {
				if((x + y) % 2 == 0) {
					color = COLOR_1;
				}
				else {
					color = COLOR_2;
				}
				gc.setFill(color);
				gc.fillRect(x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
				if(board[y][x] != null) {
					addPieceToBoard(board[y][x], x, y);
				}
			}
		}
	}

	public void addPieceToBoard(Piece piece, int xSquare, int ySquare) {
		if(board[ySquare][xSquare] != null) {
			System.out.println(piece.getImgLocation());
			Image img = new Image(piece.getImgLocation(), SQUARE_SIZE, SQUARE_SIZE, false, false);
			ImageView imgView = new ImageView();
			imgView.setImage(img);
			chessBoardAction.addImage(imgView, xSquare * SQUARE_SIZE, ySquare * SQUARE_SIZE);
		}
	}
	/* name: redrawSquare
	 * preconditions: x and y are doubles obtained from mouse x/y on canvas
	 * postconditions: All (up to 4) squares around mouse current x/y location have
	 * 					been redrawn. Draws all pieces back onto those squares, but
	 * 					does not redraw the piece that is selected by user.
	 */
	/*
	public void redrawSquare(double x, double y) {
		final int MAX_REDRAWN_SQUARES = 4;		
		BoardPoint [] points = new BoardPoint[MAX_REDRAWN_SQUARES];
		for(int i = 0; i < MAX_REDRAWN_SQUARES; i++) {
			points[i] = new BoardPoint();
		}
		
		int squareXi = ((int)x) / SQUARE_SIZE;
		int squareYi = ((int)y) / SQUARE_SIZE;
		double squareXd = x / (double)SQUARE_SIZE;
		double squareYd = y / (double)SQUARE_SIZE;

		//determine which squares we will need to redraw
		int xdiff = 0;
		int ydiff = 0;
		if(squareXd - squareXi > 0.5 && (squareXi + 1) < SQUARES_WIDE) {
			xdiff = 1;
		}
		else if((squareXi - 1) >= 0){
			xdiff = -1;
		}
		if(squareYd - squareYi > 0.5 && (squareYi + 1) < SQUARES_WIDE) {
			ydiff = 1;
		}
		else if((squareYi - 1) >= 0){
			ydiff = -1;
		}
		//set game spaces which we will iterate through and redraw
		points[0].setX(squareXi);
		points[0].setY(squareYi);
		points[1].setX(squareXi);
		points[1].setY(squareYi + ydiff);
		points[2].setX(squareXi + xdiff);
		points[2].setY(squareYi);
		points[3].setX(squareXi + xdiff);
		points[3].setY(squareYi + ydiff);
		
		//determine which color each square needs to be and redraw
		int _x;
		int _y;
		for (int i = 0; i < points.length; i++) {
			_x = points[i].getX();
			_y = points[i].getY();
			
			System.out.println("redraw square (x,y) = " + _x + ", " + _y );
			if((_x + _y) % 2 == 0) {
				gc.setFill(COLOR_1);
			}
			else {
				gc.setFill(COLOR_2);
			}
			gc.fillRect(_x * SQUARE_SIZE, _y * SQUARE_SIZE, 
						SQUARE_SIZE, SQUARE_SIZE);
			
			//check if we need to redraw a piece on this square
			if(i != 0 && board[_y][_x] != null){
				drawPiece(board[_y][_x], _x, _y);
			}
		}
	}
	
	public void canvasActionListener() {
		canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				prevLocation = new BoardPoint(((int)e.getX()) / SQUARE_SIZE, ((int)e.getY()) / SQUARE_SIZE);
			}
		});
		canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				int xSquare = (int)(e.getX() / SQUARE_SIZE);
				int ySquare = (int)(e.getY() / SQUARE_SIZE);

				if(e.isPrimaryButtonDown() && board[ySquare][xSquare] != null) {
					redrawSquare(e.getX(), e.getY());
					if(board[prevLocation.getY()][prevLocation.getX()] != null) {
						drawPiece(board[prevLocation.getY()][prevLocation.getX()], 
									e.getX() / SQUARE_SIZE, e.getY() / SQUARE_SIZE);
					}
				}
				else {
					System.out.println("empty square");
				}
			}	
		});
	}
	
	public void drawPiece(Piece piece , double x, double y) {
		System.out.println("redraw piece = " + piece.getName() + "(x,y) = " + x + ", " + y);
		if(piece.getImage() != null) {
			System.out.println("drawing");
			gc.drawImage(piece.getImage(), x * SQUARE_SIZE, y * SQUARE_SIZE);
		}
	}
*/

}
