package application;

public class SimplifyFX {

	//use string constants? (might take up a fraction of memory)
	
	public SimplifyFX() {
		//make an object, no specifics needed
	}
	
	/**@apiNote simple method of inserting a number into a style string when setting style!
	 * */
	public String numInStyle(String styleName, String input) {
		return styleName + input + "; ";
	}
	
	
	public String fontSize(double size) {
		return numInStyle("-fx-font-size: ", Double.toString(size));
	}
	public String fontSize(String size) {
		return numInStyle("-fx-font-size: ", size);
	}
	
	
	/**@apiNote syntax = <size> | <size> <size> <size> <size>
	 * top right bottom left
	 * */
	public String padding(String size) {
		return numInStyle("-fx-padding: ", size);
	}
}
