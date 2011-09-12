package ch.windmobile.server.social.mongodb.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.codec.Base64;

public class AuthenticationServiceUtil {

    public static String createSHA1(String email, byte[] pwd) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        String base = email + ":" + new String(pwd);
        byte[] result = md.digest(base.getBytes());
        return new String(Base64.encode(result));
    }

    public static boolean validateSHA1(String email, Object password, String base64) throws NoSuchAlgorithmException {
        if (password == null) {
            throw new IllegalArgumentException("password cannot be null");
        }
        MessageDigest md = MessageDigest.getInstance("SHA1");
        String base = email + ":" + password.toString();
        byte[] result = md.digest(base.getBytes());
        byte[] data = Base64.decode(base64.getBytes());
        return MessageDigest.isEqual(data, result);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String email = args[0];
        String password = args[1];
        System.out.println("Email: " + email + ", password: " + password + ", sha1:" + createSHA1(email, password.getBytes()));
    }
}
