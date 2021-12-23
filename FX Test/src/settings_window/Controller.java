package settings_window;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.function.UnaryOperator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.AnchorPane;
import quizGen.Boot;
import quizGen.Filter;

public class Controller {

	public static final Filter filter123 = new Filter();
	
	public void initialize() {
		
		//create custom textfields
			//decimal format
				DecimalFormat dFormat = new DecimalFormat("#.0");	//confused about the parameter (doesn't seem to make a difference)
				//NumberFormat iFormat = new NumberFormat("#");
				
				
			//text formatter using unary operator
				UnaryOperator<Change> filter = change -> {
					String text = change.getControlNewText();//change.getText();
					if(text.isEmpty())
						return change;
					
					//if((filter123.fixD(text)+"").matches(text)) {	return change;}
					
					if(text.length() > 5) {return null;}	//character limiter
					
					//int formatter
						if(text.matches("[0-9]*")) 	{return change;}				//my decimal+negative formatter  ->  text.matches("[0-9/.-]*")
						
						
						return null;
					
					//decimal formatter
						//ParsePosition parsePosition = new ParsePosition(0);
						//Object obj = dFormat.parseObject(text,parsePosition);					
						//if(obj == null || parsePosition.getIndex() < text.length()) {return null;}
							//else {return change;}
					
				};
				/*Some notes:
				 * 		regex * means that there is no limit to how many characters you can have from that [] range? -> match 0 or more []'s
				 * 		
				 * */
				//add the formatter to each textfield
					TextFormatter<String> textFormatter = new TextFormatter<>(filter);
					TextFormatter<String> textFormatter2 = new TextFormatter<>(filter);
					TextFormatter<String> textFormatter3 = new TextFormatter<>(filter);
					TextFormatter<String> textFormatter4 = new TextFormatter<>(filter);
					//add formatter to the textfields
						tfNumOfA.setTextFormatter(textFormatter);
						tfNumOfS.setTextFormatter(textFormatter2);
						tfNumOfM.setTextFormatter(textFormatter3);
						tfNumOfD.setTextFormatter(textFormatter4);
				//initialize each textfield with the current values (can get this from file or saved values array in GlobalS) choose: file, b/c boot is no longer initialized here
					//note: makes GlobalS reread the setting file
						setTfText();
						
		
		//add gridpanes to the anchorpanes
			
			//note: save current scores in scorebox first before ini?
						
			try {
				//Boot.initialize();
				Boot.updateSettingObjects();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			clearAll();	//might not actually be needed
			
				//System.out.println("I've been called");//debug
			//gPane.getChildren().add(Boot.globalSettingsPane);	//will be made here (using scenebuilder)
				
				
			aPane.getChildren().add(Boot.AdditionS.settingsGrid);
			sPane.getChildren().add(Boot.SubtractionS.settingsGrid);
			mPane.getChildren().add(Boot.MultiplicationS.settingsGrid);
			dPane.getChildren().add(Boot.DivisionS.settingsGrid);
		
			//Display.arrayS(Boot.AdditionS.values);	//debug
			
			//initialize other settings based on files
				//cosmetics pane
					cBoxHighlightTF.selectedProperty().set(	Boot.GlobalS.interpretValueB(Boot.GlobalS.values.get(4)));	
					cBoxDisplayPercentages.selectedProperty().set(	Boot.GlobalS.interpretValueB(Boot.GlobalS.values.get(5)));
						//cBoxHighlightTF.selectedProperty().set(true);
						//Display.arrayS(Boot.GlobalS.values);
						//Display.arrayS(Boot.GlobalS.names);
						//System.out.println("Value was: " + Boot.GlobalS.values.get(5));		//basically getting it straight from the file
			//other listeners
				//setup connection btw app controller and setting controller
					application.Controller appController = application.Main.getAppLoader().getController();
					
				//cosmetics pane (changes txt and ram)
					cBoxHighlightTF.selectedProperty().addListener((observable, oldValue, newValue) -> {
						String showS = newValue.toString();
						appController.changeHighlightB(showS);
						
					});
					
					cBoxDisplayPercentages.selectedProperty().addListener((observable, oldValue, newValue) -> {
						String showBox = newValue.toString();
						appController.changeDisplayB(showBox);
					});
					
			
			
			/*important note: settings have to be updated through the file (required not optional) 
			 * this is because of the way that the operator settings were built to get their settings.
			 * info from the text file (and be initialized with the initialize boot function) 
			 * 
			 * note: we have to get file txt data the first time regardless, but it shouldn't be required the second time
			 * 
			 * next time: ini method gets data to ram -> open app reads data -> open settings uses already existing ram data -> all panels use controller -> setting changes change ram or (ram and txt file)
			 * 		- NO INI every time settings opens up
			 * */
	}
	
	public void clearAll() {
		//gPane.getChildren().clear();
		aPane.getChildren().clear();
		sPane.getChildren().clear();
		mPane.getChildren().clear();
		dPane.getChildren().clear();
	}
	
	void setTfText() {
		//updates GlobalS in the process
		try {
			//Boot.initialize();
			Boot.updateSettingObjects();
		} 
			catch (IOException e1) {e1.printStackTrace();}
		
		tfNumOfA.setText(Boot.GlobalS.values.get(0));
		tfNumOfS.setText(Boot.GlobalS.values.get(1));
		tfNumOfM.setText(Boot.GlobalS.values.get(2));
		tfNumOfD.setText(Boot.GlobalS.values.get(3));
	}
	
	@FXML ButtonBar buttonBar;
	
	
	@FXML AnchorPane gPane;
	@FXML AnchorPane aPane;
	@FXML AnchorPane sPane; 
	@FXML AnchorPane mPane; 
	@FXML AnchorPane dPane;
	
	
	//textfields
		@FXML TextField tfNumOfA;
		@FXML TextField tfNumOfS;
		@FXML TextField tfNumOfM;
		@FXML TextField tfNumOfD;
		
	//submit button:
		@FXML Button saveGlobalSButton;
		
	
	/**@apiNote updates the global settings file with the new values for the amount of questions (only updates num of questions in text file)*/
		@FXML 
		void updateGSettings() {
			//sdsf
			int[] indexes = {0,1,2,3};
			try {
				Boot.GlobalS.changeSettingsLines(indexes, createValueArray());
			} 		
				catch (IOException e) {e.printStackTrace();}
			
		}
		
		
		
		@FXML Label aText;
		@FXML Label sText;
		@FXML Label mText;
		@FXML Label dText;
		
		
		/**@apiNotecreates values array for global settings*/
		String[] createValueArray(){
			String[] valuesA = {tfNumOfA.getText(), tfNumOfS.getText(),tfNumOfM.getText(),tfNumOfD.getText(),};
			return valuesA;
		}
		
		
		//other FX ID's
			//panes:
				//cosmetics
					@FXML CheckBox cBoxHighlightTF;
					@FXML CheckBox cBoxDisplayPercentages;
					
					
		
		
}
	
//structure to get the data 

/*
 	note: separate the boot class into more digestible and movable objects --> before doing that test out and see if the boot class can create multiple quizzes one after the other. 
 	
 	get setting information for each file
 	
 	show setting information to the controller for the settings window (at button press or at the start??)
 	
 	button that saves changes for all settings
  
 */
