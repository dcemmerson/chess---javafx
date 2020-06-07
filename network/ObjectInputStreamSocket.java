package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ObjectInputStreamSocket extends ObjectInputStream {
	public static int NUMBER_SUBSTRINGS = 2;
	
	public ObjectInputStreamSocket(Socket s) throws IOException{
		super(s.getInputStream());
	}
	
	public ChessDataPacket readCdpSocket() {
		
		ChessDataPacket cdp = null;
		try {
			cdp = (ChessDataPacket)readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		return cdp;

	}
	
	
}
