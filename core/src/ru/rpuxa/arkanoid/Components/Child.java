package ru.rpuxa.arkanoid.Components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Child extends Touchable {

    void render(SpriteBatch batch, double delta);

    void setParent(Parent parent);

}
