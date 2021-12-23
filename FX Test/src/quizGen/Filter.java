package quizGen;

public class Filter {

    private boolean negative = false;
    private boolean decimal = true;
    private boolean basedRounding = false;

    public static final double invalidD = (Integer.MIN_VALUE+1) ;     //maybe use integer and double classes to catch a null value?

    public Filter(){

    }

    /**
     * @purpose: filters out anything that is not a number (- & . allowed with conditions)
     * */
    public double fixD(String input){
        if(input.length() == 0){return invalidD;}             //returns invalidD error if there is no length

        //checks if value should be negative
        negative = input.charAt(0) == '-';

        String valS = input.replaceAll("[^0-9/.]","");      //removes all non numbers or decimal points
        double valD; int decimalIndex;

        if(valS.equals("")){return invalidD;}                            //returns invalidD error if there is no length;

        decimalIndex = valS.indexOf(".");                         //find index of the first decimal point

        valS = valS.replaceAll("/.","");        //removes all the periods from the number (temp)

        if(decimalIndex == -1)
            decimal = false;
        else{
            valS = valS.substring(0, decimalIndex) + "." + valS.substring(decimalIndex+1);  decimal = true;}
        //System.out.println(valS);//debug

        valD = Double.parseDouble(valS);        //changes string to double

        if(negative){valD = -1*Math.abs(valD);} //makes negative again
        //debug to check all values:
         //System.out.println("Negative: " + negative + " Decimal: " + decimal + " decimal index: " + decimalIndex
           //     + " OG input: " + input + " valS: " + valS + " valD: " + valD);

        return valD;
}

    public int fixI(String input)
    {
        //System.out.println(input);    //debug
        double valD = fixD(input);  //System.out.println(valD);
        //if you want the number to be accurately rounded
            if(basedRounding && !seeIfInvalid(valD)){
                int roundConst = 5;
                //if you have a negative number, subtract 0.5 from the enlarged double value to round it
                    if(valD < 0){
                    roundConst *= -1;
                }
                //brings the number to the next 10 place and rounds it, before changing it back to normal.
                    return ((int)((valD*10)+roundConst)) / 10;  //note: don't need 10.0 in this case
            }
        //else it will always round down
            else{
            return (int)valD;
        }       //boolean for an accurate rounding is false
     }

     //probably doesn't belong here
    /**
     * @param valueD: decimal value
     * @param decPlace: the decimal place you want to round up to
    * */
     public double roundTo(double valueD, int decPlace)     //20.212
     {
         if(seeIfInvalid(valueD))   //checks to see if double is invalid to quickly end method
             return invalidD;
        double multiplier = Math.pow(10,decPlace);
        return  (int)(valueD*multiplier+0.5) / multiplier;
     }

     public int findNumOfDec(double valD)
     {
         String valS = Double.toString(valD);
         int slength = valS.length();
         // decIndex = valS.indexOf(".")
         // maxIndex = valS.length()-1
         // multiplier = maxIndex - decIndex
         if(valS.contains("."))
             if(valS.substring(slength-1).equals("0") && valS.indexOf(".") == slength-2 ){
                 return 0;
             }//returns 0 is num is: xx.0
             else   return slength-1 - valS.indexOf(".");
         else{
                return 0;   //maxIndex - decimal point index = # of decimal points
         }
         //multiplier = (int) Math.pow(10,multiplier);
         //System.out.println(valD + " " + multiplier);//debug
         //ex: 23
     }


    public void setBasedRounding(boolean check)
    {
        basedRounding = check;
    }

    //all should have
    /**
     * @purpose: to check if the parameter being inputted is a valid number
     * @return: returns true if the value is invalid, false if the value isn't an error
     * */
    public static boolean seeIfInvalid(double value)
    {
        return value == invalidD;
    }



    //resetDefaults: resets all booleans to their normal values
    public void resetDefaults(){
        negative = false;
        decimal = true;
        basedRounding = false;
    }

}
