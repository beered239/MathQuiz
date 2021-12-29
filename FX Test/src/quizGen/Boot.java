package quizGen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.Scene;
import javafx.stage.Stage;


public class Boot{

    //private variables

    static Stage window;
    Scene scene0;
    //several layout types

    //window size variables
        //public static double winH=0, winW=0;	//not in use atm



    //MOVE THIS TO FORMAT CLASS WHEN MERGING PROJECTS
    /**@apiNote returns array with even formatted spacing
     *
     * */
        public static String[] formatSize(String[] stringA){
        int i, maxSize, maxIndex=0, currentI, tabsNeeded;
        StringBuilder currentS;
        String maxS;
        String[] newSA = new String[stringA.length];
        boolean morMaxB = false;

        //get max
        for(i=0; i<stringA.length; i++){
            if(stringA[i].length() > stringA[0].length()) maxIndex = i;
        }
        maxS = stringA[maxIndex];
        maxSize = maxS.length();
        //find out if max value needs a tab
        if(maxSize%4 != 0)  {morMaxB = true; maxSize+= 4 - maxSize%4;}
        //tab up all values... calculate how many tabs are needed to get all in line
        for(i=0; i<stringA.length; i++){
            tabsNeeded = 0;

            currentS = new StringBuilder(stringA[i]);
            currentI = currentS.length();
            if(currentI%4 != 0) {
                //currentS += "\t";
                tabsNeeded++;
                currentI += 4 - currentI%4;
            }
            if(i != maxIndex)
                tabsNeeded += (maxSize-currentI)/4;
            if(i == maxIndex && morMaxB)
                tabsNeeded = 1;

            currentS.append("\t".repeat(Math.max(0, tabsNeeded)));  //interesting repeat method

            //System.out.println(currentS + "tabs needed: " + tabsNeeded);    //debug
            newSA[i] = currentS.toString();
        }
        //System.out.println("max:" + maxSize); //debug
        return newSA;
    }
     
        
        
    /**@apiNote initializes everything needed for the quiz, including the questions*/
    public static void initialize() throws IOException {

        //IMPORTANT INITIALIZATIONS
            //writing to files
                fileWriter = new WriteFiles();
                ArrayList<String> directories = fileWriter.fileDirectoriesS;
            //writing to files:
            //writing quiz save files
                fileWriter.retrieveCount();
            //create these directories temporarily  (installer will add these before the app even runs for the first time)
                fileWriter.createDir("Saved Tests");
                fileWriter.createDir(directories.get(0) + File.separator + "Empty Tests Sheets");
                fileWriter.createDir(directories.get(0) + File.separator + "Completed Tests");
            //for settings location
                fileWriter.createDir("Settings");   //id: 3

                
        //set up the store setting classes for all setting files
                String sDirectories = directories.get(3) + File.separator;  //for directories
            GlobalS = new Store_GlobalSetting(sDirectories + "Global_Settings.txt");
            AdditionS = new Store_OperatorSetting(sDirectories + "Addition_Settings.txt");
            SubtractionS = new Store_OperatorSetting( sDirectories + "Subtraction_Settings.txt");
            MultiplicationS = new Store_OperatorSetting(sDirectories + "Multiplication_Settings.txt");
            DivisionS = new Store_OperatorSetting(sDirectories + "Division_Settings.txt");
    }
    
