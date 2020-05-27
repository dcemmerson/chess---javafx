package network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ObjectOutputStreamSocket extends ObjectOutputStream {

	public ObjectOutputStreamSocket(Socket s) throws IOException {
		super(s.getOutputStream());
	}

	public void printCdpSocket(ChessDataPacket cdp) {
		try {
			writeObject(cdp);
			flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
