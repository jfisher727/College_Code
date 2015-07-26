import java.awt.event.KeyEvent;
import java.util.Scanner;


public class KeystrokeProcessing {
	private static final int NUM_INPUT_SAMPLES = 8;
	private static final int MAX_ANOMALIES = 8;
    private static final double Z_SCORE = 5;
    
	private static final int entriesRequired = 8;
	private int entriesCollected = 0;
	
	private static final String codeWord = SmartTestingView.codeWord;
	
	long[] pressTimings = new long[codeWord.length()];
    long[] liftTimings = new long[codeWord.length()];
    long lastTime = 0;
    int letterPos = 0;
    KeystrokeInput userInput = new KeystrokeInput();
	
    String timings = "";
    String averagesInput = "";
    String pressAverages = "";
    String deviationsInput = "";
    String pressStandardDeviations = "";
    
    double[] liftAvgs = new double[codeWord.length()];
    double[] pressAvgs = new double[codeWord.length()];
    double[] liftVars = new double[codeWord.length()];
    double[] pressVars = new double[codeWord.length()];  
    
    SmartTestingModel model;
    SmartTestingView view;
    boolean userLoggingIn = false;
    
    private boolean userVerified = false;
    
    public KeystrokeProcessing (SmartTestingModel model, SmartTestingView view) {
    	this.model = model;
    	this.view = view;
    }
    
    public void setUserLoggingIn(boolean login) {
    	this.userLoggingIn = login;
    }
    
    public void updateLabel() {
		entriesCollected++;
		view.setCounterLabel(entriesRequired - entriesCollected);
	}
    
    public void userLoggingIn() {
    	this.averagesInput = this.model.getKeystrokeAverages();
    	this.deviationsInput = this.model.getKeystrokeDeviations();
    }
    
    public boolean getUserVerified () {
    	return userVerified;
    }
    
    public void keyPressed (KeyEvent e) {
    	//System.out.println("Key pressed");
    	if(e.getKeyCode() == KeyEvent.VK_ENTER){
            this.view.setUserInputEditabled(false);
            submitButtonPressed();
            return;
        }
    	String inputString = this.view.getUserInputText();
        if(inputString.length() <= codeWord.length() && letterPos< codeWord.length()){
            long currTime = System.currentTimeMillis();
            if(lastTime == 0){
                liftTimings[letterPos] = lastTime;
            } else {
                liftTimings[letterPos] = currTime-lastTime;
            }
            userInput.setLiftTimes(userInput.getLiftTimes() + liftTimings[letterPos] + "  ");
            lastTime = currTime;
            
        } 
    }
    
    public void keyReleased (KeyEvent e) {
    	//System.out.println("Key released");
    	if(letterPos < codeWord.length() && e.getKeyCode() != KeyEvent.VK_ENTER){
            long currTime = System.currentTimeMillis();

            pressTimings[letterPos] = currTime-lastTime;
            userInput.setPressTimes(userInput.getPressTimes() + pressTimings[letterPos] + "  " );
            lastTime = currTime;
            letterPos++;
        }
    }
    
    public void reset(){
        pressTimings = new long[codeWord.length()+1];
        liftTimings = new long[codeWord.length()+1];
        lastTime = 0;
        letterPos = 0;
        userInput = new KeystrokeInput();
        this.view.resetUserInputField();
        this.view.getUserInputField().requestFocusInWindow();
        this.view.setUserInputEditabled(true);
    }
    
    private boolean idVerified(String avgs, String stdDevs, KeystrokeInput ui){
        
        Scanner averages = new Scanner(avgs);
        Scanner standardDevs = new Scanner(stdDevs);
        int anomalyCount = 0;
        
        Scanner liftIns = new Scanner(ui.getLiftTimes());
        Scanner pressIns = new Scanner(ui.getPressTimes());        

        liftIns.nextInt();
        averages.nextInt();
        averages.nextDouble();
        standardDevs.nextDouble();
        
        //Count anomalies for first 8 (lifts)
        for(int i = 1; i < NUM_INPUT_SAMPLES; i++){
            double z = Math.abs((liftIns.nextInt() - averages.nextInt())/standardDevs.nextDouble());
            if(z > Z_SCORE){
                anomalyCount++;
            }         
        }
        
        //Count anomalies for second 8 (presses)
        for(int i = 0; i < NUM_INPUT_SAMPLES; i++){
            double z = Math.abs((pressIns.nextInt() - averages.nextInt())/standardDevs.nextDouble());
            if(z > Z_SCORE){
                anomalyCount++;
            }         
        }
        
        averages.close();
        standardDevs.close();
        liftIns.close();
        pressIns.close();
        
        if(anomalyCount <= MAX_ANOMALIES){
            return true;
        }  else return false;
    }
    
