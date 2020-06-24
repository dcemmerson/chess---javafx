/*	name: NetworkController.java
 *	last modified: 06/23/2020
 * 	description: Controller class for Network scene and connecting hosts
 * 					via sockets as well as sending messages and game
 * 					moves between hosts.
 */

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

	// Regular expressions used for parsing sent and received text.
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

	// Default constructor used when creating new NetworkController instance 
	// straight from start screen.
	public NetworkController() {
	}

	// Constructor used when MainController class is creating a NetworkController 
	// instance.
	public NetworkController(ChessHost ch, MainActions ma) {
		this.mainActions = ma;
		this.startNetworkServices(ch);
	}

	/*	name: endNetworkService
	 * 	description: Call this method upon needing to terminate the network
	 * 					connection for any reason, such as player leaving game.
	 * 					Method also sends message to opponent indicating that
	 * 					the connection is being terminated.
	 */
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

	/*	name: startNetworkService
	 * 	arguments: ch is a ChessHost object that has connected socket to other host.
	 * 	description: Call this method upon needing to allow hosts to communicate.
	 * 					Starts network services, a HostSendService and a
	 * 					HostReceiveService, used to send and receive between hosts.
	 * 					Upon sending/receiving info to other host, we continue to 
	 * 					restart service until we detect that game has ended or
	 * 					the network connection has been closed.
	 */
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

	/*	name: write
	 * 	arguments: ChessDataPacket cdp that we wish to write to socket
	 * 	description: Send ChessDataPacket to other host. HostSendService
	 * 					hss is in a waiting state and upon restarting hss,
	 * 					packet is written to socket.
	 */
	public void write(ChessDataPacket cdp) {
		hss.setChessDataPacket(cdp);
		hss.restart();
	}

	/*	name: tryConnecting
	 * 	description: Handles event of user click connect button on Network
	 * 					screen. Validates input, then calls connect method.
	 */
	@FXML
	public void tryConnecting(ActionEvent e) {
		clearInvalidSocketMessage();

		if (validInputs()) {
			connect();
		}
	}

	/*	name: cancelRemoteGame
	 * 	description: Handles event of user clicking cancel button on Network
	 * 					screen. Takes user back to start screen.
	 */
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
	
	/*	name: validUsername 
	 * 	description: Check to ensure user entered valid username and return true,
	 * 					else false.
	 */
	private boolean validUsername() {
		if (username.getText().trim().equals("")) {
			username.setStyle("-fx-background-color: #ff0000");
			return false;
		}
		return true;
	}

	/*	name: clearInvalidSocketMessage
	 * 	description: Removes any error messages off Network screen.
	 */
	private void clearInvalidSocketMessage() {
		myPort.setStyle("-fx-background-color: #ffffff");
		remotePort.setStyle("-fx-background-color: #ffffff");
		username.setStyle("-fx-background-color: #ffffff");
		remoteIpAddress.setStyle("-fx-background-color: #ffffff");
	}

	/*	name: validInputs
	 * 	description: Looks at user inputs on Network screen and places any
	 * 					necessary error messages on screen. Return true if
	 * 					all inputs valid, else false.
	 */
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

	/*	name: validIpAddr
	 * 	arguments: String ipAddr is string representation of ipv4 address, such as
	 * 				"123.123.123.123" for example - could also just be "localhost",
	 * 				but probably only to be used during dev.
	 * 	description: Returns true if user entered valid ipv4, else false.
	 */
	private boolean validIpAddr(String ipAddr) {
		if (ipAddr.equalsIgnoreCase("localhost")) {
			return true;
		}

		// https://stackoverflow.com/questions/5667371/validate-ipv4-address-in-java
		String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

		return ipAddr.matches(PATTERN);
	}

	/*	name: validPortNumber
	 * 	arguments: int port enterd by user.
	 * 	description: Returns true if user entered valid port number, else false. Valid
	 * 					ports are being restricted to 1024 - 65535.
	 */
	private boolean validPortNumber(int port) {
		if (port > PORT_MIN && port < PORT_MAX) {
			return true;
		} else {
			return false;
		}
	}

	/*	name: connect
	 * 	description: After validating user input on Network screen, we call this method
	 *	 				which then attempts to connect to host entered by user. Form 
	 *					displayed to user is disabled and we attempt to connect to 
	 *					other host. We will continue trying to connect to other host
	 *					until user chooses cancel button on screen (handled by
	 *					cancelRemoteGame above), or we establish a connection with other
	 *					host, or socket interface detects an error in trying to make
	 *					connection.
	 *
	 *					Note: We are both attempting to connect to other host as a server
	 *							and we are listening for a connection being established 
	 *							as if we are the server. Only one of these events will
	 *							succeed, meaning either ccs or scs will be canceled and the 
	 *							other will succeed on a valid connection. Succeed/canceled
	 *							events called within this method.
	 */
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

	/*	name: connectionSucceeded
	 * 	arguments: HostConnectService hcs should be HostSendService or HostReceiveService
	 * 				object that has established a connection to other host.
	 * 	description: Returns a method that is called after connect service succeeds.
	 * 					Method then fills GameType object and calls the
	 * 					screen.changeScreen method which it passed the GameType object.
	 * 					Upon failing, close socket.
	 */
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

	/*	name: connectionCancelled
	 * 	arguments: HostConnectService hcs should be HostSendService or HostReceiveService
	 * 				object that did not actually connect to other host.
	 * 	description: Returns a method that is called after connect service is cancelled.
	 * 					Ensure socket is closed.
	 */
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

	/*	name: connectionFailed
	 * 	arguments: HostConnectService hcs should be HostSendService or HostReceiveService
	 * 				object that encountered error when trying to connect.
	 * 	description: Should be rare this handler is used, but we catch the event and
	 * 					log it, followed by allowing user to try to reconnect.
	 */
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

	/*	name: setMainActions
	 * 	description: Setter type method used to set MainActions in NetworkController,
	 * 					MainActions is interface used for communication back to 
	 * 					MainController class (in this case at least).
	 */
	public void setMainActions(MainActions ma) {
		this.mainActions = ma;
	}

}
