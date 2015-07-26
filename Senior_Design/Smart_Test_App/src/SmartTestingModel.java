import org.opencv.core.Mat;


public class SmartTestingModel {
	private String userFirstName;
	private String userLastName;
	private String userName;
	private String userEmail;
	private String userPassword;
	private String keystrokeAverages;
	private String keystrokeDeviations;
	private int personNumber;
	private int incorrectLoginAttempts;
	
	private int faceRegistrationImagesCaptured;
	private Mat [] facesCaptured;
	private int imagesCapturedCounter;
	
	public SmartTestingModel () {
		this.userEmail = null;
		this.userName = null;
		this.userPassword = null;
		this.keystrokeAverages = null;
		this.keystrokeDeviations = null;
		this.personNumber = 0;
		this.incorrectLoginAttempts = 0;
		this.faceRegistrationImagesCaptured = 0;
	}
	
	public void initializeFacesCapturedArray () {
		facesCaptured = new Mat [SmartTestingController.REQ_IMAGES_NEEDED];
		imagesCapturedCounter = 0;
	}
	
	public void setUserName (String firstName, String lastName) {
		this.userFirstName = firstName;
		this.userLastName = lastName;
		this.userName = firstName + "_" + lastName;
	}
	
	public String getUserName () {
		return userName;
	}
	
	public String getUserFirstName () {
		return this.userFirstName;
	}
	
	public String getUserLastName () {
		return this.userLastName;
	}
	
	public void setUserEmail (String email) {
		userEmail = email;
	}
	
	public String getUserEmail () {
		return userEmail;
	}
	
	public void setUserPassword (String password) {
		userPassword = password;
	}
	
	public String getUserPassword () {
		return userPassword;
	}
	
	public void setKeystrokeAverages (String averages) {
		this.keystrokeAverages = averages;
	}
	
	public String getKeystrokeAverages () {
		return this.keystrokeAverages;
	}
	
	public void setKeystrokeDeviations (String deviations) {
		this.keystrokeDeviations = deviations;
	}
	
	public String getKeystrokeDeviations () {
		return this.keystrokeDeviations;
	}
	
	public void setUserNumber (int number) {
		this.personNumber = number;
	}
	
	public int getUserNumber () {
		return this.personNumber;
	}
	
	public int getFailedLoginAttempts () {
		return incorrectLoginAttempts;
	}
	
	public void incrementFailedLoginAttempts () {
		incorrectLoginAttempts++;
	}
	
	public void resetFailedLoginAttempts () {
		incorrectLoginAttempts = 0;
	}
	
	public int getRegistrationImagesCaptured () {
		return faceRegistrationImagesCaptured;
	}
	
	public void incrementImagesCaptured () {
		faceRegistrationImagesCaptured++;
	}

	public int getImagesCapturedCounter () {
		return imagesCapturedCounter;
	}
	
	public void addImageCaptured (Mat image) {
		if(image == null) {
			return;
		}
		if(imagesCapturedCounter < SmartTestingController.REQ_IMAGES_NEEDED) {
			facesCaptured[imagesCapturedCounter] = image;
			imagesCapturedCounter++;
		}
	}
	
	public Mat [] getImagesCaptured () {
		return facesCaptured;
	}
}
