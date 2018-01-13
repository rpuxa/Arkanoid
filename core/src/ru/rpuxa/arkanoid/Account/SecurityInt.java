package ru.rpuxa.arkanoid.Account;

import java.security.SecureRandom;
import java.util.Random;

public class SecurityInt {
    private int value;
    private long hash;

    public SecurityInt(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        hash = hashGen(value);
    }

    public void add(int value) {
        setValue(this.value + value);
    }

    public boolean checkHack() {
        return hashGen(value) != hash;
    }

    private long hashGen(int value) {
        Random random0 = new Random(value);
        byte[] bytes = new byte[128];
        for (int i = 0; i < bytes.length; i++)
            bytes[i] = (byte) random0.nextLong();
        SecureRandom random = new SecureRandom(bytes);
        return value ^ random.nextLong();
    }

    @Override
    public boolean equals(Object o) {
        return value == ((SecurityInt) o).value;
    }
}
