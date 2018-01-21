package ru.rpuxa.arkanoid.Components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static ru.rpuxa.arkanoid.Main.Visual.textureBank;

public class GameOver {
    Texture texture;
    int y ,x;
    boolean isShow;
    double speed;

    public GameOver(String texturePath) {
        this.texture = textureBank.get(texturePath);
    }

    public void show() {
        y = 2700;
        speed = 2000;
        isShow = true;
    }

    public void dismiss() {
        isShow = false;
    }

    private static final int ACCELERATION = 1500;

    public void render(SpriteBatch batch, double delta) {
        if (isShow) {
            y -= speed * delta;
            speed -= ACCELERATION * delta;
            if (speed < 0)
                speed = 0;
            batch.draw(texture, x, y);
        }
    }
}
