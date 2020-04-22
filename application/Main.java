package application;
	
import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainWindow.fxml"));
			Parent root = loader.load();
			MainController controller = loader.getController();
//			controller.setStage(primaryStage);
			controller.initialize(primaryStage, root);
			
			Scene scene = new Scene(root,600,500);
			scene.getStylesheets().add("/fxml/styles/mainStyles.css");
			primaryStage.setScene(scene);
			primaryStage.setTitle("Chess");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
