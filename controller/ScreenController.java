package controller;

import java.io.IOException;
import java.util.HashMap;

import application.ChangeScreen;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScreenController {
	
    private HashMap<String, SceneInfo> screenMap = new HashMap<>();
    private Stage stage;
    private ChangeScreen screen;
    
    public ScreenController(Stage stage) {
        this.stage = stage;
        
        this.screen = new ChangeScreen() {

			@Override
			public void changeScreens(String name, boolean destroy, boolean recreate) {
				String prevTitle = stage.getTitle();
				SceneInfo prevScene = screenMap.get(prevTitle);

				activate(name);

				if(destroy) {
					removeScreen(prevTitle);
		
				}
				
				if(recreate) {
					try {
						addScreen(prevTitle, prevScene.getFxmlPath(), prevScene.getStylesheetPath());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
        };
    }

    public void addScreen(String name, String fxml, String stylesheet) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
		Parent root = loader.load();
		Controller controller = loader.getController();
		controller.initialize(stage, screen);
		
		Scene scene = new Scene(root);
		
		scene.getStylesheets().add(stylesheet);
		
        screenMap.put(name, new SceneInfo(name, stylesheet, fxml, scene));
    }

    public void removeScreen(String name){
        screenMap.remove(name);
    }

    public void activate(String name){
    	Scene scene = screenMap.get(name).getScene();
		stage.setScene(scene);
		stage.setTitle(name);
		stage.show();    
    }
}
