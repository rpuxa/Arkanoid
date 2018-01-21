package ru.rpuxa.arkanoid.Physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.rpuxa.arkanoid.Main.Game;
import ru.rpuxa.arkanoid.Main.Visual;

import static ru.rpuxa.arkanoid.Main.Visual.textureBank;

public class Boom {
    public Texture texture;
    int radius;
    boolean disposed;
    int x, y;
    Game game;

    public Boom(int x, int y, Game game) {
        this.x = x;
        this.y = y;
        radius = 1;
        this.game = game;
        texture = textureBank.get("boom");
    }

    public Boom(){
        disposed = true;
    }

    public void render(SpriteBatch batch, double delta) {
        if (!disposed) {
            for (int x = 0; x < game.lets.length; x++) {
                for (int y = 0; y < game.lets[0].length; y++) {
                    if (game.lets[x][y] != null && game.lets[x][y].count > 0 && distance(x, y))
                        game.lets[x][y].removeAllPoints(new double[] {x, y});
                }
            }
            batch.draw(texture, x - radius / 2, ((radius > 2 * Visual.WIDTH) ? radius / 3 - 2 * Visual.WIDTH / 3 : 0) + y, radius, radius);
            radius += Math.round(delta * 3000);
            if (radius > 2 * Visual.HEIGHT)
                disposed = true;
        }
    }

    private boolean distance(int x, int y) {
        x = Visual.WIDTH / 16 + Visual.WIDTH / 8 * x;
        y = Visual.HEIGHT - (Visual.WIDTH / 16 + Visual.WIDTH / 8 * y);
        return Math.sqrt((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y)) <= radius;
    }
}
