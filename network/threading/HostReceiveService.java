package network.threading;

import java.io.IOException;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import network.ChessDataPacket;
import network.ChessHost;

public class HostReceiveService extends Service<ChessDataPacket> {

	private ChessHost chessHost;
	
	public HostReceiveService(ChessHost ch) {
		this.chessHost = ch;
	}
	
	protected Task<ChessDataPacket> createTask() {

		return new Task<ChessDataPacket>() {

			@Override
			protected ChessDataPacket call() throws Exception {
				return chessHost.readLine();
			}
			
		};

	}
	
	public void endService() {
		try {
			chessHost.closeSocket();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Unable to close socket.");
		}
	}
}
