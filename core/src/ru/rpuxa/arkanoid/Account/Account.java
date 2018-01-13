package ru.rpuxa.arkanoid.Account;

import java.util.ArrayList;

public class Account {
    public SecurityInt[] money;
    public ArrayList<SecurityInt> items;
    public SecurityInt ballSelected, cannonSelected;

    public static final int MONEY = 0;
    public static final int ITEMS = 1;

    public static final int STARS = 0;
    public static final int RUBIES = 1;

    public Account() {
        ballSelected = new SecurityInt(0);
        cannonSelected = new SecurityInt(0);
        items = new ArrayList<>();
        items.add(new SecurityInt(0));
        items.add(new SecurityInt(1));
    }

    public Account(ServerAccount account) {
        ballSelected = new SecurityInt(0);
        cannonSelected = new SecurityInt(0);
        for (ServerAccount.Thing thing : account.things) {
            int id = thing.id;
            Object data = thing.data;
            switch (id) {
                case MONEY:
                    int[] values = (int[]) data;
                    money = new SecurityInt[]{
                            new SecurityInt(values[0]),
                            new SecurityInt(values[1])
                    };
                    break;
                case ITEMS:
                    ArrayList<Integer> items = (ArrayList<Integer>) data;
                    this.items = new ArrayList<>();
                    for (Integer i : items) {
                        this.items.add(new SecurityInt(i));
                    }
                    break;
            }
        }
    }

    public void addItem(int itemId) {
        items.add(new SecurityInt(itemId));
    }

    public boolean haveItem(int itemId) {
        return items.contains(new SecurityInt(itemId));
    }
}
