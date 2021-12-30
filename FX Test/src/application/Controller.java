package application;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import custom_elements.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import quizGen.Boot;
import quizGen.Filter;
import quizGen.Operators;
import quizGen.Question;
import settings_window.Launch_Settings;

public class Controller {

	//constants 
		public final SimplifyFX style = new SimplifyFX();	//object to simplify styling
		public static final Filter FILTER = new Filter();
		public static final Timer TIMER = new Timer();
		public static final String NO_HIGHLIGHT = "-fx-border-color: transparent";
		public static final Launch_Settings settingWindow = new Launch_Settings();
	
	@FXML
	public void initialize() throws AWTException {
		Robot robot = new Robot();
		disableObjects();
		
		//listeners
			//limit size of textfield, make size dynamic
				studentResponse.textProperty().addListener((v,oldValue,newValue) -> {
					limitTF();
					studentResponse.setPrefWidth(TextUtils.computeTextWidth(studentResponse.getFont(), studentResponse.getText(), 0.0D) + 10);//resizes textfield
				});
			
			//stops most close requests
				Main.window.setOnCloseRequest(e -> {e.consume(); preventClose();});
			//remove highlight when textfield is focused
				studentResponse.focusedProperty().addListener((v, oldValue, newValue) -> removeFocus(robot));
		
				
		//elements at the start if possible
			//studentResponse.setText("21");	//test... works!
			//set window text sizes here....
			
				double defaultTSize = 200;	//200   height/5.4			(check sticky note to make text dynamic-ish)
				double questionNumSize = defaultTSize*0.1;
				//double smallerTSize = defaultTSize*0.25;
				
			parent.setStyle(style.fontSize(defaultTSize));
			questionNum.setStyle(style.fontSize(questionNumSize) + style.padding("0 0 0 " + (defaultTSize*0.3)));//question num: 50% of def
			//scorePercentages.setStyle(style.fontSize(smallerTSize) + style.padding("0") );	//plan: combine -> question Num + input -> hbox1   & hbox1 + score percentages -> vbox
			
			fixButtonSize();
	}
	
	/**@apiNote gets rid of any space from text on both buttons*/
	void fixButtonSize() {
		settingButton.setStyle(style.fontSize(0));
		playButton.setStyle(style.fontSize(0));
	}
	
	/**@apiNote disables certain inputs at the start*/
		void disableObjects() {
			studentResponse.setDisable(true);
			question.setDisable(true);
		}
	/**@apiNote enables the once disabled objects after the play button is pressed*/
		void enableObjects() {
			studentResponse.setDisable(false);
			question.setDisable(false);
		}
	/**@apiNote sets a limit to the amount of characters in the question*/
		void limitTF(){
			String text = studentResponse.getText();
	        int sLength = text.length();
	            //Question currentQuestion = Operators.allQuestions.get(Display.qIndex);
	            //int stringLimit = currentQuestion.getMaxInputLength();      //add 1 for decimals?
	        	int stringLimit = 3;	//limit for now (debug)  (might be better using this if you don't have a plan to allow double range values being inserted)
	        if(sLength >= stringLimit+1) {
	            studentResponse.setText(text.substring(0,stringLimit));
	        }
	        //limit text type
	        if (text.matches("[0-9/.]*") || sLength > stringLimit) return;
	        else
	        	studentResponse.setText(text.replaceAll("[^0-9/.]", ""));
		}
	/**@apiNote removes the highlight of the textfield when refocused*/
		void removeFocus(Robot robot){
			if(studentResponse.isFocused()) {
				robot.keyPress(39);
				robot.keyRelease(39);
				studentResponse.setStyle("-fx-border-color: #68FCEB;");
			}
			else {
				studentResponse.setStyle(NO_HIGHLIGHT);
			}
		}	
	/**@apiNote takes care of all close requests except for file -> close (add condition) and task manager close (normal)*/
		void preventClose() {
			Main.exitWarning.create("Finish First", "Warning");
		}
	/**@apiNote update question number label to show the current question number*/
		void updateQuestionNumLabel() {
			questionNum.setText("Question " + (questionIndex+2) + ":");
		}
	/**@apiNote updates the question label to show the next question*/
		void updateQuestionLabel(String newQuestion){
			question.setText(newQuestion);
		}
	/**@apiNote enables the button and resets (on action) certain things to */
		@FXML
		void startNewQuiz() {
			//maybe use new objects and create a new Boot object????? ... might need to recreate the way the boot class works...
			
			playButton.setDisable(false);
			playButton.setStyle("-fx-opacity: 1");
			//playButton.requestFocus();
			//studentResponse.setDisable(true);
			//Operators.allQuestions.clear();
			Operators.clearQuestions();
			
			questionIndex = 0;
			//quizGen.Boot.GlobalS.iniCorrectAnswerDisplayBox();
			studentResponse.clear();
			studentResponse.setStyle(NO_HIGHLIGHT);

			question.setText("[empty]");
			
			disableObjects();
		}
	
