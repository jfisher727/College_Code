import javax.swing.BoxLayout;
import javax.swing.JFrame;


public class FaceWindow extends JFrame{	
	
	public FaceWindow () {
		setTitle ("Face Detection");
		setSize (400,500);
				
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
