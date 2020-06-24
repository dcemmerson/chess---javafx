/*	filename: StartController.java
 *	last modified: 06/23/2020
 * 	description: Controller class for start scene.
 */

package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class StartController extends Controller implements Initializable {

	@FXML
	private Button startLocalButton;
	@FXML
	private Button startRemoteButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

	/*	name: handleStartRemoteButton
	 * 	description: Handle event of user clicking to start a remote game.
	 * 					Pass over screen name "Network", destroy=false,
	 * 					and recreate=false to screen.changeScreens.
	 */
	@FXML
	private void handleStartRemoteButton(ActionEvent event) {
		screen.changeScreens("Network", false, false);
	}

	/*	name: handleStartRemoteButton
	 * 	description: Handle event of user clicking to start a local game.
	 * 					Pass over screen name "StartLocal", gametype=null,
	 * 					 destroy=false, and recreate=false to screen.changeScreens.
	 */
	@FXML
	private void handleStartLocalButton(ActionEvent event) throws IOException {		
		screen.changeScreens("StartLocal", null, false, false);		
	}
}
