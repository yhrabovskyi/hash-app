import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Panel contains options and hash algorithms selections.
 */
class Options extends JPanel {
	
	private String algName = null;
	// First must check this flag
	private boolean doNothing = true;
	// Then this flag
	private boolean compare = false;
	private JButton startButton;
	
	public Options() {
		// ActionListener is registered in main panel
		startButton = new JButton(StringConstants.TTLStartButton);
		JRadioButton doNothingButton = addRadioButton("Show", true, false);
		JRadioButton compareButton = addRadioButton("Compare", false, true);
		JRadioButton outputButton = addRadioButton("Output", false, false);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(doNothingButton);
		bg.add(compareButton);
		bg.add(outputButton);
		
		// Set up combobox
		final JComboBox<String> hashAlgs = new JComboBox<String>();
		hashAlgs.setEditable(false);
		String[] algs = Hash.getAlgorithms();
		for (String s : algs) {
			hashAlgs.addItem(s);
		}
		algName = (String) hashAlgs.getSelectedItem();
		
		hashAlgs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				algName = (String) hashAlgs.getSelectedItem();
			}
		});
		
		this.add(doNothingButton);
		this.add(compareButton);
		this.add(outputButton);
		this.add(hashAlgs);
		this.add(startButton);
	}
	
	/**
	 * Get selected algorithm name.
	 * @return algorithm name
	 */
	public String getAlgorithmName() {
		return algName;
	}
	
	/**
	 * Get selected option.
	 * @return true if show is selected
	 */
	public boolean getDoNothingOption() {
		return doNothing;
	}
	
	/**
	 * Get selected option.
	 * @return true if compare is selected or false if output is selected
	 */
	public boolean getCompareOption() {
		return compare;
	}
	
	/**
	 * Get start button.
	 * @return start button
	 */
	public JButton getStartButton() {
		return startButton;
	}
	
	/**
	 * Creates JRadioButton.
	 * @param title name of button
	 * @param fDoNothing set doNothing flag
	 * @param fCompare set compare flag
	 * @return
	 */
	private JRadioButton addRadioButton(String title, final boolean fDoNothing, final boolean fCompare) {
		JRadioButton button = new JRadioButton(title, fDoNothing);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doNothing = fDoNothing;
				compare = fCompare;
			}
		});
		return button;
	}
}
