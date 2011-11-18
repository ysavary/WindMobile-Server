/*******************************************************************************
 * Copyright (c) 2011 epyx SA.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
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
