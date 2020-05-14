package data;

public class King extends Piece {
	
	public King(boolean white) {
		super("King", white);
		if(white) {
			setImageLocation("king_white.png");
		}
		else {
			setImageLocation("king_black.png");
		}
	
	}
	
	public boolean isValidMoveType(Board board, int fromX, int fromY, int toX, int toY) {
		Piece[][] gameboard = board.getBoard();
		int xDiff = toX - fromX;
		int yDiff = toY - fromY;
		
		if(xDiff == 0 && yDiff == 0) {
			return false;
		}
		else if(Math.abs(xDiff) > 1 || Math.abs(yDiff) > 1) {
			return false;
		}
		else if(gameboard[toY][toX] != null && gameboard[toY][toX].isWhite() == gameboard[fromY][fromX].isWhite()) {
			return false;
		}
		else {
			return true;
		}
	}
}
