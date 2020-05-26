package network;

import java.io.IOException;
import java.net.Socket;

public class ChessClient extends ChessHost {

	public ChessClient(String host, String port) {
		this.host = host;
		this.port = Integer.parseInt(port);
	}

	public boolean connect() {
		try {
			Socket s = new Socket(this.host, this.port);
			PrintWriterSocket out = new PrintWriterSocket(s);
			BufferedReaderSocket in = new BufferedReaderSocket(s);

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
