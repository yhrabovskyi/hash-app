import java.io.File;

import java.io.IOException;

/**
 * It's a class, which contains a wrapper to File object and some useful methods.
 */
class FileContainer {
	
	private File file;
	
	public FileContainer(String name) {
		file = new File(name);
	}
	
	/**
	 * Tests whether file exists.
	 * @return true if file exists 
	 */
	public boolean isExist() {
		return file.isFile();
	}
	
	/**
	 * Creates a new file.
	 * @return true if file was created
	 * @throws IOException
	 */
	public boolean create() throws IOException {
		return file.createNewFile();
	}
	
	public File getFile() {
		return file;
	}
	
	public long getSize() {
		return file.length();
	}
}
