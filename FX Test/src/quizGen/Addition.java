package quizGen;

@SuppressWarnings("ALL")
public class Addition extends Operator{

    //private variables
        /*private static int difficultyI; //tech not needed
        private static int numOfQ;*/
        public static Integer[] pt2Range;// = Boot.DivisionS.getRangeValuesFromSetting(Boot.AdditionS.values.get(3));//fix
        public static int practiceNum;// = Boot.filter.fixI(Boot.AdditionS.values.get(5));//fix
    public Addition(){
        setSymbol(Symbols.PLUS);
        //numOfQ = Integer.parseInt(Boot.AdditionS.values.get(1));
        pt2Range = Boot.AdditionS.getRangeValuesFromSetting(Boot.AdditionS.values.get(3));
        practiceNum = Boot.filter.fixI(Boot.AdditionS.values.get(5));
    }

    //generate parts methods
    /**
     * Name: genPt1()
     * @purpose: to generate the first key part of the question
     * */
    public void genPt1(){
        super.genPt1(Operator.level1DefaultRange);
    }

    /**@purpose: to generate a number that will depend on the difficulty chosen
     * @apiNote generate rand num based on difficulty
     * */
    public void genPt2(){
        //difficulty: 1-5 --> easy mode, medium mode, hard mode, sandbox/range mode, practice mode
        super.genPt2(Boot.AdditionS,difficultyValAdd,4);
    }

    public void genPt3(){
        super.genPt3((getPt1() + getPt2()));
        setOrder(getPt1(), getPt2(), getPt3());
    }

    public Question generateQuestion() {
        genPt1();genPt2();genPt3();
        return new Question(getX1(), getX2(), getY1(), getSymbol());
    }



}
