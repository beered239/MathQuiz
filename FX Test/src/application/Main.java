package application;
	

import custom_elements.AlertBox;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


@SuppressWarnings("unused")
public class Main extends Application {
	
	public static Stage window;
	
	//public static double defTSize;
	public static final AlertBox exitWarning = new AlertBox();
	
	//public static double height;
	
	
	//public variables?
		//loader for main app controller fxml and all (will be used to get app controller in settings controller)
			public static FXMLLoader appLoader;				//could call this or get method...
			public static FXMLLoader getAppLoader() {
				return appLoader;
			}
	
	
	@Override
	public void start(Stage primaryStage) {
		
		try {
			window = primaryStage;
			
			
			appLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
			Parent root = appLoader.load();
			
			
			//BorderPane root = new BorderPane();
			//Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));	//used with window builder and fxml

			
			
			Scene scene = new Scene(root,400,400);	//used with window builder

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());	//adds css 
			
			//defTSize = 100;
			//SimplifyFX style = new SimplifyFX();
			//root.setStyle( style.fontSize(defTSize) );
			
			//root.applyCss();
			
			window.setScene(scene);
			
			window.setFullScreen(true);
			window.setFullScreenExitHint("");
			
			window.show();
			
			//height = window.getHeight();
			//System.out.println(window.getHeight() + " " + window.getWidth());	//H: 1080 W: 1920    w: H: 439 W: 416
						
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//other stuff
		
			//exit fullscreen 
			//@FXML MenuItem exitButton;
		
		
		
		
		if(true == false) {
			System.out.println("folding works :( not");
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);

	}
	
	
	public static void exitScreen() {
		window.setFullScreen(false);
	}
	
}

/*goal: 
 * learn how to either add elements to an already made app (from fxml) but add it in the main method
 * 
 * learn how to refrence elements in the fxml file and use the listener on them
 * 
 * 
 * NOTE: found: https://code.makery.ch/blog/javafx-2-event-handlers-and-change-listeners/   -> use initialize method to add listeners  (in controller)
 * */


