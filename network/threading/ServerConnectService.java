package network.threading;

import network.ChessServer;

public class ServerConnectService extends HostConnectService {
	
	public ServerConnectService(String port, String username) {
		this.server = true;
		this.port = port;
		this.username = username;
	}
	public Object connect() throws InterruptedException {
		chessHost = new ChessServer(port, username);

		chessHost.listen();
		
		return chessHost;
	}

}
