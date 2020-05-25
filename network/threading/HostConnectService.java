package network.threading;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class HostConnectService extends Service<Object> {
	
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
		
}

