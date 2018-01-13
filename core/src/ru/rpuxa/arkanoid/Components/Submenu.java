package ru.rpuxa.arkanoid.Components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Submenu implements Child, Parent {

    private int x, y;
    private int width, height;
    private Parent parent;
    private Texture[] backgrounds;
    private Child[][] children;
    private ShiftButton[] shiftButtons;
    private int select;

    public Submenu(int x, int y, int width, int height, Texture[] backgrounds, Child[][] children, ShiftButton[] shiftButtons) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.backgrounds = backgrounds;
        setChildren(children);
        select = 0;
        this.shiftButtons = shiftButtons;
    }

    public void setChildren(Child[][] children) {
        this.children = children;
        for (int i = 0; i < children.length; i++) {
            for (int j = 0; j < children[i].length; j++) {
                children[i][j].setParent(this);
            }
        }
    }

    @Override
    public int getX() {
        return x + parent.getX();
    }

    @Override
    public int getY() {
        return y = parent.getY();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public boolean collision(int x, int y) {
        return x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height;
    }

    @Override
    public void touchUp() {
        if (children.length != 0)
        for (int j = 0; j < children[select].length; j++) {
                children[select][j].touchUp();
        }
    }

    @Override
    public void touchDragged(int x, int y) {
        if (children.length != 0)
            for (int j = 0; j < children[select].length; j++) {
                children[select][j].touchDragged(x - this.x, y - this.y);
            }
    }

    @Override
    public boolean firstTouch(int x, int y) {
        for (int i = 0; i < shiftButtons.length; i++) {
            if (shiftButtons[i].collusion(x, y)) {
                select = i;
                break;
            }
        }
            for (int j = 0; j < children[select].length; j++) {
                children[select][j].firstTouch(x - this.x, y - this.y);
            }
        return collision(x, y);
    }

    @Override
    public void render(SpriteBatch batch, double delta) {
        batch.draw(backgrounds[select], x + parent.getX(), y + parent.getY(), width, height);
        for (int j = 0; j < children[select].length; j++) {
            children[select][j].render(batch, delta);
        }
    }

    @Override
    public void setParent(Parent parent) {
       this.parent = parent;
    }

    public static class ShiftButton {
        int x, y;
        int width, height;

        public ShiftButton(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean collusion(int x, int y) {
            return x >= this.x - width / 2 && y >= this.y - height / 2 && x <= this.x + width / 2 && y <= this.y + height / 2;
        }
    }
}
