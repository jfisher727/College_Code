

public class RecognitionResults {
	boolean correct;
	float confidenceValue;
	
	public RecognitionResults (boolean result, float confidence) {
		correct = result;
		confidenceValue = confidence;
	}
}
