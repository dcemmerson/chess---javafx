package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChessServer extends ChessHost {
	
	ServerSocket serverSocket;

	int port;
	
	public ChessServer(String port, String username) {
		this.port = Integer.parseInt(port);
		this.host = "localhost";
		this.username = username;
	}

	public void closeSocket() throws IOException {
		if(serverSocket != null) {
			serverSocket.close();
			serverSocket = null;
//			serverSocket.setReuseAddress(true);
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
