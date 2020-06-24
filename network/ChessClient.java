/* 	filename: ChessClient.java
 * 	last modified: 06/24/2020
 * 	description: Child class to ChessHost. ChessClient performs the task
 * 					of attempting to connect, and upon successful connection sets
 * 					the ObjectInputStreamSocket and ObjectOutputStreamSocket
 * 					in the parent class.
 */

package network;

import java.io.IOException;
import java.net.Socket;

public class ChessClient extends ChessHost {

	/*	name: ChessClient constructor
	 * 	arguments:	host - String representing host ipv4 address. Should already
	 * 					be validated as a valid ipv4 addres.
	 * 				port - String representation of port number. Should already
	 * 					be validated as a valid port number between 1024 - 65536.
	 * 				username - User entered String username.
	 */
	public ChessClient(String host, String port, String username) {
		this.host = host;
		this.port = Integer.parseInt(port);
		this.username = username;
	}
	
	/*	name: connect
	 * 	description: Attempt to connect to opponent ChessServer. Will set 
	 * 					ObjectOutputStreamSocket and ObjectInputStreamSocket 
	 * 					in parent class upon successful connection.
	 */
	public boolean connect() {
		try {
			Socket s = new Socket(this.host, this.port);
			ObjectOutputStreamSocket oos = new ObjectOutputStreamSocket(s);
			ObjectInputStreamSocket ois = new ObjectInputStreamSocket(s);

			this.socket = s;
			this.oos = oos;
			this.ois = ois;
			return true;

		} catch (IOException e) {
			System.out.println(e);
			System.out.println("client catch");
			return false;
		}
	}

}
