
public class KeystrokeInput {
	String PressText;
    String LiftText;
    
    public KeystrokeInput(){
    	PressText = "";
    	LiftText = "";
    }
    public void setPressTimes(String text){
    	PressText = text;
    }

	public void setLiftTimes(String text){
	    LiftText = text;
	}
	
	public String getPressTimes(){
	    return PressText;
	}
	
	public String getLiftTimes(){
	    return LiftText;
	}

}
