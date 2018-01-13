package ru.rpuxa.arkanoid.Components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Button implements Touchable {
    public int x, y;
    public int width, height;
    public int textureWidth, textureHeight;
    private int touchX, touchY;
    public boolean firstTouch;
    private int maxNumberOfVibrations;

    public Button(int x, int y, int width, int height, int maxNumberOfVibrations) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxNumberOfVibrations = maxNumberOfVibrations;
        textureWidth = width;
        textureHeight = height;
    }

    @Override
    public boolean collision(int x, int y) {
        return x >= this.x - width / 2 && y >= this.y - height / 2 && x <= this.x + width / 2 && y <= this.y + height / 2;
    }

    public boolean checkClick(int x, int y) {
        if (firstTouch && collision(x, y)) {
            onClick(x, y);
            return true;
        }
        return false;
    }

    @Override
    public boolean firstTouch(int x, int y) {
        if (collision(x, y)) {
            firstTouch = true;
            touchDragged(x, y);
            return true;
        }
        return false;
    }

    @Override
    public void touchUp() {
        checkClick(touchX, touchY);
        firstTouch = false;
    }

    @Override
    public void touchDragged(int x, int y) {
        if (firstTouch) {
            touchX = x;
            touchY = y;
        }
    }

    private static final int VELOCITY_TYPING = 1000;
    private static final double PERCENT_SIZING = .6;
    private static final int INITIAL_VELOCITY_RISING = 200;
    private static final int ACCELERATION_RISING = 20;

    void sizing(double delta) {
        if (collision(touchX, touchY) && firstTouch) {
            textureWidth = reduction(width, textureWidth, delta);
            textureHeight = reduction(height, textureHeight, delta);
            velocityRising[0] = INITIAL_VELOCITY_RISING;
            velocityRising[1] = INITIAL_VELOCITY_RISING;
            numberVibrations = 0;
        } else {
            textureWidth = rising(width, textureWidth, delta, 0);
            textureHeight = rising(height, textureHeight, delta, 1);
        }
    }

    private int reduction(int edge, int textureEdge, double delta) {
        if (textureEdge - delta * VELOCITY_TYPING < edge * PERCENT_SIZING)
            textureEdge = (int) (edge * PERCENT_SIZING);
        else
            textureEdge -= delta * VELOCITY_TYPING;
        return textureEdge;
    }

    private double[] velocityRising = new double[2];
    private int numberVibrations;

    private int rising(int edge, int textureEdge, double delta, int i) {
        if (numberVibrations <= maxNumberOfVibrations) {
            if (textureEdge + delta * velocityRising[i] > edge) {
                velocityRising[i] /= -2;
                textureEdge = edge;
                numberVibrations++;
            } else {
                textureEdge += delta * velocityRising[i];
                velocityRising[i] += ACCELERATION_RISING;
            }
        } else {
            textureEdge = edge;
        }
        return textureEdge;
    }

    public abstract void onClick(int x, int y);

    public void render(SpriteBatch batch, double delta) {
        sizing(delta);
    }
}
