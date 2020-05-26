package network.threading;

import network.ChessServer;

public class ServerConnectService extends HostConnectService {
	
	public ServerConnectService(String port) {
		this.server = true;
		this.port = port;
	}
	public Object connect() throws InterruptedException {
		chessHost = new ChessServer(port);

		chessHost.listen();
		
		return chessHost;
	}

}
