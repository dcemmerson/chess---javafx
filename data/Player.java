package data;

public class Player {
	private Piece rook1;
	private Piece rook2;
	private Piece bishop1;
	private Piece bishop2;
	private Piece knight1;
	private Piece knight2;
	private Piece queen;
	private Piece king;
	
	private Piece pawn1;
	private Piece pawn2;
	private Piece pawn3;
	private Piece pawn4;
	private Piece pawn5;
	private Piece pawn6;
	private Piece pawn7;
	private Piece pawn8;

	
	public Player(boolean white) {
		rook1 = new Rook(white);
		rook2 = new Rook(white);
		bishop1 = new Bishop(white);
		bishop2 = new Bishop(white);
		knight1 = new Knight(white);
		knight2 = new Knight(white);
		queen = new Queen(white);
		king = new King(white);
		
		pawn1 = new Pawn(white);
		pawn2 = new Pawn(white);
		pawn3 = new Pawn(white);
		pawn4 = new Pawn(white);
		pawn5 = new Pawn(white);
		pawn6 = new Pawn(white);
		pawn7 = new Pawn(white);
		pawn8 = new Pawn(white);

	}	

	public Piece getRook1() {
		return rook1;
	}

	public Piece getRook2() {
		return rook2;
	}

	public Piece getBishop1() {
		return bishop1;
	}

	public Piece getBishop2() {
		return bishop2;
	}

	public Piece getKnight1() {
		return knight1;
	}

	public Piece getKnight2() {
		return knight2;
	}

	public Piece getQueen() {
		return queen;
	}

	public Piece getKing() {
		return king;
	}

	public Piece getPawn1() {
		return pawn1;
	}

	public Piece getPawn2() {
		return pawn2;
	}

	public Piece getPawn3() {
		return pawn3;
	}

	public Piece getPawn4() {
		return pawn4;
	}

	public Piece getPawn5() {
		return pawn5;
	}

	public Piece getPawn6() {
		return pawn6;
	}

	public Piece getPawn7() {
		return pawn7;
	}

	public Piece getPawn8() {
		return pawn8;
	}


}
