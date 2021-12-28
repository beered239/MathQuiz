package quizGen;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Store_GlobalSetting extends Store_Setting{

    public Store_GlobalSetting(String fileNameS){
        super(fileNameS);
    }

    

    public void addCorrectValue(){

    }

    /**@purpose: to have booleans set to represent there being or not being any questions for an operator
     * @note: useless rn*/
    public void createOperatorScoreInstances(){
        boolean add=false,sub=false,mul=false,div=false;
        if(Integer.parseInt(values.get(0)) > 0){
            add = true;

        }//addition
        else if(Integer.parseInt(values.get(1)) > 0){
            sub = true;
        }//sub
        else if(Integer.parseInt(values.get(2)) > 0){
            mul = true;
        }//multiplication
        else if(Integer.parseInt(values.get(3)) > 0){
            div = true;
        }//division
    }

    public Integer totalRight = 0;
    public Integer questionsCompleted = 0;
    public Integer totalQuestions = 0;
    public Integer[] correctOperatorA;  //array for int values for correct answers for each operator
        //a,s,m,d
    public ArrayList<Text> differingScores;
    public Text totalScore;
    public HBox scoreHBox;
    //display box for the correct score
    /**
     * @note: initialize in the initialize method in main,
     * */
    public void iniCorrectAnswerDisplayBox(){
        scoreHBox = new HBox();
        iniTotalQ();
        updateTotalScore(); //gets the totalScore textbox set up
        setScoreStyle(totalScore);
        differingScores = new ArrayList<Text>(4);// = new Text[4];
        correctOperatorA = new Integer[]{0, 0, 0, 0};//new Integer[values.size()];
        scoreHBox.getChildren().add(totalScore);
        for(int i=0; i<correctOperatorA.length; i++){
            if(Integer.parseInt(values.get(i)) > 0){
                differingScores.add(i,new Text(names.get(i) + " 0/" + values.get(i)));   //...could replace with 0 since at the start no questions are right??
                scoreHBox.getChildren().add(differingScores.get(i));
                scoreHBox.setPadding(new Insets(3,20,3,20));
                setScoreStyle(differingScores.get(i));
            }
            else
                differingScores.add(i,null);//add null or text saying n/a with op name next to it?
            // new Text(names.get(i) + "N/A") -> make sure to add null check
        }
        scoreHBox.setSpacing(40);   //resize?
        scoreHBox.setAlignment(Pos.CENTER);
    }
        //used by ^
            /**@purpose: to set the score to the index of the operator's score */
            public void setScore(int i, Integer correct){
                differingScores.set(i,new Text(names.get(i) + correct + "/" + values.get(i)));
            }
            public void setScore(int i){
                correctOperatorA[i]++;
                String score = names.get(i) + " " + (correctOperatorA[i]) + "/" + values.get(i);
                //System.out.println(score);
                differingScores.set(i,new Text(score));
                //System.out.println(differingScores.get(i).getText());//debug
            }
            public void refreshCorrectAnswerDisplayBox(){
                int size = scoreHBox.getChildren().size();
                //scoreHBox.getChildren().removeAll();
                scoreHBox.getChildren().clear();
                //deal with total
                    //totalRight++; updateTotalScore();
                    scoreHBox.getChildren().add(totalScore);
                    setScoreStyle(totalScore);
                //deal with other fractions
                    for(int i=0; i<differingScores.size(); i++){
                        if(differingScores.get(i) != null){
                            scoreHBox.getChildren().add(differingScores.get(i));
                            setScoreStyle(differingScores.get(i));
                        }
                    }
            }
            public void setTotalRight(int totalRight){this.totalRight = totalRight;}
            public void updateQuestionsCompleted(){
                questionsCompleted++;
            }
            /**@apiNote finds the total questions for the test*/
            public void iniTotalQ(){
                //values 0-3
                for(int i=0; i<3; i++){
                    totalQuestions += Integer.parseInt(values.get(i));
                }
            }
            /**@purpose: to update score based on the total questions that have been completed */
            public void updateTotalScore(){
                //updateQuestionsCompleted();
                //totalScore = new Text("Total: " + totalRight + "/" + questionsCompleted);
                calcPercentageCorrect();
                totalScore = new Text("Correct: " + scoreS);//based on current completed
            }
            /**@apiNote sets the text style for the score info*/
            public void setScoreStyle(Text score){
                score.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.ITALIC, 20));
                score.setStyle("" +
                        "-fx-fill: #C8FDFB;"); //#011b29
            }

    //
    public String scoreS;
    public double scorePercentageD;
    /**@apiNote edits scoreS and scorePercentageD to accurately reflect the current score*/
    public void calcPercentageCorrect(){
        scorePercentageD = (totalRight/(questionsCompleted * 1.0)) * 100;
        scorePercentageD = Boot.filter.roundTo(scorePercentageD,2);
        if(Boot.filter.findNumOfDec(scorePercentageD) == 0)
            scoreS = (int)scorePercentageD + "%";
        else
            scoreS = scorePercentageD + "%";
    }

}
