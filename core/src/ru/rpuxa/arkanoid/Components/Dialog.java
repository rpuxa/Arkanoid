package ru.rpuxa.arkanoid.Components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.rpuxa.arkanoid.Main.Visual;

public class Dialog implements Touchable, Parent {
    int x, y;
    public int width, height;
    private boolean turnable;
    private boolean dismissed;
    private Texture background;
    private Child[] children;

    public Dialog(int width, int height, String texturePath, boolean turnable, Child... children) {
        this.width = width;
        this.height = height;
        this.turnable = turnable;
        this.children = children;
        background = new Texture(texturePath);
        x = Visual.WIDTH / 2;
        y = Visual.HEIGHT / 2;
    }

    public Dialog() {
        dismissed = true;
    }

    public void dismiss() {
        dismissed = true;
    }

    public boolean isDismissed() {
        return dismissed;
    }

    @Override
    public boolean collision(int x, int y) {
        return !dismissed;
    }

    public void setChildren(Child... children) {
        this.children = children;
        for (Child child : children)
            child.setParent(this);
    }

    @Override
    public void touchUp() {
        if (!dismissed)
        for (Child child : children)
            child.touchUp();
    }

    @Override
    public void touchDragged(int x, int y) {
        if (!dismissed)
        for (Child child : children)
            child.touchDragged(x - this.x, y - this.y);
    }

    @Override
    public boolean firstTouch(int x, int y) {
        if (!dismissed) {
            if (!(x >= this.x - width / 2 && y >= this.y - height / 2 && x <= this.x + width / 2 && y <= this.y + height / 2) && turnable)
                dismiss();
            for (Child child : children)
                child.firstTouch(x - this.x, y - this.y);
        }
        return false;
    }

    public void render(SpriteBatch batch, double delta){
        if (!dismissed) {
            batch.draw(background, x - width / 2, y - height / 2, width, height);
            for (Child child : children)
                child.render(batch, delta);
        }
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }
}
