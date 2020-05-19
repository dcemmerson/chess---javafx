package data;

public class Board {
	public final static int SQUARES_WIDE = 8;
	public final static int SQUARES_HIGH = 8;
	
	private Piece board[][];
	
	public Board(Player playerWhite, Player playerBlack) {
		
		initializeBoard();
		placePieces(playerWhite, playerBlack);
	}
	
	public Piece[][] getBoard() {
		return board;
	}
	public boolean isValidMoveType(int fromX, int fromY, int toX, int toY) {
		Piece piece = board[fromY][fromX];
		
		if(piece.isValidMoveType(this, fromX, fromY, toX, toY) 
				&& (board[toY][toX] == null || board[toY][toX].isWhite() != board[fromY][fromX].isWhite())) {
			return true;
		}
		
		return false;
	}
	
	public boolean isPieceBetween(int fromX, int fromY, int toX, int toY) {
		int xDiff = toX - fromX;
		int yDiff = toY - fromY;
		
		while(Math.abs(xDiff) > 1 || Math.abs(yDiff) > 1) {
			if(xDiff > 0) {
				xDiff--;
			} else if(xDiff < 0) {
				xDiff++;
			}
			if(yDiff > 0) {
				yDiff--;
			} else if(yDiff < 0) {
				yDiff++;
			}
			
			if(board[toY - yDiff][toX - xDiff] != null) {
				return true;
			}
		}
	
		return false;
	}
	
	public Piece execMove(int fromX, int fromY, int toX, int toY) {
		System.out.println("making move: (" + fromX + ", " + fromY + ") to (" + toX + ", " + toY + ")");

		Piece captured = board[toY][toX];		
		board[toY][toX] = board[fromY][fromX];
		board[fromY][fromX] = null;
		
		return captured;
	}
	
	public void unexecMove(Piece uncaptured, int toX, int toY, int fromX, int fromY) {
		System.out.println("unmaking move");
		board[fromY][fromX] = board[toY][toX];
		board[toY][toX] = uncaptured;
		
	}
	
	private void initializeBoard() {
		board = new Piece[SQUARES_HIGH][];
		
		for(int j = 0; j < SQUARES_HIGH; j++) {
			board[j] = new Piece[SQUARES_WIDE];
			for(int i = 0; i < SQUARES_WIDE; i++) {
				this.board[j][i] = null;
			}
		}
	}
	private void placePieces(Player player1, Player player2) {
		//rooks
		this.board[0][0] = player1.getRook1();
		this.board[0][7] = player1.getRook2();
		this.board[7][0] = player2.getRook1();
		this.board[7][7] = player2.getRook2();
		//knights
		this.board[0][1] = player1.getKnight1();
		this.board[0][6] = player1.getKnight2();
		this.board[7][1] = player2.getKnight1();
		this.board[7][6] = player2.getKnight2();		

		//bishops
		this.board[0][2] = player1.getBishop1();
		this.board[0][5] = player1.getBishop2();
		this.board[7][2] = player2.getBishop1();
		this.board[7][5] = player2.getBishop2();

		//queens
		this.board[0][4] = player1.getQueen();
		this.board[7][3] = player2.getQueen();
		//kings
		this.board[0][3] = player1.getKing();
		this.board[7][4] = player2.getKing();
		
		//pawns
		this.board[1][0] = player1.getPawn1();
		this.board[1][1] = player1.getPawn2();
		this.board[1][2] = player1.getPawn3();
		this.board[1][3] = player1.getPawn4();
		this.board[1][4] = player1.getPawn5();
		this.board[1][5] = player1.getPawn6();
		this.board[1][6] = player1.getPawn7();
		this.board[1][7] = player1.getPawn8();

		this.board[6][0] = player2.getPawn1();
		this.board[6][1] = player2.getPawn2();
		this.board[6][2] = player2.getPawn3();
		this.board[6][3] = player2.getPawn4();
		this.board[6][4] = player2.getPawn5();
		this.board[6][5] = player2.getPawn6();
		this.board[6][6] = player2.getPawn7();
		this.board[6][7] = player2.getPawn8();

	}
	
	public void printBoard() {
		for(int y = 0; y < Board.SQUARES_HIGH; y++) {
			System.out.print("row " + y + ": ");
			for(int x = 0; x < Board.SQUARES_WIDE; x++) {
				if(board[y][x] != null) {
					System.out.print(board[y][x].getName() + " ");
				}
				else {
					System.out.print("null ");
				}
			}
			System.out.println("");
		}
			
	}
}
