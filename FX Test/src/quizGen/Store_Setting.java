package quizGen;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

//remember:
    //in: reads what's in the file              (in: read in file)
    //out: write information to the file        (out: write to file)
/***/

//note: runs as an object but it does not need to be

/**
 * @purpose: stores the settings found in the text files into ram (does not read the text files or change them!)
 *      - use ReadFile readSettings object to get settings
 * */
public class Store_Setting {

    //private variables:
        //reading global settings
            //private File fileNameGS;    //this object will store the name of the global settings file (GS = global settings)

        // |t/f: for use of those operators     (viewable by other classes with settings object instance)
            /*
                //private static boolean add;    //add
                //private static boolean sub;    //subtract
                //private static boolean mul;    //multiply
                //private static boolean div;    //divide
            * */
        /**/
    //string arrays of acceptable terms
        //boolean terms:
        private String[] booleanDictionary = {"yes", "no", "true", "false", "y", "n", "t", "f"}; //single letter, ignore case    (evens true odds false)
        private String[] boolTrueDict = {"yes","true","y","t"};
        private String[] boolFalseDict = {"no", "false", "n","f"};
        //yes = even no = odd

        //static file data
            //public static ArrayList<String> linesS;     //lines static
            //public static String fileNameS;

        //object specific file data:  array of info, array of names, array of values,
            //array of info(filled by readFiles object)
                public ArrayList<String> lines;
                public int totalLines;
            //names and values
                public ArrayList<String> names = new ArrayList<String>();
                public ArrayList<String> values = new ArrayList<String>();
            //name of file
                public String fileNameS;
            //type of setting array: stores the type of setting in each line (inserted by programmer)
                public String[] settingTypeA;
                public String[] defaultSettingValuesA;
                public String[] ifSettingUnusedA;       //(same as default??)
                //pair with defaultValue & ifNotUsed arrays? (implement unused later if needed)
                public String[] defaultSettingNamesA;
        //consider making set and get methods and turning these private ^^

    //constructor
    public Store_Setting(){

    }
    public Store_Setting(String fileNameS){
        this.fileNameS = fileNameS;
        fileDir = new File(fileNameS);
    }

    //retrieve data: used with constructor that has a filename, it gathers all the data needed

    public void retrieveData(boolean correctErrors) throws IOException{
    	
    	if(lines != null)
    		lines.clear();//clears lines AL that's about to be filled up with recent data
    	
        ReadFiles.saveToArray(fileNameS);
        ReadFiles.sendToSettings(this);
        System.out.println("lines when retrieving data in method");
        Display.arrayS(lines);
        System.out.println("end");
        ReadFiles.clearAll();	//clears the array in the ReadFiles class 
        findData(correctErrors);
    }
        //getData: gets data after the ": " and stores it into the values array (also stores the first half)
        //NOTE: this will ignore saving any line that is not in the specified format (commented code that can store them tho)
        public void findData(boolean correctErrors){  //()make private?
	        int index; // int counter=0;
	        values.clear(); names.clear();	//clears them of their old values to make way for the new ones
	        
	        String value, name;
	        for (String line : lines) {
	            index = line.indexOf(": ");
	            if(index == -1) index = line.indexOf(":");//quick general check if can't find ": " but has ":" in settings (no spacing)
	            if (index >= 0) {
	                //get value from the file & add it to the array
	                index++;//reason: to get the value (which is past ":")
	                value = line.substring(index);
	                value = value.trim();
	                values.add(value);
	                //get name from line and add to it's array
	                name = line.substring(0, index);     //(does not include the space afterwards)
	                names.add(name);
	            }//avoids non setting lines		update: non setting lines get deleted anyways, use else to correct errors
	            
	                /*
	                 *else{
	                    values.add("  ");
	                    names.add(line);
	                }//adds the setting line and an empty value with it (just to recreate file as is when changing it )
	                 **/
	            //counter++;
	        }
	        
	        System.out.println("(in findData method) values: ");
	        Display.arrayS(values);
	        
	        if(correctErrors)
	        	correctErrors();
	        
    }


