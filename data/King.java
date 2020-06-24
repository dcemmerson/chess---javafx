/*	filename: King.java
 * 	last modified: 06/23/2020
 * 	description: Piece child class defining King type of piece. Contains
 * 					move logic for a King chess piece.
 */

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
	
	/*	name: isValidMoveType
	 * 	arguments: 	board - Board so we can check if there are any pieces in the way of toX/Y
	 * 					and fromX/Y location, as well as ensure the color of a piece is valid
	 * 					when capturing a piece.
	 * 				fromX/Y, toX/Y - ints representing to/from spaces on board (and in board array).
	 * 	description: A King moves one space in any direction, may capture pieces of the other color.
	 * 					A King may not move into check, but that logic is better handled by the game
	 * 					class since we must also enforce additional rules better dealt with at a 
	 * 					higher level, such as checkmate. Check if attempted king
	 * 					move is a valid king move and return true if so, else false.
	 */
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
