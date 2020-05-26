package controller;

import java.io.IOException;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;
import network.ChessHost;
import network.threading.ClientConnectService;
import network.threading.HostConnectService;
import network.threading.HostReceiveService;
import network.threading.HostSendService;
import network.threading.ServerConnectService;

public class NetworkController extends Controller implements Runnable {
	public static String ENDLINE = "\n..\n";
	public static String ENDLINE_REGEX = "(" + NetworkController.ENDLINE + ")?+$";

//	private ChessClient chessClient;
//	private ChessServer chessServer;
//	private ChessHost chessHost;
		
	private MainActions mainActions;

	private HostSendService hss;
	private HostReceiveService hrs;

	private ClientConnectService ccs;
	private ServerConnectService scs;
	
	@FXML
	private TextField remoteIpAddress;
	@FXML 
	private TextField remotePort;
	@FXML 
	private TextField myPort;
	@FXML
	private Button connectButton;
	@FXML
	private TextFlow headerText;
	
	public NetworkController() {
		System.out.println("default constr");
	}
	
	public NetworkController(ChessHost ch, MainActions ma) {
		this.mainActions = ma;
		this.startNetworkServices(ch);
	}

	private void startNetworkServices(ChessHost ch) {
		this.hss = new HostSendService(ch);
		this.hrs = new HostReceiveService(ch);

		hrs.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent wse) {
				String str = (String) wse.getSource().getValue();
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

	@FXML
	public void tryConnecting(ActionEvent e) {
		connectButton.setDisable(true);
		connectButton.setText("Connecting...");
		
		ccs = new ClientConnectService(remoteIpAddress.getText().trim(), remotePort.getText().trim());
		scs = new ServerConnectService(myPort.getText().trim());
		ccs.start();
		scs.start();
		
		
		ccs.setOnSucceeded(connectionSucceeded(scs));
		scs.setOnSucceeded(connectionSucceeded(ccs));
		
		ccs.setOnFailed(connectionFailed(scs));
		scs.setOnFailed(connectionFailed(ccs));
		scs.setOnCancelled(connectionCancelled(scs));
		ccs.setOnCancelled(connectionCancelled(ccs));
	}
	
	@FXML
	public void cancelRemoteGame() {
		screen.changeScreens("start", null, true, false);
		if(ccs != null) {
			ccs.cancel();
		}
		if(scs != null) {
			scs.cancel();
		}
	}
	
	private EventHandler<WorkerStateEvent> connectionSucceeded(HostConnectService hcs) {
		return new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent wse) {
				System.out.println("succeeded in " + Thread.currentThread().getName());
				ChessHost ch = (ChessHost)wse.getSource().getValue();
				hcs.cancel();
				
				if(ch.isConnected()) {
					GameType gt;
					if(hcs.isServer()) {
						gt = new GameType(true, false, false, false);
					}
					else {
						gt = new GameType(false, true, false, false);
					}
//					startNetworkServices((ChessHost) wse.getSource().getValue());

					screen.changeScreens("Chess", gt, ch, false, false);
				}
				else {
					try {
						ch.closeSocket();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
	}
	private EventHandler<WorkerStateEvent> connectionCancelled(HostConnectService hcs) {
		return new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent wse) {
				System.out.println("cancelled in " + Thread.currentThread().getName());
				if(hcs != null) {
					hcs.closeSocket();
				}

			}
		};
	}
	
	private EventHandler<WorkerStateEvent> connectionFailed(HostConnectService hcs) {
		return new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent wse) {
				System.out.println("failed in " + Thread.currentThread().getName());
				ChessHost ch = (ChessHost)wse.getSource().getValue();
				hcs.cancel();
				
				connectButton.setDisable(false);
				connectButton.setText("Connect");
			}
		};
	}

	@Override
	public void run() {

	}
	
	public void setMainActions(MainActions ma) {
		this.mainActions = ma;
	}

}
