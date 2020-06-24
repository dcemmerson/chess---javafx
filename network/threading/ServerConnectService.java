/*	filename: ServerConnectService.java
 * 	last modified: 06/24/2020
 * 	description: ServerConnectService is a child class to HostConnectService.
 * 					ServerConnectService manages listening incoming connection
 * 					from a host, in a separate thread from the main JavaFX thread.
 */

package network.threading;

import network.ChessServer;

public class ServerConnectService extends HostConnectService {
	
	public ServerConnectService(String port, String username) {
		this.server = true;
		this.port = port;
		this.username = username;
	}
	
	/*	name: connect
	 * 	description: Connect is called by the parent class createTask method.
	 * 					Continues listening until eventually it establishes a 
	 * 					connection or the closeSocket method (defined in 
	 * 					parent class) is called.
	 * 					
	 */
	public Object connect() throws InterruptedException {
		chessHost = new ChessServer(port, username);

		chessHost.listen();
		
		return chessHost;
	}

}
