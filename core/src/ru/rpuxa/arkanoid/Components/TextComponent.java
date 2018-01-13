package ru.rpuxa.arkanoid.Components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.rpuxa.arkanoid.Physics.Text;

public class TextComponent implements Child {
    Text text;
    Parent parent;
    int x, y;

    public TextComponent(String text, int x, int y, int size, Color color) {
        this.x = x;
        this.y = y;
        this.text = new Text(text, x, y, size, color);
    }

    @Override
    public boolean collision(int x, int y) {
        return false;
    }

    @Override
    public void touchUp() {
    }

    @Override
    public void touchDragged(int x, int y) {
    }

    @Override
    public boolean firstTouch(int x, int y) {
        return false;
    }

    @Override
    public void render(SpriteBatch batch, double delta) {
        text.x = x + parent.getX();
        text.y = y + parent.getY();
        text.render(batch);
    }

    @Override
    public void setParent(Parent parent) {
        this.parent = parent;
    }
}
