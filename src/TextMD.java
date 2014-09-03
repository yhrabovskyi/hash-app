import java.awt.event.*;
import java.awt.Toolkit;
import java.awt.datatransfer.*;

import javax.swing.*;

/**
 * Panel contains text field with message digest and button to copy it.
 */
class TextMD extends JPanel {
	
	private JTextField textMD;
	
	public TextMD(int columns) {
		textMD = new JTextField(columns);
		textMD.setEditable(false);
		
		JButton copyButton = new JButton("Copy");
		copyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				StringSelection selection = new StringSelection(textMD.getText());
				clipboard.setContents(selection, selection);
			}
		});
		
		this.add(textMD);
		this.add(copyButton);
	}
	
	/**
	 * Set text in text field.
	 * @param s text to be set
	 */
	public void setText(String s) {
		textMD.setText(s);
	}
}
