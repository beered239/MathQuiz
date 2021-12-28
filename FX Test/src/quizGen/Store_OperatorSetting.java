package quizGen;

import java.io.IOException;

import custom_element.OPSettingsGrid;

public class Store_OperatorSetting extends Store_Setting{

	//static constant? variables used by all of the objects made with this class
		public static final int[] indexesForSetting = {0,3,5};
	
	
    //x2 range
        public Integer[] x2Range;   //custom range r1-r2 for pt2
        //public File fileDir;    //
        //public PrintWriter outFile;

    public Store_OperatorSetting(String fileNameS){
        super(fileNameS);
        //fileDir = new File(fileNameS);
        //pt2Range = getRangeValuesFromSetting(values.get(3));    //gets x2 range values
    }

    /**@apiNote turns the x2Range instance variable to have the information from the appropriate line (contains x2 values)
     * */
    public void iniX2Range(){
        x2Range = getRangeValuesFromSetting(values.get(3));    //gets x2 range values
    }

    //make setRange, set difficulty, set practice value


            /**@purpose: to update the y value ranges once the settings have been changed
             * 
             * @note: use Double to already start implementation of doubles 		//sole implementation for integers since getRangeValuesFromSettings still only returns integers
             * */
            	public void updateYRange(String operatorSymbol) {
            		//update values array by rereading the operators file
            		
            		//if(Main.filter.findNumOfDec( Double value that could be an int ) == 0)
                    
            		
            		try {retrieveData(true);//true b/c there's no need to correct errors but just in case
            			}catch (IOException e) {System.out.println("file note found, search Store_OperatorSetting updateYRange method");	e.printStackTrace();}
                    iniX2Range();
            		
            		Double[] x1Range = getRangeValuesFromSettingD(values.get(2));	//x1 range can't be changed atm
            		//x2 range already added
            		Double minNum=null, maxNum=null;
            		
            		String min=null, max=null;
            		String y2Range = "";
            		
            		//booleans
            			boolean x1R1Neg, x1R2Neg, x2R1Neg, x2R2Neg;
            		
            			if(x1Range[0] < 0)	x1R1Neg = true; else x1R1Neg=false;
            			if(x1Range[1] < 0) x1R2Neg = true; else x1R2Neg=false;
            			if(x2Range[0] < 0)	x2R1Neg = true; else x2R1Neg=false;
            			if(x2Range[1] < 0) x2R2Neg = true; else x2R2Neg=false;
            			
            			
            		if(operatorSymbol.equalsIgnoreCase(Symbols.PLUS)) {
            			minNum = x1Range[0] + x2Range[0];
            			maxNum = x1Range[1] + x2Range[1];
            		}
            		else if(operatorSymbol.equalsIgnoreCase(Symbols.MINUS)) {
            			minNum = x1Range[0] - x2Range[0];		//ex: -400 - -400		(lowest negative - highest positive)
            			maxNum = x1Range[1] + x2Range[1];		//								(highest positive - highest positive)
            			
            			//if pure negatives in x2 -> pick lowest negative (biggest num)
            			//if neg, 0, & pos in x2 -> 
            			
            		}
            		else if(operatorSymbol.equalsIgnoreCase(Symbols.TIMES)) {
            			//if(x1Range[0] * x2Range[0] > x1Range[1] * x2Range[1])
            				
            			//smallest/lowest number: biggest positive * biggest minimum (big neg)   OR lowest negative * lowest negative (small pos)
            			//largest (small neg) (big pos): largest positive *largest positive OR lowest negative * lowest negative OR 
            			//Double pp,pn,nn;
            			
            			//nn nn
	            			if(x1R2Neg && x2R2Neg ) {
	            				minNum = x1Range[1] * x2Range[1];
	            				maxNum = x1Range[0] * x2Range[0]; 
	            			}//everything's neg
            			//pp nn
	            			else if(!x1R1Neg && x2R2Neg) {
	            				minNum = x1Range[1] * x2Range[1];
	            				maxNum = x1Range[0] * x2Range[0]; 
	            			}//x1: pp x2: nn
	            			else if(!x2R1Neg && x1R2Neg) {
	            				minNum = x1Range[1] * x2Range[1];
	            				maxNum = x1Range[0] * x2Range[0];  
	            			}//x1: nn x2: pp
            			//nn np
	            			else if(x1R2Neg && x2R1Neg && !x2R2Neg) {
	            				minNum = x1Range[0] * x2Range[1];
	            				maxNum = x1Range[0] * x2Range[0];  
	            			}//x1: nn x2: np
	            			else if(x2R2Neg && x1R1Neg && !x1R2Neg) {
	            				minNum = x2Range[0] * x1Range[1];
	            				maxNum = x2Range[0] * x1Range[0];  
	            			}//x2: nn x1: np
            			//np np
	            			else if(x1R1Neg && !x1R2Neg && x2R1Neg && !x2R2Neg) {
	            				Double nn = x1Range[0] * x2Range[0];
	            				Double pp = x1Range[1] * x2Range[1];
	            				Double x1Nx2P = x1Range[0] * x2Range[1];
	            				Double x1Px2N = x1Range[1] * x2Range[0];		//it is at this point where I decided to use a different method. I only need 6 numbers to give the whole picture
	            				if(nn > pp) {
	            					maxNum = nn;
	            				}//nn > pp
	            				else {
	            					maxNum = pp;
	            				}// pp > nn
	            				
	            				minNum = x1Range[0] * x2Range[1];
	            				maxNum = x1Range[0] * x2Range[0];  
	            			}//x1: np x2: np	            			
	            			
	            			
            			//pp np nn -> 3 groups 2 combinations
            			
            			//pp pn nn 
            			else {
            				minNum = x1Range[0] * x2Range[0];
                			maxNum = x1Range[1] * x2Range[1];
            			}//if all positive pp pp, np pp, pp np, 		np np
            		}
            		else if(operatorSymbol.equalsIgnoreCase(Symbols.DIVIDE)) {
            			minNum = x1Range[0] + x2Range[0];
            			maxNum = x1Range[1] + x2Range[1];
            		}
            		
            		//combines the min and max based on it being a double or not
	            		if(Boot.filter.findNumOfDec(minNum) == 0) {
	            			min = Integer.toString(minNum.intValue());	//ex: 12.0 -> "12"  OR 1.23 -> "1.23"
	            		}
	            		else {
	            			min = Double.toString(minNum);
	            		}
	            		
	            		if(Boot.filter.findNumOfDec(maxNum) == 0) {
	            			max = Integer.toString(maxNum.intValue());	//ex: 12.0 -> "12"  OR 1.23 -> "1.23"
	            		}
	            		else {
	            			max = Double.toString(maxNum);
	            		}
	            		
	            		y2Range = "(" + min + " ~ " + max + ")";
            		
            		
            		//updates the file on the y2range 
            		try {changeSettingLineOnIndex(4, y2Range);
            				}catch(IOException e) {e.printStackTrace();}
            		
            		
            		Boot.operators.calcMaxInputs();
            		
            		
            		//note: add ending checks to add +1 to the max if the number is negative (and set min length to 1 although a minimum for the textfield has not been added yet)
            	}
            	
