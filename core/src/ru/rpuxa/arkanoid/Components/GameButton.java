package ru.rpuxa.arkanoid.Components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static ru.rpuxa.arkanoid.Main.Visual.textureBank;

public abstract class GameButton extends Button {
    Texture texture;

    public GameButton(int x, int y, int width, int height, String texturePath) {
        super(x, y, width, height, 0);
        texture = textureBank.get(texturePath);
    }

    @Override
    public void render(SpriteBatch batch, double delta) {
        super.render(batch, delta);
        batch.draw(texture, x - textureWidth / 2, y - textureHeight / 2, textureWidth, textureHeight);
    }
}
