package gui;

import javafx.scene.image.ImageView;

public interface ChessBoardAction {
//	public void canvasAction(int x, int y, Color color);
	public void addImage(ImageView img, int x, int y);
	public void movePiece(ImageView img, int x, int y);
	public void removeImage(PieceImageView img);
}
