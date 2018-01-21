package ru.rpuxa.arkanoid.Physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.rpuxa.arkanoid.Account.Account;
import ru.rpuxa.arkanoid.Account.Server.Connection;
import ru.rpuxa.arkanoid.Main.Game;
import ru.rpuxa.arkanoid.Main.Visual;
import ru.rpuxa.arkanoid.Skins.BallInfo;

import java.util.Random;

import static ru.rpuxa.arkanoid.Account.Server.Constants.ADD_STAR;
import static ru.rpuxa.arkanoid.Main.Visual.textureBank;

public class Power {
    public int x, y;
    private Texture texture;
    private String texturePath;
    private int size;
    private Effect effect;
    private boolean used;
    public int id;

    private Power(String texture, Effect effect, int id) {
        this.texture = textureBank.get(texture);
        texturePath = texture;
        size = Visual.WIDTH / 16;
        this.effect = effect;
        this.id = id;
    }

    public boolean isUsed() {
        return used;
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
        Power power = new Power(texturePath, effect, id);
        power.x = x * a + a / 2;
        power.y = Visual.HEIGHT - a / 2 - (y * a);
        return power;
    }

    public Power clone1(int x, int y) {
        Power power = new Power(texturePath, effect, id);
        power.x = x;
        power.y = y;
        return power;
    }

    public static final Power SPREADER = new Power("spreaderPower", new Effect() {
        @Override
        public void invoke(Ball ball, Game game, Power power) {
            if (!ball.redirected) {
                Random random = new Random();
                ball.velocityAngle = Math.PI / 3 * random.nextDouble() + Math.PI / 3;
                ball.redirected = true;
                ball.setNextCollision(game);
            }
        }
    }, 0);

    public static final Power ADD_BALL = new Power("addBallPower", new Effect() {
        @Override
        public void invoke(Ball ball, Game game, Power power) {
            game.addBalls(1);
            game.powers.remove(power);
        }
    }, 1);

    public static final Power REMOVE_BALL = new Power("removeBallPower", new Effect() {
        @Override
        public void invoke(Ball ball, Game game, Power power) {
            game.addBalls(-1);
            game.powers.remove(power);
        }
    }, 2);

    public static final Power STAR = new Power("star", new Effect() {
        @Override
        public void invoke(Ball ball, Game game, Power power) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    game.powers.remove(power);
                    game.score.addStar();
                    Game.account.money[Account.STARS].add(1);
                    Connection.sendCommand(ADD_STAR, Visual.imei);
                }
            }).start();
        }
    }, 3);

    public static final Power DOUBLE_BALL = new Power("doublePower", new Effect() {
        @Override
        public void invoke(Ball ball, Game game, Power power) {
            if (!ball.doubled) {
                ball.doubled = true;
                double angle = Math.PI / 12 * (new Random().nextDouble() - .5);
                Ball ballDoubled = new Ball(ball, angle);
                ball.velocityAngle -= angle;
                ball.setNextCollision(game);
                ballDoubled.doubled = true;
                ballDoubled.setNextCollision(game);
                game.balls.add(ballDoubled);
            }
        }
    }, 4);

    public static Power getById(int id, int x, int y) {
        Power[] powers = {SPREADER, ADD_BALL, STAR, REMOVE_BALL, DOUBLE_BALL};
        for (Power power : powers) {
            if (power.id == id) {
                return power.clone1(x, y);
            }
        }
        return null;
    }

    interface Effect {
        void invoke(Ball ball, Game game, Power power);
    }
}
