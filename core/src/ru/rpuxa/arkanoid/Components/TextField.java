package ru.rpuxa.arkanoid.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;


public class TextField implements Child {
    int x, y;
    int width, height;
    private Texture background, disabledBackground;
    Parent parent;
    private String text, hint, title;
    BitmapFont font;
    public boolean disabled;

    public TextField(int x, int y, int width, int height, String texturePathActive, String texturePathDeactive, String hint, String title) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.background = new Texture(texturePathActive);
        this.disabledBackground = new Texture(texturePathDeactive);
        this.hint = hint;
        this.title = title;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts\\regular.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int)(60d / 80 * height);
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
        generator.dispose();
        text = "";
    }

    private void enterText() {
        Input.TextInputListener listener = new Input.TextInputListener() {
            @Override
            public void input(String text0) {
                text = text0;
            }

            @Override
            public void canceled() {
                text = "";
            }
        };
        Gdx.input.getTextInput(listener, title, text, (title.equals("")) ? hint : "");
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean collision(int x, int y) {
        return !disabled && x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height;
    }

    @Override
    public void touchUp() {

    }

    @Override
    public void touchDragged(int x, int y) {

    }

    @Override
    public boolean firstTouch(int x, int y) {
        if (collision(x, y))
            enterText();
        return collision(x, y);
    }

    @Override
    public void render(SpriteBatch batch, double delta) {
        if (disabled){
            batch.draw(disabledBackground, x + parent.getX(), y + parent.getY(), width, height);
        } else {
            batch.draw(background, x + parent.getX(), y + parent.getY(), width, height);
            font.draw(batch, text, x + parent.getX() + 20, y + parent.getY() + height - 10);
        }
    }

    @Override
    public void setParent(Parent parent) {
        this.parent = parent;
    }
}
