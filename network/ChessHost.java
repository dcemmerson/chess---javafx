package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ChessHost {
	String host;
	int port;
	Socket socket;
	PrintWriter out;
	BufferedReader in;

	public boolean connect() {
		System.out.println("Wrong connect - ChessHost");
		return false;
	}
	
	public void write(String str) {
		out.println(str);
	}

	public String readLine() {
		try {
			String str = in.readLine();
			return str;
		}
		catch(IOException e) {
			System.out.println(e);
			return "Error reading from socket.";
		}
	}
	
}
