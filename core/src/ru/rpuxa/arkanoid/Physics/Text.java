package ru.rpuxa.arkanoid.Physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Text {

    BitmapFont font;
    public String text;
    public int x;
    public int y;

    public Text(String text, int x, int y, int size) {
        this(text, x, y, size, Color.WHITE);
    }

    public Text(String text, int size, Color color) {
        this(text, 0, 0, size, color);
    }

    public Text(String text, int x, int y, int size, Color color) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts\\theboldfont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = color;
        font = generator.generateFont(parameter);
        generator.dispose();
        this.text = text;
        this.y = y;
        this.x = x;
    }

    public void render(SpriteBatch batch) {
        font.draw(batch, text, x, y);
    }
}
