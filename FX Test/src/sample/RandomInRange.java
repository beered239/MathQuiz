package sample;

import java.util.Random;

//note: make objects even though they don't have to be objects

public class RandomInRange {


    //object name: select range
    public RandomInRange(){

    }

    //Assume sorted: no
    public int withinArray(int[] array){
        Sort.mergeSortI(array, 0,array.length-1);       //optional sorting (keeping anyway)
        return array[(int) (Math.random() * (array.length ))];
        //note: this line returns a random number between the max and min values of the array based on positioning
        //return (int) (Math.random() * (array[array.length-1]-array[0] + 1) + array[0]);
    }

    //pt1-pt2 format
    public void stated(String text){

    }

    public int within(int pt1, int pt2){
        int max = Integer.max(pt1,pt2);
        int min = Integer.min(pt1,pt2);
        return (int) ((Math.random() * (max+1)) + min);
    }
    public int within(int val){
        //return (int) ((Math.random() * (val+1)));
        return within(0,val);
    }//default: 0-value inclusive



    //eventual goal: input info on an array, return the randomly generated index
    //initialize your array with the first values being the ones you want affected
    /*NOTE: CHANGE THE CHANCE DECIMAL TO A PERCENTAGE BY X100 AND THEN CHANGE THE VALUE TO A COMPLETE WHOLE NUMBER*/
    /**@purpose: return a random index for the values that represent part of the array
     * @apiNote returns an int representing the index of the array in question
     * @param amountOfVal:  number of indexes (size of array)
     * @param chance: 		decimal chance you want the beginning values to have of being selected
     * @param howMany:		how many starting values do you want this percentage to apply to (# of values that will be affected starting from the start)
     **/
    public int chance(int amountOfVal, double chance, int howMany){		//int listOfVal[], double chance, int r1, int r2    //(mix.chance(13, .5, 5))
            /*note: int amountOfVal = listOfVal.length;*/
        int chanceIdenom, chanceAgain, i, chanceInum; 	//chanceInum
        Random randNum = new Random();
        int multiplier = 100;	//the multiplier from the loop
        int chooser;		//really important it basically is the value that is tested on with the if statements to determine what index to use

        //changes any small decimal to a percentage:
            /*Turns chance*multiplier -> to a double
            * if it is not equal to a whole number -> the multiplier increases*/
        while( (Double.parseDouble((String.format("%.5f", chance*multiplier).trim())) != (int)(chance*multiplier  )) )
            {multiplier = multiplier*10;}
        chanceAgain = (int)(chance*multiplier); //ex:2		//percentage * x to whole number
        chanceInum = amountOfVal * chanceAgain;	// 1% as a whole number * the percentage needed as a whole number
        chanceIdenom = amountOfVal * multiplier; //ex:2			//the whole number extended to make the numerator the right percentage
        // 25% 25/2500   --> you got your 1% and if you wanted 14% -->  25*14 / 2500 which is 14%

        //create a random num gen for the index value temporarily
        chooser = randNum.nextInt(chanceIdenom)+1;//ex: 1-1		//plan get an index from min-max  so 1-max value
        //debug statements:
        //System.out.println("num from random range: " + chooser + " percentage as a whole number: " + chanceAgain + " Multiplier: " + multiplier + " \nthe whole to get a percentage: " + chanceIdenom + " and the numerator: " + chanceInum);//TEST

        //create an if statement and a loop if else if statements that change the index accordingly
        for(i=1; i<(howMany+1); i++)//ex: repeat once
        {
            if(chooser < (chanceInum*i) +1)                 //if the random index fits in any of the ranges for each start index value
                {return i-1; }  //returns the index for that value that had the range in it
            else if(chooser > ((chanceInum*howMany)-1)  )   //if the random value does not fit into any of the ranges
                { return (randNum.nextInt(amountOfVal-howMany) + howMany); }    //pick a random index that does not include the start numbers
        }
        return 12345;
    }


//between: exclusive of the last value
//within: inclusive of all values

}
