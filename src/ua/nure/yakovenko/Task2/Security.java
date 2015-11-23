package ua.nure.yakovenko.Task2;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Security {
	
	public static final String generateSHA256(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String text = "This is some text";
	
			md.update(text.getBytes("UTF-8"));
			byte[] digest = md.digest();
			
			return String.format("%064x", new java.math.BigInteger(1, digest));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return null;
	}
}
