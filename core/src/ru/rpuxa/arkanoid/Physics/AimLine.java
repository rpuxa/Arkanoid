package ru.rpuxa.arkanoid.Physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.rpuxa.arkanoid.Main.Visual;

import static ru.rpuxa.arkanoid.Main.Visual.textureBank;

public class AimLine {
    Texture point;
    Cannon cannon;
    int iteration;

    public AimLine(Cannon cannon) {
        point = textureBank.get("point");
        this.cannon = cannon;
        iteration = 0;
    }

    private static final int POINT_DISTANCE = 20, POINT_SIZE = 6;

    public void render(SpriteBatch batch, Let[][] lets) {
        double x = cannon.x + cannon.platformRotateCenter[0] + cannon.barrelWidth * Math.cos(cannon.angle),
                y = cannon.y + cannon.platformRotateCenter[1] + cannon.barrelWidth * Math.sin(cannon.angle);
        x += Math.cos(cannon.angle) * iteration;
        y += Math.sin(cannon.angle) * iteration;
        label:
        while (true) {
            x += Math.cos(cannon.angle) * POINT_DISTANCE;
            y += Math.sin(cannon.angle) * POINT_DISTANCE;
            for (Let[] let1 : lets)
                for (int k = 0; k < lets[0].length; k++) {
                    Let let = let1[k];
                    if (let != null && (let.x - let.size / 2 < x && let.y - let.size / 2 < y &&
                            let.x + let.size / 2 > x && let.y + let.size / 2 > y || x < 0 || x > Visual.WIDTH ||
                            y > Visual.HEIGHT)) {
                        break label;
                    }
                }
            batch.draw(point, (float) (x - POINT_SIZE / 2), (float) (y - POINT_SIZE / 2), POINT_SIZE, POINT_SIZE);
        }
        if (iteration == POINT_DISTANCE)
            iteration = 0;
        iteration += 2;
    }
}
