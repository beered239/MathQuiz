package custom_elements;

import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;

public class KeyActions {

	public static final Robot USERINPUT = new Robot();
	
	private KeyActions(){
		
	}
	
	/** if 'enter' is pressed a tab press is called to switch to focus on the next focusable/visible component 
	 * @param keyPressed a keycode usually taken from a KeyEvent using .getCode
	 * @return returns a boolean that indicates if enter was pressed
	 */
	public static boolean nextComponentAction(KeyCode keyPressed ) {
		boolean enterClicked = keyPressed == KeyCode.ENTER;
		if(enterClicked)
			  goToNextFocusable();
		return enterClicked;
	}
	
	public static void goToNextFocusable() {
		USERINPUT.keyPress(KeyCode.TAB);
	}
	
	
}


