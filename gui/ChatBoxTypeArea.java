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
	private boolean white;
	
	public ChatBoxTypeArea(MainActions ma, boolean isWhite) {
		super();
		
		this.mainActions = ma;
		this.white = isWhite;
		textArea = new TextArea();
		
		setCenter(textArea);
		
		textArea.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent key) {
				if(key.getCode() == KeyCode.ENTER) {
					ArrayList<String> str = new ArrayList<String>();
					str.add(textArea.getText() + "\n");
					textArea.setText("");

					mainActions.appendToChatBox(str, white);
				}
			}
			
		});
		
	}

}
