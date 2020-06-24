/*	filename: Game.java
 * 	last modified: 06/24/2020
 * 	description: Contains game class, which mostly deals with validating
 * 					attempted moves on the board, maintaining the state of
 * 					the game, determining checkmate, determining stalemate, 
 * 					etc.
 */

package data;

public class Game {

	private Player playerWhite;
	private Player playerBlack;

	private Board board;
	private boolean ended;
	private boolean checkmate;
	private boolean stalemate;
	
	/*	name: Game constructor
	 * 	arguments:	whiteIsLocal/blackIsLocal - booleans indicating which players are local 
	 * 				whiteIsCpu/blackIsCpu - booleans indicating which players are cpus. To be
	 * 					a cpu, the player must be local.
	 * 	description: This constructor should be used when creating instance of Game class.
	 */
	public Game(boolean whiteIsLocal, boolean blackIsLocal, boolean whiteIsCpu, boolean blackIsCpu) {
		
		this.playerWhite = new Player(true, whiteIsLocal, whiteIsCpu);
		this.playerBlack = new Player(false, blackIsLocal, blackIsCpu);

		this.board = new Board(playerWhite, playerBlack);
		this.ended = false;
		this.checkmate = false;
		this.stalemate = false;
	}

	public Player getPlayerWhite() {
		return playerWhite;
	}
	public Player getPlayerBlack() {
		return playerBlack;
	}
	
	/*	name: moveFulltTurn
	 * 	arguments: fromX/Y, toX/Y - ints representing to/from square on board
	 * 	description: This is the function to call when attempting to make a move.
	 * 					The GUI end of program should call this method, 
	 * 					when a player attempts to make a move. Method will
	 * 					return false if invalid move, else valid if true and
	 * 					set appropriate flags in game class, including Game.ended,
	 * 					Game.checkmate, etc.
	 */
	public boolean moveFullTurn(int fromX, int fromY, int toX, int toY) {
		Player currPlayer;
		Player nextPlayer;
		
		if(playerWhite.isTurn()) {
			currPlayer = playerWhite;
			nextPlayer = playerBlack;
		}
		else {
			currPlayer = playerBlack;
			nextPlayer = playerWhite;
		}
		
		if(toX < 0 || toX > Board.SQUARES_WIDE || toY < 0 || toY > Board.SQUARES_HIGH) return false;
		
		boolean moveMade = move(currPlayer, nextPlayer, fromX, fromY, toX, toY);
		
		if(moveMade) {
			board.queenify(toX, toY);
			if(!hasMovesAvailable(nextPlayer)) {
				if(nextPlayer.isInCheck()) {
					nextPlayer.setWon(false);
					currPlayer.setWon(true);
					this.ended = true;
					this.checkmate = true;
					System.out.println("is in checkmate");
				}
				else {
					this.stalemate = true;
					this.ended = true;
					System.out.println("is in stalemate");

				}
			}
		}
		
		return moveMade;
	}
	
	/*	name: hasMoveAvailable
	 * 	arguments: currPlayer - Player instance which we are checking if they
	 * 					are in checkmate or stalemate.
	 * 	description: This method does not determine checkmate or stalemate, but
	 * 					does determine if a player has any available moves. This
	 * 					method should be called at the end of a player making a 
	 * 					move, on the player whose turn it is next. This way we
	 * 					can determine if after player A makes a move, if that
	 * 					move results in either a checkmate of player B, or a
	 * 					stalemate, and set the appropriate Game flags.
	 */
	public boolean hasMovesAvailable(Player currPlayer) {
		Piece[][] gameboard = board.getBoard();
		for(int y = 0; y < Board.SQUARES_HIGH; y++) {
			for(int x = 0; x < Board.SQUARES_WIDE; x++) {
				if(gameboard[y][x] != null && gameboard[y][x].isWhite() == currPlayer.isWhite()) {
					if(pieceHasAnyValidMoves(currPlayer, gameboard[y][x], x, y)) {//then this move gets player out of check
						return true;
					}
				}
			}
		}
		// Then no available moves and currPlayer is either in checkmate
		// or it's a stalemate.
		return false;			
	}

	
	/*	pieceHasAnyValidMoves
	 * 	arguments: 	currPlayer - Player instance who owns the piece
	 * 				piece - Piece instance we are checking if it has moves
	 * 				fromX/fromY - ints representing piece current location
	 * 	description: This method iterates through game board checking if piece
	 * 					is able to make any valid moves. This is a helper
	 * 					function to hasMovesAvailable, but is not restricted
	 * 					to being used by hasMoveAvailable.
	 */
	public boolean pieceHasAnyValidMoves(Player currPlayer, Piece piece, int fromX, int fromY) {
		for(int y = 0; y < Board.SQUARES_HIGH; y++) {
			for(int x = 0; x < Board.SQUARES_WIDE; x++) {
				Piece[][] gameboard = board.getBoard();		
				
				if(gameboard[fromY][fromX] != null && gameboard[fromY][fromX].isWhite() == playerWhite.isTurn() && board.isValidMoveType(fromX, fromY, x, y)) {			
					Piece captured = board.execMove(fromX, fromY, x, y);

					this.updatePlayersInCheck();
					
					if(currPlayer.isInCheck()) {
						board.unexecMove(captured, x, y, fromX, fromY);
						this.updatePlayersInCheck();
					}
					else {
						board.unexecMove(captured, x, y, fromX, fromY);
						this.updatePlayersInCheck();
						return true;
					}
				}
			}
		}
		return false;
		
	}
	
