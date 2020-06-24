/*	filename: ChessMove.java
 * 	last modified: 06/24/2020
 * 	description: Just a helper class to organize a move toX/Y, fromX/Y
 * 					info, specifically when sending move across network.
 */
package utility;

import java.io.Serializable;

public class ChessMove implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int fromX;
	private int fromY;
	private int toX;
	private int toY;
	
	public ChessMove(int fromX, int fromY, int toX, int toY) {
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
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
