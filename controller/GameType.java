package controller;

public class GameType {
	private boolean p1Local;
	private boolean p2Local;
	private boolean p1IsCpu;
	private boolean p2IsCpu;
	private boolean remoteGame;
	
	public GameType(boolean p1Local, boolean p2Local, boolean p1IsCpu, boolean p2IsCpu, boolean remoteGame) {
		this.p1Local = p1Local;
		this.p2Local = p2Local;
		
		this.p1IsCpu = p1IsCpu;
		this.p2IsCpu = p2IsCpu;
		
		this.remoteGame = remoteGame;
	}

	public boolean isP1Local() {
		return p1Local;
	}

	public boolean isP2Local() {
		return p2Local;
	}

	public boolean isP1IsCpu() {
		return p1IsCpu;
	}

	public boolean isP2IsCpu() {
		return p2IsCpu;
	}
	
	public boolean isRemoteGame() {
		return remoteGame;
	}
}
