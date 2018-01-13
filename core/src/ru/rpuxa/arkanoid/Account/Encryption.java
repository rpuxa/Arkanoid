package ru.rpuxa.arkanoid.Account;

import java.security.SecureRandom;
import java.util.Random;

public class Encryption {

    public static byte[] hash(String text) {
        SecureRandom random = new SecureRandom(text.getBytes());
        byte[] hash = new byte[16];
        for (int i = 0; i < hash.length; i++) {
            if (i < text.length())
                hash[i] = (byte) ((long) text.charAt(i) ^ random.nextLong());
            else
                hash[i] = (byte) random.nextLong();
        }
        return hash;
    }
}
