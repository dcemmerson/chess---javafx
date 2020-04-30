package data;

import java.io.IOException;

import javafx.scene.image.Image;

public abstract class Piece {
	protected String name;
	protected Image img;
	protected String imgLocation;
	protected boolean white;
	
	public Piece(String name, String imgName, boolean white) {
		this.name = name;
		this.imgLocation = "gui/images/" + imgName;
		this.white = white;
	}
	public boolean isValidMove(Board board, int fromX, int fromY, int toX, int toY) {
		System.out.println("makeMove not implemented for " + this.name);
		return false;
	}
	
	
	public String getName() {
		return this.name;
	}
	public boolean isWhite() {
		return white;
	}
	
	protected void setImage(String imgName) {
		try {
			this.img = openPNG(imgName);
		}
		catch(IOException e) {
			this.img = null;
		    String workingDir = System.getProperty("user.dir");
		    System.out.println("Current working directory : " + workingDir);
		    e.printStackTrace();		
		}
	}
	private Image openPNG(String name) throws IOException {
		this.imgLocation = "gui/images/" + name;
		Image img = new Image(this.imgLocation, 0, 40, false, false);
		return img;
	}
	public Image getImage() {
		return this.img;
	}
	public String getImgLocation() {
		return this.imgLocation;
	}
}
