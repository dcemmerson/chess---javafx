package controller;

import java.io.IOException;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import network.ChessDataPacket;
import network.ChessHost;
import network.threading.ClientConnectService;
import network.threading.HostConnectService;
import network.threading.HostReceiveService;
import network.threading.HostSendService;
import network.threading.ServerConnectService;

public class NetworkController extends Controller implements Runnable {
	static int PORT_MIN = 1024;
	static int PORT_MAX = 65535;

	public static String ENDLINE = "\n..\n";
	public static String CHAT_ENDLINE = "\n.CHAT.\n";
	public static String CHESSMOVE_ENDLINE = "\n.CHESSMOVE.\n";
	public static String ENDLINE_REGEX = "(" + NetworkController.ENDLINE + ")?+$";
	public static String CHAT_ENDLINE_REGEX = "(" + NetworkController.CHAT_ENDLINE + ")?+$";
	public static String CHESSMOVE_ENDLINE_REGEX_ = "(" + NetworkController.CHESSMOVE_ENDLINE + ")?+$";

	private MainActions mainActions;

	private HostSendService hss;
	private HostReceiveService hrs;

	private ClientConnectService ccs;
	private ServerConnectService scs;

	@FXML
	private TextField username;
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
	}

	public NetworkController(ChessHost ch, MainActions ma) {
		this.mainActions = ma;
		this.startNetworkServices(ch);
	}

	public void endNetworkServices() {
		this.hrs.endService();
		
		ChessDataPacket cdp = new ChessDataPacket();
		cdp.setOpponentResigned(true);
		this.write(cdp);
		
		hss.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent wse) {
				hss.endService();

			}
		});

	}

	private void startNetworkServices(ChessHost ch) {
		this.hss = new HostSendService(ch);
		this.hrs = new HostReceiveService(ch);

		hrs.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent wse) {
				ChessDataPacket cdp = (ChessDataPacket) wse.getSource().getValue();
				mainActions.receiveChessDataPacket(cdp);

				if(!cdp.connectionLost() && !cdp.isOpponentResigned()) {
					hrs.restart();
				}
				else { //close socket and end services if running
					hrs.endService();
					hss.endService();
				}
			}

		});

		hss.start();
		hrs.start();
	}

	public void write(ChessDataPacket cdp) {
		hss.setChessDataPacket(cdp);
		hss.restart();
	}

	@FXML
	public void tryConnecting(ActionEvent e) {
		clearInvalidSocketMessage();

		if (validInputs()) {
			connect();
		}
	}

	@FXML
	public void cancelRemoteGame() {
		screen.changeScreens("Start", null, true, false);
		if (ccs != null) {
			ccs.cancel();
		}
		if (scs != null) {
			scs.cancel();
		}
	}

	private boolean validUsername() {
		if (username.getText().trim().equals("")) {
			username.setStyle("-fx-background-color: #ff0000");
			return false;
		}
		return true;
	}

	private void clearInvalidSocketMessage() {
		myPort.setStyle("-fx-background-color: #ffffff");
		remotePort.setStyle("-fx-background-color: #ffffff");
		username.setStyle("-fx-background-color: #ffffff");
		remoteIpAddress.setStyle("-fx-background-color: #ffffff");
	}

	private boolean validInputs() {
		boolean mPortVal = false;
		boolean rPortVal = false;
		boolean ipAddrVal = false;

		try {
			if (validPortNumber(Integer.parseInt(myPort.getText().trim()))) {
				mPortVal = true;
			} else {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			mPortVal = false;
			myPort.setStyle("-fx-background-color: #f00000");
		}

		try {
			if (validPortNumber(Integer.parseInt(remotePort.getText().trim()))) {
				rPortVal = true;
			} else {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			mPortVal = false;
			remotePort.setStyle("-fx-background-color: #f00000");
		}

		if (validIpAddr(remoteIpAddress.getText().trim())) {
			ipAddrVal = true;
		} else {
			remoteIpAddress.setStyle("-fx-background-color: #f00000");
		}

		return validUsername() && mPortVal && rPortVal && ipAddrVal;
	}

	private boolean validIpAddr(String ipAddr) {
		if (ipAddr.equalsIgnoreCase("localhost")) {
			return true;
		}

		// https://stackoverflow.com/questions/5667371/validate-ipv4-address-in-java
		String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

		return ipAddr.matches(PATTERN);
	}

	private boolean validPortNumber(int port) {
		if (port > PORT_MIN && port < PORT_MAX) {
			return true;
		} else {
			return false;
		}
	}

	private void connect() {
		connectButton.setDisable(true);
		connectButton.setText("Connecting...");
		username.setDisable(true);
		remoteIpAddress.setDisable(true);
		remotePort.setDisable(true);
		myPort.setDisable(true);

		String un = username.getText().trim();

		ccs = new ClientConnectService(remoteIpAddress.getText().trim(), remotePort.getText().trim(), un);
		scs = new ServerConnectService(myPort.getText().trim(), un);
		ccs.start();
		scs.start();

		ccs.setOnSucceeded(connectionSucceeded(scs));
		scs.setOnSucceeded(connectionSucceeded(ccs));

		ccs.setOnFailed(connectionFailed(scs));
		scs.setOnFailed(connectionFailed(ccs));
		scs.setOnCancelled(connectionCancelled(scs));
		ccs.setOnCancelled(connectionCancelled(ccs));
	}

	private EventHandler<WorkerStateEvent> connectionSucceeded(HostConnectService hcs) {
		return new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent wse) {
				System.out.println("succeeded in " + Thread.currentThread().getName());
				ChessHost ch = (ChessHost) wse.getSource().getValue();
				hcs.cancel();

				if (ch.isConnected()) {
					GameType gt;
					if (hcs.isServer()) {
						gt = new GameType(true, false, false, false);
					} else {
						gt = new GameType(false, true, false, false);
					}

					screen.changeScreens("Chess", gt, ch, true, true);
				} else {
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
				if (hcs != null) {
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
