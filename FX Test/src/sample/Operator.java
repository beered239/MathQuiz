package sample;

public class Operator {

    private  int x1,x2,y1;     //ex: x1 OP x2 = y1

    private int pt1, pt2, pt3; //(values depend on what operator is used) pt1: default, pt2: depends on difficulty, pt3: found from 1 and 2

    private String symbol;

    //ranges for (- & /) & (+ * x)
    public static final int[] level1DefaultRange = {0,1,2,10,			3,4,5,6,7,8,9};//{0,1,2,3,4,5,6,7,8,9,10};          //for addition and subtraction
    public static final int[] level2DefaultRange = {0,1,2,3,4,5,6,7,8,9,10,11,12}; //for multiplication and division
    //public static final Integer[] specialNumberSelection = {1,5,10,11,0,2,3,4,6,7,8,9,12};
    public static final int[] difficultyValDiv = {1,2,5,10,11,       3,4,6,7,8,9,12};                           //  /
    public static final int[] difficultyValMulti = {0,1,2,5,10,11,       3,4,6,7,8,9};                          // x
    public static final int[] difficultyValAdd = level1DefaultRange, difficultyValSub = level1DefaultRange;  //+ & -
    /*addition indexes: 0-
        division & multiplication will have separate arrays
               Division: 1,2,5,10,11,       ,3,4,6,7,8,9
        addition & subtraction will have the same               --> use level1DefaultRange from the getgo
    * */
    //ArrayList<Integer> sdf = new ArrayList<Integer>(Arrays.asList(specialNumberSelection));;

    /*custom x1,x2, & y ranges
    note: level 2 default range is already the x1 range?
    note: will only contain x2 range for now
    note: assume x2 range is in line 4 for all of them
    note: rename these ranges to pt1, pt2, pt3???*/

    //public static int[] x2Range =

    public Operator()
    {

    }


//unused old method
/*
*     public void createDiv()
    {
        //Division.generateQuestion();        //will update values based on
        //Operators.addOperator(this);        //adds to arraylist of questions in operators
    }

* */

    //custom:
    public void genPt1(int[] range){
        pt1 = Main.selectRange.withinArray(range); //((0 to 0.99) * (13)) + 0    --> 0-12       (note: already gets sorted in method.. but should still be optional)
    }

    /**@purpose: to generate x2(pt2) based on the difficulty selected
     * @apiNote to generate x2 based on the difficulty selected
     * @importantVars: difficultyI: difficulty number inputted by the class taking care of it
     * @param settingInfo object that has most info on an operator file
     * @param range the range of values each operator uses (differs between operators)
     * @param howMany the number of indexes from the start of 'range' that will be affected
     * */
    public void genPt2(Store_Setting settingInfo, int[] range, int howMany){

        int difficultyI = Integer.parseInt(settingInfo.values.get(0));
        Integer[] pt2Range = settingInfo.getRangeValuesFromSetting(settingInfo.values.get(3));
        int practiceNum = Main.filter.fixI(settingInfo.values.get(5));

        int index, value;
             if(difficultyI == 1){
            index = Main.selectRange.chance(range.length,0.12,howMany);     //howMany: AS: 4, D: 5, M:6
            pt2 = range[index];
        }//easy mode
        else if(difficultyI == 2){
            pt2 = Main.selectRange.withinArray(range); return;
        }//normal mode
        else if(difficultyI == 3){
            index = Main.selectRange.chance(range.length,0.03, howMany);
            pt2 = range[index];
        }//hard mode
        else if(difficultyI == 4){
            pt2 = Main.selectRange.within(pt2Range[0],pt2Range[1]);
        }//sandbox mode
        else if(difficultyI == 5){
            pt2 = practiceNum;
        }//practice mode

        //note: add error for going over 5 just in case
    }

    /**@purpose: to generate the last part of the problem based on the first two values
     * @apiNote (x1 or y) generates the last part of the problem based on the first two values
     *
     * */
    public void genPt3(int value){
        pt3 = value;
    }

    public void addQuestion(String qType){
        if(qType.equalsIgnoreCase("d") )
            Operators.addDivQuestion(new Question(x1,x2,y1,symbol));
        else if(qType.equalsIgnoreCase("s"))
            Operators.addSubQuestion(new Question(x1,x2,y1,symbol));
        //ADD OTHERS
    }


    //get and set:

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public  int getY1() {
        return y1;
    }

    public  void setY1(int ans1) {
        y1 = ans1;
    }

    public  int getX1() {
        return x1;
    }

    public  void setX1(int val1) {
        x1 = val1;
    }

    public  int getX2() {
        return x2;
    }

    public  void setX2(int val2) {
        x2 = val2;
    }

    public int getPt1(){return pt1;}
    public int getPt2(){return pt2;}
    public int getPt3(){return pt3;}

    public void setOrder(int x1, int x2, int y1){
        this.x1 = x1; this.x2 = x2; this.y1 = y1;
    }
}


//maybe make an operator holder: to hold operator objects?