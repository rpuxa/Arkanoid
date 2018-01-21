package ru.rpuxa.arkanoid.Components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static ru.rpuxa.arkanoid.Main.Visual.textureBank;

public abstract class TextureButton extends Button implements Child {

    private Parent parent;
    private Texture texture;

    public TextureButton(int x, int y, int width, int height, String texturePath) {
        super(x, y, width, height, 3);
        texture = textureBank.get(texturePath);
    }

    @Override
    public void setParent(Parent parent) {
        this.parent = parent;
    }

    @Override
    public void render(SpriteBatch batch, double delta) {
        super.render(batch, delta);
        batch.draw(texture, x - textureWidth / 2 + parent.getX(), y - textureHeight / 2 + parent.getY(), textureWidth, textureHeight);

    }
}
