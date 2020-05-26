package network.threading;

import java.io.IOException;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import network.ChessHost;

public class HostConnectService extends Service<Object> {
	
	protected ChessHost chessHost;
	protected String port;
	
	@Override
	protected Task<Object> createTask() {
		return new Task<Object>() {

			@Override
			protected Object call() throws Exception {
				return connect();
			}	
		};
	}
	
	protected Object connect() throws InterruptedException {
		System.out.println("Wrong connect - define in child class.");
		return null;
	}
	
	public void closeSocket() {
		try {
			chessHost.closeSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
}

