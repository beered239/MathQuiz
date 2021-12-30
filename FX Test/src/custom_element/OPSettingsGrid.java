package custom_element;

import java.io.IOException;
import java.util.function.UnaryOperator;

import custom_elements.KeyActions;
import custom_elements.TfFilter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

//import javafx.scene.control.cell.ComboBoxTableCell;

public class OPSettingsGrid extends GridPane{
	
	  //other aspects of the settings grid
		private ObservableList<String> options;
			//object specific controller refrence
				private FXMLLoader fxmlLoader;
				public FXMLLoader getSettingLoader() {
					return fxmlLoader;
				}
		private TfFilter numFilter;
	  
	  //fxml elements that will be used
		@FXML
	  	private ComboBox<String> difficultySelection;
	  	@FXML
	  	private HBox rangeBoxX2;
	  	@FXML
	  	private TextField rangeTF1;
	  	@FXML
	  	private TextField rangeTF2;
	  	@FXML
	  	private TextField practiceValTF;
	  	@FXML 
	  	private Label settingName;
	  	@FXML
	  	private String optionEasy;
	  	//labels for each setting
	  		@FXML private Label label1;	//for Difficulty Option setting label
	  	//@FXML
	  	//private ComboBoxTableCell<>
	  	
	  //constructor 
		  public OPSettingsGrid() {
		        fxmlLoader = new FXMLLoader(getClass().getResource("OPSettingGrid.fxml"));
		        fxmlLoader.setRoot(this);
		        fxmlLoader.setController(this);
		        getStylesheets().add(getClass().getResource("settingGrid.css").toExternalForm());
		        try {
		            fxmlLoader.load();
		        } catch (IOException exception) {
		            throw new RuntimeException(exception);
		        }
		        
		        //initialize object that are needed
		        	//numFilter = new TfFilter();	//used to add a filter to the textfields		can't be setup here
		  }
	  //initializer	
		  public void initialize() {
			  //test
			  	//System.out.println("Custom Component Was initialized");	//debug hidden
			  
			  //fill combo box here at start
			  	options = FXCollections.observableArrayList("Easy", "Medium", "Hard", "Select Range", "Practice");
			  	
			  //initialize components that will be used by other controllers
			  	difficultySelection.setItems(options);
			  //start by focusing on combobox
			  	//Platform.runLater(()->difficultySelection.requestFocus());
			  	//instead: when a certain pane is focused a request will be called to the OPgrid to get the difficulty selection focused
			  	//on second thought: it probably isn't needed, might just add an enter event when the pane is focused to focus on the OPGrid (but not show the list)
			  	
			 //add text formatters to the textfields
			  	numFilter = new TfFilter();
			  	TfFilter practiceValFilter = new TfFilter(11,"[0-9]*");
			  	numFilter.create(4, "[0-9]*");
				UnaryOperator<Change> filter = numFilter.getFilter();
			  	rangeTF1.setTextFormatter(new TextFormatter<>(filter));
			  	rangeTF2.setTextFormatter(new TextFormatter<>(filter));
			  	practiceValTF.setTextFormatter(new TextFormatter<>(practiceValFilter.getFilter()));
			  	
				
			 //listen for selected item and their index
			  	difficultySelectionValueProperty().addListener(((v, oldValue, newValue) -> {
			  		//System.out.println(getDifficultySelectionValue() );	//debug hidden
			  		int difficultyNumIndex = getDifficultySelectionIndex();
			  		if(difficultyNumIndex!=4)	practiceValTF.textProperty().setValue("None");
			  		rangeBoxX2.setDisable(difficultyNumIndex!=3);
			  		practiceValTF.setDisable(difficultyNumIndex!=4);
			  		}) );
			  	
			//listener for the key pressed
			  	//KeyEvent.
			  	difficultySelection.focusedProperty().addListener((v,oldV,newV) -> {
			  		if(v.getValue()) {
			  			//System.out.println("focused");	//debug hidden
			  			if(difficultySelectionValueProperty().get() == null || difficultySelectionValueProperty().get().equals("")) difficultySelection.show();
			  		}
			  		
			  	});
			  	//difficultySelection.setF
			  	difficultySelection.setOnKeyPressed( e -> difficultySelectionHandleKeyPress(e));
			  	this.setOnKeyPressed(e -> KeyActions.nextComponentAction(e.getCode()));	//by default clicking enter switches the focus to the next focusable component
			  	
		  }
		  
      //Methods for the Title of the setting
		  public StringProperty settingNameTextProperty() {
			  return settingName.textProperty();
		  }
		  public String getSettingNameText() {
			  return settingNameTextProperty().get();
		  }
		  public void setSettingNameText(String value) {
			  settingNameTextProperty().setValue(value);
			  
		  }
	
	  //difficulty selection value property
		  public ObjectProperty<String> difficultySelectionValueProperty() {
			  return difficultySelection.valueProperty();
		  }
		  public String getDifficultySelectionValue() {
			  return difficultySelectionValueProperty().get();
		  }
		  //no set method needed
		  
	  //getting the option from the difficulty selection combo box
		  public int getDifficultySelectionIndex() {
			  return options.indexOf(getDifficultySelectionValue());
		  }
		  public int getDifficultySelectionIndex(String value) {
			  return options.indexOf(value);
		  }//overloaded method incase you already have the value (probably not needed)
		  
	 //general get methods for components (might be more useful than the others. only make get methods for those that need them)
		  public ComboBox<String> getDifficultySelection(){
			  return difficultySelection;
		  }
		  public TextField getRangeTf1() {
			  return rangeTF1;
		  }
		  public TextField getRangeTf2() {
			  return rangeTF2;
		  }
		  public TextField getPracticeValTf() {
			  return practiceValTF;
		  }
		  
		  
      //methods for listeners of components
		  private void difficultySelectionHandleKeyPress(KeyEvent e) {
			  KeyCode keyPressed = e.getCode();
		  		
		  		if(keyPressed == KeyCode.DOWN) {
		  			e.consume();
		  			difficultySelection.show();
		  			//System.out.println("down pressed");	//debug hidden
		  		}
		  		KeyActions.nextComponentAction(keyPressed);
		  }

	//startup method
		  /**
		   */
		  public void loadFields(String difficultyVal, String rangeVal1, String rangeVal2, String practiceVal) {
			  //difficultySelectionValueProperty().setValue(difficultyValS);
			  difficultySelection.setValue(options.get( Integer.parseInt(difficultyVal)-1 ));
			  
			  //both range textfields
			  	rangeTF1.textProperty().setValue(rangeVal1);
			  	rangeTF2.textProperty().setValue(rangeVal2);
			  practiceValTF.textProperty().setValue(practiceVal); //doessn't use the property methods that the combo component has
		  }
		  
    @FXML Button saveButton;
    
    public Button getSaveButton() {
    	return saveButton;
    }
    
    /**action ran when the save button is clicked
     * @apiNote in this case it is overridden by the settings controllers which has it's own listeners (program dependent)
     * @apiNote default is to switch over to the next component after enter
     * */
    @FXML
    protected void saveClicked() {
    	//KeyActions.goToNextFocusable();	//commented out b/c in the math program it is overridden
    }
    
    
}
