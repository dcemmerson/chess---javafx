package data;

public class Pawn extends Piece {
	public Pawn() {
		super("Pawn", "pawn_black.png");
		this.setImage("pawn_black.png");
	}
	
	public boolean makeMove(int fromX, int fromY, int toX, int toY) {
		return false;
	}
}
