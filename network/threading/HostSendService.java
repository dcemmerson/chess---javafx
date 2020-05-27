package network.threading;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import network.ChessDataPacket;
import network.ChessHost;

public class HostSendService extends Service<Object> {

	private ChessHost chessHost;
	private ChessDataPacket chessDataPacket;
	
	public HostSendService(ChessHost ch) {
		this.chessHost = ch;
	}
	
	@Override
	protected Task<Object> createTask() {

		return new Task<Object>() {

			protected Object call() throws Exception {
				if(chessDataPacket != null) {
					// Create local var holding send packet and set packet = null to remove 
					// potential for same message sent multiple times due to slow network 
					// and user entering multiple commands quickly.
					ChessDataPacket cdp = chessDataPacket;
					chessDataPacket = null;

					chessHost.write(cdp);
				}
				
				
				return null;
			}
			
		};

	}

	public void setChessDataPacket(ChessDataPacket cdp) {
		this.chessDataPacket = cdp;
	}
}
