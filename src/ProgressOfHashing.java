import javax.swing.*;

class ProgressOfHashing extends JPanel{
	
	private JProgressBar bar;
	
	public ProgressOfHashing() {
		bar = new JProgressBar(0, 100);
		
		this.add(bar);
	}
	
	/**
	 * Set percents of progress.
	 * @param n percents of done process
	 */
	public void setProgress(int n) {
		bar.setValue(n);
	}
	
	/**
	 * Set to show percents text.
	 * @param b if true then percents are showed
	 */
	public void setShowPercents(boolean b) {
		bar.setStringPainted(b);
	}
}