    //interpret values (interpretValuesBool renamed to getBoolSettingA)
    //note: this will only work for setting values that are boolean values in the first place
    /**@purpose: to interpret the settings found as boolean values being returned
     * @param indexStart starting index from the values array acquired
     * @param indexEnd  end index to interpret from the values array
     * */
    public boolean[] getBoolSettingA(int indexStart, int indexEnd){
        int size = indexEnd-indexStart+1;
        boolean[] boolValues = new boolean[size];  //ex: start:0 end:5 total values: 5-0+1 -> 6
        Boolean foundToBe;  //used object to catch error
        for(int i=0; i< size; i++){
            foundToBe = (interpretValueB(values.get(indexStart+i))       );
            if(foundToBe == null){
                values.set(i,"false");
                foundToBe = false;
                System.out.println("caught a null! amount: " + size);//debug
                boolValues[i] = false;
            }//catches error and sets that nonsensical value in values array to false
            else{
                boolValues[i] = (boolean)foundToBe;
            }
        }
        return boolValues;
    }
    public boolean[] getBoolSettingA(){
        return getBoolSettingA(0,values.size()-1);
    }

    //interpret value
        /*note: assumes that either a true or false is where it should be (if not then it automatically assumes false)
            false is shown later in upper method using the null that was returned*/
    /**@purpose: to see if a string value meant true or false
     * @param value the value being tested against the library of acceptable terms
     * @note: technically this covers an integer being boolean and then checks the dictionary to see if the string is boolean (valueB is better than valueIB)
     * */
    public Boolean interpretValueB(String value){
        //filter class used to check if the boolean sections have numbers
            Filter filterNum = new Filter();
            filterNum.setBasedRounding(true); int valueI = filterNum.fixI(value);
            
            //System.out.println("value in: " + value + " int val: " + valueI + " what is shown: " + value.equalsIgnoreCase(boolTrueDict[1])); //debug
        
        //check if the number matches: (easiest)
	        if(valueI>0)
	        	return true;
	        else if(valueI==0)
	        	return false;
        //check bool dictionary (longer)
        	Boolean check = trueOrFalse(value);
	        if(check != null)
	        	return check;
        	
        //check if num is invalid(last b/c word's will automatically be invalid)
        	if(Filter.seeIfInvalid(valueI))	return false;
        
        return null;   //ASSUMES FALSE DESPITE NO MATCHES BEING FOUND
    }

    /**@purpose: uses the string arraylist values from the setting, and returns an array of integers from them
     * @apiNote returns the values arraylist as an array of integers
     * @param isInvalid0 changes the invalid number to 0 if true
     * */
    public int[] getIntSettingA(boolean isInvalid0){
        return getIntSettingA(0,values.size()-1,isInvalid0);
    }
    public int[] getIntSettingA(int indexStart, int indexEnd, boolean isInvalid0){
        int valueI;
        int size = indexEnd-indexStart+1;
        int[] ints = new int[size];

        for(int i=0; i<size; i++){
            valueI = interpretValueIB(values.get(indexStart+i));
            if(isInvalid0 && Filter.seeIfInvalid(valueI))
                ints[i] = 0;
            else
                ints[i] = valueI;
        }
        return ints;
    }

    //note: useful for settings that can't be lower than 0
    /**@purpose: to return an integer object depending on the string value that is supposed to represent a boolean integer (false = 0, any pos int = true)
     * @apiNote returns an amount setting value where 0 would also represent false/none
     * @param value string being seen as an integer from 0 and up/ empty to containing
     * */
    public Integer interpretValueIB(String value){
        int valueI = stringToI(value);
        //System.out.println("Interperted value IB: " + valueI);	//debug invisible
        Boolean check = trueOrFalse(value);
        if(valueI>=0)
        	return valueI;
        else if(!Boolean.TRUE.equals(check))
        	return 0;
        else if(Filter.seeIfInvalid(valueI))    /*->*/        return -1;
        
        else
        	return null;
        
    }

    /**@purpose: returns an integer for the string setting inputted, or null if it is not valid (can be used to change value to default)
     * @apiNote returns an integer object that represents the string inputted
     * @param value the value of a setting as a string variable
     * */
    public Integer interpretValueI(String value){
        int valueI = stringToI(value);
        //System.out.println("value was actually: " + valueI);	//debug invisible
        if(Filter.seeIfInvalid(valueI) )    /*->*/        return null;
        else                               /*->*/        return valueI; //values.get(indexStart+i);
    }
    //side method that ^ use
        /**@purpose: quickly converts a string to I using the filter class without the need of creating multiple objects
         * @apiNote convert string to int with smart rounding
         * @param value a string value about to be turned to an integer
         * */
        private int stringToI(String value){
        	//System.out.println("string: " + value);	//debug invisible
            Filter filtNum = new Filter();
            filtNum.setBasedRounding(true);
            //System.out.println("true test: " + filtNum.fixI("0") + " and " + value);	//debug invisible
            return filtNum.fixI(value);
        }

