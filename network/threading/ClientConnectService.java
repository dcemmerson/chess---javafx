/*	filename: ClientConnectService.java
 * 	last modified: 06/24/2020
 * 	description: ClientConnectService is a child class to HostConnectService.
 * 					ClientConnectService manages attempting to connect to
 * 					a host, in a separate thread from the main JavaFX thread.
 */

package network.threading;

import network.ChessClient;

public class ClientConnectService extends HostConnectService {
	
	private String hostname;

	/*	name: ClientConnectSerivice constructor
	 * 	arguments:	host - String representing host ipv4 address. Should already
	 * 					be validated as a valid ipv4 address.
	 * 				port - String representation of port number. Should already
	 * 					be validated as a valid port number between 1024 - 65536.
	 * 				username - User entered String username.
	 */
	public ClientConnectService(String hostname, String port, String username) {
		this.server = false;
		this.port = port;
		this.hostname = hostname;
		this.username = username;
	}
	
	/*	name: connect
	 * 	description: Connect is called by the parent class createTask method.
	 * 					Attempts to connect to a listening server at this.port,
	 * 					this.hostname, if failed, repeats every 2 seconds until
	 * 					eventually it establishes a connection or the closeSocket
	 * 					method (defined in parent class) is called.
	 * 					
	 */
	public Object connect() throws InterruptedException {
		chessHost = new ChessClient(hostname, port, username);
			
		while(!(chessHost.connect())) {
			System.out.println("no connection - sleep 2000 - " + Thread.currentThread().getName());
			Thread.sleep(2000);
		}
		return chessHost;
	}
}
