package network.threading;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import network.ChessHost;

public class HostSendService extends Service<Object> {

	private ChessHost chessHost;
	private String msg;
	
	public HostSendService(ChessHost ch) {
		this.chessHost = ch;
	}
	
	@Override
	protected Task<Object> createTask() {

		return new Task<Object>() {

			protected Object call() throws Exception {
				if(msg != null) {
					chessHost.write(msg);
				}
				
				msg = null;
				
				return null;
			}
			
		};

	}

	public void setText(String str) {
		this.msg = str;
	}
	
}
