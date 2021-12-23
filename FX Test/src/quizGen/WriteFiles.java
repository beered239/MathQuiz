package quizGen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

//note: specific to this application
public class WriteFiles {

    //var for questions
        private ArrayList<Question> questions;// = Operators.allQuestions;
    //var for names of saved tests
        private final String testFileNameSQ = "Test Sheet for Test ";//string questions
        private final String getTestFileNameSQA = "Answer Sheet ";//string questions and answers
    //var for counting file (keeps track of )
        public static Integer counter;
    //scanner (s?) & printwriter
        //private Scanner inFile;
        public PrintWriter toFullTest, toTestQuestions;
    //file directories (maybe use arraylist that can have multiple ones)
        ArrayList<String> fileDirectoriesS = new ArrayList<String >();    //unused for now
        public String savedTestsDirS;   //remember indexes: 1: empty  tests | 2: saved tests

    public WriteFiles() {
        //get question data

        //get setting data
            //remember: Boot. (GlobalS, DivisionS, etc)
    }

    public void retrieveCount() throws IOException {
        //PrintWriter outFile = new PrintWriter(ReadFiles.counterFile);
        //outFile.println(" ");
        //outFile.close();
        ReadFiles.saveCounter();
        ReadFiles.sendCounterInfo(this);
    }

    /**@purpose: creates both test save file after a test is completed
     * @note: to capture more info in case of shutdown: use this but add to the save as each answer is inputted..
     * */
    public void createSavedTestFile() throws IOException {
        String emptyTestPath = fileDirectoriesS.get(1);
        String savedTestPath = fileDirectoriesS.get(2);
        File testQuestions = new File(emptyTestPath + File.separator +      testFileNameSQ + counter + ".txt");
        File testSummary = new File(    savedTestPath + File.separator +    getTestFileNameSQA + counter + ".txt");
        toTestQuestions = new PrintWriter(testQuestions);
        toFullTest = new PrintWriter(testSummary);
        questions = Operators.allQuestions;
        for(Question q : questions){
            toFullTest.println(q + " " + q.getAnalyzedResponse());
            toTestQuestions.println(q);
        }
        //writing the correct percentage to
            toFullTest.print("Score: " + Boot.GlobalS.scoreS);
            if(Boot.GlobalS.scorePercentageD > 90) toFullTest.println(" Great Job!");
            toTestQuestions.close(); toFullTest.close();
    }

    public void addToSaveFiles(Question currentQuestion) throws IOException {
        File testQuestions = new File(testFileNameSQ + counter);
        File testSummary = new File(getTestFileNameSQA + counter);
        toTestQuestions = new PrintWriter(testQuestions);
        toFullTest = new PrintWriter(testSummary);

            toFullTest.println(currentQuestion + " " + currentQuestion.getAnalyzedResponse());
            toTestQuestions.println(currentQuestion);
            toTestQuestions.write("fdgjkh",0,2);
        toTestQuestions.close(); toFullTest.close();
    }

    /**@purpose: increase the counter by 1 as the application ends or closes
     * */
        public void increaseCounter() throws IOException{
            PrintWriter outCounter = new PrintWriter(ReadFiles.counterFile);
            outCounter.println(++counter);
            outCounter.close();
        }

    //
        public void createDir(String fileName){
            File file = new File(fileName);
            file.mkdir();
            //savedTestsDirS = fileName;
            fileDirectoriesS.add(fileName);         //remember indexes: 1: empty  tests | 2: saved tests
        }

    //first: saving question info into two folders


}
