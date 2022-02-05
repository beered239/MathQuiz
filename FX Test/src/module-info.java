module Math_App{
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	
	opens application to javafx.graphics, javafx.fxml;
	opens settings_window to javafx.graphics, javafx.fxml;
	opens custom_element to javafx.graphics, javafx.fxml;
	
}