    /**@purpose: to return a proper range string "(num1 ~ num2" or a null if it is not in the right format
     * @note: example of indexes being used: 0,1  + 1,midStart+1 +   midStart,midEnd +  midEnd,stringLength-1   -> ( + num1 + " ~ " + num2 + )
     * @note: this will fix any errors in the numbers
     * @apiNote return a corrected int range value as a string
     * @param value a string val representing a range
     * */
    public String interpretValueIR(String value){
        value = value.trim(); Filter filter = new Filter();
        boolean correctStart = (value.charAt(0) == '(');
        boolean correctEnd = (value.charAt(value.length()-1)== ')');
        //ex: (0 ~ 4) -> ( = 0,1    0 = 1,2     " ~ " = 2,5   4 = 5,6    ) = 6  -->  0,1  + 1,midStart+1 +   midStart,midEnd +  midEnd,stringLength-1
        int midStart = value.indexOf(" ~ ");
        int midEnd, firstVal, secondVal, low,high;

        if(correctEnd && correctStart && midStart != -1){
            midEnd = midStart+3;
            firstVal = filter.fixI(value.substring(1,midStart+1));
            secondVal = filter.fixI(value.substring(midEnd,value.length()-1));
            if(Filter.seeIfInvalid(firstVal) || Filter.seeIfInvalid(secondVal))
                return null;
            else{
                low = Integer.min(firstVal,secondVal); high = Integer.max(firstVal,secondVal);
                return "(" + low + " ~ " + high + ")";
            }
        }
        else
            return null;
    }//allowed valueIR format: (number ~ number)
    //side method to get the two numbers from a ^ value (IR value)
        /**@purpose: returns an array of size 2 that has the range for an ir setting line in the array
         * @apiNote returns 2 values in an array representing the range found in a setting
         * @note: assume the setting is in the correct format   (correct integers and format)
         * @note: assume min then max are in the order
         * @param valueIR the setting line being put in to get the two range values ex: (0 ~ 144)
         * */
        //public Integer[] x2Range; already saved in each operator
        public static Integer[] getRangeValuesFromSetting(String valueIR){
            //maybe make an if statement for if the valueIR is the default n/a value or something
            Integer[] range = new Integer[2];
            int midIndex = valueIR.indexOf("~");
            range[0] = Integer.parseInt(valueIR.substring(1, midIndex).trim());
            range[1] = Integer.parseInt(valueIR.substring(midIndex+1,valueIR.length()-1).trim());
            return range;
        }
        
        public Double[] getRangeValuesFromSettingD(String valueIR){
            //maybe make an if statement for if the valueIR is the default n/a value or something
            Double[] range = new Double[2];
            int midIndex = valueIR.indexOf("~");
            range[0] = Double.parseDouble(valueIR.substring(1, midIndex).trim());
            range[1] = Double.parseDouble(valueIR.substring(midIndex+1,valueIR.length()-1).trim());
            return range;
        }
        
        /**string compared against boolean dictionary to see if it is true, false, or neither/null
         * @param value the string value being compared
         * */
        private Boolean trueOrFalse(String value) {
        	int i=0; 
        	while(i<booleanDictionary.length) {
        		if(value.equalsIgnoreCase(booleanDictionary[i])) {
        			if(i%2==0) {
                		return true;
                	}//if even index -> true
        			else
        				return false;
        		}
        		i++;
        	}
        	return null;
        }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////Section: correct values array////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**@purpose: to correct any supposed error in the setting file array values, being stored in memory
     * @apiNote corrects all parts of the setting file
     * */
    public void correctErrors(){
        int numOfErrorFixes = 0;
    	
    	numOfErrorFixes += correctInvalidValues();
        numOfErrorFixes += correctInvalidNames();
        //change txt file if corrections were needed: 
        	
        	if(numOfErrorFixes>0) {
        		System.out.println("Making setting file corrections... " + numOfErrorFixes);	//debug visible
        		Display.arrayS(values);
		        String[] valuesS = values.toArray(new String[0]);
        		try {
					changeSettingsLines(valuesS);
				} 
		        	catch (IOException e) {e.printStackTrace();}
        	}
        	
    }

