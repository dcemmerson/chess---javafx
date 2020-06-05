package gui;

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
	private String username;
	
	public ChatBoxTypeArea(MainActions ma, boolean isWhite, String username) {
		super();
		
		this.mainActions = ma;
		this.white = isWhite;
		this.username = username;
		textArea = new TextArea();
		
		setCenter(textArea);
				
		textArea.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent key) {
				if(key.getCode() == KeyCode.ENTER) {
					if(username != null) {
						mainActions.appendToChatBox(username + "(me): " + textArea.getText() + "\n", white);
					}
					else {
						mainActions.appendToChatBox(textArea.getText() + "\n", white);
					}
					mainActions.sendText(textArea.getText());

					textArea.clear();
					System.out.println("ta: " + textArea.getText());
					
				}
			}
			
		});
		
	}
	
	public TextArea getTextArea() {
		return this.textArea;
	}

}
