package quizGen;

import java.util.ArrayList;

//really it is a class that holds question objects made from the operator classes
public class Operators {

        //idk if i'll use these yet
    //private static boolean[] operatorBools;// = new boolean[4];//{false,false,false,false};  // times, divide, add, minus;
    public static ArrayList<Question> aQuestions = new ArrayList<>();
    public static ArrayList<Question> sQuestions = new ArrayList<>();
    public static ArrayList<Question> mQuestions = new ArrayList<>();
    public static ArrayList<Question> dQuestions = new ArrayList<>(10);
    public static int[] numOfQuestionsInOp = new int[4];        //A,S,M,D

    public static ArrayList<Question> allQuestions;
    //file objects for most settings files (global & all operator files) (in main but I could make references here)



    public Operators()
    {}

    public static void addDivQuestion(Question question){
        dQuestions.add(question);
    }
    public static void addSubQuestion(Question question){
        sQuestions.add(question);
    }
    public static void addAddQuestion(Question question){
        aQuestions.add(question);
    }
    public static void addMulQuestion(Question question){
        mQuestions.add(question);
    }


    public static void getGlobalSData(){
            numOfQuestionsInOp = Boot.GlobalS.getIntSettingA(0,3,true);
                //for(int intVal : numOfQuestionsInOp)   System.out.print(intVal + ", ");    //debug
    }

    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////

    //A,S,M,D
    public void createAllQuestions() {
        int i;
        for(i=1; i<=numOfQuestionsInOp[0]; i++){
            addAddQuestion(Boot.addition.generateQuestion());
        }//for addition
        for(i=1; i<=numOfQuestionsInOp[1]; i++){
            addSubQuestion(Boot.subtraction.generateQuestion());
        }//for subtraction
        for(i=1; i<=numOfQuestionsInOp[2]; i++){
            addMulQuestion(Boot.multiplication.generateQuestion());
        }//for multiplication
        for (i = 1; i <= numOfQuestionsInOp[3]; i++) {
            addDivQuestion(Boot.division.generateQuestion());
        }//for division

    }//end of method

    //randomly add each method to a display arraylist.
    public void createQuiz(){
        int aSize = aQuestions.size(), sSize = sQuestions.size(), mSize = mQuestions.size(), dSize = dQuestions.size();
        //eX: 5,0,6,3   (14)  (0,4 skip  5,10  11,13)
        //ex: 1,2,1,1
        int r, listSize = aSize+sSize+mSize+dSize;
        allQuestions = new ArrayList<>(listSize);
        int iA=0, iS=0, iM=0, iD=0;

        //order: 0 12 3 4     42130 -> 4,2,1,1,0  | 0-4 0 12 3 4,   0-3noD 0 12 3 _ , 0-2 0 1 2 _, 0-1 0 _ 1 _ , 0-0 0
        //test irl: works!
            //System.out.println(aSize + " " + sSize + " " + mSize + " " + dSize + " " + listSize); //debug
        for(int i=0; i< listSize; i++){
            r  = Boot.selectRange.within(aSize+sSize+mSize+dSize   -1);
            if(r < aSize){
                allQuestions.add(aQuestions.get(iA)); iA++;aSize--;
            }//if it falls within the range of addition values
            else if(r<aSize+sSize){
                allQuestions.add(sQuestions.get(iS));iS++;sSize--;
            }
            else if(r<aSize+sSize+mSize){
                allQuestions.add(mQuestions.get(iM)); iM++;mSize--;
            }
            else if(r<aSize+sSize+mSize+dSize){
                allQuestions.add(dQuestions.get(iD)); iD++;dSize--;
            }//random choose to add

            //
        }
        //Boot.dispQuiz.displayAllQuestion();
    }

    //ArrayList<Integer> maxSizes = new ArrayList<Integer>(4);
    public Integer[] maxValues = new Integer[4];//asmd
    /**@apiNote assumes that y can only be an integer!!
     * @apiNote assumes that the 2nd y range value is the highest (no negative that could have a higher input size)
     * */
    public void calcMaxInputs(){
        Integer maxValue;
        String answerRangeLine;
        Store_Setting opInfo = Boot.GlobalS;    //just to initialize (could use = null)
        int numOfQ;
        for(int i=0; i<numOfQuestionsInOp.length;i++){
            numOfQ = numOfQuestionsInOp[i];
            if(numOfQ>0){
                if(i==0)        opInfo = Boot.AdditionS;
                else if(i==1)   opInfo = Boot.SubtractionS;
                else if(i==2)   opInfo = Boot.MultiplicationS;
                else if(i==3)   opInfo = Boot.DivisionS;
                answerRangeLine = opInfo.values.get(4);		//MAKE SURE THIS GETS UPDATED WHEN Y GETS UPDATED    (update valuesAL ram to get an accurate answer)
                maxValue = opInfo.getRangeValuesFromSetting(answerRangeLine)[1];
                maxValues[i] = maxValue;
            }
        }
    }


    public static void clearQuestions() {
    	allQuestions.clear();
    	aQuestions.clear();
    	sQuestions.clear();
    	mQuestions.clear();
    	dQuestions.clear();
    }
    

}//end of class


/*
 * Plan for class:
 *
 * - goal: create classes based on their operator settings and then add them to an array!
 *
 * */