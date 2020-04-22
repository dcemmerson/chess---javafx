package controller;

import data.Game;
import gui.ChessBoard;
import gui.ChessBoardAction;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class GUIController {

	private AnchorPane chessAnchorPane;
	private ChessBoard chessboard;

	
	public GUIController(Game game, AnchorPane chessAnchorPane, Canvas chessCanvas, int SQUARES_WIDE, int SQUARES_HIGH) {

		this.chessboard = new ChessBoard(game.getBoard(), chessCanvas, SQUARES_WIDE, SQUARES_HIGH, new ChessBoardAction() {

			public void addImage(ImageView img, int x, int y) {
				img.setLayoutX(x);
				img.setLayoutY(y);
				chessAnchorPane.getChildren().add(img);
			}
			
		});
		this.chessAnchorPane = chessAnchorPane;
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
