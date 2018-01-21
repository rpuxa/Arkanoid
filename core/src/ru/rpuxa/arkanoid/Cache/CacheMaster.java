package ru.rpuxa.arkanoid.Cache;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.*;

import ru.rpuxa.arkanoid.Main.Game;
import ru.rpuxa.arkanoid.Skins.BallInfo;
import ru.rpuxa.arkanoid.Skins.CannonInfo;

public class CacheMaster {

    private static final String FOLDER = "Caches/";

    public static void loadUpdates() {
        BallInfo.balls = (BallInfo[]) loadObjInternal("balls");
        CannonInfo.cannons = (CannonInfo[]) loadObjInternal("cannons");
        for (BallInfo info : BallInfo.balls)
            info.recombineTextures();
        for (CannonInfo info : CannonInfo.cannons)
            info.recombineTextures();
    }

    public static boolean loadAct(Game game) {
        Act act = (Act) loadObj("act");
        if (act == null)
            return false;
        act.unpack(game);
        return true;
    }

    public static void saveAct(Game game) {
        Act act = new Act(game);
        saveObj(act, "act");
    }

    private static Object loadObjInternal(String file) {
        try (ObjectInputStream in = new ObjectInputStream(Gdx.files.internal(FOLDER + file).read())) {
            return in.readObject();
        } catch (IOException | ClassNotFoundException | GdxRuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object loadObj(String file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(Gdx.files.getExternalStoragePath() + file))) {
            return in.readObject();
        } catch (IOException | ClassNotFoundException | GdxRuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void saveObj(Object obj, String file) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(Gdx.files.getExternalStoragePath() + file))) {
            out.writeObject(obj);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
