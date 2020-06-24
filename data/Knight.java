/*	filename: Knight.java
 * 	last modified: 06/23/2020
 * 	description: Piece child class defining Knight type of piece. Contains
 * 					move logic for a Knight chess piece.
 */

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
	
	/*	name: isValidMoveType
	 * 	arguments: 	board - Board so we can check if there are any pieces in the way of toX/Y
	 * 					and fromX/Y location, as well as ensure the color of a piece is valid
	 * 					when capturing a piece.
	 * 				fromX/Y, toX/Y - ints representing to/from spaces on board (and in board array).
	 * 	description: A knight moves either (+/- 2 spaces in the x direction and +/- 1 space in the y
	 * 					direction), or (+/- 1 space in the x direction and +/- 2 spaces in the y
	 * 					direction). Knights may jump over other pieces and may capture pieces of the
	 * 					opposite color. Check if attempted knight move is a valid pawn move 
	 * 					and return true if so, else false.
	 */
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
