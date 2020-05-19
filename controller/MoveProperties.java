package controller;

import gui.PieceImageView;

public class MoveProperties {
	private String msg;
	private PieceImageView piv;
	
	public MoveProperties(String msg, PieceImageView piv) {
		this.msg = msg;
		this.piv = piv;
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
	
}
