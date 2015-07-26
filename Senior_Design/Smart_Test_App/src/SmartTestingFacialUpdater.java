import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;


public class SmartTestingFacialUpdater extends Thread{
	private static final String cascadeFile = "C:/Users/Jason/Dropbox/Workspace/Smart_Test_App/haarcascade_frontalface_default.xml";
	//private static final String cascadeFile = "C:/Users/Jason Laptop/Dropbox/Workspace/Smart_Test_App/haarcascade_frontalface_default.xml";
	
	SmartTestingView view;
	
	private Mat face;
	private CascadeClassifier faceDetector;
	private VideoCapture camera;
	private boolean faceInImage = false;
	private JFrame primaryFrame;
	private JPanel imagePanel;
	private JLabel imageLabel;
	private boolean parentThreadClosed;

	public SmartTestingFacialUpdater (SmartTestingView view) {
		this.view = view;
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		faceDetector = new CascadeClassifier (cascadeFile);
		camera = new VideoCapture (0);
		primaryFrame = this.view.getFacialFrame();
		imagePanel = this.view.getImagePanel();
		imageLabel = this.view.getImageLabel();
		parentThreadClosed = false;
	}
	
	public Mat getFaceFromImage () {
		if(faceInImage) {
			return face;
		}
		else {
			return null;
		}
	}
	
	public void parentThreadClosing () {
		parentThreadClosed = true;
	}
	
	public void run () {		
		while(!parentThreadClosed) {
			//System.out.println("Facial updater running.");
			Mat image = new Mat ();
			camera.retrieve(image);
			
			MatOfRect faceDetections = new MatOfRect ();
			faceDetector.detectMultiScale(image, faceDetections);
			
			if(faceDetections.toArray().length >= 1) {
				faceInImage = true;
				Rect biggest = new Rect (0,0,0,0);
				for(Rect rect: faceDetections.toArray()) {
					if(rect.width > biggest.width && rect.height > biggest.height) {
						biggest = rect;
					}
				}
				Core.rectangle(image, new Point(biggest.x, biggest.y), new Point(biggest.x + biggest.width, biggest.y + biggest.height), new Scalar(0,255,0));
				face = image.submat(biggest.y, biggest.y + biggest.height, biggest.x, biggest.x + biggest.width);
				Imgproc.cvtColor(face, face, Imgproc.COLOR_RGB2GRAY);
			}
			else {
				faceInImage = false;
			}
						
			try {
				Image icon = SmartTestingController.convertMatToImage(image);
				imagePanel.remove(imageLabel);
				imageLabel= new JLabel (new ImageIcon (icon));
				imagePanel.add(imageLabel);
				primaryFrame.pack();
			} catch (Exception e) {
				e.printStackTrace();
			}
			primaryFrame.repaint();
		}
	}
	
	public void closeThread() {
		return;
	}
}
