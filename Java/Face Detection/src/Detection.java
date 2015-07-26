import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

//
// Detects faces in an image, draws boxes around them, and writes the results
// to "faceDetection.png".
//
class DetectFaceDemo {
	
	public void run() {
		System.out.println("\nRunning DetectFaceDemo");

		// Create a face detector from the cascade file in the resources
		// directory.
		CascadeClassifier faceDetector = new CascadeClassifier ("C:/Users/Jason/Dropbox/Workspace/Face Detection/haarcascade_frontalface_default.xml");
		VideoCapture camera = new VideoCapture (0);
		JFrame frame = new FaceWindow();
		JPanel container = new JPanel ();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		JPanel imagePanel = new JPanel();
		JPanel buttonPanel = new JPanel ();
		JButton capture = new JButton ("Capture");
		JLabel label = new JLabel ();
		buttonPanel.add(capture);
		imagePanel.add(label);
		
		container.add(imagePanel);
		container.add(buttonPanel);
		frame.add(container);
		frame.setVisible(true);
		    
		while (true) {
			Mat image = new Mat ();
			camera.retrieve(image);
			
	
			// Detect faces in the image.
			// MatOfRect is a special container class for Rect.
			MatOfRect faceDetections = new MatOfRect();
			faceDetector.detectMultiScale(image, faceDetections);
	
			//System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
			
			// Draw a bounding box around each face.
			Rect biggest = new Rect(0,0,0,0);
			for (Rect rect : faceDetections.toArray()) {
				if(rect.width > biggest.width && rect.height > biggest.height) {
					biggest = rect;
				}
			}
			Core.rectangle(image, new Point(biggest.x, biggest.y), new Point(biggest.x + biggest.width, biggest.y + biggest.height), new Scalar(0, 255, 0));
			
			Mat face = image.submat(biggest.y, biggest.y + biggest.height, biggest.x, biggest.x + biggest.width);
			Imgproc.cvtColor(face, face, Imgproc.COLOR_RGB2GRAY);
			//FaceRecognizer faceRecognizer = createEigenFaceRecognizer();
			//faceRecognizer.load("C:/Users/Jason/Dropbox/Workspace/Face Detection/lbpcascade_frontalface.xml");
			
			//Imgproc.equalizeHist(face, face);
			
			//FaceRecognizer faceRecognizer = createEigenFaceRecognizer();
			
			// Save the visualized detection.
			String filename = "faceDetection.png";
			System.out.println(String.format("Writing %s", filename));
			Highgui.imwrite(filename, image);
			
			String filename2 = "face.png";
			System.out.println(String.format("Writing %s", filename2));
			Highgui.imwrite(filename2, face);
						
			MatOfByte matOfByte = new MatOfByte ();
			Highgui.imencode(".jpg", image, matOfByte);
			byte [] byteArray = matOfByte.toArray();
			BufferedImage bufImage = null;
			
			try {
				InputStream in = new ByteArrayInputStream (byteArray);
				bufImage = ImageIO.read(in);
				imagePanel.remove(label);
				label = new JLabel (new ImageIcon(bufImage));
				imagePanel.add(label);
				capture.setAlignmentY(Component.BOTTOM_ALIGNMENT);
				frame.pack();
			} catch (Exception e) {
				e.printStackTrace();
			}
			frame.repaint();
		}
	}
}

public class Detection {
  public static void main(String[] args) {
    System.out.println("Hello, OpenCV");

    // Load the native library.
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    new DetectFaceDemo().run();
  }
}