	/**@apiNote sets the color of the textfields highlight temporarily to red or green based on the accuracy of the response*/
		void temporaryHighlight(boolean correct) {
			if(correct) {
				studentResponse.setStyle("-fx-border-color: #0FFA36");
			}
			else {
				studentResponse.setStyle("-fx-border-color: #F20707");	
			}
			//note: not the source of shutdown problem
			final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
			Long longVal = 100L;
			//System.out.println("start");	//debug
			executorService.schedule(new Runnable() {
		        @Override
		        public void run() {
		            willWait();
		        }}, longVal, TimeUnit.MILLISECONDS);
			
			//studentResponse.setStyle("NOHIGHLIGHT");	
			//studentResponse.setStyle("-fx-border-color: #68FCEB;");
			executorService.shutdown();	
		}
		
		/**@apiNote just a blank method for the wait period*/
		 void willWait() {
			 //this method will be called and do nothing for x amount of time
			 //System.out.println("returns to normal highlight");	//debug
			 if(studentResponse.isFocused())	//stops highlight glitch
				 studentResponse.setStyle("-fx-border-color: #68FCEB;");
			 
		 }
		
	/**@apiNote called when exit button is pressed, closes the application*/
	@FXML
    void closeOperation(){
		//javafx.application.PlatformImpl.tkExit();\
		Boot.in.close();
		Main.window.close();
		System.exit(0);		//should close program properly
		//Platform.exit();
   }
	
	/**@purpose: to print the student response in the terminal (debug)
	 * */
	void printStudentResponse() {
		System.out.println(studentResponse.getText());
		studentResponse.clear();
	}//prints textfields data into terminal
	
	/**@apiNote called when the textfield for the student response is set on action*/
	@FXML 
	void interpretStudentResponse(ActionEvent event) {
		
		//printStudentResponse();
		
		Question nextQuestion = null, currentQuestion = null;//initialized
        String response = studentResponse.getText();
        
        if(response.equals("")) {
        	event.consume();
        }
        else {
				currentQuestion = Operators.allQuestions.get(questionIndex);
	        //adding the response to the question
	            currentQuestion.setResponse(FILTER.fixI(response));
	            currentQuestion.calcAnalyzedResponse(); //should also update the answers correct count?
	            
	            //highlights green and red based on accuracy of question (if setting is enabled)
		            boolean correct = currentQuestion.getCorrect();
		            
		            //boolean preformHighlight = Boot.GlobalS.interpretValueB(Boot.GlobalS.values.get(4));//gets bool for highlight from value ram -> changes it to actual bool 
		            	//System.out.println("preform highlight: " + preformHighlight);//debug 		(not visible)
		            if(preformHighlight)
		            	temporaryHighlight(correct);	//this doesn't interrupt a proper close
	            //displays/hides score box depending on setting
		            //boolean displayScores = Boot.GlobalS.interpretValueB(Boot.GlobalS.values.get(5));//gets bool for highlight from value ram -> changes it to actual bool 
		            //Display.arrayS(Boot.GlobalS.values);
		            percentageBox.setVisible(displayScores);
		            
		            
	                //System.out.print("\nQuestion " + Display.qIndex + ": " + currentQuestion + " " + response);    //debug
	            //deals with potential next question OR what to do after the test is over
	                if(questionIndex == quizGen.Operators.allQuestions.size()-1){
	                    quizGen.Boot.GlobalS.calcPercentageCorrect();    //calculates the percentage correct (used to write into quiz file)
	                    try{quizGen.Boot.fileWriter.createSavedTestFile();
	                        quizGen.Boot.fileWriter.increaseCounter();}
	                    catch (IOException ioException){ioException.printStackTrace();}//shouldn't be called at any point
	                    
	                    //disable the student response textfield
		                    studentResponse.setStyle(NO_HIGHLIGHT);
		                    
		                    //studentResponse.setDisable(true);         
		                //disable question
		                    question.setDisable(true);
		                questionNum.setText("[empty]");
	                    //show button to start a new quiz
	                    	startNewQuiz();
	                    closeItem.setDisable(false);											//enable the close button from the menu
	                }//writes all responses to files, disables input
	                else{
	                    nextQuestion = quizGen.Operators.allQuestions.get(questionIndex+1);
	                    updateQuestionNumLabel();
	                    updateQuestionLabel(nextQuestion.toString());
	                    studentResponse.setText("");
	                    questionIndex++;
	                }//moves onto next question, clears input, increases question count
	                
	                //ArrayList<Question> questions = quizGen.Operators.allQuestions
		}	
        
	}
	
	
	/**called when delete (from the menu) is pressed*/
	@FXML
		void delete() {
			studentResponse.clear();
		}
	
	
	@FXML TextField studentResponse;
	@FXML TextField tf2;
	@FXML GridPane gp;
	
	
	@FXML MenuItem closeItem;	//button in navigation bar that closes the program
	@FXML MenuItem reset;

