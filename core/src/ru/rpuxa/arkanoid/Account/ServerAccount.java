package ru.rpuxa.arkanoid.Account;

import java.io.Serializable;
import java.util.ArrayList;

public class ServerAccount implements Serializable {
    public ArrayList<Thing> things;

    public ServerAccount(ArrayList<Thing> things) {
        this.things = things;
    }

    public static class Thing implements Serializable {
        public int id;
        public Object data;

        public Thing(int id, Object data) {
            this.id = id;
            this.data = data;
        }
    }
}
