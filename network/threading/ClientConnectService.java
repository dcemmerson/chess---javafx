package network.threading;

import network.ChessClient;

public class ClientConnectService extends HostConnectService {
	
	private String hostname;

	public ClientConnectService(String hostname, String port) {
		this.port = port;
		this.hostname = hostname;
	}
	
	public Object connect() throws InterruptedException {
		ChessClient chessClient = new ChessClient(hostname, port);
			
		while(!(chessClient.connect())) {
			System.out.println("no connection - sleep 2000 - " + Thread.currentThread().getName());
			Thread.sleep(2000);
			
		}
		return chessClient;
	}
}
