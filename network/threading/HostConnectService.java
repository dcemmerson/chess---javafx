/*	filename: HostConnectService.java
 * 	last modified: 06/24/2020
 * 	description: HostConnectService abstract class provides parent class to 
 * 					ClientConnectService and ServerConnectService
 * 					classes, which perform tasks of establishing connection
 * 					between hosts for remote game.
 */

package network.threading;

import java.io.IOException;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import network.ChessHost;

public abstract class HostConnectService extends Service<Object> {
	
	protected ChessHost chessHost;
	protected String port;
	protected boolean server;
	protected String username;
	
	/*	name: createTask
	 * 	description: Entry point in new thread. Just call the connect method, which is
	 * 					abstract and must be appropriately defined in child classes.
	 */
	@Override
	protected Task<Object> createTask() {
		return new Task<Object>() {

			@Override
			protected Object call() throws Exception {
				return connect();
			}	
		};
	}
	
	protected abstract Object connect() throws InterruptedException;
	
	public void closeSocket() {
		try {
			chessHost.closeSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isServer() {
		return server;
	}
		
}

