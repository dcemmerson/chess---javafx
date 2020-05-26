package controller;

import java.util.ArrayList;

public interface MainActions {
	public void appendToChatBox(ArrayList<String> str, boolean white);
	public void sendMoveToOtherPlayer(int fromX, int fromY, int toX, int toY);
	public void sendText(String str);
	public void receiveText(String str);
	public void startGame(boolean player1IsRemote, boolean player2IsRemote);
}
