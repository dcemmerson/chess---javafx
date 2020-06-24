/*	filename: ScreenController.java
 *	last modified: 06/23/2020
 * 	description: Controller class for changing screens. Uses hashmap to
 * 					keep track of screen. Defines ChangeScreen interface
 * 					methods, which are then passed to the controllers for
 * 					scenes and used to change screens on demand.
 */

package controller;

import java.io.IOException;
import java.util.HashMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.ChessHost;

public class ScreenController {

	private HashMap<String, SceneInfo> screenMap = new HashMap<>();
	private Stage stage;
	private ChangeScreen screen;

	public ScreenController(Stage stage) {
        this.stage = stage;
        
        this.screen = new ChangeScreen() {
        	/*	name: changeScreens
        	 * 	arguments:	name - String name of screen to activate and switch to
        	 * 				destroy - boolean indicating whether or not to dispose of
        	 * 							the current scene.
        	 * 				recreate - boolean indicating whether or not to recreate
        	 * 							current scene if being disposed.
        	 * 	description: Interface method allowing other scene controllers to simply
        	 * 					switch scenes by calling this method.
        	 */
        	 @Override
 	        public void changeScreens(String name, boolean destroy, boolean recreate) {
 	        	String prevTitle = stage.getTitle();
 	        	SceneInfo prevScene = screenMap.get(prevTitle);
 	
 	        	try {
 	        		activate(name);
 	        	} catch (IOException e1) {
 					e1.printStackTrace();
 				}
 		
 				if (destroy) {
 					removeScreen(prevTitle);
 		
 				}
 		
 				if (recreate) {
 					try {
 						addScreenActive(prevTitle, prevScene.getFxmlPath(), prevScene.getStylesheetPath());
 					} catch (IOException e) {
 						e.printStackTrace();
 					}
 				} else {
 					addScreenInactive(prevTitle, prevScene.getFxmlPath(), prevScene.getStylesheetPath());
 		
 				}
 			}
        	 
         	/*	name: changeScreens
         	 * 	arguments:	Same arguments as previous overloaded changeScreen method, with additional
         	 * 				GameType argument, containing flags fo which type of game we will be
         	 * 				starting (local, remote, etc).
         	 * 	description: Interface method allowing other scene controllers to simply
         	 * 					switch scenes by calling this method.
         	 */
			@Override
			public void changeScreens(String name, GameType args, boolean destroy, boolean recreate) {
				String prevTitle = stage.getTitle();
				SceneInfo prevScene = screenMap.get(prevTitle);
				
				try {
					activate(name, args);
				} catch (IOException e1) {
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

         	/*	name: changeScreens
         	 * 	arguments:	Same arguments as previous overloaded changeScreen method, with additional
         	 * 				ChessHost argument, containing connected socket to remote host for
         	 * 				sending text over socket as well as sending objects used to make moves
         	 * 	description: Interface method allowing other scene controllers to simply
         	 * 					switch scenes by calling this method
         	 * 					Generally would be used to switch from Network scene to Chess scene.
         	 */
			@Override
			public void changeScreens(String name, GameType args, ChessHost ch, boolean destroy, boolean recreate) {
				String prevTitle = stage.getTitle();
				SceneInfo prevScene = screenMap.get(prevTitle);
				
				try {
					activate(name, args, ch);
				} catch (IOException e1) {
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

	/*	name: addScreenInactive
	 * 	arguments:	name - String name by which we will store screen in hashmap.
	 * 						Can be anything which we will later use to change to
	 * 						that screen.
	 * 				fxml - String path to fxml file for this screen. Best to be
	 * 						a relative path.
	 * 				stylesheet - String path to stylesheet used for this screen.
	 * 	description: Place scene info in SceneInfo object for later access/creation
	 * 					of scene. Does not create scene here. If creating a scene
	 * 					if resource intensive, or we do not necessarily want to create
	 * 					the scene until user makes some choices, use this method.
	 */
	public void addScreenInactive(String name, String fxml, String stylesheet) {
		screenMap.put(name, new SceneInfo(name, stylesheet, fxml, null));
	}

	/*	name: addScreenActive
	 * 	arguments:	Same arguments as addScreenInactive
	 * 	description: Creates scene.
	 * 				 Place scene info in SceneInfo object for later access
	 * 					of scene.
	 */	
	public void addScreenActive(String name, String fxml, String stylesheet) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
		Parent root = loader.load();
		Controller controller = loader.getController();
		controller.initialize(stage, screen, null);

		Scene scene = new Scene(root);

		scene.getStylesheets().add(stylesheet);

		screenMap.put(name, new SceneInfo(name, stylesheet, fxml, scene));
	}

	/*	name: removeScreen
	 * 	description: Remove screen from hashmap.
	 */
	public void removeScreen(String name) {
		screenMap.remove(name);
	}

	/*	name: activate
	 * 	arguments:	name - name of screen in hashmap to create and change to
	 * 	description: Find name of screen in hashmap, load fxml and stylesheet
	 * 					and change to that scene.
	 */
	public void activate(String name) throws IOException {
		Scene scene = screenMap.get(name).getScene();
		if (scene == null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(screenMap.get(name).getFxmlPath()));
			Parent root = loader.load();
			Controller controller = loader.getController();
			controller.initialize(stage, screen);

			scene = new Scene(root);
			scene.getStylesheets().add(screenMap.get(name).getStylesheetPath());

			screenMap.get(name).setScene(scene);

		}

		stage.setScene(scene);
		stage.setTitle(name);
		stage.show();
	}
	
	/*	name: activate
	 * 	arguments:	name - name of screen in hashmap to create and change to
	 * 				args - GameType object which contains flags for starting a
	 * 						new chess game.
	 * 	description: Find name of screen in hashmap, load fxml and stylesheet
	 * 					and change to that scene. Call this activate method
	 * 					when switching to the chess game scene and passing in
	 * 					arguments for game type (eg local, etc).
	 */
	public void activate(String name, GameType args) throws IOException {
		Scene scene = screenMap.get(name).getScene();
		if (scene == null) {
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
	
	/*	name: activate
	 * 	arguments:	name - name of screen in hashmap to create and change to
	 * 				args - GameType object which contains flags for starting a
	 * 						new chess game.
	 * 				ch	 - ChessHost object created by NetworkController class.
	 * 	description: Find name of screen in hashmap, load fxml and stylesheet
	 * 					and change to that scene. Call this activate method
	 * 					when switching to the chess game scene and passing in
	 * 					arguments for game type (eg remote, etc).
	 */
	public void activate(String name, GameType args, ChessHost ch) throws IOException {
		Scene scene = screenMap.get(name).getScene();
		if (scene == null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(screenMap.get(name).getFxmlPath()));
			Parent root = loader.load();
			Controller controller = loader.getController();
			controller.initialize(stage, screen, args, ch);

			scene = new Scene(root);
			scene.getStylesheets().add(screenMap.get(name).getStylesheetPath());

			screenMap.get(name).setScene(scene);

		}

		stage.setScene(scene);
		stage.setTitle(name);
		stage.show();
	}

}