    	/**@purpose: 
    	 * @apiNote takes setting objects and adds file data to all; of them
    	 * @note: object is already created this just updates its values
    	 * */
	    public static void updateSettingObjects() throws IOException{
	    	//initializes objects if they haven't been initialized yet
	    		if(GlobalS == null)
	    			initialize();
	    	
		    //get all data into file ram objects
		        GlobalS.setCorrectFileInfo(globalSTypes, globalSDefaultNames, globalSDefaultValues );
		        GlobalS.retrieveData(true);
		            //Display.arrayS(GlobalS.names);
		    //get operator setting info into ram
		        DivisionS.setCorrectFileInfo(divSTypes,divSDefaultNames,divSDefaultVals);
		        DivisionS.retrieveData(true);
		            DivisionS.iniX2Range();
		        //Display.arrayS(DivisionS.values);
		            /*//Display.arrayS(GlobalS.lines);
		              //Display.arrayS(GlobalS.values);*/
		        AdditionS.setCorrectFileInfo(addSTypes,addSDefaultNames,addSDefaultVals);
		        AdditionS.retrieveData(true);
		            AdditionS.iniX2Range();
		        //Display.arrayS(AdditionS.values);
		        SubtractionS.setCorrectFileInfo(subSTypes,subSDefaultNames,subSDefaultVals);
		        SubtractionS.retrieveData(true);
		            SubtractionS.iniX2Range();
		        //
		        MultiplicationS.setCorrectFileInfo(multSTypes,multSDefaultNames,multSDefaultVals);
		        MultiplicationS.retrieveData(true);
		            MultiplicationS.iniX2Range();
		        //
		//Display.arrayS(GlobalS.values);
		    //initialize operator subclasses
		        addition = new Addition();
		        subtraction = new Subtraction();
		        division = new Division();
		        multiplication = new Multiplication();
		
		    //Display displayDiv = new Display();
		    Operators.getGlobalSData();
		    operators.calcMaxInputs();          //gets the maximum value (used for the length allowed in the input text box)
		    
		    
		    //Prompts userQuestions = new Prompts();    //prompts class is obsolete
	    }
    
    
    	//split initialize method into 2 sections (one updates the score box info and the other has all the other ini.)
    		//potential future plan: changes in settings that won't be immediate get saved to a temp save file, immediate changes change the ram/elements
    			//things like rest quiz -> replace old settings with new settings if changes were made (use ram value to detect if changes were made). 
    /**
     *             GlobalS.iniCorrectAnswerDisplayBox();//initialize the box holding the scores
     *             note: this was removed from initialize method make sure that it's initialized when it should be 
     *             		reason: this doesn't have to be called when setting are opened
     * */
    
    
    /**@apiNote creates the actual questions*/
    public static void createQuiz() {
    	operators.createAllQuestions();
        operators.createQuiz();
    }

    
    
    //division
        public static Scanner in = new Scanner(System.in);
        public static RandomInRange selectRange = new RandomInRange();
        public static Filter filter = new Filter();
        public static Display dispQuiz = new Display();

        public static Operators operators = new Operators();

        public static Division division;// = new Division();
        public static Addition addition;
        public static Subtraction subtraction;
        public static Multiplication multiplication;
        //live objects that store file data
            public static Store_GlobalSetting GlobalS;		//don't need to capitalize this...
                public static String[] globalSDefaultValues = {"0","0","0","0","true","true"};
                public static String[] globalSTypes = {"valueIB", "valueIB", "valueIB", "valueIB","valueB","valueB"};
                public static String[] globalSDefaultNames = {"Addition:", "Subtraction:", "Multiplication:", "Division:","Indicate if answer was correct:","Display Scores On Screen:"};  //not used yet
            public static Store_OperatorSetting DivisionS;
                public static String[] divSDefaultVals =    {"2","10","(1 ~ 144)","(1 ~ 12)","(1 ~ 12)","no","yes"};
                public static String[] divSTypes  =          {"valueI","valueI","valueIR","valueIR","valueIR","valueB","valueB"};
                public static String[] divSDefaultNames  = {
                        "Difficulty Option:","Amount of Questions:",
                        "x1 Range Values:","x2 Range Values:","y Range Values:",
                        "Practice Value:","Show Correct answer:"};
            public static Store_OperatorSetting AdditionS;
                public static String[] addSDefaultVals =    {"2","10","(0 ~ 10)","(0 ~ 10)","(0 ~ 20)","no","yes"};
                public static String[] addSTypes =        divSTypes;        //the type of values are the same for all operators
                public static String[] addSDefaultNames = divSDefaultNames; //names is same for all operators
            public static Store_OperatorSetting SubtractionS;
                public static String[] subSDefaultVals =    {"2","10","(0 ~ 20)","(0 ~ 10)","(0 ~ 10)","no","yes"};
                public static String[] subSTypes =          divSTypes;
                public static String[] subSDefaultNames = divSDefaultNames;
            public static Store_OperatorSetting MultiplicationS;
                public static String[] multSDefaultVals =    {"2","10","(0 ~ 12)","(0 ~ 12)","(0 ~ 144)","no","yes"};
                public static String[] multSTypes =          divSTypes;
                public static String[] multSDefaultNames = divSDefaultNames;
    //writing to files
            public static WriteFiles fileWriter;
}


/**to add to settings file:
 * 	add to default values, types, def names arrays
 * 	edit settings panel accordingly (or hardcoded searching )
 * 
 * */