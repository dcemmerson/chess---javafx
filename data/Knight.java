package data;

public class Knight extends Piece {
	public Knight() {
		super("Knight", "knight_black.png");
	}
	public boolean makeMove(int fromX, int fromY, int toX, int toY) {
		return true;
	}
}
