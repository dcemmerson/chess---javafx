package network;

import java.io.Serializable;

import utility.ChessMove;

public class ChessDataPacket implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean isMove;
	private ChessMove chessMove;
	private boolean isMessage;
	private String message;
	
	public ChessDataPacket(int fromX, int fromY, int toX, int toY) {
		this.chessMove = new ChessMove(fromX, fromY, toX, toY);
		this.isMove = true;
		this.isMessage = false;
	}
	
	public ChessDataPacket(String str) {
		this.message = str;
		this.isMove = false;
		this.isMessage = true;
	}
	
	public boolean isMove() {
		return isMove;
	}

	public void setMove(boolean isMove) {
		this.isMove = isMove;
	}

	public ChessMove getChessMove() {
		return chessMove;
	}

	public void setChessMove(ChessMove chessMove) {
		this.chessMove = chessMove;
	}

	public boolean isMessage() {
		return isMessage;
	}

	public void setMessage(boolean isMessage) {
		this.isMessage = isMessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