    /**@purpose: to change the values string array values with the set default value for it, in case any are invalid
     * @apiNote correct incorrect setting values in values arraylist
     * @note: make sure to run the setSettingTypeA first
     * */
    private int correctInvalidValues(){

    	int numOfErrors = 0;
    	
    	System.out.println("value size: " + values.size()  + " vs. " + settingTypeA.length);
    	
        if(values == null || values.size() != settingTypeA.length){
        	changeValuesToDefault(); 
        	System.out.println("Had to change values to default");
        	return 1;
        	}
        
        //System.out.println("number of active lines: " + lines.size());	//debug (invis)
        
        String currentSettingType;
        String currentValue;
        Boolean valueB; Integer valueIB; Integer valueI; String valueIR;
        for(int i=0; i<values.size(); i++){
            currentSettingType = settingTypeA[i];       //the current type of setting
            currentValue = values.get(i);

            if(currentSettingType.equalsIgnoreCase("valueB")){
                valueB = interpretValueB(currentValue);
                
                //System.out.println("correcting value b: " + currentValue + " sent as: " + valueB);//debug invisible
                
                if(valueB == null) {
                	values.set(i,defaultSettingValuesA[i]);
                	numOfErrors++;
                }
                else
                    values.set(i,valueB.toString());
            }
            else if(currentSettingType.equalsIgnoreCase("valueIB")) {
            	valueIB = interpretValueIB(currentValue);
                if (valueIB == null || valueIB == -1) {
                	values.set(i, defaultSettingValuesA[i]);
                	numOfErrors++;
                }//note: valueIB can't be null if value is gibberish it will become 0
                else
                    values.set(i, valueIB.toString());
            }
            else if(currentSettingType.equalsIgnoreCase("valueI")){
                valueI = interpretValueI(currentValue);
                if(valueI == null) {
                	values.set(i, defaultSettingValuesA[i]);
                	numOfErrors++;
                }
                else
                    values.set(i,valueI.toString());
            }
            else if(currentSettingType.equalsIgnoreCase("valueIR")){
                valueIR = interpretValueIR(currentValue);
                if(valueIR == null) {
                	values.set(i,defaultSettingValuesA[i]);
                	numOfErrors++;
                }
                else
                    values.set(i,valueIR);
            }
        }
         //System.out.println("Number of invalid Values: " + numOfErrors);	//debug visible
        return numOfErrors;
    }
            /**used only in ^
             * @purpose: changes values arraylist to contain the default settings
             * @apiNote changes values arraylist to contain the default settings
             * */
            private void changeValuesToDefault(){
                //System.arraycopy(defaultSettingValuesA,0,values.toArray(),0,defaultSettingValuesA.length);
                if(values != null)
                	values.clear();
            	values = new ArrayList<>(Arrays.asList(defaultSettingValuesA));
            }
    /**@purpose: to change the names string array values with the set defaults if they are invalid
     * @apiNote fixes any typo/wrong labels in the names array
     * */
        private int correctInvalidNames(){
            int numOfErrors = 0;
        	
        	if(names == null || names.size() != defaultSettingNamesA.length) {changeNamesToDefault(); return 1;}
            String currentName, currentDefName;
            for(int i=0; i<names.size(); i++){
                currentName = names.get(i);
                currentDefName = defaultSettingNamesA[i];
                if(!currentName.equalsIgnoreCase(currentDefName)) {
                	names.set(i,currentDefName);
                	numOfErrors++;
                }
                    
            }
            return numOfErrors;
        }
            /**used in ^
             * @purpose: to change the names to their default values
             * @apiNote to change the names to their default values
             * */
            private void changeNamesToDefault(){
            	if(names != null)
            		names.clear();
            	names = new ArrayList<>(Arrays.asList(defaultSettingNamesA));     //note: no need to clear array when copied over
            }

    //methods that set arrays: default names and values, type of value
            public void setCorrectFileInfo(String[] settingTypes, String[] settingNames, String[] settingValues){
                setSettingTypeA(settingTypes);
                setDefaultSettingNamesA(settingNames);
                setDefaultSettingValuesA(settingValues);
            }

