import java.awt.EventQueue;
import javax.swing.JFrame;

public class Start {
	
	public static void main(String[] args) {
		if (args.length > 0) {
			ConsoleGUI cgui = new ConsoleGUI(args);
			cgui.start();
		}
		else {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					JFrame frame = new MainFrame();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
				}
			});
		}
	}
}