	/*	name: move
	 * 	arguments:	currPlayer - Player instance whose trying to make move
	 * 				nextPlayer - Player instance whose turn it is not
	 * 				fromX/Y, toX/Y - ints representing move currPlayer is making
	 * 	description: Helper method to moveFullTurn method. This method checks to
	 * 					verify requested move is valid move for piece at fromX/Y
	 * 					location on board and makes the move. We then update the
	 * 					players in check and verify that currPlayer (player that 
	 * 					just made the move) did not move into check, as this would
	 * 					not be a valid move. If currPlayer did move into check,
	 * 					then un-execute that move and update player in check again. 
	 * 					Return true if move was successfully made, else false.
	 */
	public boolean move(Player currPlayer, Player nextPlayer, int fromX, int fromY, int toX, int toY) {
		Piece[][] gameboard = board.getBoard();		
	
		if(gameboard[fromY][fromX] != null 
				&& gameboard[fromY][fromX].isWhite() == playerWhite.isTurn() 
				&& board.isValidMoveType(fromX, fromY, toX, toY)) {			

			Piece captured = board.execMove(fromX, fromY, toX, toY);

			updatePlayersInCheck();
			
			if(currPlayer.isInCheck()) {
				board.unexecMove(captured, toX, toY, fromX, fromY);
				this.updatePlayersInCheck();

				return false;
			}
			
			playerWhite.setTurn(!playerWhite.isTurn());
			playerBlack.setTurn(!playerBlack.isTurn());

			return true;
		}

		return false;
	}
	
	/*	name: updatePlayersInCheck
	 * 	description: Helper method to move/moveFullTurn, but not restricted
	 * 					to being used with those two methods. Iterate through
	 * 					game board, finding the kings of each team and call
	 * 					updateKingInCheck, which will update the flags for each
	 * 					player if they are in check.
	 */
	public void updatePlayersInCheck() {

		Piece[][] gameboard = board.getBoard();
		
		playerWhite.setInCheck(false);
		playerBlack.setInCheck(false);
		
		//first get both players kings
		for(int y = 0; y < Board.SQUARES_HIGH; y++) {
			for(int x = 0; x < Board.SQUARES_WIDE; x++) {
				if(gameboard[y][x] instanceof King) {
					updateKingInCheck(gameboard[y][x], x, y);
				}
			}
		}
	}
	
	/*	name: updateKingInCheck
	 * 	arguments: 	king - An instance of Piece, but really should be called with
	 * 					an instance of King (which inherits from Piece). This allows
	 * 					us to easily call base class methods on king.
	 * 				kingX/Y - Location of king on board
	 * 	description: Iterate through game board checking if each piece can make a 
	 * 					valid move to capture king. Piece must be of opposite color,
	 * 					and be able to validly move to where kingX/Y is on board.
	 */
	public void updateKingInCheck(Piece king, int kingX, int kingY) {
		Piece[][] gameboard = board.getBoard();
		//now check if any piece of the other player can move and capture our king
		for(int y = 0; y < Board.SQUARES_HIGH; y++) {
			for(int x = 0; x < Board.SQUARES_WIDE; x++) {
				if(gameboard[y][x] != null && (gameboard[y][x].isWhite() != king.isWhite())) {
					if(board.isValidMoveType(x, y, kingX, kingY)) {
						if(king.isWhite()) playerWhite.setInCheck(true);
						else playerBlack.setInCheck(true);
					}
				}
			}
		}		
	}
	
	public Board getBoard() {
		return board;
	}
	
	public boolean isEnded() {
		return ended;
	}
	
	public void setEnded(boolean ended) {
		this.ended = ended;
	}
	
	public void endGame() {
		playerBlack.endGame();
		playerWhite.endGame();
//		board.endGame();
	}
	public boolean isCheckmate() {
		return checkmate;
	}

	public boolean isStalemate() {
		return stalemate;
	}
}
