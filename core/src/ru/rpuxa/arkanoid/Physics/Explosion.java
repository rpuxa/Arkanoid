package ru.rpuxa.arkanoid.Physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static ru.rpuxa.arkanoid.Main.Visual.textureBank;

public class Explosion {
    TextureRegion[] textures;
    int fps;
    int x, y;
    int size;
    double frame;
    boolean rendering;

    public Explosion(int fps, String texturePath, int rows, int columns) {
        this.fps = fps;
        Texture texture = textureBank.get(texturePath);
        int height = texture.getHeight() / rows, width = texture.getWidth() / columns;
        int index = 0;
        textures = new TextureRegion[columns * rows];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                textures[index] = new TextureRegion(texture, x * width, y * height, width, height);
                index++;
            }
        }
    }

    public void explode(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        frame = 0;
        rendering = true;
    }

    public void render(SpriteBatch batch, double delta) {
        if (rendering) {
            frame += delta * fps;
            if ((int) frame > textures.length - 1) {
                rendering = false;
                return;
            }
            batch.draw(textures[(int) frame], x - size / 2, y - size / 2, size, size);
        }
    }
}
