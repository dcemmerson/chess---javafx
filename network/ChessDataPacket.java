package network;

import java.io.Serializable;

import utility.ChessMove;

public class ChessDataPacket implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean isMove;
	private ChessMove chessMove;
	private boolean isMessage;
	private String message;
	private boolean connectionLost;
	private boolean opponentResigned;
	
	public ChessDataPacket(int fromX, int fromY, int toX, int toY) {
		this.chessMove = new ChessMove(fromX, fromY, toX, toY);
		this.isMove = true;
		this.isMessage = false;
		this.connectionLost = false;
		this.opponentResigned = false;

	}
	
	public ChessDataPacket(String str) {
		this.message = str;
		this.isMove = false;
		this.isMessage = true;
		this.connectionLost = false;
		this.opponentResigned = false;

	}
	public ChessDataPacket(boolean connectionLost) {
		this.message = null;
		this.isMove = false;
		this.isMessage = false;
		this.connectionLost = connectionLost;
	}
	public ChessDataPacket() {
		this.message = null;
		this.isMove = false;
		this.isMessage = false;
		this.connectionLost = false;
		this.opponentResigned = false;

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
	
	public boolean connectionLost() {
		return connectionLost;
	}
	
	public void setOpponentResigned(boolean resigned) {
		this.opponentResigned = resigned;
	}
	
	public boolean isOpponentResigned() {
		return this.opponentResigned;
	}
}
