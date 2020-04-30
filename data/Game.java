package data;

public class Game {

	private Player playerWhite;
	private Player playerBlack;
	private Board board;
	private boolean whiteTurn;

	public Game() {
		
		this.playerWhite = new Player(true);
		this.playerBlack = new Player(false);

		this.board = new Board(playerWhite, playerBlack);
		this.whiteTurn = true;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public boolean isWhiteTurn() {
		return whiteTurn;
	}

	public void setWhiteTurn(boolean whiteTurn) {
		this.whiteTurn = whiteTurn;
	}

}
