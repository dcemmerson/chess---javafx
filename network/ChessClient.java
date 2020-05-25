package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChessClient extends ChessHost {

	public ChessClient(String host, String port) {
		this.host = host;
		this.port = Integer.parseInt(port);
	}

	public boolean connect() {
		try {
			Socket s = new Socket(this.host, this.port);
			PrintWriter out = new PrintWriter(s.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

			this.socket = s;
			this.out = out;
			this.in = in;
			return true;

		} catch (IOException e) {
			System.out.println(e);
			System.out.println("client catch");
			return false;
		}
	}
	/*
	 * public void listen() throws IOException { String fromServer;
	 * 
	 * while((fromServer = in.readLine()) != null) { System.out.println("Server: " +
	 * fromServer); }
	 * 
	 * }
	 */
}