    private void storeVals(String s1, String s2, String timings){
        String lift;
        String press;
        int[][] liftMatrix = new int[NUM_INPUT_SAMPLES+1][codeWord.length()];
        int[][] pressMatrix = new int[NUM_INPUT_SAMPLES+1][codeWord.length()];
        
        Scanner timingScanner = new Scanner(timings);        
            
        for(int i = 0; i < NUM_INPUT_SAMPLES; i++){
            
                //Read 2 lines of stored timings
                lift = timingScanner.nextLine(); 
                press = timingScanner.nextLine();
                
                //Scan each line for values to store into each row of the matrix
                Scanner lineScanner = new Scanner(lift);
                int keyPos = 0;
                while(lineScanner.hasNext()){
                    int tmp =  Integer.parseInt(lineScanner.next());
                    liftMatrix[i][keyPos] = tmp;
                    liftMatrix[NUM_INPUT_SAMPLES][keyPos] += tmp;                    
                    keyPos++;
                    
                }
                keyPos = 0;
                lineScanner.close();
                lineScanner = new Scanner(press);
                while(lineScanner.hasNext()){
                    int tmp =  Integer.parseInt(lineScanner.next());
                    pressMatrix[i][keyPos] = tmp;
                    pressMatrix[NUM_INPUT_SAMPLES][keyPos] += tmp;                    
                    keyPos++;
                }
                lineScanner.close();
        }
        timingScanner.close();
        
        //Calculate averages for reach row
        String liftAverages = "";
        
        for(int i = 0; i < codeWord.length(); i++){
            liftAverages = liftAverages + ((liftMatrix[8][i] / NUM_INPUT_SAMPLES) + " ");
            liftAvgs[i] = (liftMatrix[8][i] / NUM_INPUT_SAMPLES);
            pressAverages = pressAverages + ((pressMatrix[8][i] / NUM_INPUT_SAMPLES) + " ");  
            pressAvgs[i] = (pressMatrix[8][i] / NUM_INPUT_SAMPLES);
        }
                
        //Sum of Differences
        for(int i = 0; i < codeWord.length(); i++){
            for(int j = 0; j < NUM_INPUT_SAMPLES; j++){
                liftVars[i] += Math.abs(liftMatrix[j][i]-liftAvgs[i]);
                pressVars[i] += Math.abs(pressMatrix[j][i]-pressAvgs[i]);
            }
        }
        String liftStandardDeviations = "";
        //Square root of averages finally calculates std dev
        for(int i = 0; i < codeWord.length(); i++){
            liftStandardDeviations = liftStandardDeviations + ((Math.sqrt(liftVars[i] / NUM_INPUT_SAMPLES)) + " ");
            pressStandardDeviations = pressStandardDeviations + ((Math.sqrt(pressVars[i] / NUM_INPUT_SAMPLES)) + " ");
        }
        
        //store the values
        averagesInput = liftAverages + pressAverages;
        deviationsInput = liftStandardDeviations + pressStandardDeviations;
        this.model.setKeystrokeAverages(averagesInput);
        this.model.setKeystrokeDeviations(deviationsInput);
    }
       
    public void submitButtonPressed () {
    	//Word entered correctly
    	String inputString = this.view.getUserInputText();
        if(codeWord.equals(inputString)){
            
        	if(!this.userLoggingIn) {
	            if(entriesCollected < entriesRequired){
	                timings = timings+(userInput.getLiftTimes() + "\n" + userInput.getPressTimes() + "\n");
	                
	                reset();
	                updateLabel ();
	                
	                if (entriesCollected == entriesRequired){
	                    storeVals(userInput.getLiftTimes(), userInput.getPressTimes(), timings);
	                }
	            }
        	}
            //Enough samples already, check input for verification
            else {
            	userVerified = idVerified(averagesInput, deviationsInput, userInput);
            }
        }
    }
}
