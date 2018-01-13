package ru.rpuxa.arkanoid.Physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.rpuxa.arkanoid.Account.Account;
import ru.rpuxa.arkanoid.Main.Game;
import ru.rpuxa.arkanoid.Main.Visual;
import ru.rpuxa.arkanoid.Skins.BallInfo;

import java.util.Random;

public class Power {
    private int x, y;
    private Texture texture;
    private String texturePath;
    private int size;
    private Effect effect;
    private boolean used;

    private Power(String texture, Effect effect) {
        this.texture = new Texture(texture);
        texturePath = texture;
        size = Visual.WIDTH / 16;
        this.effect = effect;
    }

    public boolean isUsed() {
        return used;
    }

    void moveDown() {
      y -= size * 2;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture,x - size / 2, y - size / 2, size, size);
    }

    public void collusion(Ball ball, Game game) {
        if (distance(ball) <= (BallInfo.DIAMETER + size) / 2) {
            used = true;
            effect.invoke(ball, game, this);
        }
    }

    private double distance(Ball ball) {
        return Math.sqrt((ball.x - x) * (ball.x - x) + (ball.y - y) * (ball.y - y));
    }

    public Power clone(int x, int y) {
        int a = Visual.WIDTH / 8;
        Power power = new Power(texturePath, effect);
        power.x = x * a + a / 2;
        power.y = Visual.HEIGHT - a / 2 - (y * a);
        return power;
    }

    public static final Power SPREADER = new Power("Powers\\spreader.jpg", new Effect() {
        @Override
        public void invoke(Ball ball, Game game, Power power) {
            if (!ball.redirected) {
                Random random = new Random();
                ball.velocityAngle = 2 * Math.PI * random.nextDouble() / 3 + Math.PI / 6;
                ball.redirected = true;
            }
        }
    });

    public static final Power ADD_BALL = new Power("Powers\\addBall.jpg", new Effect() {
        @Override
        public void invoke(Ball ball, Game game, Power power) {
            game.addBalls(1);
            game.powers.remove(power);
        }
    });

    public static final Power REMOVE_BALL = new Power("Powers\\removeBall.jpg", new Effect() {
        @Override
        public void invoke(Ball ball, Game game, Power power) {
            game.addBalls(-1);
            game.powers.remove(power);
        }
    });

    public static final Power STAR = new Power("Powers\\star.jpg", new Effect() {
        @Override
        public void invoke(Ball ball, Game game, Power power) {
          //  game.account.money[Account.STARS].add(1);
            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();
        }
    });

    public static final Power DOUBLE_BALL = new Power("Powers\\double.jpg", new Effect() {
        @Override
        public void invoke(Ball ball, Game game, Power power) {
            if (!ball.doubled) {
                ball.doubled = true;
                ball.velocityAngle -= Math.PI / 12;
                Ball ballDoubled = new Ball(ball.info, ball.x, ball.y, ball.velocityAngle + Math.PI / 6);
                ballDoubled.doubled = true;
                game.balls.add(ballDoubled);
            }
        }
    });

    interface Effect {
        void invoke(Ball ball, Game game, Power power);
    }
}
