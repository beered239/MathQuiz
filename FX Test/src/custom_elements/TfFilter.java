package custom_elements;

import java.util.function.UnaryOperator;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

public class TfFilter{

	private UnaryOperator<Change> filter;
	
	public TfFilter() {
		//empty
	}
	public TfFilter(int maxLength, String regexMatch)	{
		create(maxLength, regexMatch);
	}
	
	/**@purpose: creates a textfield filter
	 * @apiNote typical int filter: "[0-9]*"
	 * */
	public void create(int maxLength, String regexMatch) {
		
				 filter = change -> {
					String text = change.getControlNewText();
					if(text.isEmpty())
						return change;
				
					if(text.length() > maxLength) {return null;}	//character limiter
					
					//regex formatter: 
						if(text.matches(regexMatch)) 	{return change;}				//my decimal+negative formatter  ->  text.matches("[0-9/.-]*")
						
						return null;
					
				};
				/*Some notes:
				 * 		regex * means that there is no limit to how many characters you can have from that [] range? -> match 0 or more []'s
				 * 		
				 * */		
	}
	
	public UnaryOperator<Change> getFilter() {
		return filter;
	}
	
	
	
	//kinda useless rn
	public static TextFormatter<String> getTextFormatter(UnaryOperator<Change> filter){
		return new TextFormatter<String>(filter);
	}
	
}
