package sample;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

//remember:
//in: reads what's in the file              (in: read in file)
//out: write information to the file        (out: write to file)
/**/

/**
 * @purpose: gets info on a single text file based on the object
 */
public class ReadFiles {

    //variables that have info on the file
    //public static int totalLines;          //set this value to the number of lines in the text
    //^ can be found using lines.size();
    //variables to take information from a text file
        public static File fileName;
        public static Scanner inFile;
    //variables that store file data
        public static ArrayList<String> lines;
    //stores the name of the file as a string
        public static String fileNameS;
    //variables needed for counting file (keeps track of number of tests that there have been)
        public static Integer counter;
        public static final File counterFile = new File( "Times_Ran" + File.separator + "Times_Ran.txt");
            //^ can be changed
//methods

    /**
     * @param fileNameInput a string variable that represents the file name/location
     * @purpose: reads a file and saves each separate line into the arraylist
     * @apiNote it will save to teh static array in this class
     */
    public static void saveToArray(String fileNameInput) throws IOException {
        fileNameS = fileNameInput;
        lines = new ArrayList<String>();
        fileName = new File(fileNameS.replaceAll(".txt", "") + ".txt");
        inFile = new Scanner(fileName);
        while (inFile.hasNext()) {
            String txt = inFile.next();
            txt += inFile.nextLine();
            lines.add(txt);
            //totalLines++;
        }
        inFile.close();
    }

    /**
     * @param file_data a object that will store the specified files info
     * @purpose: sends the lines array, the file name, and the number of lines to the file object inputted
     * @apiNote sends info to the Store_Setting object
     */
    public static void sendToSettings(Store_Setting file_data) {
        file_data.fileNameS = fileNameS;
        file_data.lines = new ArrayList<String>(lines);    //copies to the static array in store_settings (they are not linked)
        file_data.totalLines = lines.size();
    }

    /**
     * @purpose: to clear the static values (arraylist is the only one that needs to be cleared)
     * @apiNote clears class data to add more
     */
    public static void clearAll() {
        lines.clear();  //fileName and everything else should get cleared each time this class is used
    }

    //counter

        public static void saveCounter() throws IOException{
            Scanner inFile = new Scanner(counterFile);  //create a new scanner since the last one was closed?
            counter = Main.filter.fixI(inFile.next());
            if(Filter.seeIfInvalid(counter)){
                counter = 0;
                System.out.println("Debug msg: counter file mamde no sense");   //debug
            }
            inFile.close();
        }
        /**@apiNote WriteFiles counter gets updated
         * */
        public static void sendCounterInfo(WriteFiles fileWriter){
            WriteFiles.counter = counter;
        }

}

//Examples of object names
/*
 * - readGlobalS
 * - readDivisionS, etc*/