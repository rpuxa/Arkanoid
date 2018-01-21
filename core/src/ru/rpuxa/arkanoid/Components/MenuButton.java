package ru.rpuxa.arkanoid.Components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.rpuxa.arkanoid.Main.Visual;

import static ru.rpuxa.arkanoid.Main.Visual.textureBank;

public class MenuButton extends Button {

    private int startX;
    private double velocityDirection;
    private Texture texture;
    private Parent parent;

    MenuButton(int x, int y, int width, int height, String texturePath, Parent parent) {
        super(x, y, width, height, 0);
        texture = textureBank.get(texturePath);
        startX = x;
        this.parent = parent;
    }

    @Override
    public void touchDragged(int x, int y) {
        super.touchDragged(x, y);
        if (firstTouch) {
            velocityDirection = ((x - super.x) > 0) ? 1 : ((x - super.x == 0) ? velocityDirection : -1);
            if (Math.abs(startX - x) <= parent.getWidth() && x >= width / 2 && x <= Visual.HEIGHT - width / 2) {
                setX(x);
            }
        }
    }

    @Override
    public boolean firstTouch(int x, int y) {
        velocity = INITIAL_VELOCITY;
        return super.firstTouch(x, y);
    }

    private void setX(int x){
        parent.addX(x - super.x);
        super.x = x;
    }

    @Override
    public void onClick(int v1, int v2) {
        if (x == parent.getWidth() || x == Visual.HEIGHT - width / 2)
            velocityDirection = -1;
        else if (x == width / 2 || x == Visual.WIDTH - parent.getWidth())
            velocityDirection = 1;
    }

    private static final double INITIAL_VELOCITY = 100;
    private static final double ACCELERATION = 3000;

    private double velocity;

    @Override
    public void render(SpriteBatch batch, double delta) {
        super.render(batch, delta);
        batch.draw(texture, x - width / 2, y - textureHeight / 2, textureWidth, textureHeight);
        if (!firstTouch) {
            double deltaX = x + delta * velocityDirection * velocity;
            velocity += ACCELERATION * delta;
            if (deltaX >= width / 2 && deltaX <= Visual.WIDTH - width / 2 && Math.abs(startX - deltaX) <= parent.getWidth()) {
                setX((int) deltaX);
            } else {
                velocityDirection = 0;
                if (deltaX < width / 2)
                    setX(width / 2);
                else if (deltaX > Visual.WIDTH - width / 2)
                    setX(Visual.WIDTH - width / 2);
                else if (parent.isLeft()) {
                    setX(parent.getWidth() + width / 2);
                } else
                    setX(Visual.WIDTH - parent.getWidth() - width / 2);
            }
        }
    }
}
