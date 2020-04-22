package data;

public class King extends Piece {
	public King() {
		super("King", "king_black.png");
	}
	public boolean makeMove(int fromX, int fromY, int toX, int toY) {
		return true;
	}
}
