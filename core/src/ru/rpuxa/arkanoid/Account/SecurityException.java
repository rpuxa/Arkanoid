package ru.rpuxa.arkanoid.Account;

public class SecurityException extends RuntimeException {

    private SecurityException(String s) {
        super(s);
    }

    public static void throwException(int value, long hash) {
        throw new SecurityException("Value: " + value + " Hash: " + hash);
    }
}
