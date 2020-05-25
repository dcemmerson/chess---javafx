package network.threading;

import network.ChessServer;

public class ServerConnectService extends HostConnectService {
	
	public ServerConnectService(String port) {
		this.port = port;
	}
	public Object connect() throws InterruptedException {
		ChessServer chessServer = new ChessServer(port);
			
		chessServer.listen();
		
		return chessServer;
	}

}
