package ch.windmobile.server.social.mongodb.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.core.codec.Base64;

public class UserServiceUtil {
	public String createSHA1(String email,char pwd[]) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA1");
		String base = email+":"+new String(pwd);
		byte[] result = md.digest(base.getBytes(Charset.forName("UTF-8")));
		return new String(Base64.encode(result));
	}
	
	public boolean validateSHA1(String email,Object password, String base64) throws NoSuchAlgorithmException {
		if ( password == null ) {
			throw new IllegalArgumentException("password cannot be null");
		}
		MessageDigest md = MessageDigest.getInstance("SHA1");
		String base = email+":"+password.toString();
		byte[] result = md.digest(base.getBytes(Charset.forName("UTF-8")));
		byte[] data  = Base64.decode(base64.getBytes());
		return MessageDigest.isEqual(data, result);
	}
}
