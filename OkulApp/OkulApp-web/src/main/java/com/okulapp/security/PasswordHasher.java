/*
 * 
 * 
 * 
 */
package com.okulapp.security;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
public class PasswordHasher {

    public static String hash(char[] password, String alghorithm) {
        try {
            final MessageDigest digester = MessageDigest.getInstance(alghorithm);
            byte[] passwordAsBytesArray = Charset.forName("UTF-8").encode(CharBuffer.wrap(password)).array();
            final byte[] digest = digester.digest(passwordAsBytesArray);
            String digestHex = digestToHex(digest);
            return digestHex;
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String digestToHex(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                // could use a for loop, but we're only dealing with a single byte
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
