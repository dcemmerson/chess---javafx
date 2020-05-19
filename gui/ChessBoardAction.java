package gui;

import javafx.scene.image.ImageView;

public interface ChessBoardAction {
	public void addImage(ImageView img, int x, int y);
	public void movePiece(ImageView img, int x, int y);
	public void removeImage(PieceImageView img);
//	public void switchTurns(boolean isWhiteTurn);
	public void switchTurns(boolean isWhiteTurn, String captureMessage);
	public void sendMoveToOtherPlayer(int fromX, int fromY, int toX, int toY);
	public void refresh();
	public void displayMessage(String captureMessage);
	}
