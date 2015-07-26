import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.JButton;

import org.opencv.core.Mat;

public class SmartTestingController implements ActionListener{
	private static final String phpScriptName = "C:/xampp/htdocs/Test/bridge.php";
	private static final String phpURLAddress = "http://localhost/Test/bridge.php";
	private static final int MAX_FAILED_ATTEMPTS = 3;
	public static final int REQ_IMAGES_NEEDED = 10;	
	
	private static final String dbServerAddress = "jdbc:mysql://localhost:3306/";
	private static final String dbName = "accounts";
	private static final String dbUserName = "root";
	private static final String dbPassword = "";
	Connection dbConnection;
	Statement dbStatement;
	ResultSet dbResult;
	//private static final int dbServerPort = 8181;
	//private Socket dbServerSocket;
	//private PrintWriter writeToSocket;
	//private BufferedReader readFromSocket;
	
	private boolean registrationProcess = false;
	private boolean loginProcess = false;
	private boolean panelChanged = false;
	private boolean threadStarted = false;
	private boolean imagesSent = false;
	private boolean finished = false;
		
	SmartTestingModel model;
	SmartTestingView view;
	FacialProcessing facialProc;
	KeystrokeProcessing keystrokeProc;
	
	SmartTestingFacialUpdater facialThread;
	KeyAdapter keys;
	
	public SmartTestingController (SmartTestingModel model, SmartTestingView view) {
		this.model = model;
		this.view = view;		
				
		this.view.setBackButtonAL(this);
		this.view.setNextButtonAL(this);
		this.view.setCancelButtonAL(this);
		this.view.setLoginButtonAL(this);
		this.view.setRegistrationButtonAL(this);
		this.facialProc = new FacialProcessing(model);
		this.keystrokeProc = new KeystrokeProcessing(model, view);
			
		keys = new KeyAdapter() {
			public void keyPressed (KeyEvent e) {
				keystrokeProc.keyPressed(e);
			}				
			public void keyReleased (KeyEvent e) {
				keystrokeProc.keyReleased(e);
			}
		};
	}
	
