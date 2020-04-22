package data;

public class Rook extends Piece {
	public Rook() {
		super("Rook", "rook_black.png");
	}
	public boolean makeMove(int fromX, int fromY, int toX, int toY) {
		return true;
	}
}
