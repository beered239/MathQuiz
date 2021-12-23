package sample;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Main extends Application {

    //private variables

    static Stage window;
    Scene scene0;
    //several layout types
    StackPane stackPane;
    HBox hBox0,hBox1,hBox2;
    public static VBox questionVP, vBox1,vBox2;
    public static BorderPane borderL0, borderL1, borderL2, borderL3;

    //window size variables
        public static double winH=0, winW=0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        borderL0 = new BorderPane();
        initialize();
        //resizing a text box:
        //textArea, TextField
        //order to test: borderL, vBox, hBox, text & input

        //create main panel for question
            createQuestionNumInfoBox();
            createQuestionHBox(Operators.allQuestions.get(0).toString());   //initializes: fullQuestionL
            //createQuestionHBox("Enter To Start");
            questionVP = new VBox();
            questionVP.getChildren().addAll(questionNumberHL,fullQuestionHL,GlobalS.scoreHBox); questionVP.setAlignment(Pos.CENTER);

        //create top nav bar (background pane)
            createNavBar();  //border for nav bar
        //shows mini boxes that display the size of each part of the main pane
            //tempMiniBoxes();
        //initialize the exit button:
            iniExitButton();

        borderL0.setTop(navPane);
        borderL0.setCenter(questionVP);  //adds vBox to the center part of the borderLayout
        borderL0.setStyle("-fx-background-color: #7c7c7d");

        scene0 = new Scene(borderL0,500,500);

        window.setScene(scene0);
        //updates navPanes height
            /*
                            borderL0.heightProperty().addListener(e ->{
            double navHeight = (borderL0.getHeight()/25.0);     //old vals: 37.0
            //settingIcon -> sButton -> settingBox -> navPane -> borderL0
            if(navHeight >= 15)
                settingIcon.setFitHeight(navHeight-1);
            //settingBox.setMinHeight(navHeight);
            sButton.setPrefHeight(navHeight);
            //settingBox.setPrefHeight(navHeight);
            //navPane.setPrefHeight(navHeight);

            //System.out.println(borderL0.getHeight() + " & " + navHeight);
        });
             * */

        //enters string input to be interpreted (not done yet)
        //questions
        //Question question;


        //sets a limit of 4 for the size of the text (next add a numbers only filter)
        //note: set max characters allowed based on the largest possible value according to the settings
            questionInput.textProperty().addListener(e -> {
                String text = questionInput.getText();
                int sLength = text.length();
                Question currentQuestion = Operators.allQuestions.get(Display.qIndex);
                int stringLimit = currentQuestion.getMaxInputLength();      //add 1 for decimals?
                if(sLength >= stringLimit+1) {
                    questionInput.setText(text.substring(0,stringLimit));
                }
                if (text.matches("[0-9/.]*") || sLength > stringLimit) return;
                questionInput.setText(text.replaceAll("[^0-9/.]", ""));
            });

        //listens for each new question value entered
            questionInput.setOnAction(e -> {
                Question nextQuestion = null, currentQuestion = null;//initialized
                String response = questionInput.getText();
                currentQuestion = Operators.allQuestions.get(Display.qIndex);
                //adding the response to the question
                    currentQuestion.setResponse(filter.fixI(response));
                    currentQuestion.calcAnalyzedResponse(); //should also update the answers correct count?
                        //System.out.print("\nQuestion " + Display.qIndex + ": " + currentQuestion + " " + response);    //debug
                    //deals with potential next question OR what to do after the test is over
                        if(Display.qIndex == Operators.allQuestions.size()-1){
                            GlobalS.calcPercentageCorrect();    //calculates the percentage correct (used to write into quiz file)
                            try{fileWriter.createSavedTestFile();
                                fileWriter.increaseCounter();}
                            catch (IOException ioException){
                                ioException.printStackTrace();
                            }//shouldn't be called at any point
                            questionInput.setDisable(true);         //System.out.println("Debug: hello?");
                            exitBtn.setDisable(false);
                        }//writes all responses to files, disables input
                        else{
                            nextQuestion = Operators.allQuestions.get(Display.qIndex+1);
                            setQuestionInfoBox();
                            setQuestionHBox(nextQuestion.toString());
                            questionInput.setText("");
                            Display.qIndex++;
                        }//moves onto next question, clears input, increases question count
            });


        //System.out.println("test: " + Operators.allQuestions.get(0));

        //get window screen set up

            window.setFullScreen(true);
            window.setFullScreenExitHint("");
            window.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            window.show();

        //get the proper heights
            //System.out.println(window.getWidth() + " " + borderL0.getHeight());   //debug
            winH = window.getHeight();
            winW = window.getWidth();

        iniGlobalSettingsPane();    //the 2nd pane where the user can change the settings

        //System.out.println("height: " + fullQuestionHL.getHeight());  //debug
    }

    //methods

    //button to close window?
        public static Button exitBtn;
        public static void iniExitButton(){
            exitBtn = new Button();   //temp

            ImageView exitIcon = new ImageView("cam_exit_btn2.png");//cam_exit_icon.png,exit_button 2.png
            //setup icon
            exitIcon.setFitHeight(20);
            exitIcon.setPreserveRatio(true);
            //setup button w/icon
            exitBtn.setGraphic(exitIcon);
            exitBtn.setStyle("" +
                    "-fx-background-color: transparent;" +
                    "-fx-border-color: transparent;");
            exitBtn.setPadding(new Insets(1, 3, 1, 3));

            exitBtn.setOnAction(e -> {
                        //add method that saves all values to the string?
                        window.close();
                        in.close();
                    });
            navPane.setLeft(exitBtn);
            exitBtn.setDisable(true);
        }

    //temporary method
        public static void tempMiniBoxes(){
            //temp: show true size of each part of the question
            HBox tempHL = new HBox(), tempHL2 = new HBox(), finalTempHL = new HBox();
            tempHL.setStyle("-fx-border-color: black"); tempHL2.setStyle("-fx-border-color:black");
            tempHL.setMinWidth(473);    tempHL2.setMinWidth(1000-473);
            //tempHL.setMinWidth(500); tempHL2.setMinWidth(1366-500);
            finalTempHL.setPrefHeight(40);
            finalTempHL.getChildren().addAll(tempHL,tempHL2);   finalTempHL.setAlignment(Pos.CENTER);
            tempHL.setAlignment(Pos.CENTER);
            questionVP.getChildren().add(finalTempHL);
        }

    /**@apiNote create the main panel that displays the question* */
        public static double fontSize;
        public static Text questionT;
        public static TextField questionInput;
        public static HBox fullQuestionHL;
        public static void createQuestionHBox(String questionS){
            questionT = new Text(questionS); //txt1 = new Text("beans");
            questionInput = new TextField();
            fullQuestionHL = new HBox();
            fontSize = 200;//vBox0.getHeight()/2.848;//200;//changed to x-20 when setting fontSize for input    (or x - (x/10)?)
            //input.resize(5,5);
            questionT.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.ITALIC, fontSize));

            //txt1.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.ITALIC, 60));
            //style input
            questionInput.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.ITALIC, fontSize-20));   //def:40
            questionInput.setAlignment(Pos.CENTER); //sets writing to the center
            questionInput.setPrefHeight(fontSize);//input.setMinHeight(200);
            questionInput.setPrefWidth(600);//input.setMaxWidth(600);
            questionInput.setStyle("" +
                    "-fx-background-color: transparent; " +
                    "-fx-border-color: black; " +
                    "-fx-border-radius: 40,40,0,0;"   +
                    "-fx-padding: 0px;" +
                    "-fx-border-width: 10;");   //someone said using fx-background-(color,insets,radius) is better
            //add both parts to the h box
                fullQuestionHL.getChildren().addAll(questionT,questionInput);
            //fullQuestionHL.setStyle("-fx-border-color:black");

            fullQuestionHL.setAlignment(Pos.CENTER);  //will center the box to the center of it's parent pane
            //info
                //System.out.println("Layout width: " + fullQuestionHL.getWidth() + "\n" +
                //      "Layout height: " + fullQuestionHL.getHeight());
                //fullQuestionHL.setStyle("-fx-border-color: black");
                //get the size of the whole question based on the text and the input textField size
                //basicInputA[finalI].setPrefWidth(TextUtils.computeTextWidth(basicInputA[finalI].getFont(),
                //      basicInputA[finalI].getText(), 0.0D) + 10);
                //double questionSize = TextUtils.computeTextWidth(questionT.getFont(),questionS,0.0D);
                //double totalSize = questionSize + (questionSize*1.2);   //factor of 1.2 for input box
                //System.out.println(questionSize);   //in this case: 473
        }

        public static HBox questionNumberHL;
        public static Text questionNumberT;
        public static int questionNumber = 1;//starts at 1
        public static void createQuestionNumInfoBox(){
            questionNumberHL = new HBox();
            questionNumberT = new Text("Question " + questionNumber + ":");
            questionNumberHL.getChildren().add(questionNumberT);
            //style text
                questionNumberT.setFont(Font.font("Calibri", FontWeight.LIGHT, FontPosture.REGULAR, 24));   //resize?
                questionNumberT.setUnderline(true);
                questionNumberT.setStyle("" +
                        "-fx-fill: #1b3a4a;");
            //style box
                questionNumberHL.setAlignment(Pos.TOP_LEFT);
                questionNumberHL.setPadding(new Insets(0,0,0,50));  //resize?
                questionNumberHL.setStyle("" +
                    "-fx-border-color: transparent");
        }

        /**@purpose: show the current score for the quiz
         * */

        public static void createQuestionScore(){
            Text score = new Text();

        }

        public static void setQuestionInfoBox(){
            questionNumberT.setText("Question " + (++questionNumber) + ":");
        }
        public static void setQuestionHBox(String newQuestion){
            questionT.setText(newQuestion);
        }

    //deals with the global settings pane
        public static BorderPane globalSettingsPane;
        public static TextField[] basicInputA;
        public static void iniGlobalSettingsPane(){
            globalSettingsPane = new BorderPane();
            HBox titlePane = new HBox();
            Text title = new Text("Global Settings:");
            //create title text
            title.setFont(Font.font("Calibri", FontWeight.LIGHT, FontPosture.REGULAR, 50));
            //txt0.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.ITALIC, 200));
            title.setStyle("" +
                    "-fx-border-color: black;");
            //create title box, hbox
            titlePane.getChildren().add(title);
            titlePane.setAlignment(Pos.CENTER);
            titlePane.setStyle("" +
                    "-fx-padding: 10,0,0,0;" +
                    "");
            //horizontal settings text (not input)
            HBox[] settingNamesL = new HBox[4];
            String[] settingNamesSA = {"Addition:","Subtraction:","Multiplication:","Division:"};
            //String[] formattedSettingNamesSA = formatSize(settingNamesSA);
            Text[] settingNamesTA = new Text[4];
            //for(String settingName : settingNamesA) settingNamesL.getChildren().add( new Text(settingName));
            //settingNamesL.setAlignment(Pos.CENTER_LEFT);
            //horizontal setting inputs
            HBox[] settingInputsLA = new HBox[4];
            basicInputA = new TextField[4];//now public
            //for submit global settings button
                HBox submitSettingsButtonHL;
                Button submitSettingsButtonB;

            //basicInput.setAlignment(Pos.CENTER);
            //settingInputsL.setAlignment(Pos.CENTER_RIGHT);

            //vertical holding both parts:
                VBox presentedSettingsL = new VBox();
                HBox presentedSettingsHL = new HBox();
            //horizontal container array holding (each hBox has txt and input)
                HBox[] settingA = new HBox[4];
            //gridPane alternative?
                //GridPane settingsGrid;
            for(int i=0; i<4; i++){
                //initializing all arrays
                settingA[i] = new HBox();
                settingNamesL[i] = new HBox();
                settingNamesTA[i] = new Text();
                settingInputsLA[i] = new HBox();
                    int numOfQ = Integer.parseInt(GlobalS.values.get(i));//
                basicInputA[i] = new TextField(numOfQ+"");  //show actual values from the file
                //setting up setting name boxes
                settingNamesTA[i].setText(settingNamesSA[i]);
                //style
                settingNamesTA[i].setStyle("");
                settingNamesTA[i].setFont(Font.font("forum", FontWeight.LIGHT,FontPosture.REGULAR,20));
                settingNamesL[i].getChildren().add(settingNamesTA[i]);
                    settingNamesL[i].setPadding(new Insets(0,0,0,0));
                    //settingNamesL[i].setStyle("-fx-border-color: black;");
                //setting up the input boxes
                settingInputsLA[i].getChildren().add(basicInputA[i]);
                //style input boxes
                basicInputA[i].setStyle("" +
                        "-fx-background-color: transparent;" +
                        "-fx-border-color:black;" +
                        "-fx-border-radius: 15;" +
                        "-fx-padding: 1px;");
                basicInputA[i].setFont(Font.font("Contemporary Brush",FontWeight.SEMI_BOLD,FontPosture.REGULAR,19));
                basicInputA[i].setPrefHeight(20);   //resize?
                basicInputA[i].setAlignment(Pos.CENTER);

                //set preferred textField
                    basicInputA[i].setPrefWidth(100);
                //dynamic textField (with bugs!!)
                                /*
                                        int finalI = i;
                                basicInputA[finalI].textProperty().addListener(new ChangeListener<String>() {
                                            @Override
                                            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                                                //basicInputA[finalI].setPrefColumnCount(((basicInputA[finalI].getText()).length() + 1));
                                                basicInputA[finalI].setPrefWidth(TextUtils.computeTextWidth(basicInputA[finalI].getFont(),
                                                        basicInputA[finalI].getText(), 0.0D) + 10);
                                            }
                                        });
                                        
                                        
                                        tf.textProperty().addListner(new ChangeListner<String>() {
                                        	@Override
                                        	public void changed(ObservabkeValue<? extends String> observableValue, String s, String t1) {
                                        		
                                        		tf.setPrefWidth(TextUtils.computeTextWidth(tf.getFont(), tf.getText(), 0.0D) + 10);
                                        		}
                                        	
                                        	})
                                        	note difference in listeners: typical eventHandler<ActionEvent>()  vs ChangeListener<String>()
                                        	//also: TextUtils comes from custom class
                                */

                //setting up the full setting hBox
                    settingA[i].getChildren().addAll(settingNamesL[i],settingInputsLA[i]);
                //style paired box
                    //settingA[i].setStyle("-fx-border-color: black;");
                    //settingA[i].prefWidth(100);
                    //settingA[i].setAlignment(Pos.CENTER);
                    settingA[i].setPadding(new Insets(0,20,0,0));//resize?
                //add to vertical box (or another hBox???)
                    //presentedSettingsL.getChildren().add(settingA[i]);
                    presentedSettingsHL.getChildren().add(settingA[i]);
                        presentedSettingsHL.setPadding(new Insets(0,0,0,0));
            }
            //add submit box to vbox
                submitSettingsButtonHL = new HBox();
                submitSettingsButtonB = new Button("Submit");//temp set text
                submitSettingsButtonHL.getChildren().addAll(submitSettingsButtonB);
                //style button?
                    submitSettingsButtonB.setStyle("" +
                            "-fx-background-color: transparent;" +
                            "-fx-border-color: #373d40;" +
                            "-fx-border-radius: 10;" +
                            "-fx-font-size: 20;");
                    submitSettingsButtonB.setPadding(new Insets(0,0,0,0));
                    //submitSettingsButtonB.setGraphic();
                //listener:
                    submitSettingsButtonB.setOnAction(e -> {
                        try {GlobalS.submitGlobalChanges();} catch (IOException ioException) {ioException.printStackTrace();}
                    });
                //style hbox?
                    submitSettingsButtonHL.setPadding(new Insets(1,0,0,0));
                    //submitSettingsButtonHL.setAlignment(Pos.CENTER);
                //add button to vbox (and gridBox)

            //add text showing difficulties really quickly
                Text difficulties = new Text("" +
                        "1: easy mode                EM\n" +
                        "2: medium mode              MM\n" +
                        "3: hard mode                HM\n" +
                        "4: sandbox mode (range)     SM\n" +
                        "5: practice mode (one value)PM");
                    //difficulties.setTextAlignment(TextAlignment.CENTER);
                    presentedSettingsHL.getChildren().add(submitSettingsButtonHL); //note: added hl
                    iniOperatorGridBox();   //has gridbox for every operator?
                    presentedSettingsL.getChildren().addAll(presentedSettingsHL,settingsGrid);  //settingsGrid!!!!! presentedSettingsHL??
                        presentedSettingsHL.setAlignment(Pos.CENTER);
            //adding to border pane
                globalSettingsPane.setTop(titlePane);
                globalSettingsPane.setCenter(presentedSettingsL);   //settingsGrid
        }

        //add to the presentedSettingsL vbox
        public static GridPane settingsGrid;
        public static Text[] operatorTitles = {new Text("Addition"), new Text("Subtraction"), new Text("Multiplication"), new Text("Division")};
        /**@purpose: to initialize the operator gridbox which contains each settings grid for each operator
         * @note: */
        public static void iniOperatorGridBox(){
            //settings grid ini
                settingsGrid = new GridPane();
                settingsGrid.setPadding(new Insets(25,25,10,25));       //resize?
                        //add methods that add v boxes for: all other operators
                settingsGrid.setGridLinesVisible(false);
            //setting grid gaps based on window size
                AtomicReference<Double> gridWidth = new AtomicReference<>(AdditionS.settingsGrid.getWidth());
                AtomicReference<Double> hSpacing = new AtomicReference<>((double) 0);
                //listener to get the height of the gridbox (resize) works!!
                    settingsGrid.widthProperty().addListener(e -> {
                        gridWidth.set(AdditionS.settingsGrid.getWidth());
                        hSpacing.set(dispQuiz.resize(gridWidth.get(), GlobalS.values.size(), filter.fixI(winW + "")));
                        settingsGrid.setHgap(hSpacing.get());
                        //System.out.println("change spacing: " + hSpacing + " box space: " + gridWidth);//debug 46
                        //System.out.println("change detected");
                    });
                //double gridWidth = AdditionS.settingsGrid.getWidth();
                //^ resize method to get a proper hgap, 88% coverage for 4 (for rn) boxes, total width
                //note: not working because need an accurate size of the individ setting pane window
                //System.out.println("spacing: " + hSpacing + " box space: " + gridWidth);//debug 46
                settingsGrid.setHgap(128);
                settingsGrid.setVgap(10);
            //add all boxes
                //empty box
                settingsGrid.setAlignment(Pos.CENTER);
                settingsGrid.add(AdditionS.settingsGrid, 0,0);
                settingsGrid.add(SubtractionS.settingsGrid, 1,0);
                settingsGrid.add(MultiplicationS.settingsGrid, 2,0);
                settingsGrid.add(DivisionS.settingsGrid, 3,0);
        }

        public static void iniAdditionGridBox(){
            //gridpane, vbox style: (centered text title (2 column width), 2nd row: 1stSettingName C1, inputC2.... etc)

            //note: making setting panel in operator(s) class???? take setting info as a setting
                //gridpane array?
                //gridpane array?
        }


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

    //deals with the navigation bar
        public static ImageView settingIcon;
        public static Button sButton;
        public static HBox settingBox;
        public static BorderPane navPane;
        public static void createNavBar() {
            settingIcon = new ImageView("settings_icon.png");
            sButton = new Button();
            settingBox = new HBox();
            navPane = new BorderPane();
            //setup icon
            settingIcon.setFitHeight(20);
            settingIcon.setPreserveRatio(true);
            //setup button w/icon
            sButton.setGraphic(settingIcon);
            sButton.setStyle("" +
                    "-fx-background-color: transparent;" +
                    "-fx-border-color: transparent;");
            sButton.setPadding(new Insets(1, 3, 1, 3));
            //sButton.setAlignment(Pos.CENTER);//
            //create button action
            sButton.setOnAction(e -> {
                callSettingButtonAction();
                //System.out.println("hello");
            });
            //create hBox with button
            settingBox.getChildren().add(sButton);
            //settingBox.setPrefHeight(20);
            settingBox.setMaxWidth(40);
            //settingBox.setStyle("-fx-border-color: white");
            //set borderpane
            navPane.setRight(settingBox);
            //borderP.rightProperty();
            navPane.setStyle("-fx-background-color: #383535");      //old colors: #8d07a8 #3434c7,#677f8a, #9cc7db!
        }

    //deal with button call
        public static boolean notMain = false; //settings pressed
        /**@apiNote called when the settings button is clicked* */
        public static void callSettingButtonAction(){
            //switch layouts?
            if(!notMain)    borderL0.setCenter(globalSettingsPane);
            else            borderL0.setCenter(questionVP);
            notMain = !notMain;
        }




    /**@apiNote launches the window*/
     public static void main(String[] args) throws IOException {
        /*
        //get all data into file ram objects
        GlobalS.setCorrectFileInfo(globalSTypes, globalSDefaultNames, globalSDefaultValues );
        GlobalS.retrieveData(true);
        Display.arrayS(GlobalS.names);

        DivisionS.setCorrectFileInfo(divSTypes,divSDefaultNames,divSDefaultVals);
        DivisionS.retrieveData(true);
        Display.arrayS(DivisionS.values);
            //Display.arrayS(GlobalS.lines);
              //Display.arrayS(GlobalS.values);
        AdditionS.setCorrectFileInfo(addSTypes,addSDefaultNames,addSDefaultVals);
        AdditionS.retrieveData(true);
        //Display.arrayS(AdditionS.values);
        SubtractionS.setCorrectFileInfo(subSTypes,subSDefaultNames,subSDefaultVals);
        SubtractionS.retrieveData(true);
        //
        MultiplicationS.setCorrectFileInfo(multSTypes,multSDefaultNames,multSDefaultVals);
        MultiplicationS.retrieveData(true);
        //

        //initialize operator classes
        addition = new Addition();
        subtraction = new Subtraction();
        division = new Division();
        multiplication = new Multiplication();

        //Display displayDiv = new Display();
        Operators.getGlobalSData();
        operators.createAllQuestions();
        operators.createQuiz();
        Prompts userQuestions = new Prompts();

        in.close();*/
         launch(args);
         //dispQuiz.displayResponses();//debug
    }

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

         /*
                     if(true){
             //live objects that store file data
             Store_Setting GlobalS = new Store_Setting("Global_Settings.txt");
             String[] globalSDefaultValues = {"0","0","0","0"};
             String[] globalSTypes = {"valueIB", "valueIB", "valueIB", "valueIB"};
             String[] globalSDefaultNames = {"Addition:", "Subtraction:", "Multiplication:", "Division:"};  //not used yet
             Store_Setting DivisionS = new Store_Setting("Settings" + File.separator + "Division_Settings.txt");
             String[] divSDefaultVals =    {"2","10","(1 ~ 144)","(1 ~ 12)","(1 ~ 12)","no","yes"};
             String[] divSTypes =          {"valueI","valueI","valueIR","valueIR","valueIR","valueIB","valueB"};
             String[] divSDefaultNames = {
                     "Difficulty Option:","Amount of Questions:",
                     "x1 Range Values:","x2 Range Values:","y Range Values:",
                     "Practice Value:","Show Correct answer:"};
             Store_Setting AdditionS = new Store_Setting("Settings" + File.separator + "Addition_Settings.txt");
             String[] addSDefaultVals =    {"2","10","(0 ~ 10)","(0 ~ 10)","(0 ~ 20)","no","yes"};
             String[] addSTypes =        divSTypes;        //the type of values are the same for all operators
             String[] addSDefaultNames = divSDefaultNames; //names is same for all operators
             Store_Setting SubtractionS = new Store_Setting("Settings" + File.separator + "Subtraction_Settings.txt");
             String[] subSDefaultVals =    {"2","10","(0 ~ 20)","(0 ~ 10)","(0 ~ 10)","no","yes"};
             String[] subSTypes =          divSTypes;
             String[] subSDefaultNames = divSDefaultNames;
             Store_Setting MultiplicationS = new Store_Setting("Settings" + File.separator + "Multiplication_Settings.txt");
             String[] multSDefaultVals =    {"2","10","(0 ~ 12)","(0 ~ 12)","(0 ~ 144)","no","yes"};
             String[] multSTypes =          divSTypes;
             String[] multSDefaultNames = divSDefaultNames;
         }
        * */
        //set up the store setting classes for all setting files
                String sDirectories = directories.get(3) + File.separator;  //for directories
            GlobalS = new Store_GlobalSetting(sDirectories + "Global_Settings.txt");
            AdditionS = new Store_OperatorSetting(sDirectories + "Addition_Settings.txt");
            SubtractionS = new Store_OperatorSetting( sDirectories + "Subtraction_Settings.txt");
            MultiplicationS = new Store_OperatorSetting(sDirectories + "Multiplication_Settings.txt");
            DivisionS = new Store_OperatorSetting(sDirectories + "Division_Settings.txt");
        //get all data into file ram objects
            GlobalS.setCorrectFileInfo(globalSTypes, globalSDefaultNames, globalSDefaultValues );
            GlobalS.retrieveData(true);
                //Display.arrayS(GlobalS.names);
        //get operator setting info into ram
            DivisionS.setCorrectFileInfo(divSTypes,divSDefaultNames,divSDefaultVals);
            DivisionS.retrieveData(true);
                DivisionS.iniX2Range();
                DivisionS.iniSettingLayout("Division");
            //Display.arrayS(DivisionS.values);
                /*//Display.arrayS(GlobalS.lines);
                  //Display.arrayS(GlobalS.values);*/
            AdditionS.setCorrectFileInfo(addSTypes,addSDefaultNames,addSDefaultVals);
            AdditionS.retrieveData(true);
                AdditionS.iniX2Range();
                AdditionS.iniSettingLayout("Addition");
            //Display.arrayS(AdditionS.values);
            SubtractionS.setCorrectFileInfo(subSTypes,subSDefaultNames,subSDefaultVals);
            SubtractionS.retrieveData(true);
                SubtractionS.iniX2Range();
                SubtractionS.iniSettingLayout("Subtraction");
            //
            MultiplicationS.setCorrectFileInfo(multSTypes,multSDefaultNames,multSDefaultVals);
            MultiplicationS.retrieveData(true);
                MultiplicationS.iniX2Range();
                MultiplicationS.iniSettingLayout("Multiplication");
            //

        //initialize operator subclasses
            addition = new Addition();
            subtraction = new Subtraction();
            division = new Division();
            multiplication = new Multiplication();

            GlobalS.iniCorrectAnswerDisplayBox();//initialize the box holding the scores

        //Display displayDiv = new Display();
        Operators.getGlobalSData();
        operators.calcMaxInputs();          //gets the maximum value (used for the length allowed in the input text box)
        operators.createAllQuestions();
        operators.createQuiz();
        //Prompts userQuestions = new Prompts();    //prompts class is obsolete
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
            public static Store_GlobalSetting GlobalS;
                public static String[] globalSDefaultValues = {"0","0","0","0"};
                public static String[] globalSTypes = {"valueIB", "valueIB", "valueIB", "valueIB"};
                public static String[] globalSDefaultNames = {"Addition:", "Subtraction:", "Multiplication:", "Division:"};  //not used yet
            public static Store_OperatorSetting DivisionS;
                public static String[] divSDefaultVals =    {"2","10","(1 ~ 144)","(1 ~ 12)","(1 ~ 12)","no","yes"};
                public static String[] divSTypes  =          {"valueI","valueI","valueIR","valueIR","valueIR","valueIB","valueB"};
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