	public String hashPassword (String password) {
		String md5 = null;
		if(password == null) {
			return null;
		}
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(password.getBytes(), 0, password.length());
			md5 = new BigInteger(1, digest.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5;
	}
	
	public void control () {
		view.setBackButtonEnabled(false);
		view.setNextButtonEnabled(false);
		view.showFrame();
	}
	
	public void enableFlowButtons () {
		this.view.setRegistrationButtonEnabled(false);
		this.view.setLoginButtonEnabled(false);
		this.view.setBackButtonEnabled(true);
		this.view.setNextButtonEnabled(true);
	}
	
	public void disableFlowButtons () {
		this.view.setBackButtonEnabled(false);
		this.view.setNextButtonEnabled(false);		
	}
	
	public boolean comparePasswords () {
		String password = this.view.getPasswordText();
		if(password.equals(this.view.getConfirmPasswordText())) {
			String hashedPassword = hashPassword(password);
			this.model.setUserPassword(hashedPassword);
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean compareEmails () {
		String email = this.view.getEmailText();
		if(email.equals(this.view.getConfirmEmailText())) {
			this.model.setUserEmail(email);
			return true;
		}
		else {
			return false;
		}
	}
	
	public void storeName () {
		String firstName = this.view.getFirstNameText();
		String lastName = this.view.getLastNameText();
		
		this.model.setUserName(firstName, lastName);
	}
	
	public void retryMatchingInput () {
		this.view.retryInformationMatch();	
		this.view.setOkButtonAL(this);
		this.view.setOkButtonEnabled(true);	
	}
	
	public boolean captureRegistrationInfo () {
		if(comparePasswords()) {
			if(compareEmails()) {
				storeName();
				return true;
			}
			else {
				retryMatchingInput();
				return false;
			}
		}
		else {
			retryMatchingInput();
			return false;
		}
	}
	
	public void closeDBConnection() {
		try {
			dbConnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet queryDB (String query) {
		try {
			dbConnection = DriverManager.getConnection(dbServerAddress+dbName, dbUserName, dbPassword);
			dbStatement = dbConnection.createStatement();
			dbResult = dbStatement.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dbResult;
	}
	
	public void updateDB (String update) {
		try {
			dbConnection = DriverManager.getConnection(dbServerAddress+dbName, dbUserName, dbPassword);
			dbStatement = dbConnection.createStatement();
			dbStatement.executeUpdate(update);
			dbConnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean verifyLoginInfo() {
		String hashedPassword = hashPassword(this.view.getPasswordText());
		String email = this.view.getEmailText();
		
		String query = "SELECT * FROM registered_users WHERE Email = \"" + email + "\";";
		ResultSet results = queryDB(query);
		
		try {
			while(results.next()) {
				//Assumes that if the result isn't invalid, there was an email registered
				//System.out.println(queryResult);
				this.model.setUserEmail(results.getString("Email"));
				this.model.setUserName(results.getString("First_name"), results.getString("Last_name"));
				String storedPassword = results.getString("password"); //password
				this.model.setKeystrokeAverages(results.getString("keystroke_averages"));
				this.model.setKeystrokeDeviations(results.getString("keystroke_deviations"));
				this.model.setUserNumber(Integer.valueOf(results.getString("Person_number")));
				this.model.setUserPassword(storedPassword);
				if(hashedPassword.equals(storedPassword)) {
					//open next window, login was successful
					return true;
				}
			}
			closeDBConnection();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void lockInputFields () {
		this.view.getEmailField().setEditable(false);
		this.view.getPasswordField().setEditable(false);
	}

	public boolean loginSuccessful () {
		if(this.model.getFailedLoginAttempts() <= MAX_FAILED_ATTEMPTS) {
			if(!this.view.getEmailField().equals("") && !this.view.getPasswordText().equals("")) {
				if(verifyLoginInfo()) {
					this.model.resetFailedLoginAttempts();
					return true;
				}
				else {
					this.view.retryInvalidInformation();
					this.model.incrementFailedLoginAttempts();
					this.view.setOkButtonAL(this);
					return false;
				}
			}
			else {
				lockInputFields ();
			}
		}
		return false;
	}
	
	public boolean isUniqueUser () {
		String query = "SELECT * FROM registered_users WHERE Email = \"" + this.model.getUserEmail() + "\";";
		ResultSet results = queryDB (query);
		try {
			while(results.next()) {
				return false;
			}
			closeDBConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean handleNavigationButtons (ActionEvent e) {
		if (e.getSource() == this.view.getBackButton()) {
			//changes the next button back from 'finish' to 'next'
			if(this.view.getNextButton().getText().equals("Finish")) {
				this.view.getNextButton().setText("Next");
			}
			String currentPanelName = this.view.getCurrentPanelName();
			if(!currentPanelName.equals(SmartTestingView.INITIAL_PANEL)) {
				this.view.goToPreviousCard();
				panelChanged = true;
			}
			return true;
		}
		//checks if the next button was pressed when it said 'finish'
		else if(((JButton)e.getSource()).getText().equals("Finish")) {
			finishProcess();
			this.view.goToNextCard();
			finished = false;
			panelChanged = true;
			return true;
		}
		else if (e.getSource() == this.view.getNextButton()) {
			//login panels
			String currentPanelName = this.view.getCurrentPanelName();
			switch (currentPanelName) {
			case SmartTestingView.LOGIN_PANEL:
				if(this.model.getFailedLoginAttempts() < MAX_FAILED_ATTEMPTS) {
					if(loginSuccessful()) {
						this.model.resetFailedLoginAttempts();
						this.view.goToNextCard();
						panelChanged = true;
					}
					else {
						this.model.incrementFailedLoginAttempts();
						this.view.resetLoginFields();
					}
				}
				break;
			case SmartTestingView.FACE_LOGIN_PANEL:
				if(this.model.getImagesCapturedCounter() >= 1) {
					//more than 1 image captured, process image
					if(this.model.getFailedLoginAttempts() < MAX_FAILED_ATTEMPTS) {
						if(facialProc.compareFaces()) {
							this.view.goToNextCard();
							this.model.resetFailedLoginAttempts();
							panelChanged = true;
						}
						else {
							this.view.retryImageCapture();
							this.view.setOkButtonAL(this);
							this.view.showFacialFrame();
							this.model.incrementFailedLoginAttempts();
						}
					}
				}
				break;
			case SmartTestingView.REGISTRATION_PANEL:
				if(captureRegistrationInfo()) {
					if(isUniqueUser()) {
						this.view.goToNextCard();
						panelChanged = true;
					}
					else {
						//need to retry
						this.view.retryInformationAlreadyInUse();
						this.view.setOkButtonAL(this);
					}
				}
				break;
			case SmartTestingView.FACE_REGISTRATION_PANEL:
				if(this.model.getImagesCapturedCounter() == REQ_IMAGES_NEEDED) {
					//save images
					if(!imagesSent) {
						facialProc.registerFace();
						this.view.hideFacialWindow();
						this.view.goToNextCard();
						panelChanged = true;
						imagesSent = true;
					}
				}
				break;
			default:
				//if not any of the specified panels, go to the next one
				this.view.goToNextCard();
				panelChanged = true;
			}
			return true;
		}
		else if (e.getSource() == this.view.getCancelButton()) {
			this.view.closeWindow();
			return true;
		}
		return false;
	}
	
	public void registerUser () {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		String dbInsertCommand = "INSERT INTO registered_users (Email, First_name, Last_name, password, keystroke_averages, keystroke_deviations, " +
				"Person_number, Last_Verification) VALUES (";
		dbInsertCommand += "'" + this.model.getUserEmail() + "', ";
		dbInsertCommand += "'" + this.model.getUserFirstName() + "', ";
		dbInsertCommand += "'" + this.model.getUserLastName() + "', ";
		dbInsertCommand += "'" + this.model.getUserPassword() + "', ";
		dbInsertCommand += "'" + this.model.getKeystrokeAverages() + "', ";
		dbInsertCommand += "'" + this.model.getKeystrokeDeviations() + "', ";
		dbInsertCommand += "'" + this.model.getUserNumber() + "', ";
		dbInsertCommand += "'" + cal.getTimeInMillis() + "' );";
		updateDB (dbInsertCommand);
	}
	
	public void updateLastVerification() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		String dbUpdateCommand = "UPDATE registered_users SET last_verification='" + cal.getTimeInMillis() + "' WHERE Email='" + this.model.getUserEmail() + "';";
		updateDB (dbUpdateCommand);
	}
	
	public void finishProcess () {
		if(loginProcess) {
			updateLastVerification();
			//create panel for successful login
		}
		else if (registrationProcess) {
			registerUser();
			//create panel for successful registration
		}
	}
	
	public void handlePanelSpecificButtons (ActionEvent e) {
		if (e.getSource() == this.view.getCaptureButton()) {
			//capture button
			String currentPanelName = this.view.getCurrentPanelName();
			if(currentPanelName.equals(SmartTestingView.FACE_LOGIN_PANEL)) {
				this.model.addImageCaptured(facialThread.getFaceFromImage());
				this.view.hideFacialWindow();
			}
			else if(currentPanelName.equals(SmartTestingView.FACE_REGISTRATION_PANEL)) {
				if(this.model.getImagesCapturedCounter() < REQ_IMAGES_NEEDED) {
					this.model.addImageCaptured(facialThread.getFaceFromImage());
					this.view.setImagesNeededField(Integer.toString(REQ_IMAGES_NEEDED - this.model.getImagesCapturedCounter()));
				}
				else {
					this.view.hideFacialWindow();
				}
			}
		}
		else if (e.getSource() == this.view.getSubmitButton()) {
			//submit button
			keystrokeProc.submitButtonPressed();
		}
		else if (e.getSource() == this.view.getResetButton()) {
			keystrokeProc.reset();
		}
		else if (e.getSource() == this.view.getContinueButton()) {
			generateURL();
			openLink();
			finished = true;
			this.view.setFacialUpdaterParentClosing();
		}
		else if (e.getSource() == this.view.getLoginButton()) {
			loginProcess = true;
			registrationProcess = false;
			this.view.addLoginCards();
			this.view.goToNextCard();
			this.view.setCaptureButtonAL(this);
			this.view.setResetButtonAL(this);
			this.view.setContinueButtonAL(this);
			this.model.initializeFacesCapturedArray();
			this.keystrokeProc.setUserLoggingIn(true);
			this.view.setInputFieldKeyAdapter(keys);
			enableFlowButtons();
			panelChanged = true;
		}
		else if (e.getSource() == this.view.getRegisterButton()) {
			registrationProcess = true;
			loginProcess = false;
			this.view.addRegistrationCards();
			this.view.goToNextCard();
			this.view.setCaptureButtonAL(this);
			this.view.setResetButtonAL(this);
			this.view.setContinueButtonAL(this);
			this.model.initializeFacesCapturedArray();
			this.keystrokeProc.setUserLoggingIn(false);
			this.view.setInputFieldKeyAdapter(keys);
			enableFlowButtons();
			panelChanged = true;
		}
		else if(e.getSource() == this.view.getOkButton()) {
			this.view.closePopupWindow();
		}
	}
	
	public void generateURL () {
		String phpString = "<?php session_start();\n$_SESSION['user_email'] = '"
				+ this.model.getUserEmail() + "';\nheader('Location: student2.php');\n?>";
		File phpFile = new File (phpScriptName);
		try {
			FileWriter writer = new FileWriter (phpFile);
			writer.write(phpString);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void openLink () {
		if(java.awt.Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			
			if(desktop.isSupported(Desktop.Action.BROWSE)) {
				URL url;
				try {
					url = new URL (phpURLAddress);
					URI uri = url.toURI();
					desktop.browse(uri);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(threadStarted) {
			if(!facialThread.isAlive()) {
				threadStarted = false;
			}
		}
		panelChanged = false;
		if(!handleNavigationButtons(e)) {
			handlePanelSpecificButtons(e);			
		}
		if(panelChanged) {
			checkCurrentPanel ();
		}
		if(finished) {
			this.view.closeWindow();
		}
	}
	
	public void checkCurrentPanel () {
		String currentPanelName = this.view.getCurrentPanelName();
		
		if(!currentPanelName.equals(SmartTestingView.INITIAL_PANEL)) {
			this.view.setBackButtonEnabled(true);
		}
		switch (currentPanelName) {
		case SmartTestingView.LOGIN_PANEL:
			if(facialThread == null && !threadStarted) {
				this.view.createFacialLoginFrame();
				facialThread = this.view.getFacialUpdater();
				facialThread.start();
				threadStarted = true;
			}
			break;
		case SmartTestingView.REGISTRATION_PANEL:
			if(facialThread == null && !threadStarted) {
				this.view.createFacialRegistrationFrame();
				facialThread = this.view.getFacialUpdater();
				facialThread.start();
				threadStarted = true;
			}
			break;
		case SmartTestingView.FACE_LOGIN_PANEL:
			if(facialThread != null && threadStarted) {
				this.view.setLocationFacialCapture();
				this.view.showFacialFrame();
			}
			break;
		case SmartTestingView.FACE_REGISTRATION_PANEL:
			if(facialThread != null && threadStarted) {
				this.view.setLocationFacialCapture();
				this.view.showFacialFrame();
			}
			break;
		case SmartTestingView.INITIAL_PANEL:
			this.view.setBackButtonEnabled(false);
			break;
		case SmartTestingView.KEYSTROKE_LOGIN_PANEL:
			keystrokeProc.userLoggingIn(); //grabs the data stored in the model
			this.view.getNextButton().setText("Finish");
			break;
		case SmartTestingView.KEYSTROKE_REGISTRATION_PANEL:
			this.view.getNextButton().setText("Finish");
			break;
		case SmartTestingView.SUCCESSFUL_LOGIN:
			disableFlowButtons();
			break;
		case SmartTestingView.SUCCESSFUL_REGISTRATION:
			disableFlowButtons();
			break;
		}
	}
	
	public int getImagesNeeded () {
		return SmartTestingController.REQ_IMAGES_NEEDED;
	}
	
	public static Image convertMatToImage (Mat m) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if(m.channels() > 1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = m.channels()*m.cols()*m.rows();
		byte [] b = new byte [bufferSize];
		m.get(0, 0, b);
		BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
		final byte [] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);
		return image;
	}
}
