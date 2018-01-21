package ru.rpuxa.arkanoid.Physics;

import ru.rpuxa.arkanoid.Main.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.rpuxa.arkanoid.Main.Visual;

import java.util.*;

import static ru.rpuxa.arkanoid.Main.Visual.textureBank;

public class Let {
    static Texture[] textures;
    private Particle[] particles;
    private Texture[] numberTextures;
    public int x, y;
    public int size;
    public double count;
    private double[] ballPosition;
    private Game game;

    public Let(int x, int y, double count, Game game) {
        size = Visual.WIDTH / 8;
        this.x = x;
        this.y = y;
        this.count = count;
        this.game = game;
    }

    public static void setTextures() {
        textures = new Texture[] {textureBank.get("block0"), textureBank.get("block1"), textureBank.get("block2"), textureBank.get("block3")};
    }


    public void moveDown() {
        y -= size;
        numberTextures = null;
    }

    public void removeAllPoints(double[] center) {
        removePoints(-count, center);
    }

    public void removePoints(double count, double[] ballPosition) {
        this.count += count;
        if (this.count <= 0) {
            game.score.addPoint((int) (5 - count));
            game.updateEdges();
        } else
            game.score.addPoint(1);
        this.ballPosition = ballPosition;
        numberTextures = null;
    }

    public boolean isDestroyed() {
        return count <= 0 && allDestroyedParticles();
    }

    private Texture getTexture() {
        if (count < 10)
            return textures[0];
        if (count < 25)
            return textures[1];
        if (count < 45)
            return textures[2];
        return textures[3];
    }

    public void render(SpriteBatch batch, double delta) {
        if (count > 0) {
            batch.draw(getTexture(), x - size / 2, y - size / 2, size, size);
            if (numberTextures == null)
                numberTextures = Number.parseNumber((int) Math.ceil(count));
            Number.renderNumber(batch, numberTextures, x, y, size / 4, size / 2);
        } else {
            if (particles == null) {
                final int countParticles = 64;
                particles = new Particle[countParticles];
                for (int i = 0; i < countParticles; i++) {
                    particles[i] = new Particle(x - size / 2, y - size / 2, size, (int) (size / Math.round(Math.sqrt(countParticles))), i, ballPosition[0], ballPosition[1]);
                }
            }
            for (Particle particle : particles)
                particle.render(batch, delta);
        }
    }

    @Override
    public boolean equals(Object o) {
        Let let = (Let) o;
        return let.x == x && let.y == y;
    }

    private boolean allDestroyedParticles() {
        if (particles == null)
            return false;
        for (Particle particle : particles)
            if (particle.time > 0)
                return false;
        return true;
    }

    private class Particle {
        double x,y;
        int row, column;
        double[] velocity;
        int time;
        int size;
        int count;

        private static final double GRAVITY = 1500;
        private static final double INITIAL_VELOCITY = 350;

        Particle(double xLet, double yLet, int sizeLet, int size, int number, double xCenter, double yCenter) {
            count = sizeLet / size;
            row = (number % count);
            column = (number / count);
            this.x = xLet + row * size;
            this.y = yLet + column * size;
            time = new Random().nextInt(100);
            this.size = size;
            double velocityAngle = Math.atan2(y - yCenter, x - xCenter) + (new Random().nextDouble() - 0.5);
            velocity = new double[]{INITIAL_VELOCITY * Math.cos(velocityAngle), INITIAL_VELOCITY * Math.sin(velocityAngle)};
        }

        private void render(SpriteBatch batch, double delta) {
            time--;
            if (time > 0) {
                x += velocity[0] * delta;
                y += velocity[1] * delta;
                velocity[1] -= GRAVITY * delta;

                batch.draw(textures[0],(float) x,(float) y, size, size,
                        row * textures[0].getWidth() / count, column * textures[0].getHeight() / count, textures[0].getWidth() / count, textures[0].getHeight() / count, false, false);
            }
        }
    }
}
