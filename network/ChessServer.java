package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChessServer extends ChessHost {
	
	ServerSocket serverSocket;

	int port;
	
	public ChessServer(String port) {
		this.port = Integer.parseInt(port);
		this.host = "localhost";
	}

	public void listen() {
		try {
			ServerSocket serverSocket = new ServerSocket(port); 
			Socket clientSocket = serverSocket.accept();
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			this.serverSocket = serverSocket;
			this.socket = clientSocket;
			this.out = out;
			this.in = in;

		} catch (IOException e) {
			System.out.println(e);
			System.out.println("server catch");
		}
	}
	
	public void write(String str) {
		out.println(str);
	}
}
