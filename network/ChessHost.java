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
		ChessDataPacket cdp = ois.readCdpSocket();
		return cdp;
	}
	
	public String getUserName() {
		return username;
	}
	
}
