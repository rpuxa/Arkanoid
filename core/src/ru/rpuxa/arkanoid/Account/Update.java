package ru.rpuxa.arkanoid.Account;

import java.io.Serializable;

public class Update implements Serializable {
    public int id;
    public Object data;

    public Update(int id, Object data) {
        this.id = id;
        this.data = data;
    }
}
