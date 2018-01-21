package ru.rpuxa.arkanoid.Physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

import ru.rpuxa.arkanoid.Main.Game;
import ru.rpuxa.arkanoid.Main.Visual;
import ru.rpuxa.arkanoid.Skins.BallInfo;
import ru.rpuxa.arkanoid.Skins.CannonInfo;

import static ru.rpuxa.arkanoid.Main.Visual.textureBank;

public class Cannon {
    public CannonInfo info;
    public int x, y;
    public int countBalls;
    public int platformWidth, platformHeight;
    public int barrelWidth, barrelHeight;
    public int[] platformRotateCenter, barrelRotateCenter;
    double angle;
    public boolean isDestroyed;
    private Texture destroyed;
    ArrayList<Ball> balls;

    public Cannon(CannonInfo info, ArrayList<Ball> balls) {
        this.info = info;
        setStartPos();
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
        destroyed = textureBank.get("destroyedCannon");
        this.balls = balls;
    }

    public void setStartPos() {
        x = Visual.WIDTH / 2 - 100;
        y = Visual.HEIGHT - Visual.WIDTH / 8 * 13;
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
                            angle, info.damage, info.speed));
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException ignored) {
                    }
                }
        }}).start();
    }

    public boolean setAngle(int[] touch, Game game) {
        double angle = Math.atan2(touch[1] - (y + platformRotateCenter[1]), touch[0] - (x + platformRotateCenter[0]));
        if (game.aiming = (angle >= Math.PI / 18 && angle <= Math.PI * 17 / 18)) {
            this.angle = angle;
            return true;
        }
        return false;
    }

    public void render(SpriteBatch batch) {
        if (isDestroyed) {
            batch.draw(destroyed, x, y, platformWidth, platformHeight);
        } else {
            batch.draw(info.texture[1], x, y, platformWidth, platformHeight);
            batch.draw(info.barrel, x + platformRotateCenter[0] - barrelRotateCenter[0], y + platformRotateCenter[1] - barrelRotateCenter[1],
                    barrelRotateCenter[0], barrelRotateCenter[1], barrelWidth, barrelHeight, 1, 1, (float) Math.toDegrees(angle));
        }
    }
}
