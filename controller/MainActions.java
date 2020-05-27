package controller;

import network.ChessDataPacket;

public interface MainActions {
	public void appendToChatBox(String str, boolean white);
	public void appendToChatBox(String str);
	public void sendText(String str);
	public void receiveText(String str);
	public void receiveChessDataPacket(ChessDataPacket cdp);
	public void startGame(boolean player1IsRemote, boolean player2IsRemote);
	public void sendMoveToRemotePlayer(int fromX, int fromY, int toX, int toY);
}
