package network;

import java.io.IOException;
import java.net.Socket;

public class ChessHost {
	String host;
	int port;
	Socket socket;
	PrintWriterSocket out;
	BufferedReaderSocket in;


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
		
		if(in != null) {
			in.close();
			in = null;
		}
		
		if(out != null) {
			out.close();
			out = null;
		}
	}
	
	public void write(String str) {
		out.printSocket(str);
	}

	public String readLine() {
		try {
			String str = in.readSocket();
			return str;
		}
		catch(IOException e) {
			System.out.println(e);
			return "Error reading from socket.";
		}
	}
	
}
