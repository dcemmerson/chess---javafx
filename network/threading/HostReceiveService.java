package network.threading;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import network.ChessHost;

public class HostReceiveService extends Service<String> {

	private ChessHost chessHost;
	
	public HostReceiveService(ChessHost ch) {
		this.chessHost = ch;
	}
	
	protected Task<String> createTask() {

		return new Task<String>() {

			@Override
			protected String call() throws Exception {
				return chessHost.readLine();
			}
			
		};

	}


}
