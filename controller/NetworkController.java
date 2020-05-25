package controller;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import network.ChessHost;
import network.threading.ClientConnectService;
import network.threading.HostReceiveService;
import network.threading.HostSendService;
import network.threading.ServerConnectService;

public class NetworkController {
//	private ChessClient chessClient;
//	private ChessServer chessServer;
//	private ChessHost chessHost;
	private MainActions mainActions;
	
	private HostSendService hss;
	private HostReceiveService hrs;
	
	public NetworkController(MainActions ma) {
		this.mainActions = ma;

		ClientConnectService cs = new ClientConnectService("localhost", "8011");
		ServerConnectService ss = new ServerConnectService("8010");
		cs.start();
		ss.start();

		cs.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent wse) {
				System.out.println("cs succeeded in " + Thread.currentThread().getName());
				ChessHost ch = (ChessHost)wse.getSource().getValue();
				startNetworkServices((ChessHost) wse.getSource().getValue());
				ss.cancel();
			}

		});

		ss.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent wse) {
				System.out.println("ss succeeded in " + Thread.currentThread().getName());
				startNetworkServices((ChessHost) wse.getSource().getValue());
				cs.cancel();
			}

		});

	}
	
	private void startNetworkServices(ChessHost ch) {
		this.hss = new HostSendService(ch);
		this.hrs = new HostReceiveService(ch);
		
		hrs.setOnSucceeded(new EventHandler<WorkerStateEvent> (){

			@Override
			public void handle(WorkerStateEvent wse) {
				String str = (String)wse.getSource().getValue();
				mainActions.receiveText(str);
				hrs.restart();
			}
			
		});
		
		hss.start();
		hrs.start();
	}
	
	public void write(String str) {
		hss.setText(str);
		hss.restart();
	}
	

}
