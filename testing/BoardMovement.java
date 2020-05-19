package testing;

import java.util.ArrayList;

import data.Board;
import data.Piece;
import gui.PieceImageView;
import javafx.scene.image.Image;

public class BoardMovement {

	static public void testBoardAlignment(ArrayList<PieceImageView> pieceImagesViews, Piece[][] board) {
//		@SuppressWarnings("unchecked")
//		ArrayList<PieceImageView> copyImagesViews = (ArrayList<PieceImageView>) pieceImagesViews.clone();
		
		for(int y = 0; y < Board.SQUARES_HIGH; y++) {
			for(int x = 0; x < Board.SQUARES_WIDE; x++) {
				Image img = board[y][x].getImage();
				boolean found = false;
				
				for(int i = 0; i < pieceImagesViews.size(); i++) {
					if(img == pieceImagesViews.get(i).getImage() && pieceImagesViews.get(i).isOnBoard()) {
						found = true;
					}
				}
				
				if(!found) {
					System.out.println("Board and image array do not match at: x = " + x + ", y = " + y);
					System.exit(0);

				}
			}
		}
		
	}

}
