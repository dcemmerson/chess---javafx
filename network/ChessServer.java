package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChessServer extends ChessHost {
	
	ServerSocket serverSocket;

	int port;
	
	public ChessServer(String port) {
		this.port = Integer.parseInt(port);
		this.host = "localhost";
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
		
		if(in != null) {
			in.close();
			in = null;
		}
		
		if(out != null) {
			out.close();
			out = null;
		}
	}
	
	public void listen() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			this.serverSocket = serverSocket;

			Socket clientSocket = serverSocket.accept();
			this.socket = clientSocket;

			PrintWriterSocket out = new PrintWriterSocket(clientSocket);
			this.out = out;

			BufferedReaderSocket in = new BufferedReaderSocket(clientSocket);
			this.in = in;

		} catch (IOException e) {
			System.out.println(e);
			System.out.println("server catch");
		}
	}
	

}
