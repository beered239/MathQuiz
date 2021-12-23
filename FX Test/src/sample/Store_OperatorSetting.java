package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Store_OperatorSetting extends Store_Setting{

    //x2 range
        public Integer[] x2Range;   //custom range r1-r2 for pt2
        //public File fileDir;    //
        //public PrintWriter outFile;

    public Store_OperatorSetting(String fileNameS){
        super(fileNameS);
        //fileDir = new File(fileNameS);
        //pt2Range = getRangeValuesFromSetting(values.get(3));    //gets x2 range values
    }

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

    }



    //important variables
    public Text[] namesT;
    public TextField[] valuesTF;
    public ComboBox difficulty;        //choicebox vs combobox  (for difficulty)
        public ObservableList<String> options;
    public TextField range1, range2;
    public HBox rangeBoxX2;
    //for practice mode textfield (make a custom textfield class later)
        public TextField practiceModeTF;
    //public HBox[] valuesHB;   //not needed rn?
    public int difficultyNum;

    public void iniSettingLayoutVariables(){
        int size = names.size();        //assumes size is the same for values and names
        namesT = new Text[size];
        valuesTF = new TextField[size];
        //valuesHB = new HBox[size];
        //difficulty box
        options =
                FXCollections.observableArrayList(
                        "Easy Mode","Normal Mode", "Hard Mode", "Sandbox Mode", "Practice Mode");   //dependant
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
                    //System.out.println("text: " + difficulty.set);
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

    //loop for all values (non specific)
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
                //method to enable fields
                    iniEnabledStates(difficultyNum);

            }
            /**@purpose: to enable textfields based on the difficulty selected
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

            public void x2RangeSelected() throws IOException{
                //outFile = new PrintWriter(fileDir);
                String range = "(" + range1.getText() + " ~ " + range2.getText() + ")";
                changeSettingLineOnIndex(3,range);
            }

            public void practiceModeSelected() throws IOException{
                String practiceNumS = practiceModeTF.getText();
                int practiceNumI = Main.filter.fixI(practiceNumS);
                changeSettingLineOnIndex(5,practiceNumI+"");
            }


    //
    public int correctCounter = 0;
    public void addToCorrectCounter(){

    }

}
