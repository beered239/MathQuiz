package settings_window;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launch_Settings {

	public Launch_Settings() {
		//dsfs
	}
	
	public static Stage window;
	
	public void create() {
		window = new Stage();
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("settingsXML.fxml"));
			Scene scene = new Scene(root,300,190);
			
			//Scene scene = new Scene(new HBox(), 23,23);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());	//loads fine
			window.setScene(scene);
			
			window.showAndWait();
			
			
		} catch(Exception e) {e.printStackTrace();}
		
		window.setTitle("Settings");
		
	}
	
}
