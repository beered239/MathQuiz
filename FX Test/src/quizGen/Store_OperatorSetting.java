package quizGen;

import java.io.IOException;
import java.io.PrintWriter;

import custom_element.OPSettingsGrid;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Store_OperatorSetting extends Store_Setting{

	//static constant? variables used by all of the objects made with this class
		public static final int[] indexesForSetting = {0,3,5};
	
	
    //x2 range
        public Integer[] x2Range;   //custom range r1-r2 for pt2
        //public File fileDir;    //
        //public PrintWriter outFile;

    public Store_OperatorSetting(String fileNameS){
        super(fileNameS);
        //fileDir = new File(fileNameS);
        //pt2Range = getRangeValuesFromSetting(values.get(3));    //gets x2 range values
    }

    /**@apiNote turns the x2Range instance variable to have the information from the appropriate line (contains x2 values)
     * */
    public void iniX2Range(){
        x2Range = getRangeValuesFromSetting(values.get(3));    //gets x2 range values
    }

    //make setRange, set difficulty, set practice value


    /**@apiNote : to initialize the gridPane for each operator setting
     * */
    public void iniSettingLayout(String settingName){
        //parts
        //setting file name as title, hbox layout with the name and input for each setting
        //variables made
        //already acquired variables
        //names, values, settingTypeA (alist, alist, a), fileNameS

        //title

        Label settingTitle = new Label(settingName + " settings");
        //style
        settingTitle.setFont(Font.font("Contemporary Brush", FontWeight.SEMI_BOLD, FontPosture.REGULAR,19));
        	settingTitle.setStyle("-fx-text-fill: white;");
        settingTitle.setUnderline(true);
        //ini names and values as text and texfields
        //ignore TextField[] valuesTF = stringArrayLToTextFieldA(values);
        //ignore Text[] namesT = stringArrayLToTextA(names);
        iniSettingLayoutVariables();
        //HBox[] valuesHB =
        //gridpane
        settingsGrid = new GridPane();
        settingsGrid.setPadding(new Insets(0,0,0,0));
        settingsGrid.setHgap(30);
        settingsGrid.setVgap(10);    //resize? (for all of these values!!)
        //add to gridpane  node, column, row, column span, row span
        //plan: x___x | x_ _x
        settingsGrid.add(settingTitle,1,0,3,1);
        int counter = 0;
        //add elements
        //difficulty
        settingsGrid.add(namesT[0],1,2); settingsGrid.add(difficulty,2,2);
        //x2 range values
        settingsGrid.add(namesT[3],1,3); settingsGrid.add(rangeBoxX2,2,3);
        //practice value
        settingsGrid.add(namesT[5],1,4); settingsGrid.add(practiceModeTF,2,4);
        //
                /*for(int i=0; i<namesT.length; i++){
                    if(i != 1 && i != 2 && i!= 4) {
                        settingsGrid.add(namesT[i], 1, 2 + counter);
                        settingsGrid.add(valuesTF[i], 2, 2 + counter);
                        counter++;
                    }
                }*/
        //System.out.println("width? " + settingsGrid.getWidth());
        settingsGrid.setStyle("-fx-text-fill: white;");
    }



    //important variables
    public Label[] namesT;
    public TextField[] valuesTF;
    public ComboBox<String> difficulty;        //choicebox vs combobox  (for difficulty)
        public ObservableList<String> options;
    public TextField range1, range2;
    public HBox rangeBoxX2;
    //for practice mode textfield (make a custom textfield class later)
        public TextField practiceModeTF;
    //public HBox[] valuesHB;   //not needed rn?
    public int difficultyNum;

    public void iniSettingLayoutVariables(){
        int size = names.size();        //assumes size is the same for values and names
        namesT = new Label[size];
        valuesTF = new TextField[size];
        //valuesHB = new HBox[size];
        //difficulty box
        options =
                FXCollections.observableArrayList(
                        "Easy Mode","Normal Mode", "Hard Mode", "Sandbox Mode", "Practice Mode");   //Dependent
        difficulty = new ComboBox(options);
        difficulty.setStyle("" +
                "-fx-background-color: transparent;" +
                "-fx-border-color: black;" +
                "-fx-border-radius: 10;");
        difficulty.setPadding(new Insets(1,0,1,3));
        //difficulty.setPlaceholder(new Text(values.get(0)));
        difficulty.setValue(options.get(Integer.parseInt(values.get(0))-1));   //gets difficulty level from text and makes it the index for the options array
            //difficulty.promptTextProperty().addListener(e ->System.out.println(difficulty.valueProperty()));

            //listener:
                difficulty.setOnAction(e -> {
                    //System.out.println("text: " + difficulty.getValue());	//debug
                    try {difficultySelected();} catch (IOException ioException) {System.out.println("file not found");}
                });
                //difficulty.promptTextProperty().addListener(e -> System.out.println("Sdfsdf"));
                //difficulty.itemsProperty().addListener(e -> System.out.println("sdf"));

                
        //x2 range values

            range1 = new TextField("" + x2Range[0]); range2 = new TextField("" + x2Range[1]);
            rangeBoxX2 = new HBox();
            rangeBoxX2.getChildren().addAll(range1, new Text(" - "), range2);
            setClassicInputLook(range1);
            range1.setMaxWidth(55); range2.setMaxWidth(55);
            setClassicInputLook(range2);
            //listener: for pressing enter on range 2
                range2.setOnAction( e ->
                    {try {x2RangeSelected();} catch (IOException ioException) {System.out.println("file not found");}}
                );

        //for practice mode box
            practiceModeTF = new TextField(values.get(5));
            setClassicInputLook(practiceModeTF);
            //listener: when enter is pressed
                practiceModeTF.setOnAction(e -> {
                    try {practiceModeSelected();} catch (IOException ioException) {System.out.println("file not found");}
                });
    //find what states should be enabled
        //disable all first (range,practice mode)
            rangeBoxX2.setDisable(true); practiceModeTF.setDisable(true);
            calcDifficultyNum();//find difficulty num
            iniEnabledStates(difficultyNum);

    //loop for all values (non specific)   (styles all name labels)
        for(int i=0; i<size; i++){
            if(i != 1 && i != 2 && i!= 4){
                namesT[i] = new Label(names.get(i));
                	namesT[i].setStyle("-fx-text-fill: white;");
                
                valuesTF[i] = new TextField(values.get(i));
                    /*ignore valuesHB[i] = new HBox();
                    valuesHB[i].getChildren().add(valuesTF[i]);*/
                //style?
                valuesTF[i].setStyle("" +
                        "-fx-background-color: transparent;" +
                        "-fx-border-color: black;" +
                        "-fx-border-radius: 10;"
                        + "-fx-text-fill: white;");	//last one doesn't work 
                valuesTF[i].setPadding(new Insets(1,0,1,5));
            }
        }
    }
        //uses: setClassicInputLook (in super)

            /**@purpose: called when a different difficulty choice is picked
             * @note: rewriting the text for each individual change like this is a bit inefficient
             * @note: alternative: this method only saves the selection value, and disables text boxes based on the difficulty
             * @note: have a method that uses all the saved states to rewrite the file after the submit is entered?
             * */
            public void difficultySelected() throws IOException {
                calcDifficultyNum();
                int i=0;
                /*
                System.out.println("difficulty num: " + difficultyNum);
                System.out.println("file dir: " + fileNameS);
                System.out.println("total lines? " + lines.size());
                Display.arrayS(lines);
                */
                outFile = new PrintWriter(fileDir); //index specific!!
                outFile.println(names.get(0) + " " + difficultyNum);
                for(i=1; i<names.size();i++){
                    outFile.println(names.get(i) + " " + values.get(i));    //note: if error -> create printout to see if this works
                }//note: uses names and values b/c lines arraylist could be empty/corrupt
                outFile.close();    //make sure to close (updates file)
                
                //change the actual values arraylist (to stop error when using the changeSettingOnIndex method (it uses the old values arraylist and could rewrite the difficulty back to the og))
                values.set(0, difficultyNum+"");
                
                //method to enable fields
                    iniEnabledStates(difficultyNum);

            }
            /**@purpose: to enable/disable textfields based on the difficulty selected
             * @note: consider making a disable all others method??*/
                public void iniEnabledStates(int difficultyNum){
                    //if statements to disable/enable textfields
                        if(difficultyNum == 4){
                            rangeBoxX2.setDisable(false);
                            practiceModeTF.setDisable(true);
                        }//enables range
                        else if(difficultyNum == 5){
                            practiceModeTF.setDisable(false);
                            rangeBoxX2.setDisable(true);
                        }
                        else{
                            practiceModeTF.setDisable(true);
                            rangeBoxX2.setDisable(true);
                        }
                }
                /**@purpose: to find the difficulty number picked(not index) */
                    public void calcDifficultyNum(){
                        String selection = difficulty.valueProperty().getValue().toString();
                        int i=0;
                        for(i=0; i< options.size(); i++){
                            //System.out.println(options.get(i) + " total size: " + options.size());    //debug
                            if(selection.equalsIgnoreCase(options.get(i))){
                                difficultyNum = i+1;
                                break;}
                        }
                    }

            /**@purpose: to change the x2 range setting line when called*/
            public void x2RangeSelected() throws IOException{
                //outFile = new PrintWriter(fileDir);
                String range = "(" + range1.getText() + " ~ " + range2.getText() + ")";
                changeSettingLineOnIndex(3,range);
            }
            /**@purpose: to change the practice mode setting line when called*/
            public void practiceModeSelected() throws IOException{
                String practiceNumS = practiceModeTF.getText();
                int practiceNumI = Boot.filter.fixI(practiceNumS);
                changeSettingLineOnIndex(5,practiceNumI+"");
            }
            
            
            /**@purpose: to update the y value ranges once the settings have been changed
             * 
             * @note: use Double to already start implementation of doubles 		//sole implementation for integers since getRangeValuesFromSettings still only returns integers
             * */
            	public void updateYRange(String operatorSymbol) {
            		//update values array by rereading the operators file
            		
            		//if(Main.filter.findNumOfDec( Double value that could be an int ) == 0)
                    
            		
            		try {retrieveData(true);//true b/c there's no need to correct errors but just in case
            			}catch (IOException e) {System.out.println("file note found, search Store_OperatorSetting updateYRange method");	e.printStackTrace();}
                    iniX2Range();
            		
            		Double[] x1Range = getRangeValuesFromSettingD(values.get(2));	//x1 range can't be changed atm
            		//x2 range already added
            		Double minNum=null, maxNum=null;
            		
            		String min=null, max=null;
            		String y2Range = "";
            		
            		//booleans
            			boolean x1R1Neg, x1R2Neg, x2R1Neg, x2R2Neg;
            		
            			if(x1Range[0] < 0)	x1R1Neg = true; else x1R1Neg=false;
            			if(x1Range[1] < 0) x1R2Neg = true; else x1R2Neg=false;
            			if(x2Range[0] < 0)	x2R1Neg = true; else x2R1Neg=false;
            			if(x2Range[1] < 0) x2R2Neg = true; else x2R2Neg=false;
            			
            			
            		if(operatorSymbol.equalsIgnoreCase(Symbols.PLUS)) {
            			minNum = x1Range[0] + x2Range[0];
            			maxNum = x1Range[1] + x2Range[1];
            		}
            		else if(operatorSymbol.equalsIgnoreCase(Symbols.MINUS)) {
            			minNum = x1Range[0] - x2Range[0];		//ex: -400 - -400		(lowest negative - highest positive)
            			maxNum = x1Range[1] + x2Range[1];		//								(highest positive - highest positive)
            			
            			//if pure negatives in x2 -> pick lowest negative (biggest num)
            			//if neg, 0, & pos in x2 -> 
            			
            		}
            		else if(operatorSymbol.equalsIgnoreCase(Symbols.TIMES)) {
            			//if(x1Range[0] * x2Range[0] > x1Range[1] * x2Range[1])
            				
            			//smallest/lowest number: biggest positive * biggest minimum (big neg)   OR lowest negative * lowest negative (small pos)
            			//largest (small neg) (big pos): largest positive *largest positive OR lowest negative * lowest negative OR 
            			//Double pp,pn,nn;
            			
            			//nn nn
	            			if(x1R2Neg && x2R2Neg ) {
	            				minNum = x1Range[1] * x2Range[1];
	            				maxNum = x1Range[0] * x2Range[0]; 
	            			}//everything's neg
            			//pp nn
	            			else if(!x1R1Neg && x2R2Neg) {
	            				minNum = x1Range[1] * x2Range[1];
	            				maxNum = x1Range[0] * x2Range[0]; 
	            			}//x1: pp x2: nn
	            			else if(!x2R1Neg && x1R2Neg) {
	            				minNum = x1Range[1] * x2Range[1];
	            				maxNum = x1Range[0] * x2Range[0];  
	            			}//x1: nn x2: pp
            			//nn np
	            			else if(x1R2Neg && x2R1Neg && !x2R2Neg) {
	            				minNum = x1Range[0] * x2Range[1];
	            				maxNum = x1Range[0] * x2Range[0];  
	            			}//x1: nn x2: np
	            			else if(x2R2Neg && x1R1Neg && !x1R2Neg) {
	            				minNum = x2Range[0] * x1Range[1];
	            				maxNum = x2Range[0] * x1Range[0];  
	            			}//x2: nn x1: np
            			//np np
	            			else if(x1R1Neg && !x1R2Neg && x2R1Neg && !x2R2Neg) {
	            				Double nn = x1Range[0] * x2Range[0];
	            				Double pp = x1Range[1] * x2Range[1];
	            				Double x1Nx2P = x1Range[0] * x2Range[1];
	            				Double x1Px2N = x1Range[1] * x2Range[0];		//it is at this point where I decided to use a different method. I only need 6 numbers to give the whole picture
	            				if(nn > pp) {
	            					maxNum = nn;
	            				}//nn > pp
	            				else {
	            					maxNum = pp;
	            				}// pp > nn
	            				
	            				minNum = x1Range[0] * x2Range[1];
	            				maxNum = x1Range[0] * x2Range[0];  
	            			}//x1: np x2: np	            			
	            			
	            			
            			//pp np nn -> 3 groups 2 combinations
            			
            			//pp pn nn 
            			else {
            				minNum = x1Range[0] * x2Range[0];
                			maxNum = x1Range[1] * x2Range[1];
            			}//if all positive pp pp, np pp, pp np, 		np np
            		}
            		else if(operatorSymbol.equalsIgnoreCase(Symbols.DIVIDE)) {
            			minNum = x1Range[0] + x2Range[0];
            			maxNum = x1Range[1] + x2Range[1];
            		}
            		
            		//combines the min and max based on it being a double or not
	            		if(Boot.filter.findNumOfDec(minNum) == 0) {
	            			min = Integer.toString(minNum.intValue());	//ex: 12.0 -> "12"  OR 1.23 -> "1.23"
	            		}
	            		else {
	            			min = Double.toString(minNum);
	            		}
	            		
	            		if(Boot.filter.findNumOfDec(maxNum) == 0) {
	            			max = Integer.toString(maxNum.intValue());	//ex: 12.0 -> "12"  OR 1.23 -> "1.23"
	            		}
	            		else {
	            			max = Double.toString(maxNum);
	            		}
	            		
	            		y2Range = "(" + min + " ~ " + max + ")";
            		
            		
            		//updates the file on the y2range 
            		try {changeSettingLineOnIndex(4, y2Range);
            				}catch(IOException e) {e.printStackTrace();}
            		
            		
            		Boot.operators.calcMaxInputs();
            		
            		
            		//note: add ending checks to add +1 to the max if the number is negative (and set min length to 1 although a minimum for the textfield has not been added yet)
            	}
            	
            	public void updateYRangeV2(String operatorSymbol) {
            		//update values array by rereading the operators file
            		
            		//if(Main.filter.findNumOfDec( Double value that could be an int ) == 0)
                    
            		//update array's with latest data from files (tech optional)
	            		try {retrieveData(true);//true b/c there's no need to correct errors but just in case
	            			}catch (IOException e) {System.out.println("file note found, search Store_OperatorSetting updateYRange method");	e.printStackTrace();}
	                    iniX2Range();	
            		
            		Double[] x1Range = getRangeValuesFromSettingD(values.get(2));	//x1 range can't be changed atm
            		//x2 range already added
            		Double minNum=null, maxNum=null;
            		
            		String min=null, max=null;
            		String y2Range = "";
            		
            		//booleans
            			boolean x1R1Neg, x1R2Neg, x2R1Neg, x2R2Neg;
            		
            			if(x1Range[0] < 0)	x1R1Neg = true; else x1R1Neg=false;
            			if(x1Range[1] < 0) x1R2Neg = true; else x1R2Neg=false;
            			if(x2Range[0] < 0)	x2R1Neg = true; else x2R1Neg=false;
            			if(x2Range[1] < 0) x2R2Neg = true; else x2R2Neg=false;
            			
            		Double[] comboA = new Double[4];
        				
            			
            		if(operatorSymbol.equalsIgnoreCase(Symbols.PLUS)) {
            			comboA[0] = x1Range[0] + x2Range[0];
            			comboA[1] = x1Range[0] + x2Range[1];
            			comboA[2] = x1Range[1] + x2Range[0];
            			comboA[3] = x1Range[1] + x2Range[1];
            		}
            		else if(operatorSymbol.equalsIgnoreCase(Symbols.MINUS)) {
            			comboA[0] = x1Range[0] - x2Range[0];
            			comboA[1] = x1Range[0] - x2Range[1];
            			comboA[2] = x1Range[1] - x2Range[0];
            			comboA[3] = x1Range[1] - x2Range[1];
            		}
            		else if(operatorSymbol.equalsIgnoreCase(Symbols.TIMES)) {
            			comboA[0] = x1Range[0] * x2Range[0];
            			comboA[1] = x1Range[0] * x2Range[1];
            			comboA[2] = x1Range[1] * x2Range[0];
            			comboA[3] = x1Range[1] * x2Range[1];
            		}
            		else if(operatorSymbol.equalsIgnoreCase(Symbols.DIVIDE)) {
            			comboA[0] = x1Range[0] / x2Range[0] ;
        				comboA[1] = x1Range[0] / x2Range[1] ;
        				comboA[2] = x1Range[1] / x2Range[0];
        				comboA[3] = x1Range[1] / x2Range[1];
            		}
            		
            		Sort.mergeSortD(comboA, 0, comboA.length-1);		//consider using a more efficient sort?
        			minNum = comboA[0];//1st one
        			maxNum = comboA[3];//4th one
            		
        			//combines the min and max based on it being a double or not
	            		if(Boot.filter.findNumOfDec(minNum) == 0) {
	            			min = Integer.toString(minNum.intValue());	//ex: 12.0 -> "12"  OR 1.23 -> "1.23"
	            		}
	            		else {
	            			min = Double.toString(minNum);
	            		}
	            		
	            		if(Boot.filter.findNumOfDec(maxNum) == 0) {
	            			max = Integer.toString(maxNum.intValue());	//ex: 12.0 -> "12"  OR 1.23 -> "1.23"
	            		}
	            		else {
	            			max = Double.toString(maxNum);
	            		}
	            		
	            		y2Range = "(" + min + " ~ " + max + ")";
        		
	           		//updates the file on the y2range  & update the values array with the new y2Range		(tech you only need to update the array)
	           		try {changeSettingLineOnIndex(4, y2Range);
	           				}catch(IOException e) {e.printStackTrace();}	//optional
	           		values.set(4, y2Range);	//needed
	            	
	            	//Boot.operators.calcMaxInputs();
	            		
        			
            	}
           
            	public void getBiggestLength(String operatorSymbol) {
            		
            	}
            	

    //
    public int correctCounter = 0;
    public void addToCorrectCounter(){

    }

    /*=======================================Method(s) to interact with the custom component that replaces the grid here=============================================*/
    /**Loads the settings saved in ram to the operator grid gui (ram values should have recently been updated in the controller class where this is used)
     * @param operatorGrid the custom component for the gui where the user can change the settings related to this class
     */
    public void loadToOperatorGrid(OPSettingsGrid operatorGrid) {
    	operatorGrid.loadFields(values.get(0), ""+x2Range[0], ""+x2Range[1], values.get(5));
    }
    
    public void saveSettings(OPSettingsGrid operatorGrid) {
    	String[] opSValues = {
				(operatorGrid.getDifficultySelectionIndex()+1)+"",
				"(" + operatorGrid.getRangeTf1().getText() + "~" +operatorGrid.getRangeTf2().getText() + ")",
				operatorGrid.getPracticeValTf().getText()
		};
		try {
			changeSettingsLines(indexesForSetting, opSValues);	//0 3 5
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    
}
