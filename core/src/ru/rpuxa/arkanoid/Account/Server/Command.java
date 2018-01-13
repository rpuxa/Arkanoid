package ru.rpuxa.arkanoid.Account.Server;

import java.io.Serializable;

public class Command implements Serializable {
    public int id;
    public Object data;
    public String from;

    public Command(int id, Object data) {
        this.id = id;
        this.data = data;
    }

    public Command(int id, Object data, String from) {
        this.id = id;
        this.data = data;
        this.from = from;
    }
}
