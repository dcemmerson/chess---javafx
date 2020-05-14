package application;
	
import java.io.IOException;

import controller.ScreenController;
import controller.StartController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	private ScreenController sc;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			sc = new ScreenController(primaryStage);
			sc.addScreen("start", "/fxml/startWindow.fxml", "/fxml/styles/mainStyles.css");
			
			sc.activate("start");

			loadScenes();

			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void loadScenes() throws IOException {
		//chess main screen
		
		sc.addScreen("Chess", "/fxml/mainWindow.fxml", "/fxml/styles/mainStyles.css");
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
