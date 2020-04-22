package data;

public class Bishop extends Piece {
	public Bishop(){
		super("Bishop", "bishop_black.png");
	}
	public boolean makeMove(int fromX, int fromY, int toX, int toY) {
		return true;
	}
}
