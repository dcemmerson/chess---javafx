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
