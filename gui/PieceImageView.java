package gui;

import javafx.scene.image.ImageView;

public class PieceImageView extends ImageView {

	private double currX;
	private double currY;
	
	private int squareX;
	private int squareY;

	private boolean onBoard;
	
	public PieceImageView(double startX, double startY, int xSquareStart, int ySquareStart) {
		super();
		this.currX = startX;
		this.currY = startY;

		this.squareX = xSquareStart;
		this.squareY = ySquareStart;
		
		this.onBoard = true;
		
	}
	
	public void updateSquareLocation(int newX, int newY) {
		this.squareX = newX;
		this.squareY = newY;
		this.setX(this.squareX * ChessBoard.SQUARE_SIZE);
		this.setY(this.squareY * ChessBoard.SQUARE_SIZE);
	}
	public int getSquareX() {
		return squareX;
	}

	public void setSquareX(int currSquareX) {
		this.squareX = currSquareX;
	}

	public int getSquareY() {
		return squareY;
	}

	public void setSquareY(int currSquareY) {
		this.squareY = currSquareY;
	}


	public double getCurrX() {
		return currX;
	}

	public void setCurrX(double currX) {
		this.currX = currX;
	}

	public double getCurrY() {
		return currY;
	}

	public void setCurrY(double currY) {
		this.currY = currY;
	}
	
	public boolean isOnBoard() {
		return onBoard;
	}

	public void setOnBoard(boolean onBoard) {
		this.onBoard = onBoard;
	}

}
