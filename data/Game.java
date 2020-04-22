package data;

public class Game {

	private Player player1;
	private Player player2;
	private Board board;
	
	public Game(int SQUARES_WIDE, int SQUARES_HIGH) {
		
		this.player1 = new Player();
		this.player2 = new Player();

		this.board = new Board(SQUARES_WIDE, SQUARES_HIGH, player1, player2);
	}
	
	public Piece[][] getBoard() {
		return board.getBoard();
	}


}
