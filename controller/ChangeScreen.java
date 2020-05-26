package controller;

import network.ChessHost;

public interface ChangeScreen {
	public void changeScreens(String name, boolean destroy, boolean recreate);
	public void changeScreens(String name, GameType args, boolean destroy, boolean recreate);
	public void changeScreens(String name, GameType args, ChessHost ch, boolean destroy, boolean recreate);
}