        /**@purpose: changes settingTypeA to show what each string in values is supposed to represent
         * @apiNote sets the array  that tells you the type of value in that line
         * @param settingTypeA string array can only include: valueB, valueI, or valueIB
         * */
            public void setSettingTypeA(String[] settingTypeA){
                this.settingTypeA = settingTypeA;   //ex: {valueB, valueB, valueI, valueI, valueIB, valueIB}
	                //ArrayList<String> values = new ArrayList<>(Arrays.asList(this.settingTypeA));
	                //Display.arrayS(values);//debug		(invisible)
            }
        /**@purpose: basically sets what a default (values or names) array would look like for this file
         * @apiNote basically sets what a default values array would look like for this file
         * */
            public void setDefaultSettingValuesA(String[] defaultSettingValuesA){
                this.defaultSettingValuesA = defaultSettingValuesA;
            }
            public void setDefaultSettingNamesA(String[] defaultSettingNamesA){
                    this.defaultSettingNamesA = defaultSettingNamesA;
                }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////Section: create inputs to change setting values//////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
        //note: also methods for saving each quiz file
    //needed: PrintWriter, File

    
    public PrintWriter outFile;
    public File fileDir;
    /**@purpose: changes a single value in the text file based on the index for the values array it will hcange
     * @apiNote changes setting line at index pos with string value
     * @note: works
     * */
    public void changeSettingLineOnIndex(int index, String value) throws IOException{
        int i =0;
        outFile = new PrintWriter(fileDir);
        for(i=0; i<index; i++)
            outFile.println(names.get(i) + " " + values.get(i));
        outFile.println(names.get(index) + " " + value);
        for(i=index+1; i<names.size(); i++)
            outFile.println(names.get(i) + " " + values.get(i));
        outFile.close();
    }

    /**@purpose: to change multiple lines at once using an array of indexes and an array of string values
     * @param indexes sorted array of index values
     * @param valuesS ordered string values to replace the old ones in the text
     * @note: untested!!!  (works?)  (now it actually works..!!)
     * @note show this to someone!!
     * */
    public void changeSettingsLines(int[] indexes, String[] valuesS) throws IOException{
        int i =0, k=0, kS=0;//ks is k saved
        outFile = new PrintWriter(fileDir);
        //Display.arrayS(names);	//debug
        
        for(i=0; i<values.size(); i++) {
        	
        	if(k<indexes.length) {
        		if(i == indexes[k]) {
        			outFile.println(names.get(i) + " " + valuesS[k]);
        			k++; 
        		}//current line is one that needs to be changed
        		else {
            		outFile.println(names.get(i) + " " + values.get(i));
            	}
        	}
        	else {
        		outFile.println(names.get(i) + " " + values.get(i));
        	}
        }
        outFile.close();
    }
    
    /**@purpose: simpler change settings line that makes some assumptions
     * @note: assumes that the valuesS starts at index 0 of the text file and continues linearly
     * @note: potential expansion: add para: startIndex to indicate where it would start but all the indexes afterwards would be consecutive (applies to most cases and would be faster)
     * */
    public void changeSettingsLines(String[] valuesS) throws IOException{
    	int i =0, k=0, kS=0;//ks is k saved
        outFile = new PrintWriter(fileDir);
        for(i=0; i<valuesS.length; i++) {
        	outFile.println(names.get(i) + " " + valuesS[i]);
        }   
        for(k=i; k<values.size(); k++) {
        	outFile.println(names.get(i) + " " + values.get(k));
        }
        outFile.close();
    }
    

