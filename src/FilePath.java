import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Panel contains path of file and button to choose. 
 */
class FilePath extends JPanel {
	private File file = null;
	private JTextField filePath;
	
	public FilePath(final int mode, String title, int columns) {
		filePath = new JTextField(columns);
		final JFileChooser fc = new JFileChooser();
		
		JButton chooseFile = new JButton("Choose");
		chooseFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int chose;
				
				if (mode == JFileChooser.OPEN_DIALOG) {
					chose = fc.showOpenDialog(null);
				}
				else {
					chose = fc.showSaveDialog(null);
				}
				
				if (chose == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					filePath.setText(file.getAbsolutePath());
				}
			}
		});
		
		this.add(filePath);
		this.add(chooseFile);
	}
	
	/**
	 * Get file path text from text field.
	 * @return file path written in text field
	 */
	public String getFilePathText() {
		return filePath.getText();
	}
}
