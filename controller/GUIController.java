package controller;

import java.util.function.Consumer;

import data.Game;
import gui.ChessBoard;
import gui.ChessBoardAction;
import gui.PieceImageView;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class GUIController {

	private AnchorPane chessBoardAnchorPane;
	private ChessBoard chessboard;

	
	public GUIController(Game game, AnchorPane chessBoardAnchorPane, Canvas chessCanvas) {
		this.chessBoardAnchorPane = chessBoardAnchorPane;
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
			
		});
		/*
		chessboard.setChessBoardCanvas(new ChessBoardCanvas() {

			public void drawSquare(int x, int y, Color color) {
				gc.setFill(color);
				gc.fillRect(x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
			}

			*
		});
		*/
		//chessboard.drawBoard();
	}
	
}
