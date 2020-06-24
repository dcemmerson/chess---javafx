/*	name: ChangeScreen.java
 * 	last modified: 06/23/2020
 * 	description: Interface methods for changing scenes.
 */
package controller;

import network.ChessHost;

public interface ChangeScreen {
	public void changeScreens(String name, boolean destroy, boolean recreate);
	public void changeScreens(String name, GameType args, boolean destroy, boolean recreate);
	public void changeScreens(String name, GameType args, ChessHost ch, boolean destroy, boolean recreate);
}
