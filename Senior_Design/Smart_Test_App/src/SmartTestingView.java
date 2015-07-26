import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class SmartTestingView {
	private static final int DEFAULT_WINDOW_WIDTH = 350;
	private static final int DEFAULT_WINDOW_HEIGHT = 400;
	private static final int DEFAULT_PANEL_WIDTH = 250;
	private static final int DEFAULT_PANEL_HEIGHT = 300;
	private static final Dimension DEFAULT_FIELD_SIZE = new Dimension(300, 30);
	
	public static final String INITIAL_PANEL = "initial pane";
	public static final String REGISTRATION_PANEL = "registration pane";
	public static final String LOGIN_PANEL = "login pane";
	public static final String FACE_REGISTRATION_PANEL = "face registration";
	public static final String FACE_LOGIN_PANEL = "face login";
	public static final String KEYSTROKE_REGISTRATION_PANEL = "keystroke registration";
	public static final String KEYSTROKE_LOGIN_PANEL = "keystroke login";
	public static final String SUCCESSFUL_REGISTRATION = "registration complete";
	public static final String SUCCESSFUL_LOGIN = "login complete";
	
	public static final String codeWord = "keystrokes";
	
	JFrame primaryFrame;
	JFrame popupFrame;
	JFrame facialCaptureFrame;
	
	JPanel primaryPanel;
	JPanel cardsPanel;
	JPanel buttonPanel;
	JPanel imagePanel;
	
	CardLayout cardLayout;
	
	JButton loginButton;
	JButton registrationButton;
	JButton backButton;
	JButton nextButton;
	JButton cancelButton;
	JButton submitButton;
	JButton resetButton;
	JButton captureButton;
	JButton okButton;
	JButton continueButton;
	
	JLabel emailLabel;
	JLabel confirmEmailLabel;
	JLabel passwordLabel;
	JLabel confirmPasswordLabel;
	JLabel firstNameLabel;
	JLabel lastNameLabel;
	JLabel faceInstructionsLabel;
	JLabel contInstructionsLabel;
	JLabel keystrokeInstructionsLabel;
	JLabel imageLabel;
	JLabel counterLabel;
	
	JTextField emailField;
	JTextField confirmEmailField;
	JTextField firstNameField;
	JTextField lastNameField;
	JTextField imagesNeededField;
	JTextField userInputField;
	
	JPasswordField passwordField;
	JPasswordField confirmPasswordField;
	
	SmartTestingFacialUpdater facialUpdater;
	private boolean facialCaptureClosed = false;
	
	public SmartTestingView () {
		primaryFrame = new JFrame ();
		primaryFrame.setSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
		primaryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		primaryFrame.setResizable(false);
		
		primaryPanel = new JPanel ();
		cardsPanel = new JPanel();
		buttonPanel = new JPanel ();
		
		Box buttonBox = new Box(BoxLayout.X_AXIS);
		
		cardLayout = new CardLayout();
		cardsPanel.setLayout(cardLayout);
		cardsPanel.add(createInitialPanel(), INITIAL_PANEL);
		
		backButton = new JButton("Back");
		nextButton = new JButton("Next");
		cancelButton = new JButton("Cancel");
		
		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.add(new JSeparator(), BorderLayout.NORTH);
		
		buttonBox.setBorder(new EmptyBorder(new Insets(5,10,5,10)));
		buttonBox.add(backButton);
		buttonBox.add(Box.createHorizontalStrut(10));
		buttonBox.add(nextButton);
		buttonBox.add(Box.createHorizontalStrut(30));
		buttonBox.add(cancelButton);
		
		buttonPanel.add(buttonBox, BorderLayout.EAST);
		
		primaryPanel.add(cardsPanel);
		primaryPanel.add(buttonPanel);
		
		primaryFrame.add(primaryPanel);
	}
	
	public void setPrimaryFrameLocation (int x, int y) {
		primaryFrame.setLocation(x, y);
	}
	
	public JPanel createInitialPanel () {
		JPanel initial = new JPanel ();
		initial.setPreferredSize(new Dimension(DEFAULT_PANEL_WIDTH, DEFAULT_PANEL_HEIGHT));
		
		JPanel welcomePanel = new JPanel ();
		JPanel buttonsPanel = new JPanel ();
		
		JLabel welcomeLabel = new JLabel ("Welcome to Smart Testing");
		
		loginButton = new JButton ("Login");
		registrationButton = new JButton ("Register");
		
		welcomePanel.add(welcomeLabel);
		
		Box buttonBox = new Box(BoxLayout.X_AXIS);
		buttonBox.setBorder(new EmptyBorder(new Insets(5,10,5,10)));
		buttonBox.add(loginButton);
		buttonBox.add(Box.createHorizontalStrut(10));
		buttonBox.add(registrationButton);
		
		buttonsPanel.add(buttonBox);
		
		initial.setLayout(new BoxLayout(initial, BoxLayout.PAGE_AXIS));
		initial.add(welcomePanel);
		initial.add(buttonsPanel);
		
		return initial;
	}
	
	public void addLoginCards () {
		cardsPanel.removeAll();

		JPanel initialPanel = createInitialPanel ();
		initialPanel.setName(INITIAL_PANEL);
		
		JPanel loginPanel = createLoginPanel();
		loginPanel.setName(LOGIN_PANEL);
		
		JPanel faceLoginPanel = createFaceLoginPanel();
		faceLoginPanel.setName(FACE_LOGIN_PANEL);
		
		JPanel keystrokeLoginPanel = createKeystrokeLoginPanel();
		keystrokeLoginPanel.setName(KEYSTROKE_LOGIN_PANEL);
		
		JPanel loginCompletePanel = createSuccessfulLoginPanel ();
		loginCompletePanel.setName(SUCCESSFUL_LOGIN);
		
		cardsPanel.add(initialPanel);
		cardsPanel.add(loginPanel);
		cardsPanel.add(faceLoginPanel);
		cardsPanel.add(keystrokeLoginPanel);
		cardsPanel.add(loginCompletePanel);
	}
	
	public JPanel createLoginPanel () {
		JPanel login = new JPanel ();
		
		emailLabel = new JLabel ("Email");
		passwordLabel = new JLabel ("Password");
		
		emailField = new JTextField ();
		emailField.setMaximumSize(DEFAULT_FIELD_SIZE);
		passwordField = new JPasswordField();
		passwordField.setMaximumSize(DEFAULT_FIELD_SIZE);
		
		login.setLayout(new BoxLayout(login, BoxLayout.PAGE_AXIS));
		login.add(emailLabel);
		login.add(emailField);
		login.add(passwordLabel);
		login.add(passwordField);
		
		return login;
	}
	
	public JPanel createFaceLoginPanel () {
		JPanel instructionsPanel = new JPanel ();
		
		captureButton = new JButton("Capture");
		
		JLabel instructions = new JLabel ("Please use the additional window to proceed.");
		
		instructionsPanel.add(instructions);			
		
		return instructionsPanel;
	}
	
	public JPanel createKeystrokeLoginPanel() {
		JPanel keystroke = new JPanel ();
		JPanel inputPanel = new JPanel ();
		JPanel instructionsPanel = new JPanel ();
		JPanel buttonsPanel = new JPanel ();
		
		keystrokeInstructionsLabel = new JLabel ("Please type: " + codeWord);
		instructionsPanel.add(keystrokeInstructionsLabel);
		
		userInputField = new JTextField ();
		userInputField.setMaximumSize(new Dimension(250, 25));

		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
		inputPanel.add(userInputField);
		
		submitButton = new JButton("Submit");
		resetButton = new JButton("Reset");
		
		Box buttonBox = new Box(BoxLayout.X_AXIS);
		buttonBox.setBorder(new EmptyBorder(new Insets(5,10,5,10)));
		
		buttonBox.add(submitButton);
		buttonBox.add(Box.createHorizontalStrut(10));
		buttonBox.add(resetButton);
		
		buttonsPanel.add(buttonBox);
		
		keystroke.setLayout(new GridLayout(0,1));
		
		keystroke.add(instructionsPanel);
		keystroke.add(inputPanel);
		keystroke.add(buttonsPanel);
		
		return keystroke;
	}
	
	public JPanel createSuccessfulLoginPanel () {
		JPanel completePanel = new JPanel ();
		JPanel textPanel = new JPanel();
		JPanel buttonsPanel = new JPanel ();
		
		JLabel success = new JLabel ("You have successfully logged in!");
		JLabel proceed = new JLabel ("Please click the button below to continue.");
		
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.PAGE_AXIS));
		textPanel.add(success);
		textPanel.add(proceed);
		
		continueButton = new JButton ("Continue");
		
		buttonsPanel.add(continueButton);
		
		completePanel.setLayout(new GridLayout(0,1));
		
		completePanel.add(textPanel);
		completePanel.add(buttonsPanel);
		return completePanel;
	}
	
	public void addRegistrationCards () {
		cardsPanel.removeAll();
		
		JPanel initialPanel = createInitialPanel ();
		initialPanel.setName(INITIAL_PANEL);
		
		JPanel registrationPanel = createRegistrationPanel();
		registrationPanel.setName(REGISTRATION_PANEL);
		
		JPanel faceRegistrationPanel = createFaceRegistrationPanel();
		faceRegistrationPanel.setName(FACE_REGISTRATION_PANEL);
		
		JPanel keystrokeRegistrationPanel = createKeystrokeRegistrationPanel();
		keystrokeRegistrationPanel.setName(KEYSTROKE_REGISTRATION_PANEL);
		
		JPanel successfulRegistrationPanel = createSuccessfulRegistrationPanel();
		successfulRegistrationPanel.setName(SUCCESSFUL_REGISTRATION);
		
		cardsPanel.add(initialPanel);
		cardsPanel.add(registrationPanel);
		cardsPanel.add(faceRegistrationPanel);
		cardsPanel.add(keystrokeRegistrationPanel);
		cardsPanel.add(successfulRegistrationPanel);
	}
	
	public JPanel createRegistrationPanel () {
		JPanel registration = new JPanel ();
		registration.setSize(DEFAULT_PANEL_WIDTH, DEFAULT_PANEL_HEIGHT);
		
		emailLabel = new JLabel ("Email");
		confirmEmailLabel = new JLabel ("Confirm Email");
		passwordLabel = new JLabel ("Password");
		confirmPasswordLabel = new JLabel ("Confirm Password");
		firstNameLabel = new JLabel ("First Name");
		lastNameLabel = new JLabel ("Last Name");
		
		emailField = new JTextField ();
		confirmEmailField = new JTextField ();
		firstNameField = new JTextField ();
		lastNameField = new JTextField ();
		
		passwordField = new JPasswordField();
		confirmPasswordField = new JPasswordField();

		emailField.setMaximumSize(DEFAULT_FIELD_SIZE);
		confirmEmailField.setMaximumSize(DEFAULT_FIELD_SIZE);
		passwordField.setMaximumSize(DEFAULT_FIELD_SIZE);
		confirmPasswordField.setMaximumSize(DEFAULT_FIELD_SIZE);
		firstNameField.setMaximumSize(DEFAULT_FIELD_SIZE);
		lastNameField.setMaximumSize(DEFAULT_FIELD_SIZE);
		
		registration.setLayout(new GridLayout(0,1));
		
		registration.add(emailLabel);
		registration.add(emailField);
		registration.add(confirmEmailLabel);
		registration.add(confirmEmailField);
		registration.add(passwordLabel);
		registration.add(passwordField);
		registration.add(confirmPasswordLabel);
		registration.add(confirmPasswordField);
		registration.add(firstNameLabel);
		registration.add(firstNameField);
		registration.add(lastNameLabel);
		registration.add(lastNameField);
		
		return registration;
	}
	
	public JPanel createFaceRegistrationPanel () {
		JPanel instructionsPanel = new JPanel ();
		
		captureButton = new JButton("Capture");
		
		JLabel instructions = new JLabel ("Please use the additional window to proceed.");
		
		instructionsPanel.add(instructions);			
		
		return instructionsPanel;
	}
	
	public JPanel createKeystrokeRegistrationPanel () {
		JPanel keystroke = new JPanel ();		
		JPanel inputPanel = new JPanel ();
		JPanel instructionsPanel = new JPanel ();
		JPanel buttonsPanel = new JPanel ();
		
		keystrokeInstructionsLabel = new JLabel ("Please enter the word: " + codeWord);
		counterLabel = new JLabel ("Entries needed: 8");
		
		userInputField = new JTextField ();
		userInputField.setMaximumSize(new Dimension(250, 25));
		
		submitButton = new JButton("Submit");
		resetButton = new JButton("Reset");
		
		instructionsPanel.setLayout(new BoxLayout(instructionsPanel, BoxLayout.PAGE_AXIS));
		instructionsPanel.add(keystrokeInstructionsLabel);
		instructionsPanel.add(counterLabel);
		
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
		inputPanel.add(userInputField);
		
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));
		
		Box buttonBox = new Box(BoxLayout.X_AXIS);
		buttonBox.setBorder(new EmptyBorder(new Insets(5,10,5,10)));
		
		buttonBox.add(submitButton);
		buttonBox.add(Box.createHorizontalStrut(10));
		buttonBox.add(resetButton);
		
		buttonsPanel.add(buttonBox);
		
		keystroke.setLayout(new GridLayout(0,1));
		keystroke.add(instructionsPanel);
		keystroke.add(inputPanel);
		keystroke.add(buttonsPanel);
		
		return keystroke;
	}
	
	public JPanel createSuccessfulRegistrationPanel () {
		JPanel completePanel = new JPanel ();
		JPanel textPanel = new JPanel();
		JPanel buttonsPanel = new JPanel ();
		
		JLabel success = new JLabel ("You have successfully registered with Smart Testing!");
		JLabel proceed = new JLabel ("Please click the button below to continue.");
		
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.PAGE_AXIS));
		textPanel.add(success);
		textPanel.add(proceed);
		
		continueButton = new JButton ("Continue");
		
		buttonsPanel.add(continueButton);
		
		completePanel.setLayout(new GridLayout(0,1));
		
		completePanel.add(textPanel);
		completePanel.add(buttonsPanel);
		return completePanel;
	}
	
	public void showFrame () {
		primaryFrame.setVisible(true);
	}
	
	public void showFacialFrame () {
		facialCaptureFrame.setVisible(true);
	}
	
	public void goToNextCard () {
		cardLayout.next(cardsPanel);
		primaryFrame.repaint();
	}
	
	public void goToPreviousCard () {
		cardLayout.previous(cardsPanel);
		primaryFrame.repaint();
	}

	public JButton getLoginButton () {
		return this.loginButton;
	}
	
	public JButton getRegisterButton () {
		return this.registrationButton;
	}
	
	public JButton getCaptureButton () {
		return this.captureButton;
	}
	
	public JButton getCancelButton () {
		return this.cancelButton;
	}
	
	public JButton getSubmitButton () {
		return this.submitButton;
	}
	
	public JButton getNextButton () {
		return this.nextButton;
	}
	
	public JButton getBackButton () {
		return this.backButton;
	}

	public JButton getOkButton () {
		return okButton;
	}
	
	public JButton getResetButton () {
		return resetButton;
	}
	
	public JButton getContinueButton () {
		return continueButton;
	}

	public JTextField getEmailField () {
		return emailField;
	}
	
	public JPasswordField getPasswordField () {
		return passwordField;
	}

	public String getEmailText() {
		return emailField.getText();
	}
	
	public String getConfirmEmailText() {
		return confirmEmailField.getText();
	}
	
	public String convertCharToString (char [] characters) {
		String value = "";
		for(int i = 0; i < characters.length; i++) {
			value += characters[i];
		}
		return value;
	}
	
	public String getPasswordText () {
		return convertCharToString(passwordField.getPassword());		
	}
	
	public String getConfirmPasswordText () {
		return convertCharToString(confirmPasswordField.getPassword());
	}

	public String getFirstNameText () {
		return firstNameField.getText();
	}
	
	public String getLastNameText () {
		return lastNameField.getText();
	}

	public String getUserInputText () {
		return userInputField.getText();
	}
	
	public JTextField getUserInputField () {
		return userInputField;
	}
	
	public JTextField getImagesNeededField () {
		return imagesNeededField;
	}
	
	public JLabel getImageLabel () {
		return imageLabel;
	}

	public String getCurrentPanelName () {
		for(Component comp: cardsPanel.getComponents()) {
			if(comp.isVisible()) {
				return comp.getName();
			}
		}
		return null;
	}
	
	public SmartTestingFacialUpdater getFacialUpdater () {
		return facialUpdater;
	}

	public boolean getFacialWindowClosed () {
		return facialCaptureClosed;
	}

	public JFrame getFacialFrame () {
		return facialCaptureFrame;
	}

	public JPanel getImagePanel () {
		return imagePanel;
	}
	
	public void setImageLabel (JLabel image) {
		imageLabel = image;
	}
	
	public void setCounterLabel (int entries) {
		counterLabel.setText("Entries needed: " + entries);
	}
	
	public void setImagesNeededField (String text) {
		imagesNeededField.setText(text);
	}
	
	public void setBackButtonEnabled (boolean b) {
		backButton.setEnabled(b);
	}
	
	public void setBackButtonAL (ActionListener e) {
		backButton.addActionListener(e);
	}
	
	public void setNextButtonEnabled (boolean b) {
		nextButton.setEnabled(b);
	}
	
	public void setNextButtonAL (ActionListener e) {
		nextButton.addActionListener(e);
	}
	
	public void setCancelButtonEnabled (boolean b) {
		cancelButton.setEnabled(b);
	}
	
	public void setCancelButtonAL (ActionListener e) {
		cancelButton.addActionListener(e);
	}
	
	public void setCaptureButtonEnabled (boolean b) {
		captureButton.setEnabled(b);
	}
	
	public void setCaptureButtonAL (ActionListener e) {
		captureButton.addActionListener (e);
	}
	
	public void setLoginButtonEnabled (boolean b) {
		loginButton.setEnabled(b);
	}
	
	public void setLoginButtonAL (ActionListener e) {
		loginButton.addActionListener(e);
	}

	public void setRegistrationButtonEnabled (boolean b) {
		registrationButton.setEnabled(b);
	}
	
	public void setRegistrationButtonAL (ActionListener e) {
		registrationButton.addActionListener(e);
	}
	
	public void setSubmitButtonEnabled (boolean b) {
		submitButton.setEnabled(b);
	}
	
	public void setSubmitButtonAL (ActionListener e) {
		submitButton.addActionListener(e);
	}
	
	public void setOkButtonEnabled (boolean b) {
		okButton.setEnabled(b);
	}
	
	public void setOkButtonAL (ActionListener e) {
		okButton.addActionListener(e);
	}
	
	public void setResetButtonEnabled (boolean b) {
		resetButton.setEnabled(b);
	}
	
	public void setResetButtonAL (ActionListener e) {
		resetButton.addActionListener(e);
	}
	
	public void setContinueButtonAL (ActionListener e) {
		continueButton.addActionListener(e);
	}

	public void setUserInputEditabled (boolean b) {
		userInputField.setEditable(b);
	}
	
	public void setInputFieldKeyAdapter (KeyAdapter a) {
		userInputField.addKeyListener(a);
	}

	public void resetUserInputField () {
		userInputField.setText("");
	}
	
	public void closeWindow () {
		primaryFrame.dispose();
	}
	
	public void closePopupWindow () {
		popupFrame.dispose();
	}
	
	public void closeFacialWindow () {
		facialCaptureFrame.dispose();
		facialCaptureClosed = true;
	}
	
	public void hideFacialWindow() {
		facialCaptureFrame.setVisible(false);
	}

	public void resetLoginFields () {
		emailField.setText("");
		passwordField.setText("");
	}

	public void retryInformationMatch () {
		popupFrame = new JFrame ();
		popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel popupPanel = new JPanel ();
		JPanel detailsPanel = new JPanel ();
		JPanel buttonsPanel = new JPanel ();
		
		JLabel details = new JLabel ("Information entered didn't match, retry.");
		
		detailsPanel.add(details);
		
		okButton = new JButton ("Ok");
		
		buttonsPanel.add(okButton);
		
		popupPanel.setLayout(new BoxLayout(popupPanel, BoxLayout.PAGE_AXIS));
		
		popupPanel.add(detailsPanel);
		popupPanel.add(buttonsPanel);
		
		popupFrame.add(popupPanel);
		
		popupFrame.setSize(250, 250);
		popupFrame.setLocation(primaryFrame.getX(), primaryFrame.getY());
		popupFrame.setVisible(true);
	}
	
	public void retryInvalidInformation() {
		popupFrame = new JFrame ();
		popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel popupPanel = new JPanel ();
		JPanel detailsPanel = new JPanel ();
		JPanel buttonsPanel = new JPanel ();
		
		JLabel details = new JLabel ("Invalid email and/or password.");
		
		detailsPanel.add(details);
		
		okButton = new JButton ("Ok");
		
		buttonsPanel.add(okButton);
		
		popupPanel.setLayout(new BoxLayout(popupPanel, BoxLayout.PAGE_AXIS));
		
		popupPanel.add(detailsPanel);
		popupPanel.add(buttonsPanel);
		
		popupFrame.add(popupPanel);
		
		popupFrame.setSize(250, 250);
		popupFrame.setLocation(primaryFrame.getX(), primaryFrame.getY());
		popupFrame.setVisible(true);
	}
	
	public void retryInformationAlreadyInUse () {
		popupFrame = new JFrame ();
		popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel popupPanel = new JPanel ();
		JPanel detailsPanel = new JPanel ();
		JPanel buttonsPanel = new JPanel ();
		
		JLabel details = new JLabel ("Email already in use.");
		JLabel additionalDetails = new JLabel ("Please try a different one.");
		
		detailsPanel.add(details);
		
		okButton = new JButton ("Ok");
		
		buttonsPanel.add(okButton);
		
		popupPanel.setLayout(new BoxLayout(popupPanel, BoxLayout.PAGE_AXIS));
		
		popupPanel.add(detailsPanel);
		popupPanel.add(buttonsPanel);
		
		popupFrame.add(popupPanel);
		
		popupFrame.setSize(250, 250);
		popupFrame.setLocation(primaryFrame.getX(), primaryFrame.getY());
		popupFrame.setVisible(true);
	}
	
	public void retryImageCapture () {
		popupFrame = new JFrame ();
		popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel popupPanel = new JPanel ();
		JPanel detailsPanel = new JPanel ();
		JPanel buttonsPanel = new JPanel ();
		
		JLabel details = new JLabel ("Please retry capturing your face.");
		JLabel additionalDetails = new JLabel ("We were unable to recognize");
		JLabel finalDetails = new JLabel ("your face properly.");
		
		detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.PAGE_AXIS));
		detailsPanel.add(details);
		detailsPanel.add(additionalDetails);
		detailsPanel.add(finalDetails);
		
		okButton = new JButton ("Ok");
		
		buttonsPanel.add(okButton);
		
		popupPanel.setLayout(new BoxLayout(popupPanel, BoxLayout.PAGE_AXIS));
		
		popupPanel.add(detailsPanel);
		popupPanel.add(buttonsPanel);
		
		popupFrame.add(popupPanel);
		
		popupFrame.setSize(250, 250);
		popupFrame.setLocation(primaryFrame.getX(), primaryFrame.getY());
		popupFrame.setVisible(true);
	}
	
	public void removeImageLabel () {
		imagePanel.remove(imageLabel);
	}
		
	public void addImageLabel (JLabel image) {
		imageLabel = image;
		imagePanel.add(imageLabel);
		System.out.println("Image added.");
	}
	
	public void packFacialFrame () {
		facialCaptureFrame.pack();
	}
	
	public void repaintFacialFrame () {
		facialCaptureFrame.repaint();
	}

	public void createFacialRegistrationFrame () {
		facialCaptureFrame = new JFrame ();
		facialCaptureFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		facialCaptureFrame.setSize(400, 400);
		facialCaptureFrame.setResizable(false);
		
		JPanel face = new JPanel ();
		JPanel instructionsPanel = new JPanel ();
		JPanel subInstructionsPanel = new JPanel ();
		JPanel buttonsPanel = new JPanel ();
		imagePanel = new JPanel ();
		
		imageLabel = new JLabel();
		imagePanel.add(imageLabel);
		
		buttonsPanel.add(captureButton);
		
		subInstructionsPanel.setLayout(new BoxLayout(subInstructionsPanel, BoxLayout.PAGE_AXIS));
		faceInstructionsLabel = new JLabel ("Please press the 'Capture' button when your face appears in a green box.");
		contInstructionsLabel = new JLabel ("Each image should be a different view of your face.");
		
		subInstructionsPanel.add(faceInstructionsLabel);
		subInstructionsPanel.add(contInstructionsLabel);
		imagesNeededField = new JTextField (String.valueOf(10));
		imagesNeededField.setEditable(false);
		
		instructionsPanel.add(subInstructionsPanel);
		instructionsPanel.add(imagesNeededField);
		
		face.setLayout(new BoxLayout(face, BoxLayout.PAGE_AXIS));
		face.add(instructionsPanel);
		face.add(imagePanel);
		face.add(buttonsPanel);
		
		facialUpdater = new SmartTestingFacialUpdater(this);
		
		facialCaptureFrame.add(face);
	}
	
	public void createFacialLoginFrame () {
		facialCaptureFrame = new JFrame ();
		facialCaptureFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		facialCaptureFrame.setSize(400, 400);
		facialCaptureFrame.setResizable(false);
		
		JPanel face = new JPanel ();
		JPanel instructionsPanel = new JPanel();
		imagePanel = new JPanel ();
		JPanel buttonsPanel = new JPanel ();
		
		imageLabel = new JLabel ();
		
		buttonsPanel.add(captureButton);
		
		faceInstructionsLabel = new JLabel("Press 'Capture' when your face appears in the green box");
		instructionsPanel.add(faceInstructionsLabel);
		
		imagePanel.add(imageLabel);
		
		face.setLayout(new BoxLayout(face, BoxLayout.PAGE_AXIS));
		face.add(instructionsPanel);
		face.add(imagePanel);
		face.add(buttonsPanel);
		
		facialUpdater = new SmartTestingFacialUpdater(this);
		
		facialCaptureFrame.add(face);
	}
	
	public void setLocationFacialCapture() {
		facialCaptureFrame.setLocation(primaryFrame.getX(), primaryFrame.getY());
	}

	public void setFacialUpdaterParentClosing () {
		this.facialUpdater.parentThreadClosing();
	}
}
