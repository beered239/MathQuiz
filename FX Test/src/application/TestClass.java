package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class TestClass {

	public static Stage wind;
	
	public TestClass() {
		
	}
	

	@FXML static Button btn;
	
	public static void send() {
		System.out.println("would send");
		btn = new Button();
		btn.setOnAction(e -> System.out.println("yo"));
	}
	
}
