import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


public class FacialProcessing {
	private static final int DEFAULT_IMAGE_WIDTH = 200;
	private static final int DEFAULT_IMAGE_HEIGHT = 200;
	private static final double MINIMUM_CONFIDENCE = 0.75;
	
	private static final String imagesDirectory = "C:/Users/Jason/Dropbox/Workspace/Learning Server/Faces/";
	private static final String testFiles = "C:/Users/Jason/Dropbox/Workspace/Learning Server/testFiles.txt";
	private static final String tempFileLocation = "C:/Users/Jason/Dropbox/Workspace/Smart_Test_App/";
	
	private static String tempFileName;
	/*
	private static final String imagesDirectory = "C:/Users/Jason Laptop/Dropbox/Workspace/Learning Server/Faces/";
	private static final String testFiles = "C:/Users/Jason Laptop/Dropbox/Workspace/Learning Server/testFiles.txt";
	private static final String tempFileLocation = "C:/Users/Jason/Dropbox/Workspace/Smart_Test_App/";
	*/
	
	private static final String dbServerAddress = "localhost";
	private static final String recognitionDataFile = "facedata.xml";
	private static final String comparisonFile = "C:/Users/Jason/Dropbox/Workspace/Smart_Test_App/compare.txt";
	
	private String folderLocation;
	private String folderDirectory;	

	private static final int faceRecPort = 8080;
	private Socket dbServerSocket;
	private PrintWriter writeToSocket;
	private BufferedReader readFromSocket;
	
	ArrayList <String> previousEntries = new ArrayList <String> ();
	
	int lastLabelNumber = 1;
	int lastImageCaptured = 0;
	boolean fileCreated = false;
	int personNumber = 0;
	
	SmartTestingModel model;
	
	public FacialProcessing (SmartTestingModel model) {
		this.model = model;
	}
	
	private void connectToFaceServer () {
		try {
			dbServerSocket = new Socket(dbServerAddress, faceRecPort);
			writeToSocket = new PrintWriter(dbServerSocket.getOutputStream(), true);
			readFromSocket = new BufferedReader (new InputStreamReader (dbServerSocket.getInputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void disconnectFromFaceServer () {
		try {
			readFromSocket.close();
			writeToSocket.close();
			dbServerSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getPreviousEntries () {
		Scanner fileScanner;
		try {
			fileScanner = new Scanner (new File(testFiles));
			String lastLine = null;
			while(fileScanner.hasNextLine()) {
				lastLine = fileScanner.nextLine();
				previousEntries.add(lastLine);
			}
			fileScanner.close();
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		}
	}
	
	public void createFolder() {
		folderLocation = imagesDirectory + this.model.getUserName();
		File storage = new File(folderLocation);
		folderDirectory = storage.getAbsolutePath();
		if(!storage.exists()) {
			fileCreated = storage.mkdirs();
			if(fileCreated) {
				System.out.println("Successfully created new folder");
				fileCreated = true;
			}
			else {
				System.out.println("Failed to create new folder");
				fileCreated = false;
			}
		}
		else {
			fileCreated = false; //aka the file already existed
		}
	}
	
	public void scanForLabels () {
		try {
			Scanner fileScanner = new Scanner (new File(testFiles));
			String lastLine = null;
			while(fileScanner.hasNextLine()) {
				lastLine = fileScanner.nextLine();
				previousEntries.add(lastLine);
			}
			if(lastLine != null) {
				Scanner lineScanner = new Scanner (lastLine);
				lineScanner.useDelimiter(";");
				lineScanner.next(); //file location
				lineScanner.next(); //person's name
				lastLabelNumber = Integer.valueOf(lineScanner.next());
				lineScanner.close();
			}
			fileScanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File deleteFile = new File(testFiles);
		deleteFile.delete();
	}
	
	public void writeImage(Mat image, int imageNumber) {
		Imgproc.resize(image, image, new Size (DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT));
		String filename = folderLocation + "/face" + imageNumber + ".png";
		//System.out.println(String.format("Writing %s", filename));
		Highgui.imwrite(filename, image);
	}
	
	public void writeTempImage(Mat image) {
		Imgproc.resize(image, image, new Size (DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT));
		tempFileName = tempFileLocation + "temp.png";
		Highgui.imwrite(tempFileName, image);
	}
	
	public void writeComparisonFile() {
		File compareFile = new File(comparisonFile);
		FileWriter writeOneLine;
		try {
			writeOneLine = new FileWriter(compareFile);
			writeOneLine.write(tempFileName + ";" + this.model.getUserName() + ";" + this.model.getUserNumber());
			writeOneLine.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeImages () {
		File csvFile = new File(testFiles);
		try {
			FileWriter csvWriter = new FileWriter (csvFile);
			for(int i = 0; i < previousEntries.size(); i++) {
				csvWriter.write(previousEntries.get(i) + "\r\n");
			}
			Mat [] images = this.model.getImagesCaptured();
			personNumber = lastLabelNumber + 1;
			for(int i = 0; i < images.length; i++) {
				lastImageCaptured = i;
				Mat currentImage = images[i];
				writeImage(currentImage, i);
				if(fileCreated) {
					//add the images to the overall file list
					csvWriter.write(folderDirectory + "\\face" + i + ".png;" + this.model.getUserName() + ";" + personNumber + "\r\n");
				}
			}
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void getUpdatedXML() {
		connectToFaceServer();
		writeToSocket.println("update");
		
		try {
			readFromSocket.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		disconnectFromFaceServer();
	}
	
	public void registerFace () {
		createFolder();
		scanForLabels();
		getPreviousEntries();
		writeImages();
		getUpdatedXML();
		System.out.println("Person number: " + personNumber);
		this.model.setUserNumber(personNumber);
		System.out.println("User Number: " + this.model.getUserNumber());
	}
	
	public boolean compareFaces () {
		getUpdatedXML();
		writeTempImage(this.model.getImagesCaptured()[0]);
		writeComparisonFile();
		RecognitionResults comparisonResults = (new FaceRecognition().recognizeFileList(comparisonFile));
		if(!comparisonResults.correct) {
			if (comparisonResults.confidenceValue < MINIMUM_CONFIDENCE) {
				return !comparisonResults.correct;
			}
		}
		return comparisonResults.correct;
	}

}
