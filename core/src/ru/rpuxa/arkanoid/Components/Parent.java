package ru.rpuxa.arkanoid.Components;

interface Parent {

    int getX();

    int getY();

    default void addX(int x) {
    }

    int getWidth();

    default boolean isLeft() {
        return false;
    }
}
