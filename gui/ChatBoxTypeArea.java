package gui;

import java.util.ArrayList;

import controller.MainActions;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class ChatBoxTypeArea extends BorderPane {

	private TextArea textArea;
	private MainActions mainActions;
	
	public ChatBoxTypeArea(MainActions ma) {
		super();
		
		this.mainActions = ma;
		textArea = new TextArea();
		
		setCenter(textArea);
		
		textArea.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent key) {
				if(key.getCode() == KeyCode.ENTER) {
					ArrayList<String> str = new ArrayList<String>();
					str.add(textArea.getText());
					textArea.setText("");

					mainActions.appendToChatBox(str, true);
				}
			}
			
		});
		
	}
}
