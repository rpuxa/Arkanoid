package ru.rpuxa.arkanoid.Skins;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CannonInfo extends Info {

    private static final long serialVersionUID = -5436438888054479092L;

    public static CannonInfo[] cannons;

    transient public TextureRegion barrel;
    public int[] rotateBarrelCenter;
    public int[] rotatePlatformCenter;
    public int width;

    public CannonInfo(String[] texturePath, int currency, int cost, double damage, double speed, int id, int[] rotateBarrelCenter, int[] rotatePlatformCenter, int width) {
        super(texturePath, currency, cost, damage, speed, id);
        this.rotateBarrelCenter = rotateBarrelCenter;
        this.rotatePlatformCenter = rotatePlatformCenter;
        this.width = width;
    }

    @Override
    public void recombineTextures() {
        super.recombineTextures();
        if (texture != null && texture[0] != null)
            barrel = new TextureRegion(texture[0]);
    }

    public static CannonInfo get(int id) {
        for (Info info : cannons)
            if (info.id == id) {
                return (CannonInfo) info;
            }
        return null;
    }
}
