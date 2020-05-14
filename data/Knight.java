package data;

public class Knight extends Piece {
	public Knight(boolean white) {
		super("Knight", white);
		if(white) {
			setImageLocation("knight_white.png");
		}
		else {
			setImageLocation("knight_black.png");
		}	
	}
	
	public boolean isValidMoveType(Board board,int fromX, int fromY, int toX, int toY) {
		Piece[][] gameboard = board.getBoard();
		int xDiff = Math.abs(toX - fromX);
		int yDiff = Math.abs(toY - fromY);
		
		if(xDiff == 0 && yDiff == 0) {
			return false;
		}
		else if((yDiff != 2 || xDiff != 1) && (yDiff != 1 || xDiff != 2)) {
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
