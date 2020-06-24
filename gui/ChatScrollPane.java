/*	filename: ChatBoxScrollPane.java
 * 	last modified: 06/24/2020
 * 	description: Class for controlling the ScrollPane which is part
 * 					of the chat box area on chess screen.
 */

package gui;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ChatScrollPane extends BorderPane{

	private ScrollPane scrollPane;
	private TextFlow textFlow;
	
	public ChatScrollPane() {
		super();
		scrollPane = new ScrollPane();
		scrollPane.setFitToWidth(true);

		textFlow = new TextFlow();
		
		setCenter(scrollPane);
		scrollPane.setContent(textFlow);
	}
	
	public void appendText(Text text) {
		textFlow.getChildren().add(text);

		scrollPane.setVvalue(textFlow.getHeight());
	}

}
