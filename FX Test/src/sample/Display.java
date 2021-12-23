package sample;//import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings({"rawtypes", "unused"})
public class Display {

    //variables
        private int probNum = 0;


    //para: object of the operator (division so far)
    public Display()
    {
        //get name/type of operator?
    }

    public void disQuestionAndAnswer(Question op){
        System.out.print("Question " + ++probNum  + ": " + op.getX1() + op.getSymbol() + op.getX2() + " = " + op.getAnswer());
    }

    public void disQuestion(Question op){
        System.out.print("Question " + ++probNum  + ": " + op.getX1() + op.getSymbol() + op.getX2() + " = ");
    }

    public void displayAllQuestion(){
        ArrayList<Question> questions = Operators.allQuestions;
        for(Question q : questions)
        {
            disQuestionAndAnswer(q); System.out.print("\n");
        }
    }

    public void screenDisplayAllQuestions(){
        ArrayList<Question> questions = Operators.allQuestions; //just for ease
        //for(Question q : questions){
          //  screenDisplayQuestionInfo();
            //screenDisplayQuestion(q);
        //}

    }

    public void screenDisplayQuestionInfo(){
        System.out.println("Question: " + probNum++ + ": ");
    }

    public static int qIndex = 0;
    public static ArrayList<Question> questions = Operators.allQuestions;   //easier
    public void screenDisplayQuestions() throws Exception{
        //Question question = questions.get(qIndex);
        Main.questionInput.setOnAction(e -> {
            System.out.println(Main.questionInput.getText());
            Main.setQuestionHBox(Operators.allQuestions.get(qIndex).toString());
            qIndex++;
            //submit after every enter
        });
    }

    //display responses (in console)
        /**@apiNote gets question responses from Operators.allQuestions arraylist and displays them after the app window is closed
         * */
        public void displayResponses(){
            System.out.println("start displaying responses: ");
            questions = Operators.allQuestions;
            for(Question q : questions){
                System.out.println("Response: " + q.getResponse());
            }
        }



    //more general/less to do with quizzes

        /**@purpose: to display any arraylist (each value in their own line)
         * @apiNote displays inputted arraylist
         * @param array the arraylist being displayed
         * */
        public static void arrayS(ArrayList array){
            for(Object txt : array){
                System.out.println(txt);
            }
        }

        ////////////////////////////////Specific to javafx///////////////////////////////////////////////////
            /**@purpose: to organize elements in even spacing (vertical or horizontal)
             * @note: used to resize the settings grid hgap
             * @param individBoxSize the size of one 'box'
             * @param boxNum amount of content boxes that need to make up that percentage
             * @param totalSpace the total space to work with
             * @note: there will be spacing at the start and end
             * */
            public double resize(double individBoxSize, int boxNum, int totalSpace){
                double totalSpaceTaken = (individBoxSize*boxNum);
                //double individSize = totalSize/(double)boxNum;
                double leftOver = totalSpace-totalSpaceTaken;
                int totalSpaces = boxNum+1;
                double spacing = leftOver/(double)totalSpaces;
                return spacing;
            }



}
