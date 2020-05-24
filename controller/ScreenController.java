package controller;

import java.io.IOException;
import java.util.HashMap;

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
			public void changeScreens(String name, GameType args, boolean destroy, boolean recreate) {
				String prevTitle = stage.getTitle();
				SceneInfo prevScene = screenMap.get(prevTitle);

				
				try {
					activate(name, args);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if(destroy) {
					removeScreen(prevTitle);
		
				}
				
				if(recreate) {
					try {
						addScreenActive(prevTitle, prevScene.getFxmlPath(), prevScene.getStylesheetPath());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else {
					addScreenInactive(prevTitle, prevScene.getFxmlPath(), prevScene.getStylesheetPath());

				}
			}
        };
    }

    public void addScreenInactive(String name, String fxml, String stylesheet) {
        screenMap.put(name, new SceneInfo(name, stylesheet, fxml, null));
    }
    
    public void addScreenActive(String name, String fxml, String stylesheet) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
		Parent root = loader.load();
		Controller controller = loader.getController();
		controller.initialize(stage, screen, null);
		
		Scene scene = new Scene(root);
		
		scene.getStylesheets().add(stylesheet);
		
        screenMap.put(name, new SceneInfo(name, stylesheet, fxml, scene));
    }
    
    public void removeScreen(String name){
        screenMap.remove(name);
    }

    public void activate(String name, GameType args) throws IOException{
    	Scene scene = screenMap.get(name).getScene();
    	if(scene == null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(screenMap.get(name).getFxmlPath()));
			Parent root = loader.load();
			Controller controller = loader.getController();
			controller.initialize(stage, screen, args);
			
			scene = new Scene(root);
	    	scene.getStylesheets().add(screenMap.get(name).getStylesheetPath());
	    	
			screenMap.get(name).setScene(scene);
    	
    	}

		stage.setScene(scene);
		stage.setTitle(name);
		stage.show();    
    }
}
