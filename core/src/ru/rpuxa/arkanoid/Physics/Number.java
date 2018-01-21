package ru.rpuxa.arkanoid.Physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Number {

    private static Texture[] textures;

    public static void setTextures() {
        textures = new Texture[10];
        for (int i = 0; i < 10; i++)
            textures[i] = new Texture("Digits\\" + i + ".png");
    }

    public static Texture[] parseNumber(int num) {
        Texture[] textures = new Texture[(int) Math.round(Math.ceil(Math.log10(num + .1)))];
        for (int i = textures.length - 1; i >= 0; i--) {
            String n = "" + num % 10;
            textures[i] = new Texture("Digits\\" + n + ".png");
            num /= 10;
        }
        return textures;
    }

    public static void renderNumber(SpriteBatch batch, Texture[] number, int x, int y, int width, int height) {
        y -= width / 2;
        int startX = (number.length % 2 == 0) ? (x - width * number.length / 2) : (x - width / 2 - ((number.length - 1) / 2) * width);
        for (int i = 0; i < number.length; i++) {
            batch.draw(number[i], startX + width * i, y, width, height);
        }
    }
}
