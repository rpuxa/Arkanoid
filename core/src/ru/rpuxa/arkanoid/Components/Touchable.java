package ru.rpuxa.arkanoid.Components;

public interface Touchable {

    boolean collision(int x, int y);

    void touchUp();

    void touchDragged(int x, int y);

    boolean firstTouch(int x, int y);
}
