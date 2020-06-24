/*	filename: ObjectInputStreamSocket.java
 * 	last modified: 06/24/2020
 * 	description: Class designed to read data incoming from opponent host
 * 					using ObjectInputStream, cast and return that Object
 * 					to a ChessDataPacket.
 */

package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ObjectInputStreamSocket extends ObjectInputStream {
	
	public ObjectInputStreamSocket(Socket s) throws IOException {
		super(s.getInputStream());
	}
	
	public ChessDataPacket readCdpSocket() {
		
		try {
			return (ChessDataPacket)readObject();
		} catch (ClassNotFoundException | IOException e) {			
			// return ChessDataPacket with connectionLost flag set to true
			return new ChessDataPacket(true);
		}

	}
	
	
}
