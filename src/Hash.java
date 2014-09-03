import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;

class Hash {
	
	private static String[] hashAlgorithms = { "MD5", "SHA-1", "SHA-256", "SHA-512" };
	private MessageDigest md;
	
	/**
	 * Creates new Hash instance by assigning algorithm to be used.
	 * @param algorithm algorithm to be used
	 * @throws NoSuchAlgorithmException
	 */
	public Hash(String algorithm) throws NoSuchAlgorithmException {
		md = MessageDigest.getInstance(algorithm);
	}
	
	/**
	 * getAlgorithms is method which return names of available hash algorithms.
	 * @return return names of hash algorithms
	 */
	public static String[] getAlgorithms() {
		
		String[] names = new String[hashAlgorithms.length];
		for (int i = 0; i < hashAlgorithms.length; i++) {
			names[i] = hashAlgorithms[i];
		}
		return names;
	}
	
	/**
	 * Update hash state.
	 * @param fileBytes bytes, by which update the state
	 * @param bytesRead how much to read
	 */
	public void addBytes(byte[] fileBytes, int bytesRead) {
		md.update(fileBytes, 0, bytesRead);
	}
	
	/**
	 * Calculate message digest.
	 * @return byte representing of message digest
	 */
	public byte[] calculate() {
		return md.digest();
	}
}
