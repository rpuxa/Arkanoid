package ru.rpuxa.arkanoid.Physics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

import ru.rpuxa.arkanoid.Main.Visual;
import ru.rpuxa.arkanoid.Skins.BallInfo;
import ru.rpuxa.arkanoid.Skins.CannonInfo;

public class Cannon {
    CannonInfo info;
    public int x, y;
    public int countBalls;
    public int platformWidth, platformHeight;
    public int barrelWidth, barrelHeight;
    public int[] platformRotateCenter, barrelRotateCenter;
    double angle;
    ArrayList<Ball> balls;

    public Cannon(CannonInfo info, int y, ArrayList<Ball> balls) {
        this.info = info;
        x = Visual.WIDTH / 2 - 100;
        this.y = y;
        this.platformWidth = info.width;
        this.platformHeight = info.texture[1].getHeight() * platformWidth / info.texture[1].getWidth();
        barrelWidth = info.texture[0].getWidth() * platformWidth / info.texture[1].getWidth();
        barrelHeight = info.texture[1].getHeight() * barrelWidth /  info.texture[1].getWidth();
        platformRotateCenter = new int[] {
                info.rotatePlatformCenter[0] * platformWidth / info.texture[1].getWidth(),
                info.rotatePlatformCenter[1] * platformHeight / info.texture[1].getHeight()
        };
        barrelRotateCenter = new int[] {
                info.rotateBarrelCenter[0] * barrelWidth / info.texture[0].getWidth(),
                info.rotateBarrelCenter[1] * barrelHeight / info.texture[0].getHeight()
        };
        this.balls = balls;
    }

    public void shoot(BallInfo ballInfo) {
        long time = Math.round(1000 * 4 * BallInfo.DIAMETER / Ball.SPEED);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < countBalls; i++) {
                    balls.add(new Ball(ballInfo
                            , x + platformRotateCenter[0] + barrelWidth * Math.cos(angle),
                            y + platformRotateCenter[1] + barrelWidth * Math.sin(angle),
                            angle));
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException ignored) {
                    }
                }
        }}).start();
    }

    public void setAngle(int[] touch) {
        double angle = Math.atan2(touch[1] - (y + platformRotateCenter[1]), touch[0] - (x + platformRotateCenter[0]));
        if (angle >= Math.PI / 18 && angle <= Math.PI * 17 / 18)
            this.angle = angle;
    }

    public void render(SpriteBatch batch) {
        batch.draw(info.texture[1], x, y, platformWidth, platformHeight);
        batch.draw(info.barrel, x + platformRotateCenter[0] - barrelRotateCenter[0], y + platformRotateCenter[1] - barrelRotateCenter[1],
                barrelRotateCenter[0], barrelRotateCenter[1], barrelWidth, barrelHeight, 1, 1, (float) Math.toDegrees(angle));
    }
}
