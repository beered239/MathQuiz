package sample;

//import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class Prompts {

    //yes no boolean
        //boolean yNLoop = true;

    public Prompts(){

    }

    /**
     * @purpose: every prompt starts with the user inputting something after it is asked this is that something
     * */
    private String begin()
    {
        return Main.in.next() + Main.in.nextLine();
    }

    public void settingsPrompt()
    {
        String menuQuestion = "Do you want to change the settings: ";
        System.out.print(menuQuestion);   //change when you want to better display
        yesNo(begin());
    }

    //yes no responses
    public String yesNo(String response)
    {
        boolean works = checkYN(response);
        //yNLoop = !works;
        if(works)
            return response;
        error1();   //tells user to try again
        String resp = Main.in.next();
        resp+= Main.in.nextLine();
                                                //PROMPT ALL OTHER SETTINGS PROMPTS  (IN EACH SETTINGS PROMPT MAKE SURE TO CHANGE SETTING VAL)
        return yesNo(resp);
    }

    /**
     * @purpose: returns true if the yes no conditions pass (make sure to make false if the value is being checked with a loop statement)
     * */
    public boolean checkYN(String responseOG) {
        String response = responseOG.toUpperCase();
        return (response.equalsIgnoreCase("yes")) || (response.equalsIgnoreCase("no")) ||
                ((response.charAt(0) == 'Y'  || response.charAt(0) == 'N') && response.length() == 1);
    }

    /**
     * @purpose: displays error for an invalid yes no response
     */
    public void error1()
    {
        System.out.print("Input a yes or no response: ");
    }
}
