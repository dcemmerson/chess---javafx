package controller;

import javafx.scene.Scene;

public class SceneInfo {
	private Scene scene;
	private String name;
	private String stylesheetPath;
	private String fxmlPath;
	
	public SceneInfo(String name, String stylesheetPath, String fxmlPath, Scene scene){
		this.scene = scene;
		this.name = name;
		this.fxmlPath = fxmlPath;
		this.stylesheetPath = stylesheetPath;
	}

	public Scene getScene() {
		return scene;
	}
	public void setScene(Scene scene) {
		this.scene = scene;
	}


	public String getName() {
		return name;
	}

	public String getStylesheetPath() {
		return stylesheetPath;
	}

	public String getFxmlPath() {
		return fxmlPath;
	}
}
