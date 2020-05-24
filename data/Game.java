package data;

public class Game {

	private Player playerWhite;
	private Player playerBlack;

	private Board board;
	private boolean ended;
	private boolean checkmate;
	private boolean stalemate;
	
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
