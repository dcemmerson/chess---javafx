/*	filename: ChessBoardAction.java
 * 	last modified: 06/24/2020
 * 	description: Provide interface for ChessBoard class to communicate 
 * 					back to GameController and MainController classes.
 */
package gui;

import javafx.scene.image.ImageView;

public interface ChessBoardAction {
	public void addImage(ImageView img, int x, int y);
	public void removeImage(PieceImageView img);
	public void switchTurns(boolean isWhiteTurn, String captureMessage);
	public void sendMoveToOtherPlayer(int fromX, int fromY, int toX, int toY);
}
