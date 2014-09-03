import java.security.NoSuchAlgorithmException;
import java.io.FileNotFoundException;
import java.io.IOException;

class ConsoleGUI {
	
	private String[] args;
	private FileContainer file;
	private FileContainer fileWithHash;
	private String helpArg = "-help";
	private String outputArg = "-o";
	private String hashAlgNameArg = "-ha";
	private String compareArg = "-c";
	private String algorithmName;
	
	public ConsoleGUI(String[] consoleArgs) {
		args = consoleArgs;
	}
	
	/**
	 * It starts console gui.
	 */
	public void start() {
		String s = "";
		HashIO hio;
		
		file = new FileContainer(args[0]);
		if (!file.isExist()) {
			if (args[0].equals(helpArg)) {
				showHelpPage();
				return;
			}
			else {
				System.out.println("File doesn't exist!");
				return;
			}
		}
		
		try {
			for (int i = 1; i < args.length; i++) {
				s = args[i];
				
				if (s.equals(hashAlgNameArg)) {
					algorithmName = args[++i];
				}
				
				if (s.equals(outputArg)) {
					fileWithHash = new FileContainer(args[1]);
					hio = new HashIO(algorithmName, file, fileWithHash);
					hio.outputMessageDigestToFile();
					return;
				}
				
				if (s.equals(compareArg)) {
					fileWithHash = new FileContainer(args[1]);
					hio = new HashIO(algorithmName, file, fileWithHash);
					if (hio.compareMessageDigests()) {
						System.out.println("Hashes are identical!");
					}
					else {
						System.out.println("Hashes don't match!");
					}
					return;
				}
			}
			
			hio = new HashIO(algorithmName, file);
			System.out.println(hio.getMessageDigestInString());
		}
		catch (NoSuchAlgorithmException exception) {
			System.out.println("There is no such hash algorithm!");
		}
		catch (FileNotFoundException exception) {
			System.out.println("File is not found!");
		}
		catch (IOException exception) {
			System.out.println("Some IO problems!");
		}
		
	}
	
	private void showHelpPage() {
		System.out.print("Program can take such string: ");
		System.out.println("name [name] -ha algorithm_name [-other parameters]");
		System.out.println("name : name of the file to hash;");
		System.out.println("[name] : name of the file, that contains digest to compare or to where output digest;");
		System.out.println("-help : show help page;");
		System.out.println("-c : hashes file and compares to digest, which is in second file.");
		System.out.println("-o : output message digest to the second file.");
		System.out.println("-ha name : what algorithm to use. Instead of \"name\" must use algorithm name. ");
		System.out.print("\tNames of hash algorithms: ");
		String[] names = Hash.getAlgorithms();
		for (String s : names) {
			System.out.print(s + " ");
		}
		System.out.println();
	}
}
