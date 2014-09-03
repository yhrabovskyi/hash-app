import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.lang.RuntimeException;
import java.lang.Throwable;
import java.lang.Thread;

/**
 * Class contains operations, which do all necessary actions for get message digest.
 */
class HashIO implements Runnable {
	
	// How many bytes to read from file
	private static final int BYTES_TO_READ = 64;
	// Contains message digest in byte representation
	private byte[] messageDigest;
	// Indicates algorithm to use
	private String algorithm;
	// Contains message digest to get from outside
	private String md;
	// Contains file to hash
	private FileContainer fcToHash;
	// Contains file with hash
	private FileContainer fcWithHash;
	// Indicates if need to compare hashes
	private boolean isComparing;
	// Indicates if hash process is done
	private boolean isDone = false;
	// Indicates if comparing or output are successful
	private boolean isSuccessful = false;
	// Indicates if error occur
	private boolean isError = false;
	// Contains how many percents of file are hashed
	private int isDoneInPercents;
	
	/**
	 * Constructor for create instance for hash.
	 * @param alg
	 * @param fileToHash
	 */
	public HashIO(String alg, FileContainer fileToHash) {
		algorithm = alg;
		fcToHash = fileToHash;
	}
	
	/**
	 * Constructor for create instance for hash and compare.
	 * @param alg
	 * @param fileToHash
	 * @param fileWithHash
	 */
	public HashIO(String alg, FileContainer fileToHash, FileContainer fileWithHash) {
		this(alg, fileToHash);
		fcWithHash = fileWithHash;
	}
	
	/**
	 * Constructor for create instance when using GUI.
	 */
	public HashIO(String alg, boolean compare, FileContainer source, FileContainer dest) {
		this(alg, source, dest);
		isComparing = compare;
	}
	
	/**
	 * It's for GUI implementation.
	 */
	@Override
	public void run() {
		try {
			if (fcWithHash != null) {
				if (isComparing) {
					isSuccessful = compareMessageDigests();
				}
				else {
					isSuccessful = outputMessageDigestToFile();
				}
			}
			else {
				md = getMessageDigestInString();
			}
			isDone = true;
		}
		catch (NoSuchAlgorithmException exception) {
			isError = true;
			throw new RuntimeException(exception.getMessage());
		}
		catch (IOException exception) {
			isError = true;
			throw new RuntimeException(exception.getMessage());
		}
	}
	
	/**
	 * Get message digest of file.
	 * @throws NoSuchAlgorithmException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void getMessageDigest()
			throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		// Represent hash algorithm and current state of hash
		Hash hash = new Hash(algorithm);
		// Array to where read bytes are put
		byte[] fileBytes = new byte[BYTES_TO_READ];
		// Bytes read from file by read method
		int bytesRead = 0;
		// Sum of read bytes
		long howMuchBytesRead = 0;
		// Size of file
		long fullBytesSize = fcToHash.getSize();
		// Byte stream of file
		FileInputStream fileToHash = new FileInputStream(fcToHash.getFile()); 
		
		try {
			// While there are bytes to read from file and thread is not requested to terminate
			while ( ((bytesRead = fileToHash.read(fileBytes)) > 0) && !Thread.currentThread().isInterrupted() ) {
				hash.addBytes(fileBytes, bytesRead);
				
				// Calculate in percents size of hashed file
				howMuchBytesRead += bytesRead;
				isDoneInPercents = (int) (((howMuchBytesRead * 1.0) / fullBytesSize) * 100.0);
			}
			
			if (Thread.currentThread().isInterrupted()) {
				//throw new CancelException();
			}
			
			messageDigest = hash.calculate();
		}
		finally {
			fileToHash.close();
		}
	}
	
	/**
	 * Get message digest of file in string.
	 * @return string representation of message digest 
	 */
	public String getMessageDigestInString()
			throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		getMessageDigest();
		
		return bytesToHexString(messageDigest);
	}
	
	/**
	 * Compare two hashes: one from file, which is hashed and second from text file.
	 * @return true if two hashes are identical
	 * @throws NoSuchAlgorithmException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public boolean compareMessageDigests()
			throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		String hashToCompare = getHashFromFile();
		getMessageDigest();
		String hash = bytesToHexString(messageDigest);
		
		if (hash.equals(hashToCompare)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Output message digest to file.
	 * @throws NoSuchAlgorithmException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public boolean outputMessageDigestToFile()
			throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		// Indicates if writing to the file is successful
		boolean isDone = false;
		
		fcWithHash.create();
		String hashToWrite = getMessageDigestInString() + "\n";
		
		FileWriter fw = new FileWriter(fcWithHash.getFile());
		try {
			fw.write(hashToWrite);
			isDone = true;
		}
		catch (IOException exception) {
			isDone = false;
		}
		finally {
			fw.close();
		}
		
		return isDone;
	}
	
	/**
	 * Get in percents how much of file is hashed.
	 * @return percents of hashed file
	 */
	public int getIsDoneInPercents() {
		return isDoneInPercents;
	}
	
	/**
	 * Get message digest in string representation.
	 * @return message digest in string
	 */
	public String getMD() {
		return md;
	}
	
	/**
	 * Get status of hashing process.
	 * @return true if file is hashed
	 */
	public boolean getIsDone() {
		return isDone;
	}
	
	/**
	 * Get status if operation is finished successful
	 * @return true if operation is finished normally
	 */
	public boolean getIsSuccessful() {
		return isSuccessful;
	}
	
	/**
	 * Get status of error is occur.
	 * @return true if error occur
	 */
	public boolean getIsError() {
		return isError;
	}
	
	/**
	 * Read hash text from file and return it.
	 * @return hash string representation from file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private String getHashFromFile()
			throws FileNotFoundException, IOException {
		BufferedReader bf = new BufferedReader(new FileReader(fcWithHash.getFile()));
		try {
			return bf.readLine();
		}
		finally {
			bf.close();
		}
	}
	
	/**
	 * Convert byte array to hex representation in String
	 * @param a array to convert
	 * @return Hex string representation of array
	 */
	private String bytesToHexString(byte[] a) {
		StringBuilder sb = new StringBuilder();
		String s;
		
		for (byte b : a) {
			s = Integer.toHexString(0xFF & b);
			if (s.length() == 1) {
				sb.append("0");
			}
			sb.append(s);
		}
		
		return new String(sb);
	}
}
