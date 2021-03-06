package ru.rpuxa.arkanoid.Skins;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Info extends CostInfo {

    private static final long serialVersionUID = 88462314723L;

    public String[] texturePath;
    public transient Texture[] texture;
    public double damage;
    public double speed;

    public Info(String[] texturePath, int currency, int cost, double damage, double speed, int id) {
        super(currency, cost, id);
        this.texturePath = texturePath;
        this.damage = damage;
        this.speed = speed;
    }

    public void recombineTextures() {
        try {
            texture = new Texture[texturePath.length];
            for (int i = 0; i < texturePath.length; i++) {
                texture[i] = new Texture(texturePath[i] + ".png");
            }
        } catch (GdxRuntimeException ignore){
        }
    }
}
