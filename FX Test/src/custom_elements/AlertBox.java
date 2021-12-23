package custom_elements;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AlertBox {

	public AlertBox() {
		//no
	}
	
	public void create(String message, String title) {
		Stage window = new Stage();
		window.setTitle(title);
		HBox hBox = new HBox();
		Button btn = new Button(message);
		btn.setOnAction(e -> window.close());
		btn.setStyle("-fx-background-color: transparent; "
				+ "-fx-text-fill: white;"
				+ "-fx-border-color:black;");
		hBox.getChildren().add(btn);
		hBox.setAlignment(Pos.CENTER);
		hBox.setStyle("-fx-background-color: grey;");
		Scene scene = new Scene(hBox);	//edit sizes based off of window and message later?
		window.setScene(scene);
		window.show();
		
	}
	
	//note: maybe instead make a display or create method with methods that return alert boxes, textfields etc
		//would be hard with more specific things nm..
			//but: this uses normal elements with certain aspects not custom/subclass elements -> those can't use a display or create class
	
}
