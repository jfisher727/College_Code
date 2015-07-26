import javax.swing.SwingUtilities;


public class SmartTesting {
	
	public static void main (String [] args) {
		try {
			//Class.forName("org.gjt.mm.mysql.Driver");
			Class.forName("com.mysql.jdbc.Driver");
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run () {
					SmartTestingModel model = new SmartTestingModel();
					SmartTestingView view = new SmartTestingView ();
					SmartTestingController controller = new SmartTestingController(model, view);
					view.setPrimaryFrameLocation(800,300);
					controller.control();
				}
			});
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
