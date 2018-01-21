package ru.rpuxa.arkanoid.Components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.rpuxa.arkanoid.Main.Visual;

import static ru.rpuxa.arkanoid.Main.Visual.textureBank;

public class Scroll implements Child, Parent {

    int x;
    int scrollY;
    Parent parent;
    int width;
    Texture texture, upTexture;
    Child[] children;
    int maxScrollY;
    boolean scrolled = false;

    public Scroll(int x, int width, String texturePath, String upTexturePath, int maxScrollY, Child... children) {
        this.x = x;
        this.width = width;
        this.children = children;
        texture = textureBank.get(texturePath);
        upTexture = textureBank.get(upTexturePath);
        for (Child child : children) {
            child.setParent(this);
        }
        this.maxScrollY = maxScrollY;
    }

    @Override
    public int getX() {
        return x + parent.getX();
    }

    @Override
    public int getY() {
        return scrollY + parent.getY();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public boolean collision(int x, int y) {
        return x >= this.x && x <= this.x + width;
    }

    @Override
    public void touchUp() {
        firstTouch = false;
        for (Child child : children) {
            if (scrolled && child instanceof Button)
                ((Button) child).touchUpWithoutClick();
            else
                child.touchUp();
        }
    }

    @Override
    public void touchDragged(int x, int y) {
        if (firstTouch) {
            int newValue = y - startY + startScrollY;
            if (newValue >= 0 && newValue <= maxScrollY) {
                if (scrollY != newValue)
                    scrolled = true;
                scrollY = newValue;
            }
            else if (newValue < 0)
                scrollY = 0;
            else
                scrollY = maxScrollY;
        }
        for (Child child : children)
            child.touchDragged(x - this.x, y - scrollY);
    }

    int startScrollY, startY;
    boolean firstTouch;

    @Override
    public boolean firstTouch(int x, int y) {
        scrolled = false;
        if (collision(x, y)) {
            firstTouch = true;
            startScrollY = scrollY;
            startY = y;
        }
        for (Child child : children)
            child.firstTouch(x - this.x, y - scrollY);
        return collision(x ,y);
    }

    @Override
    public void render(SpriteBatch batch, double delta) {
        batch.draw(texture, x + parent.getX(), parent.getY(), width, Visual.HEIGHT);
        for (Child child : children)
            child.render(batch, delta);
        batch.draw(upTexture, x + parent.getX(), parent.getY(), width, Visual.HEIGHT);
    }

    @Override
    public void setParent(Parent parent) {
        this.parent = parent;
    }
}