    /**@apiNote sets a classic look for textfields
     * */
    public void setClassicInputLook(TextField textField){
        textField.setStyle("" +
                "-fx-background-color: transparent;" +
                "-fx-border-color: black;" +
                "-fx-border-radius: 10;");
        textField.setPadding(new Insets(1,0,1,3));
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    //ignore bottom arrays for now

    //check if boolean
    /**@purpose: checks if a string value is boolean, if so --> returns true
     * */
    public boolean checkIfBool(String value){
        for(String allowed : booleanDictionary){
            if(value.equalsIgnoreCase(allowed)){
                return true;}
        }
        return false;
    }




    public void searchGlobalS(){

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////Section: create the proper gridpane for setting file///////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**@purpose: to create a gridpane based off of the settings acquired
     * @note: //resize? make sure to resize any values with a set height or anything
     * */
    public GridPane settingsGrid;
    public void iniSettingLayout(String settingName){
        //parts
        //setting file name as title, hbox layout with the name and input for each setting
        //variables made
            //already acquired variables
                //names, values, settingTypeA (alist, alist, a), fileNameS

            //title

                Text settingTitle = new Text(settingName + " settings");
                //style
                    settingTitle.setFont(Font.font("Contemporary Brush", FontWeight.SEMI_BOLD, FontPosture.REGULAR,19));
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
                        settingsGrid.add(namesT[3],1,3); settingsGrid.add(valuesTF[3],2,3);
                    //practice value
                        settingsGrid.add(namesT[5],1,4); settingsGrid.add(valuesTF[5],2,4);
                    //
                /*for(int i=0; i<namesT.length; i++){
                    if(i != 1 && i != 2 && i!= 4) {
                        settingsGrid.add(namesT[i], 1, 2 + counter);
                        settingsGrid.add(valuesTF[i], 2, 2 + counter);
                        counter++;
                    }
                }*/

    }
        //^ uses
            //note: since these are meant specifically for the settings pane they will not return a value rather change variables
            //for a final product: hbox with textfields for the setting inputs, text for the names of each, etc
            public Text[] stringArrayLToTextA(ArrayList<String> stringAL){
                Text[] textA = new Text[stringAL.size()];
                for(int i=0; i<stringAL.size(); i++){
                    textA[i] = new Text(stringAL.get(i));
                    //style them?
                        //create a 2nd parameter that allows massStyleClass
                            //mass style class has methods like setStyle, alignment, etc
                            //after creating a massStyleClass object and using those methods -> add here as para
                                //use apply method with the Text[] as a para and return (maybe string[]?)
                }
                return textA;
            }
            public TextField[] stringArrayLToTextFieldA(ArrayList<String> stringAL){
                TextField[] textFieldA = new TextField[stringAL.size()];
                for(int i=0; i<stringAL.size(); i++){
                    textFieldA[i] = new TextField(stringAL.get(i));
                    //style them?
                    //create a 2nd parameter that allows massStyleClass
                    //mass style class has methods like setStyle, alignment, etc
                    //after creating a massStyleClass object and using those methods -> add here as para
                    //use apply method with the Text[] as a para and return (maybe string[]?)
                        //note: use mass style class as well but use method for textfields?
                }
                return textFieldA;
            }
            //important variables
                public Text[] namesT;
                public TextField[] valuesTF;
                public ComboBox difficulty;        //choicebox vs combobox  (for difficulty)
                public TextField range1, range2;
                //public HBox[] valuesHB;   //not needed rn?

            //specific to this programs settings
            public void iniSettingLayoutVariables(){
                int size = names.size();        //assumes size is the same for values and names
                namesT = new Text[size];
                valuesTF = new TextField[size];
                //valuesHB = new HBox[size];
                //difficulty box
                    ObservableList<String> options =
                            FXCollections.observableArrayList(
                                    "Easy Mode","Normal Mode", "Hard Mode", "Sandbox Mode", "Practice Mode");
                    difficulty = new ComboBox(options);
                        difficulty.setStyle("" +
                                "-fx-background-color: transparent;" +
                                "-fx-border-color: black;" +
                                "-fx-border-radius: 10;");
                        //difficulty.setPlaceholder(new Text(values.get(0)));
                        difficulty.setPromptText(options.get(Integer.parseInt(values.get(0))-1));   //gets difficulty level from text and makes it the index for the options array
                //x2 range values
                    range1 = new TextField(); range2 = new TextField();

                for(int i=0; i<size; i++){
                    if(i != 1 && i != 2 && i!= 4){
                        namesT[i] = new Text(names.get(i));
                        valuesTF[i] = new TextField(values.get(i));
                    /*ignore valuesHB[i] = new HBox();
                    valuesHB[i].getChildren().add(valuesTF[i]);*/
                        //style?
                        valuesTF[i].setStyle("" +
                                "-fx-background-color: transparent;" +
                                "-fx-border-color: black;" +
                                "-fx-border-radius: 10;");
                        valuesTF[i].setPadding(new Insets(1,0,1,5));
                    }
                }
            }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////



}

//files needed:
/*
  Global_Settings:      stores booleans to show which operators will be used
  Current_Quiz_Info:    updates every time a question is answered
    - (would be a good idea to encrypt it or give it a strange file extension)
        - Keeps track of Q right and wrong
        - stores total number of questions
        - will store a percentage at the end
        - (optional: will store the correct formatting for printF when making the quiz)
            - takes largest and min sized values to determine how spaced out everything will be...
  Division_Settings:    will store info on the operator's role
        - store number of division questions
        - store ????
  */

