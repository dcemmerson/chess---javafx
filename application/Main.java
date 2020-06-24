/*	filename: Main.java
 * 	description: Contains entry point to application. Loads up screen controller
 * 					and passes necessary files or file paths depending upon 
 * 					options set.
 */
package application;
	
import java.io.IOException;

import controller.ScreenController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {
	private ScreenController sc;
	
	/*	name: start
	 * 	description: Entry point for application GUI.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.getIcons().add(new Image("gui/images/pawn_black.png"));
			sc = new ScreenController(primaryStage);
			sc.addScreenActive("Start", "/fxml/startWindow.fxml", "/fxml/styles/mainStyles.css");
			
			sc.activate("Start", null);

			loadScenes();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 	name: loadScenes
	 * 	description: Loads scene names and file paths to assets required for creating
	 * 					scenes into ScreenController sc object for simple access
	 * 					at later point in application.
	 */
	public void loadScenes() throws IOException {
		//chess main screen
		
		sc.addScreenInactive("Chess", "/fxml/chessWindow.fxml", "/fxml/styles/mainStyles.css");
		sc.addScreenInactive("StartLocal", "/fxml/startLocalWindow.fxml", "/fxml/styles/mainStyles.css");
		sc.addScreenInactive("Network", "/fxml/networkWindow.fxml", "/fxml/styles/mainStyles.css");

	}
	
	/*	name: main
	 * 	description: Main entry point for application.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