	/**
	 * when setting button is hovered over
	 */
	@FXML Button settingButton;
	@FXML Button playButton;
	@FXML 
		void settingHovered() {
			//settingButton.setEffect(new Glow(10));
			//System.out.println("hovered");	//debug
		}
	@FXML
		void settingUnHovered() {
			settingButton.setEffect(null);
		}
	@FXML
		void settingClicked() {
			studentResponse.requestFocus();
				//System.out.println("done"); //debug
			//open settings window
				settingWindow.create();	//error: calls ini method from this class again 
		}
	
	/**when play button is clicked*/
	@FXML 
		void playClicked() throws IOException{
			//enable objects
				enableObjects();
			//generate quiz
				Boot.initialize();
				Boot.updateSettingObjects();
				Boot.GlobalS.iniCorrectAnswerDisplayBox();		//make sure this works properly...
				Boot.createQuiz();
				
				//initialize dynamic setting values when play is clicked
					preformHighlight = Boot.GlobalS.interpretValueB(Boot.GlobalS.values.get(4));//gets bool for highlight from value ram -> changes it to actual bool 
					displayScores = Boot.GlobalS.interpretValueB(Boot.GlobalS.values.get(5));
				
				percentageBox.setVisible(displayScores);	//percentage box change
				
				
			//load first question to question label (old version)
				//ArrayList<Question> questions = quizGen.Operators.allQuestions;
				//Question firstQuestion = questions.get(0);
				//question.setText(firstQuestion.toString());
				
				//Operators.clearQuestions();
				//Display.arrayS(Operators.allQuestions);	//debugs
				
				
			//set questionIndex
				questionIndex = 0;
			//delete play button (for now)
				playButton.setDisable(true);
				playButton.setStyle("-fx-opacity: 0");
				
			//load first question to question label
				questionNum.setText("Question: 1");
				try {
					question.setText(Operators.allQuestions.get(0).toString());
					closeItem.setDisable(true);
				} catch (Exception e) {
					// TODO: handle exception
					AlertBox noQuestions = new AlertBox();
					noQuestions.create("Go To Setting and Add Questions First", "Error");
					startNewQuiz(); 
				}
				
			//removes old percentage box and add the new one
				percentageBox.getChildren().clear();	
				percentageBox.getChildren().add(quizGen.Boot.GlobalS.scoreHBox);
				
		}
	
	//menu bar actions (just made)
	@FXML
	void exitFS() {
		Main.window.setFullScreen(false);
		//System.out.println("exited?");	//debug
		//TestClass.send();
	}
	
	@FXML HBox percentageBox;
	
	
	
	//variables for dynamic settings
		Boolean preformHighlight;
		Boolean displayScores;
		
	//methods to change dynamic settings!!
		public void changeHighlightB(String showS) {
			Boot.GlobalS.values.set(4, showS);
			try {
				Boot.GlobalS.changeSettingLineOnIndex(4, showS);
			} catch (IOException e) {e.printStackTrace();}	
			
			if(preformHighlight!= null) {
				//should be calculated when the play button is clicked
					preformHighlight = !preformHighlight;
			}
					
		}
		public void changeDisplayB(String showBox) {
			Boot.GlobalS.values.set(5, showBox);
			try {
				Boot.GlobalS.changeSettingLineOnIndex(5, showBox);
			} catch (IOException e) {e.printStackTrace();}
			
			if(displayScores != null) {
				//should be calculated when play button is clicked
					displayScores = !displayScores;
					percentageBox.setVisible(displayScores);
			}
		}
		//test
			public void testInteraction() {
				System.out.println("Test worked!");
			}
	
	
	//other important variables:
		@FXML MenuItem exitButton;
		
		
	
		//labels for score info , question, and question num
		@FXML Label questionNum;
		//@FXML Label scorePercentages;
		@FXML Label question;
		
		//parent container
		@FXML GridPane parent;
		@FXML GridPane mainGridPane;
		
		
		//Reference to quiz-gen variables after the initialization
			//ArrayList<Question> questions;
			int questionIndex;
}
 