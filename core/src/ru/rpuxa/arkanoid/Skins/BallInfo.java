package ru.rpuxa.arkanoid.Skins;

public class BallInfo extends Info {

    public static BallInfo[] balls;

    public static final int DIAMETER = 30;

    public BallInfo(String texturePath, int currency, int cost, int damage, int speed, int id) {
        super(new String[]{texturePath}, currency, cost, damage, speed, id);
    }
}
