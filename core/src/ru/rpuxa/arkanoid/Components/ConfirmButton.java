package ru.rpuxa.arkanoid.Components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static ru.rpuxa.arkanoid.Main.Visual.textureBank;

public abstract class ConfirmButton extends Button implements Child {

    Parent parent;
    Texture button;
    ButtonsToConfirm accept, deny;
    boolean visibleButtons;

    public ConfirmButton(int x, int y, int width, int height, String texturePath) {
        super(x, y, width, height, 0);
        button = textureBank.get(texturePath);
        accept = new ButtonsToConfirm((int) (96d / 500 * width), 0, (int) (95d / 500 * width), (int) (95d / 200 * height), "roundGreenButton", this) {
            @Override
            public void onClick(int x, int y) {
                onAccept();
                visibleButtons = false;
            }
        };

        deny = new ButtonsToConfirm((int) (196d / 500 * width), 0, (int) (95d / 500 * width), (int) (95d / 200 * height), "roundRedButton", this) {
            @Override
            public void onClick(int x, int y) {
                visibleButtons = false;
            }
        };
    }

    private int getX() {
        return x + parent.getX();
    }

    private int getY() {
        return y + parent.getY();
    }

    boolean showButtons() {
        return true;
    }

    public abstract void onAccept();

    @Override
    public void onClick(int x, int y) {
        if (showButtons()) {
            if (!visibleButtons) {
                visibleButtons = true;
            } else {
                boolean clicked = accept.checkClick(x - this.x, y - this.y);
                clicked |= deny.checkClick(x - this.x, y - this.y);
                if (!clicked)
                    visibleButtons = false;
            }
        }
    }

    @Override
    public boolean firstTouch(int x, int y) {
        boolean touched = false;
        if (visibleButtons) {
            touched = accept.firstTouch(x - this.x, y - this.y);
            touched |= deny.firstTouch(x - this.x, y - this.y);
        }
        if (!touched)
            super.firstTouch(x, y);
        return false;
    }

    @Override
    public void touchDragged(int x, int y) {
        super.touchDragged(x, y);
        if (visibleButtons) {
            accept.touchDragged(x - this.x, y - this.y);
            deny.touchDragged(x - this.x, y - this.y);
        }
    }

    @Override
    public void touchUp() {
        super.touchUp();
        if (visibleButtons) {
            accept.touchUp();
            deny.touchUp();
        }
    }

    @Override
    public void render(SpriteBatch batch, double delta) {
        super.render(batch, delta);
        batch.draw(button, x - textureWidth / 2 + parent.getX(), y - textureHeight / 2 + parent.getY(), textureWidth, textureHeight);
        accept.render(batch, delta, visibleButtons);
        deny.render(batch, delta, visibleButtons);
    }

    @Override
    public void setParent(Parent parent) {
        this.parent = parent;
    }

    private abstract class ButtonsToConfirm extends Button {

        Texture texture;
        ConfirmButton parent;

        ButtonsToConfirm(int x, int y, int width, int height, String texturePath, ConfirmButton parent) {
            super(x, y, width, height, 3);
            texture = textureBank.get(texturePath);
            this.parent = parent;
        }

        public void render(SpriteBatch batch, double delta, boolean visible) {
            super.render(batch, delta);
            double width = (double) ConfirmButton.super.textureWidth / ConfirmButton.super.width,
                    height = (double) ConfirmButton.super.textureHeight / ConfirmButton.super.height;
            if (visible)
                batch.draw(texture, (float) ((x - textureWidth / 2) * width) + parent.getX(), (float) ((y - textureHeight / 2) * height) + parent.getY(), (float) (textureWidth * width), (float) (textureHeight * height));
        }
    }
}
