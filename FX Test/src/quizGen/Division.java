package quizGen;//this method will use objects just in case you want to complicate things further with multiple types of division settings

public class Division extends Operator {

    //variables

        //difficulty level:
            private static int difficultyI;

    private static int numOfQ;
    //private static int difficultyOption;    //the option that will affect which method gets used and how x2 is determined

    //range
    //multiplier

    private static int minRangeMulti, maxRangeMulti;
    //numerator
    private static int minRangeDenom, maxRangeDenom;
    //values

    private int denominator;  //can come from the numerator range
    private int answer;     //can come from the multiplier range
    //leave default for now (set methods will take care of it... later find methods will take info from files)

        /*custom x1,x2, & y ranges
    note: level 2 default range is already the x1 range?
    note: will only contain x2 range for now
    note: assume x2 range is in line 4 for all of them
    note: rename these ranges to pt1, pt2, pt3???*/

    public static Integer[] pt2Range = Boot.DivisionS.getRangeValuesFromSetting(Boot.DivisionS.values.get(3));

    public static int practiceNum = Boot.filter.fixI(Boot.DivisionS.values.get(5));

    public Division(){
        setSymbol(Symbols.DIVIDE);
        numOfQ = Integer.parseInt(Boot.DivisionS.values.get(1));
    }


    //looks at the array and specific sections in the file class
    //the file class reads and stores information from the text file representing settings
    public void findRanges() {
    }

    //update x1,x2, & y
    public void updateOpInfo(){

    }

    /**
     * Name: genPt1()
     * @purpose: to generate the first key part of the question ()
     * */
    public void genPt1(){
        super.genPt1(Operator.level2DefaultRange);
    }

    /**@purpose: to generate a number that will depend on the difficulty chosen
     * @apiNote generate rand num based on difficulty
     * */
    public void genPt2(){
        //difficulty: 1-5 --> easy mode, medium mode, hard mode, sandbox/range mode, practice mode
        super.genPt2(Boot.DivisionS,difficultyValDiv,5);
    }

    public void genPt3(){
        super.genPt3(getPt1() * getPt2());
        setOrder(getPt3(), getPt2(), getPt1());
    }

    //generate question: as a default it should only include the set methods, the others should be separated later

    public Question generateQuestion() {
        /*
        * denominator = calcNum(minRangeDenom, maxRangeDenom);   //X2 value is made
        setX2(denominator);
        answer = calcNum(minRangeMulti, maxRangeMulti);
        setY1(answer);
        setX1(getY1() * getX2());
        * */
        genPt1();genPt2();genPt3();
        return new Question(getX1(), getX2(), getY1(), getSymbol());
        //System.out.println(denominator + " " + answer);   //debug
    }

    /**
     * addQuestion: creates a question obj based on the values from the values in this class
     *      - then it adds it to the operators, arraylist of questions.
     */
    public void addQuestion()
    {
        generateQuestion();
        Operators.addDivQuestion(new Question(getX1(), getX2(), getY1(), getSymbol()));
        //System.out.println(String.format("%d %d %d %s", getX1(), getX2(), getY1(), getSymbol())); //debug
    }

    //maybe don't use 2 methods below
    /*public void addQuestion()
    {
        generateQuestion();
        Operators.addDivQuestion(this);     //ISSUE HERE: NEED TO KEEP MAKING A CERTAIN NUMBER OF OBJECTS
    }*/

    public void addQuestions()
    {
        for(int i=0; i<Division.getNumOfQ(); i++)
            addQuestion();
    }


    //generates multiplier/answer

    public static int calcNum(int min, int max)
    {
        //make a instance variable later (the random number/answer)
        //range: x1 - x2    --> in math random Math.random()
        //ex: 4-10          --> Math.random()+10
        //for now the default answers lie between 0-12
        //make these the instance variables
        //min= 0, max = 12;
        int range = max-min+1;  //13 values
        return (int) ((Math.random() * range) + min);   //random number based on range
    }

    //Temporary get and set  methods

    public int getMinRangeMulti() {
        return minRangeMulti;
    }
    public static void setMinRangeMulti(int minRangeMultiX) {
        minRangeMulti = minRangeMultiX;
    }

    public int getMaxRangeMulti() {
        return maxRangeMulti;
    }

    public static void setMaxRangeMulti(int maxRangeMultiX) {
        maxRangeMulti = maxRangeMultiX;
    }

    public int getMinRangeDenom() {
        return minRangeDenom;
    }

    public static void setMinRangeDenom(int minRangeDenomX) {
        minRangeDenom = minRangeDenomX;
    }

    public int getMaxRangeDenom() {
        return maxRangeDenom;
    }

    public static void setMaxRangeDenom(int maxRangeDenomX) {
        maxRangeDenom = maxRangeDenomX;
    }

    public static int getNumOfQ() {
        return numOfQ;
    }

    public static void setNumOfQ(int numOfQ) {
        Division.numOfQ = numOfQ;
    }
}
//note: have a problem class with subclasses for storing division, and other types of problems