            	public void updateYRangeV2(String operatorSymbol) {
            		//update values array by rereading the operators file
            		
            		//if(Main.filter.findNumOfDec( Double value that could be an int ) == 0)
                    
            		//update array's with latest data from files (tech optional)
	            		try {retrieveData(true);//true b/c there's no need to correct errors but just in case
	            			}catch (IOException e) {System.out.println("file note found, search Store_OperatorSetting updateYRange method");	e.printStackTrace();}
	                    iniX2Range();	
            		
            		Double[] x1Range = getRangeValuesFromSettingD(values.get(2));	//x1 range can't be changed atm
            		//x2 range already added
            		Double minNum=null, maxNum=null;
            		
            		String min=null, max=null;
            		String y2Range = "";
            		
            		//booleans
            			boolean x1R1Neg, x1R2Neg, x2R1Neg, x2R2Neg;
            		
            			if(x1Range[0] < 0)	x1R1Neg = true; else x1R1Neg=false;
            			if(x1Range[1] < 0) x1R2Neg = true; else x1R2Neg=false;
            			if(x2Range[0] < 0)	x2R1Neg = true; else x2R1Neg=false;
            			if(x2Range[1] < 0) x2R2Neg = true; else x2R2Neg=false;
            			
            		Double[] comboA = new Double[4];
        				
            			
            		if(operatorSymbol.equalsIgnoreCase(Symbols.PLUS)) {
            			comboA[0] = x1Range[0] + x2Range[0];
            			comboA[1] = x1Range[0] + x2Range[1];
            			comboA[2] = x1Range[1] + x2Range[0];
            			comboA[3] = x1Range[1] + x2Range[1];
            		}
            		else if(operatorSymbol.equalsIgnoreCase(Symbols.MINUS)) {
            			comboA[0] = x1Range[0] - x2Range[0];
            			comboA[1] = x1Range[0] - x2Range[1];
            			comboA[2] = x1Range[1] - x2Range[0];
            			comboA[3] = x1Range[1] - x2Range[1];
            		}
            		else if(operatorSymbol.equalsIgnoreCase(Symbols.TIMES)) {
            			comboA[0] = x1Range[0] * x2Range[0];
            			comboA[1] = x1Range[0] * x2Range[1];
            			comboA[2] = x1Range[1] * x2Range[0];
            			comboA[3] = x1Range[1] * x2Range[1];
            		}
            		else if(operatorSymbol.equalsIgnoreCase(Symbols.DIVIDE)) {
            			comboA[0] = x1Range[0] / x2Range[0] ;
        				comboA[1] = x1Range[0] / x2Range[1] ;
        				comboA[2] = x1Range[1] / x2Range[0];
        				comboA[3] = x1Range[1] / x2Range[1];
            		}
            		
            		Sort.mergeSortD(comboA, 0, comboA.length-1);		//consider using a more efficient sort?
        			minNum = comboA[0];//1st one
        			maxNum = comboA[3];//4th one
            		
        			//combines the min and max based on it being a double or not
	            		if(Boot.filter.findNumOfDec(minNum) == 0) {
	            			min = Integer.toString(minNum.intValue());	//ex: 12.0 -> "12"  OR 1.23 -> "1.23"
	            		}
	            		else {
	            			min = Double.toString(minNum);
	            		}
	            		
	            		if(Boot.filter.findNumOfDec(maxNum) == 0) {
	            			max = Integer.toString(maxNum.intValue());	//ex: 12.0 -> "12"  OR 1.23 -> "1.23"
	            		}
	            		else {
	            			max = Double.toString(maxNum);
	            		}
	            		
	            		y2Range = "(" + min + " ~ " + max + ")";
        		
	           		//updates the file on the y2range  & update the values array with the new y2Range		(tech you only need to update the array)
	           		try {changeSettingLineOnIndex(4, y2Range);
	           				}catch(IOException e) {e.printStackTrace();}	//optional
	           		values.set(4, y2Range);	//needed
	            	
	            	//Boot.operators.calcMaxInputs();
	            		
        			
            	}
           
            	public void getBiggestLength(String operatorSymbol) {
            		
            	}

    //
    public int correctCounter = 0;
    public void addToCorrectCounter(){

    }

    /*=======================================Method(s) to interact with the custom component that replaces the grid here=============================================*/
    /**Loads the settings saved in ram to the operator grid gui (ram values should have recently been updated in the controller class where this is used)
     * @param operatorGrid the custom component for the gui where the user can change the settings related to this class
     */
    public void loadToOperatorGrid(OPSettingsGrid operatorGrid) {
    	operatorGrid.loadFields(values.get(0), ""+x2Range[0], ""+x2Range[1], values.get(5));
    }
    
    public void saveSettings(OPSettingsGrid operatorGrid) {
    	String[] opSValues = {
				(operatorGrid.getDifficultySelectionIndex()+1)+"",
				"(" + operatorGrid.getRangeTf1().getText() + "~" +operatorGrid.getRangeTf2().getText() + ")",
				operatorGrid.getPracticeValTf().getText()
		};
		try {
			changeSettingsLines(indexesForSetting, opSValues);	//0 3 5
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    
}
