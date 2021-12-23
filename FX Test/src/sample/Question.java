package sample;

public class Question {

    private  int x1;

    private  int x2; //numbers being asked to be solved
    private  int answer;     //answer

    private String symbol;

    private int response;
    private String analyzedResponse;

    public Question(int x1, int x2, int y1, String symbol)
    {
        this.x1 = x1;
        this.x2 = x2;
        answer = y1;
        //response
        this.symbol = symbol;
    }


    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public String getAnalyzedResponse() {
        return analyzedResponse;
    }

    public void setAnalyzedResponse(String analyzedResponse) {
        this.analyzedResponse = analyzedResponse;
    }

    public void calcAnalyzedResponse(){
        //analyzedResponse = response + ""
        String infoOnResponse;
        Main.GlobalS.updateQuestionsCompleted();    //update stating that a question was completed
        Main.GlobalS.updateTotalScore();
        if(getResponse() == getAnswer()){
            infoOnResponse = " * correct";
            addCorrectToOperator();
        }//if correct
        else  infoOnResponse = " * incorrect (answer was " + answer + ")";
        analyzedResponse = response + infoOnResponse;
        Main.GlobalS.refreshCorrectAnswerDisplayBox();
    }

    public void addCorrectToOperator(){
        Store_GlobalSetting gSettings = Main.GlobalS;//.totalRight++;
        gSettings.setTotalRight(gSettings.totalRight+1);
        gSettings.updateTotalScore();
        if(symbol.equals(Symbols.PLUS))
            gSettings.setScore(0);
        else if(symbol.equals(Symbols.MINUS))
            gSettings.setScore(1);
        else if(symbol.equals(Symbols.TIMES))
            gSettings.setScore(2);
        else if(symbol.equals(Symbols.DIVIDE))
            gSettings.setScore(3);
        //gSettings.refreshCorrectAnswerDisplayBox();
    }


    //more efficient: make a method that calculates the max value once (for every operator) and then get that value for everything else

    /**@purpose: get the max value based on the questions symbol
     * @apiNote returns doubles but can only get an int for now
     * */
    public Double getMaxValue(){
        Integer[] range = new Integer[2];
        String rangeLine;
        if(symbol.equalsIgnoreCase(Symbols.PLUS)){
            return (double)Main.operators.maxValues[0];
        }
        else if(symbol.equalsIgnoreCase(Symbols.MINUS)){
            return (double)Main.operators.maxValues[1];
        }
        else if(symbol.equalsIgnoreCase(Symbols.TIMES)){
            return (double)Main.operators.maxValues[2];
        }
        else if(symbol.equalsIgnoreCase(Symbols.DIVIDE)){
            return (double)Main.operators.maxValues[3];
        }
        else    System.out.println("Error: Operator was not set on this list");     //potential error
        return null;
    }

    /**@purpose: gets the max input needed for a question depending on the range of answers for the operator
     * */
    public Integer getMaxInputLength(){
        if(Main.filter.findNumOfDec(getMaxValue()) == 0)
            return Integer.toString(getMaxValue().intValue()).length();		//if no decimals
        else
            return (Double.toString(getMaxValue())).length();				//else if decimals
    }


    public String toString(){
        return "" + getX1() + getSymbol() + getX2() + " =";
    }
}
