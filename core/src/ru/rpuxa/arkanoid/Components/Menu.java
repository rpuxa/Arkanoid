package ru.rpuxa.arkanoid.Components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.rpuxa.arkanoid.Main.Visual;

public class Menu implements Parent, Touchable {
    int x;
    int width;
    Texture background;
    boolean isLeft;
    MenuButton button;
    Child[] children;

    public Menu(int width, String background, boolean isLeft, int yButton, Child... children) {
        this.width = width;
        this.background = new Texture(background);
        this.isLeft = isLeft;
        if (isLeft)
            x = -width;
        else
            x = Visual.WIDTH;
        int widthButton = 80;
        button = new MenuButton((isLeft) ? widthButton / 2 : Visual.WIDTH - widthButton / 2, yButton, widthButton, 200, "menuButton.png", this);
        this.children = children;
        for (Child child : children)
            child.setParent(this);
    }

    public void render(SpriteBatch batch, double delta) {
        batch.draw(background, x, 0, width, Visual.HEIGHT);
        button.render(batch, delta);
        for (Child child : children)
            child.render(batch, delta);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public void addX(int i) {
        x += i;
    }

    @Override
    public boolean isLeft() {
        return isLeft;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public boolean collision(int x, int y) {
        return x >= this.x && x <= this.x + width || button.collision(x, y);
    }

    @Override
    public void touchUp() {
        button.touchUp();
        for (Child child : children)
            child.touchUp();
    }

    @Override
    public void touchDragged(int x, int y) {
        button.touchDragged(x, y);
        for (Child child : children)
            child.touchDragged(x - this.x, y);
    }

    @Override
    public boolean firstTouch(int x, int y) {
        for (Child child : children)
            child.firstTouch(x - this.x, y);
        button.firstTouch(x, y);
        return collision(x, y);
    }
}
