package data;

public class Queen extends Piece {
	public Queen(boolean white) {
		super("Queen", "queen_black.png", white);
	}
	public boolean isValidMove(Board board,int fromX, int fromY, int toX, int toY) {
		Piece[][] gameboard = board.getBoard();
		int xDiff = toX - fromX;
		int yDiff = toY - fromY;
		
		if(xDiff == 0 && yDiff == 0) {
			return false;
		}
		else if((Math.abs(xDiff) != Math.abs(yDiff) && (xDiff != 0 && yDiff != 0)) 
				|| board.isPieceBetween(fromX, fromY, toX, toY)) {
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