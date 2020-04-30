package gui;

import java.util.ArrayList;
import java.util.function.Consumer;

import data.Board;
import data.Piece;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class ChessBoard {
	private final Color COLOR_1 = Color.BLUE;
	private final Color COLOR_2 = Color.GREEN;

	public static int SQUARE_SIZE;
	
	private Board board;
	private ArrayList<PieceImageView> pieceImagesViews;

	
	private Canvas chessCanvas;
	private GraphicsContext gc;

	private ChessBoardAction chessBoardAction;
	
	private double clickOffsetX;
	private double clickOffsetY;
	
//	private ChessBoardCanvas chessBoardCanvas;
		
	public ChessBoard(Board board, Canvas canvas, ChessBoardAction cba) {
		this.board = board;
		this.chessCanvas = canvas;
		this.gc = chessCanvas.getGraphicsContext2D();
		this.chessBoardAction = cba;
		this.pieceImagesViews = new ArrayList<PieceImageView>();
		
		this.SQUARE_SIZE = (int)(chessCanvas.getHeight() / Board.SQUARES_HIGH);
		
		drawBoard();
		
//		initializeCanvasActionListener();
	}

//	public void setChessBoardCanvas(ChessBoardCanvas cbc) {
//		this.chessBoardCanvas = cbc;
//	}
	
	public void drawBoard() {
		Color color;
		Piece[][] gameboard = board.getBoard();
		
		for(int y = 0; y < Board.SQUARES_HIGH; y++) {
			for(int x = 0; x < Board.SQUARES_WIDE; x++) {
				if((x + y) % 2 == 0) {
					color = COLOR_1;
				}
				else {
					color = COLOR_2;
				}
				gc.setFill(color);
				gc.fillRect(x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
				if(gameboard[y][x] != null) {
					addPieceToBoard(gameboard[y][x], x, y);
				}
			}
		}
	}

	public void addPieceToBoard(Piece piece, int xSquare, int ySquare) {
			Image img = new Image(piece.getImgLocation(), SQUARE_SIZE, SQUARE_SIZE, false, false);
			PieceImageView pieceImgView = new PieceImageView(xSquare * SQUARE_SIZE, ySquare * SQUARE_SIZE, xSquare, ySquare);
			pieceImgView.setImage(img);
			chessBoardAction.addImage(pieceImgView, 0, 0);
			pieceImgView.setX(xSquare * SQUARE_SIZE);
			pieceImgView.setY(ySquare * SQUARE_SIZE);
			
			this.pieceImagesViews.add(pieceImgView);
			
			addPieceActionListener(pieceImgView);
	}
	
	public void addPieceActionListener(PieceImageView pieceImgView) {
		
		//clicked
		pieceImgView.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				clickOffsetX = e.getX() - pieceImgView.getSquareX() * SQUARE_SIZE;
				clickOffsetY = e.getY() - pieceImgView.getSquareY() * SQUARE_SIZE;
			}
		});
		
		//dragged
		pieceImgView.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				pieceImgView.setX(e.getX() - clickOffsetX);
				pieceImgView.setY(e.getY() - clickOffsetY);
			}
		});
		
		//end drag
		pieceImgView.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				int fromX = pieceImgView.getSquareX();
				int fromY = pieceImgView.getSquareY();
				int toX = (int)(e.getX() / SQUARE_SIZE);
				int toY = (int)(e.getY() / SQUARE_SIZE);
				if(board.move(fromX, fromY, toX, toY)) {
					removeCapturedPiece(toX, toY);
					pieceImgView.updateSquareLocation(toX, toY);
				}
				else {
					pieceImgView.updateSquareLocation(fromX, fromY);

				}
			}			
		});
		
	}

	public void removeCapturedPiece(int squareX, int squareY) {
		pieceImagesViews.forEach(new Consumer<PieceImageView>() {
			
			@Override
			public void accept(PieceImageView piece) {
				
				if(piece.getSquareX() == squareX && piece.getSquareY() == squareY) {
					System.out.println("removing piece");
					chessBoardAction.removeImage(piece);
					return;
				}
			}
			
		});
	}

}
