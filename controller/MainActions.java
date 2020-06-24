/* 	filename: MainActions.java
 * 	last modified: 06/23/2020
 * 	description: MainActions interface prototypes used for communication
 * 					between NetworkController, ChatBoxTypeArea, ChatScrollPane,
 * 					and GameController, all funneled through MainController. 
 */
package controller;

import network.ChessDataPacket;

public interface MainActions {
	public void appendToChatBox(String str, boolean white);
	public void appendToChatBox(String str);
	public void sendText(String str);
	public void receiveText(String str);
	public void receiveChessDataPacket(ChessDataPacket cdp);
	public void sendMoveToRemotePlayer(int fromX, int fromY, int toX, int toY);
}
