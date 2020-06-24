/*	filename: Pawn.java
 * 	last modified: 06/23/2020
 * 	description: Piece child class defining Pawn type of piece. Contains
 * 					move logic for a Pawn chess piece.
 */

package data;

public class Pawn extends Piece {
	private boolean hasMoved;

	public Pawn(boolean white) {
		super("Pawn", white);
		if(white) {
			setImageLocation("pawn_white.png");
		}
		else {
			setImageLocation("pawn_black.png");
		}
		this.hasMoved = false;
	}
	
	/*	name: toQueen
	 * 	arguments: color of this piece
	 * 	description: If pawn reaches opponents end of board, the pawn becomes a queen.
	 */
	public Piece toQueen(boolean isWhite) {
		return new Queen(isWhite);
	}

	/*	name: isValidMoveType
	 * 	arguments: 	board - Board so we can check if there are any pieces in the way of toX/Y
	 * 					and fromX/Y location, as well as ensure the color of a piece is valid
	 * 					when capturing a piece.
	 * 				fromX/Y, toX/Y - ints representing to/from spaces on board (and in board array).
	 * 	description: An ordinary pawn move is one space forward, but the pawn may move two spaces
	 * 					forward if this is the first time the pawn has moved this game. Forward moves
	 * 					cannot be capture moves. A pawn may make a capture move of a piece of the
	 * 					opposite color by moving one move forward and one move sideways, for a 
	 * 					forward-diagonal move. Check if attempted pawn move is a valid pawn move 
	 * 					and return true if so, else false.
	 */
	public boolean isValidMoveType(Board board, int fromX, int fromY, int toX, int toY) {
		//@TODO: rework Pawn.isvalidMove to be more readable
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
