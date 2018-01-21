package ru.rpuxa.arkanoid.Skins;

import ru.rpuxa.arkanoid.Physics.Ball;

public class BallInfo extends Info {

    private static final long serialVersionUID = 7158189679174844549L;

    public static BallInfo[] balls;

    public static final int DIAMETER = 30;

    public BallInfo(String texturePath, int currency, int cost, double damage, double speed, int id) {
        super(new String[]{texturePath}, currency, cost, damage, speed, id);
    }

    public static BallInfo get(int id) {
        for (Info info : balls)
            if (info.id == id) {
                return (BallInfo) info;
            }
        return null;
    }
}
