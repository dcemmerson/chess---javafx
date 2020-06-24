/* 	filename: ChessServer.java
 * 	last modified: 06/24/2020
 * 	description: Child class to ChessHost. ChessServer performs the task
 * 					of listening, and upon successful connection sets
 * 					the ObjectInputStreamSocket and ObjectOutputStreamSocket
 * 					in the parent class.
 * 
 * 				If ChessClient (the other ChessHost child class) successfully 
 * 					connects to opponent host first, calling the closeSocket
 * 					method should be done to close this socket.
 */

package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChessServer extends ChessHost {
	
	ServerSocket serverSocket;

	int port;
	
	/*	name: ChessServer constructor
	 * 	arguments: 	port - String representation of port number. Should already
	 * 					be validated as a valid port number between 1024 - 65536.
	 * 				username - User entered String username.
	 * 				host is assumed to be "localhost" since this is the server class.
	 */
	public ChessServer(String port, String username) {
		this.port = Integer.parseInt(port);
		
		// Since this is the server element, should always be localhost since we
		// are listening.
		this.host = "localhost";
		this.username = username;
	}

	/*	name: closeSocket
	 * 	description: Closes listening socket. If this player's ChessClient establishes
	 * 					connection to opponent's ChessServer before this player's
	 * 					ChessServer (this class) establishes connection, call 
	 * 					closeSocket method to shutdown socket.
	 */
	public void closeSocket() throws IOException {
		if(serverSocket != null) {
			serverSocket.close();
			serverSocket = null;
		}
		if(socket != null) {
			socket.close();
			socket = null;
		}
		
		if(ois != null) {
			ois.close();
			ois = null;
		}
		
		if(oos != null) {
			oos.close();
			oos = null;
		}
	}
	
	/*	name: listen
	 * 	description: Listens for a connection from opponent ChessClient attempting
	 * 					to connect and will set ObjectOutputStreamSocket and 
	 * 					ObjectInputStreamSocket in parent class upon successful
	 * 					connection.
	 */
	public void listen() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			this.serverSocket = serverSocket;

			Socket clientSocket = serverSocket.accept();
			this.socket = clientSocket;

			ObjectOutputStreamSocket oos = new ObjectOutputStreamSocket(clientSocket);
			this.oos = oos;

			ObjectInputStreamSocket ois = new ObjectInputStreamSocket(clientSocket);
			this.ois = ois;

		} catch (IOException e) {
			System.out.println(e);
			System.out.println("server catch");
		}
	}
	

}
