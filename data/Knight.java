package data;

public class Knight extends Piece {
	public Knight(boolean white) {
		super("Knight", "knight_black.png", white);
	}
	public boolean isValidMove(Board board,int fromX, int fromY, int toX, int toY) {
		Piece[][] gameboard = board.getBoard();
		int xDiff = toX - fromX;
		int yDiff = toY - fromY;
		
		System.out.println("xdiff = " + xDiff + " ydiff = " + yDiff);
		if(xDiff == 0 && yDiff == 0) {
			return false;
		}
		else if((Math.abs(yDiff) != 2 && Math.abs(xDiff) != 1) && (Math.abs(yDiff) != 1 && Math.abs(xDiff) != 2)) {
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
