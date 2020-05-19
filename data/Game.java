package data;

public class Game {

	private Player playerWhite;
	private Player playerBlack;

	private Board board;
	private boolean ended;

	public Game(boolean whiteIsLocal, boolean blackIsLocal, boolean whiteIsCpu, boolean blackIsCpu) {
		
		this.playerWhite = new Player(true, whiteIsLocal, whiteIsCpu);
		this.playerBlack = new Player(false, blackIsLocal, blackIsCpu);

		this.board = new Board(playerWhite, playerBlack);
		this.ended = false;
	}

	public Player getPlayerWhite() {
		return playerWhite;
	}
	public Player getPlayerBlack() {
		return playerBlack;
	}
	public boolean isInCheckMate(Player currPlayer) {
		if(currPlayer.isInCheck()) {
			Piece[][] gameboard = board.getBoard();
			for(int y = 0; y < Board.SQUARES_HIGH; y++) {
				for(int x = 0; x < Board.SQUARES_WIDE; x++) {
					if(gameboard[y][x] != null && gameboard[y][x].isWhite() == currPlayer.isWhite()) {
						if(pieceHasAnyValidMoves(currPlayer, gameboard[y][x], x, y)) {//then this move gets player out of check
							return false;
						}
					}
				}
			}
			return true;			
		}

		return false;
	}
	
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
		
		if(toX < 0 || toX > 7 || toY < 0 || toY > 7) return false;
		
		boolean moveMade = move(currPlayer, nextPlayer, fromX, fromY, toX, toY);
		
/*		
		if(moveMade) {
			board.printBoard();
		}
*/	
		if(isInCheckMate(nextPlayer)) {
			nextPlayer.setWon(false);
			currPlayer.setWon(true);
			this.ended = true;
			System.out.println("is in checkmate");
		}
		return moveMade;
	}
	
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
	
	public void updateKingInCheck(Piece king, int kingX, int kingY) {
		Piece[][] gameboard = board.getBoard();
		//now check if any piece of the other player can move and capture our king
		for(int y = 0; y < Board.SQUARES_HIGH; y++) {
			for(int x = 0; x < Board.SQUARES_WIDE; x++) {
				if(gameboard[y][x] != null && (gameboard[y][x].isWhite() != king.isWhite())) {
					if(board.isValidMoveType(x, y, kingX, kingY)) {
						System.out.println("check move is: from(X,Y) = " + x + ", " + y + " to(x,Y) = " + kingX + ", " + kingY);
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
}
