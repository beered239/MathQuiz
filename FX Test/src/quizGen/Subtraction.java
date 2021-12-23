package quizGen;

public class Subtraction extends Operator{

    //note: difficulty and num of questions are already saved in settings object (and not needed here)
    public static Integer[] pt2Range;   //custom range r1-r2 for pt2
    public static int practiceNum;      //custom practice number
    public Subtraction(){
        setSymbol(Symbols.MINUS);
        pt2Range = Boot.SubtractionS.getRangeValuesFromSetting(Boot.SubtractionS.values.get(3));
        practiceNum = Boot.filter.fixI(Boot.SubtractionS.values.get(5));
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
            super.genPt2(Boot.SubtractionS,difficultyValSub,4);
        }
    /**@purpose: to generate the result from pt1 and pt2
     * */
        public void genPt3(){
            super.genPt3((getPt1() + getPt2()));
            setOrder(getPt3(), getPt2(), getPt1());
        }
    /**@apiNote returns a question object using op's values*/
    public Question generateQuestion() {
        genPt1();genPt2();genPt3();
        return new Question(getX1(), getX2(), getY1(), getSymbol());
    }//end of method
}//end of class