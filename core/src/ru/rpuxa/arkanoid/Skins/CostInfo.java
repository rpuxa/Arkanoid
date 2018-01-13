package ru.rpuxa.arkanoid.Skins;

import java.io.Serializable;

public class CostInfo implements Serializable {

    public int currency;
    public int cost;
    public int id;

    public CostInfo(int currency, int cost, int id) {
        this.currency = currency;
        this.cost = cost;
        this.id = id;
    }
}
