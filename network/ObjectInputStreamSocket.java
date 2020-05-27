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
		
		/*
		String str = "";
		String s[] = new String[NUMBER_SUBSTRINGS];
		
		
		while(!str.contains(NetworkController.ENDLINE)) {
			str += (char)read();
		}
*/	
//		s = str.split(NetworkController.ENDLINE_REGEX, NUMBER_SUBSTRINGS);
//		System.out.println("line read: " + s[0]);
//		return s[0];

	}
	
	
}
