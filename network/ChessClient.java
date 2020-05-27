package network;

import java.io.IOException;
import java.net.Socket;

public class ChessClient extends ChessHost {

	public ChessClient(String host, String port, String username) {
		this.host = host;
		this.port = Integer.parseInt(port);
		this.username = username;
	}

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
	/*
	 * public void listen() throws IOException { String fromServer;
	 * 
	 * while((fromServer = in.readLine()) != null) { System.out.println("Server: " +
	 * fromServer); }
	 * 
	 * }
	 */
}
