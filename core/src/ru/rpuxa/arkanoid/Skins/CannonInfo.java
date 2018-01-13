package ru.rpuxa.arkanoid.Skins;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CannonInfo extends Info {

    public static CannonInfo[] cannons;

    transient public TextureRegion barrel;
    public int[] rotateBarrelCenter;
    public int[] rotatePlatformCenter;
    public int width;

    public CannonInfo(String[] texturePath, int currency, int cost, int damage, int speed, int id, int[] rotateBarrelCenter, int[] rotatePlatformCenter, int width) {
        super(texturePath, currency, cost, damage, speed, id);
        this.rotateBarrelCenter = rotateBarrelCenter;
        this.rotatePlatformCenter = rotatePlatformCenter;
        this.width = width;
    }

    @Override
    void recombineTextures() {
        super.recombineTextures();
        if (texture != null && texture[0] != null)
            barrel = new TextureRegion(texture[0]);
    }
}
