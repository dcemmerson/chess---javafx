package data;

public class Pawn extends Piece {
	private boolean hasMoved;

	public Pawn(boolean white) {
		super("Pawn", "pawn_black.png", white);
		this.setImage("pawn_black.png");
		this.hasMoved = false;
	}
	
	public boolean isValidMove(Board board, int fromX, int fromY, int toX, int toY) {
		Piece[][] gameboard = board.getBoard();
		int xDiff = toX - fromX;
		int yDiff = toY - fromY;
		
		if(!white) {
			yDiff = -yDiff;
		}
		
		if(xDiff == 0 && yDiff == 0) {
			return false;
		}
		else if(!hasMoved && yDiff == 2 && xDiff == 0 && gameboard[toY][toX] == null 
				&& !board.isPieceBetween(fromX, fromY, toX, toY)) {
			//this pawn's first move out of starting location - moving two spaces.
			this.hasMoved = true;
			return true;
		}
		else if(yDiff == 1 && xDiff == 0  && gameboard[toY][toX] == null) {
			//just a regular non-capturing move
			this.hasMoved = true;

			return true;
		}
		else if(yDiff == 1 && (xDiff == 1 || xDiff == -1) && gameboard[toY][toX] != null 
				&& (gameboard[toY][toX].isWhite() != gameboard[fromY][fromX].isWhite())) {
			//then the pawn is capturing a piece on the other team
			this.hasMoved = true;
			return true;
		}
		else {
			return false;
		}
	}
	
}
