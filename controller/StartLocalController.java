/*	filename: StartLocalController.java
 * 	last modified: 06/23/2020
 * 	description: Controller class for user starting a local game.
 */

package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class StartLocalController extends Controller implements Initializable {

	@FXML
	private Button startLocalButton;
	@FXML
	private Button startRemoteButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	/*	name: handleStartCpuVsCpuButton
	 * 	description: Handle event of user clicking to start a cpu vs cpu game.
	 */
	@FXML
	private void handleStartCpuVsCpuButton(ActionEvent event) {
		GameType gt = new GameType(true, true, true, true);
		screen.changeScreens("Chess", gt, false, false);
	}

	/*	name: handleStartLocal1PlayerButton
	 * 	description: Handle event of user clicking to start a local vs cpu game.
	 */
	@FXML
	private void handleStartLocal1PlayerButton(ActionEvent event) {
		GameType gt = new GameType(true, true, false, true);
		screen.changeScreens("Chess", gt, false, false);
	}
	
	/*	name: handleStartLocalButton
	 * 	description: Handle event of user clicking to start a local vs local game.
	 */
	@FXML
	private void handleStartLocalButton(ActionEvent event) throws IOException {		
		GameType gt = new GameType(true, true, false, false);
		screen.changeScreens("Chess", gt, false, false);	
	}

	/*	name: handleGoBackButton
	 * 	description: Handle event of user choosing to go back to main start screen.
	 */
	@FXML
	private void handleGoBackButton(ActionEvent event) throws IOException {		
		screen.changeScreens("Start", null, false, false);	
	}
}
