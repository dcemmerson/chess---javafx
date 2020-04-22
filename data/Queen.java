package data;

public class Queen extends Piece {
	public Queen() {
		super("Queen", "queen_black.png");
	}
	public boolean makeMove(int fromX, int fromY, int toX, int toY) {
		return true;
	}
}
