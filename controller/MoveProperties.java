/* 	filename: MoveProperties.java
 * 	last modified: 06/23/2020
 * 	description: Helper class used to pass information back from a MoveService
 * 					thread to the onSucceeded/onFailed handlers.
 */

package controller;

import gui.PieceImageView;

public class MoveProperties {
	private String msg;
	private PieceImageView piv;
	
	private int fromX;
	private int fromY;
	private int toX;
	private int toY;

	public MoveProperties(String msg, PieceImageView piv) {
		this.msg = msg;
		this.piv = piv;
	}
	
	public MoveProperties(String msg, PieceImageView piv, int fromX, int fromY, int toX, int toY) {
		this.msg = msg;
		this.piv = piv;
		
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;

	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public PieceImageView getPiv() {
		return piv;
	}

	public void setPiv(PieceImageView piv) {
		this.piv = piv;
	}
	public int getFromX() {
		return fromX;
	}

	public void setFromX(int fromX) {
		this.fromX = fromX;
	}

	public int getFromY() {
		return fromY;
	}

	public void setFromY(int fromY) {
		this.fromY = fromY;
	}

	public int getToX() {
		return toX;
	}

	public void setToX(int toX) {
		this.toX = toX;
	}

	public int getToY() {
		return toY;
	}

	public void setToY(int toY) {
		this.toY = toY;
	}
	
}
