/*	filename: Rook.java
 * 	last modified: 06/23/2020
 * 	description: Piece child class defining Rook type of piece. Contains
 * 					move logic for a Rook chess piece.
 */


package data;

public class Rook extends Piece {
	public Rook(boolean white) {
		super("Rook", white);
		if(white) {
			setImageLocation("rook_white.png");
		}
		else {
			setImageLocation("rook_black.png");
		}	
	}
	
	/*	name: isValidMoveType
	 * 	arguments: 	board - Board so we can check if there are any pieces in the way of toX/Y
	 * 					and fromX/Y location, as well as ensure the color of a piece is valid
	 * 					when capturing a piece.
	 * 				fromX/Y, toX/Y - ints representing to/from spaces on board (and in board array).
	 * 	description: A Rook moves either x direction only or y direction only. A rook may not move
	 * 					through pieces, but may capture pieces of the opposite color.
	 * 					Check if attempted pawn move is a valid queen move 
	 * 					and return true if so, else false.
	 */
	public boolean isValidMoveType(Board board, int fromX, int fromY, int toX, int toY) {
		Piece[][] gameboard = board.getBoard();
		int xDiff = toX - fromX;
		int yDiff = toY - fromY;
		
		if(xDiff == 0 && yDiff == 0) {
			return false;
		}
		else if(xDiff != 0 && yDiff != 0) {
			return false;
		}
		else if(board.isPieceBetween(fromX, fromY, toX, toY)) {
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
