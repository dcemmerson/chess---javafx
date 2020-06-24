/* 	filename: ChessHost.java
 * 	last modified: 06/24/2020
 * 	description: Parent class to ChessClient and ChessServer. Using
 * 					this parent class, we can listen for a connection 
 * 					and attempt to connect to other host at same time 
 * 					using the child class ChessClient and ChessServer.
 * 					Upon successful connection to remote host, one host's
 * 					ChessClient will have succeeded with the connection to
 * 					the other host's ChessServer. However, since ChessClient
 * 					and ChessServer inherit from ChessHost and only differ
 * 					by their listen/connect methods, thereafter the connection
 * 					has been established we can essentially treat these
 * 					objects the same, sending and receiving, thus simplifying
 * 					our code.
 */

package network;

import java.io.IOException;
import java.net.Socket;

public class ChessHost {
	protected String username;
	
	protected String host;
	protected int port;
	protected Socket socket;
	protected ObjectOutputStreamSocket oos;
	protected ObjectInputStreamSocket ois;


	public boolean isConnected() {
		return socket.isConnected();
	}
	public boolean connect() {
		System.out.println("Wrong connect - ChessHost");
		return false;
	}
	public void listen() {
		System.out.println("Wong listen - ChessHost");
	}
	
	public void closeSocket() throws IOException {
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
	
	public void write(ChessDataPacket cdp) {
		oos.printCdpSocket(cdp);
	}

	public ChessDataPacket readLine() {
		return ois.readCdpSocket();
	}
	
	public String getUserName() {
		return username;
	}
	
}
