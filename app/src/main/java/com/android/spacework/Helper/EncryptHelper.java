package com.android.spacework.Helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptHelper {

    private final static char[] hexArray = "0123456789abcdef".toCharArray();

    public String hash(String str) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.reset();
        byte[] hash = digest.digest(str.getBytes());
        return bytesToStringHex(hash);
    }

    private static String bytesToStringHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length*2];
        for(int i = 0; i<bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i*2] = hexArray[v >>> 4];
            hexChars[i*2+1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
