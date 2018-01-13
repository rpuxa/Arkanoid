package ru.rpuxa.arkanoid.Skins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.*;
import java.util.ArrayList;

public class CacheMaster {

    private static final String FOLDER = "Caches/";

    public static void loadUpdates() {
        BallInfo.balls = (BallInfo[]) loadObj("balls");
        CannonInfo.cannons = (CannonInfo[]) loadObj("cannons");
        for (BallInfo info : BallInfo.balls)
            info.recombineTextures();
        for (CannonInfo info : CannonInfo.cannons)
            info.recombineTextures();
    }

    private static Object loadObj(String file) {
        try (ObjectInputStream in = new ObjectInputStream(Gdx.files.internal(FOLDER + file).read())) {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void saveObj(Object obj, String file) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FOLDER + file))) {
            out.writeObject(obj);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
