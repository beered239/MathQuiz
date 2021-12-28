package settings_window;

import java.io.IOException;
import java.util.function.UnaryOperator;

import custom_element.OPSettingsGrid;
import custom_elements.TfFilter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.AnchorPane;
import quizGen.Boot;
import quizGen.Filter;

public class Controller {

	public static final Filter filter123 = new Filter();
	
	@FXML OPSettingsGrid additionGrid;
	@FXML OPSettingsGrid subtractionGrid;
	@FXML OPSettingsGrid multiplicationGrid;
	@FXML OPSettingsGrid divisionGrid;
	
	public void initialize() {
		
		//create custom textfields
			//decimal format
				//DecimalFormat dFormat = new DecimalFormat("#.0");	//confused about the parameter (doesn't seem to make a difference)
				//NumberFormat iFormat = new NumberFormat("#");
				
				
			//text formatter using unary operator
				TfFilter numFilter = new TfFilter();
				numFilter.create(5, "[0-9]*");
				UnaryOperator<Change> filter = numFilter.getFilter();
		
				//add the formatter to each textfield
					//TextFormatter<String> textFormatter = new TextFormatter<>(filter);   --> what I used before
					
					//add formatter to the textfields
						tfNumOfA.setTextFormatter(new TextFormatter<>(filter));
						tfNumOfS.setTextFormatter(new TextFormatter<>(filter));
						tfNumOfM.setTextFormatter(new TextFormatter<>(filter));
						tfNumOfD.setTextFormatter(new TextFormatter<>(filter));
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
			//load content to each operator grid
					Boot.AdditionS.loadToOperatorGrid(additionGrid);
					Boot.SubtractionS.loadToOperatorGrid(subtractionGrid);
					Boot.MultiplicationS.loadToOperatorGrid(multiplicationGrid);
					Boot.DivisionS.loadToOperatorGrid(divisionGrid);
					
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
					
				//listen to the buttons of each setting grid
					additionGrid.getSaveButton().setOnAction( e -> Boot.AdditionS.saveSettings(additionGrid));	//saves to file
					subtractionGrid.getSaveButton().setOnAction( e -> Boot.SubtractionS.saveSettings(subtractionGrid));
					multiplicationGrid.getSaveButton().setOnAction( e -> Boot.MultiplicationS.saveSettings(multiplicationGrid));
					divisionGrid.getSaveButton().setOnAction( e -> Boot.DivisionS.saveSettings(divisionGrid));
			
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
			int[] indexes = {0,1,2,3};
			try {
				Boot.GlobalS.changeSettingsLines(indexes, createValueArray());
			} 		
				catch (IOException e) {e.printStackTrace();}
		}
			/**@apiNotecreates values array for global settings*/
				private String[] createValueArray(){
					String[] valuesA = {tfNumOfA.getText(), tfNumOfS.getText(),tfNumOfM.getText(),tfNumOfD.getText(),};
					return valuesA;
				}
		
		//other FX ID's
			//panes:
				//cosmetics
					@FXML CheckBox cBoxHighlightTF;
					@FXML CheckBox cBoxDisplayPercentages;
				//might need these?? prob not
					@FXML AnchorPane gPane;
					@FXML AnchorPane aPane;
					@FXML AnchorPane sPane;
					@FXML AnchorPane mPane;
					@FXML AnchorPane dPane;	//might need
		
}
