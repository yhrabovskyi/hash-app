import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

class MainFrame extends JFrame {
	
	public MainFrame() {
		JPanel panel = new MainPanel();
		this.add(panel);
		this.setLocationByPlatform(true);
		this.pack();
		this.setTitle(StringConstants.TTLProgram);
	}
}

class MainPanel extends JPanel {
	
	private static final int TEXT_FIELD_COLUMNS = 30;
	private TextMD textMDPanel;
	private ProgressOfHashing progressPanel;
	private Options optionsPanel;
	private FilePath fileToHashPanel;
	private FilePath fileWithMDPanel;
	private Thread startThread;
	
	public MainPanel() {
		this.setLayout(new GridLayout(5, 1));
		
		// Set up file choose panels
		fileToHashPanel = new FilePath(JFileChooser.OPEN_DIALOG, "File to hash", TEXT_FIELD_COLUMNS);
		fileWithMDPanel = new FilePath(JFileChooser.SAVE_DIALOG, "File with message digest", TEXT_FIELD_COLUMNS);
		addBorder("File to hash", fileToHashPanel);
		addBorder("File contains message digest", fileWithMDPanel);
		this.add(fileToHashPanel);
		this.add(fileWithMDPanel);
		
		// Set up options panel
		optionsPanel = new Options();
		addBorder("Hash options", optionsPanel);
		this.add(optionsPanel);
		// Set up start button in this panel
		optionsPanel.getStartButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton button = optionsPanel.getStartButton();
				
				// If that was start button then start hash process
				if (button.getText().equals(StringConstants.TTLStartButton)) {
					button.setText(StringConstants.TTLCancelButton);
					DoHash doHashObj = new DoHash();
					startThread = new Thread(doHashObj);
					startThread.start();
				}
				// It was a cancel button then cancel all processes
				else {
					startThread.interrupt();
					button.setText(StringConstants.TTLStartButton);
				}
			}
		});
		
		// Set up text field panel for message digest
		textMDPanel = new TextMD(TEXT_FIELD_COLUMNS);
		addBorder("Message digest", textMDPanel);
		this.add(textMDPanel);
		
		// Set up progress bar panel
		progressPanel = new ProgressOfHashing();
		addBorder("Hashing progress", progressPanel);
		this.add(progressPanel);
	}
	
	/**
	 * Set border for panel.
	 * @param title title of border
	 * @param panel panel to which add border
	 */
	private void addBorder(String title, JPanel panel) {
		Border b = BorderFactory.createEtchedBorder();
		Border t = BorderFactory.createTitledBorder(b, title);
		panel.setBorder(t);
	}
	
	/**
	 * Controls all operations after click "Start" button, such as
	 * hashes file in separate thread, shows result respectively to selected options,
	 * shows errors in message dialog.
	 */
	private class DoHash implements Runnable {
		
		@Override
		public void run() {
			long sleepMillis = 200;
			
			// File to hash
			FileContainer source = new FileContainer(fileToHashPanel.getFilePathText());
			// File contains hash or to output to it
			FileContainer dest = null;
			if (!optionsPanel.getDoNothingOption()) {
				 dest = new FileContainer(fileWithMDPanel.getFilePathText());
			}
			
			// Creates object that will hashing file in different thread
			HashIO hashObj = new HashIO(optionsPanel.getAlgorithmName(), optionsPanel.getCompareOption(),
					source, dest);
			
			Thread t = new Thread(hashObj);
			// Set uncaught handler
			Thread.UncaughtExceptionHandler handler = new ErrorOfHashingProcessHandler();
			t.setUncaughtExceptionHandler(handler);
			t.start();
			
			progressPanel.setShowPercents(true);
			
			// While hashing process is not done or error is not occur
			try {
				while (!hashObj.getIsDone() && !hashObj.getIsError()) {
					Thread.sleep(sleepMillis);
					progressPanel.setProgress(hashObj.getIsDoneInPercents());
				}
			}
			catch (InterruptedException exception) {
				t.interrupt();
				return;
			}
			
			// Operation is ended, return start button
			optionsPanel.getStartButton().setText(StringConstants.TTLStartButton);
			
			// If error is occured
			if (hashObj.getIsError()) {
				return;
			}
			
			// Show message digest in text field
			if (dest == null) {
				textMDPanel.setText(hashObj.getMD());
				return;
			}
			
			// Show dialog window for result of comparing or output
			// If was needed to compare
			if (optionsPanel.getCompareOption()) {
				showDialogOfDone("Message digests are identical!", "Message digests are not identical!",
						"Compare", hashObj, JOptionPane.INFORMATION_MESSAGE);
			}
			// Else to output
			else {
				showDialogOfDone("Output to file is successful", "Output to file is not successful",
						"Output", hashObj, JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		/**
		 * Show message dialog of result of compare or output
		 * @param goodStr show message if operation is successful
		 * @param badStr show message if operation is not successful
		 * @param title title of dialog
		 * @param hashObj HashIO object, which is done operation
		 */
		private void showDialogOfDone(String goodStr, String badStr, String title, HashIO hashObj, int paneOption) {
			String msg;
			if (hashObj.getIsSuccessful()) {
				msg = goodStr;
			}
			else {
				msg = badStr;
			}
			
			JOptionPane.showMessageDialog(null, msg, title, paneOption);
		}
	}
	
	/**
	 * Represents handler instance, which handles unchecked exceptions from run method of thread.
	 */
	private class ErrorOfHashingProcessHandler implements Thread.UncaughtExceptionHandler {
		
		@Override
		public void uncaughtException(Thread t, Throwable se) {
			JOptionPane.showMessageDialog(null, se.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
		}
	}
}
