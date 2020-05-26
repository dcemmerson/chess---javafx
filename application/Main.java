package application;
	
import java.io.IOException;

import controller.ScreenController;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
	private ScreenController sc;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			sc = new ScreenController(primaryStage);
			sc.addScreenActive("start", "/fxml/startWindow.fxml", "/fxml/styles/mainStyles.css");
			
			sc.activate("start", null);

			loadScenes();

			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void loadScenes() throws IOException {
		//chess main screen
		
		sc.addScreenInactive("Chess", "/fxml/chessWindow.fxml", "/fxml/styles/mainStyles.css");
		sc.addScreenInactive("Network", "/fxml/networkWindow.fxml", "/fxml/styles/mainStyles.css");

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
