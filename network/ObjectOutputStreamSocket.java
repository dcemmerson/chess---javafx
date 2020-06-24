/*	filename: ObjectInputStreamSocket.java
 * 	last modified: 06/24/2020
 * 	description: Class designed to send ChessDataPacket to opponent host
 * 					using ObjectOutputStream.
 */

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
