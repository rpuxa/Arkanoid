package ru.rpuxa.arkanoid.Physics;

import ru.rpuxa.arkanoid.Skins.BallInfo;
import com.badlogic.gdx.graphics.g2d.*;
import ru.rpuxa.arkanoid.Main.Game;


public class Ball {
    public BallInfo info;
    public double x, y;
    public double velocityAngle;
    public boolean redirected;
    private Edge nextCollision, lastCollision;
    public boolean fold;
    public boolean doubled;

    public static double SPEED = 1000;

    public Ball(BallInfo info, double x, double y, double velocityAngle) {
        this.info = info;
        this.x = x;
        this.y = y;
        this.velocityAngle = velocityAngle;
    }

    public void render(SpriteBatch batch, double delta, Game game) {
        if (fold) {
            final int speed = 6000;
            double angle = Math.atan2(game.cannon.y - y,game.cannon.x - x);
            x += delta * speed * Math.cos(angle);
            y += delta * speed * Math.sin(angle);
        } else {
            double speed = delta * SPEED;
            double[][] vector = {{x, y}, {x + speed * Math.cos(velocityAngle), y + speed * Math.sin(velocityAngle)}};
            while (true) {
                if (nextCollision == null)
                    setNextCollision(game);
                double v;
                if ((v = nextCollision.collusion(vector, new double[]{x, y})) != 0) {
                    velocityAngle -= v;
                    if (nextCollision.let != null && nextCollision.let.count <= 0)
                        for (Ball ball : game.balls)
                            ball.setNextCollision(game);
                    nextCollision = null;
                } else
                    break;
            }
            for (Power power : game.powers.toArray(new Power[0])) {
                power.collusion(this, game);
            }
            x = vector[1][0];
            y = vector[1][1];
        }
        batch.draw(info.texture[0], (int) x - BallInfo.DIAMETER / 2, (int) y - BallInfo.DIAMETER / 2, BallInfo.DIAMETER, BallInfo.DIAMETER);
    }

    public void setNextCollision(Game game) {
        double minDist = Double.POSITIVE_INFINITY;
        double[][] vector = {{x, y}, {x + Math.cos(velocityAngle), y + Math.sin(velocityAngle)}};
        for (Edge edge : game.letEdges) {
            float dist = edge.distance(vector);
            if (dist < minDist) {
                minDist = dist;
                nextCollision = edge;
            }
        }
        if (minDist == Double.POSITIVE_INFINITY)
            nextCollision = new Edge();
    }

    private void hit() {
        redirected = false;
    }

    @Override
    public boolean equals(Object o) {
        Ball ball = (Ball) o;
        return x == ball.x && y == ball.y;
    }
}
