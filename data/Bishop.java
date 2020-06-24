/*	filename: Bishop.java
 * 	last modified: 06/23/2020
 * 	description: Piece child class defining Bishop type of piece. Contains
 * 					move logic for a Bishop chess piece.
 */

package data;

public class Bishop extends Piece {
	
	public Bishop(boolean white){
		super("Bishop", white);
		if(white) {
			setImageLocation("bishop_white.png");
		}
		else {
			setImageLocation("bishop_black.png");
		}
	}
	
	/*	name: isValidMoveType
	 * 	arguments: 	board - Board so we can check if there are any pieces in the way of toX/Y
	 * 					and fromX/Y location, as well as ensure the color of a piece is valid
	 * 					when capturing a piece.
	 * 				fromX/Y, toX/Y - ints representing to/from spaces on board (and in board array).
	 * 	description: A Bishop moves diagonally on the board and cannot move through other pieces,
	 * 					but may capture pieces of the opposite color. Check if attempted bishop
	 * 					move is a valid bishop move and return true if so, else false.
	 */
	public boolean isValidMoveType(Board board, int fromX, int fromY, int toX, int toY) {
		Piece[][] gameboard = board.getBoard();
		int xDiff = toX - fromX;
		int yDiff = toY - fromY;
		
		if(xDiff == 0 && yDiff == 0) {
			return false;
		}
		else if(Math.abs(xDiff) != Math.abs(yDiff) || board.isPieceBetween(fromX, fromY, toX, toY)) {
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
