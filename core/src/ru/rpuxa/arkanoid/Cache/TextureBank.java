package ru.rpuxa.arkanoid.Cache;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

public class TextureBank {
    Map<String, Texture> textures;

    public TextureBank() {
        this.textures = new HashMap<>();
    }

    public void fill(ProgressBar bar) {
        String[][] all = {
                {"block0", "Blocks/2.png"},
                {"block1", "Blocks/0.png"},
                {"block2", "Blocks/1.png"},
                {"block3", "Blocks/3.png"},
                {"speed", "Buttons/ComponentButtons/StoreButton/speed.png"},
                {"damage", "Buttons/ComponentButtons/StoreButton/damage.png"},
                {"selected", "Buttons/ComponentButtons/StoreButton/selected.png"},
                {"sold", "Buttons/ComponentButtons/StoreButton/sold.png"},
                {"storeButton", "Buttons/ComponentButtons/StoreButton/storeButton.png"},
                {"confirmButton", "Buttons/ComponentButtons/confirm.png"},
                {"loginButton", "Buttons/ComponentButtons/loginButton.png"},
                {"loginOrRegButton", "Buttons/ComponentButtons/loginOrReg.png"},
                {"storeMenuButton", "Buttons/ComponentButtons/storeMenuButton.png"},
                {"noButton", "Buttons/ComponentButtons/noButton.png"},
                {"roundGreenButton", "Buttons/ComponentButtons/roundGreenButton.png"},
                {"roundRedButton", "Buttons/ComponentButtons/roundRedButton.png"},
                {"yesButton", "Buttons/ComponentButtons/yesButton.png"},
                {"accelerationGameButton", "Buttons/GameButtons/accelerationGameButton.png"},
                {"backToMenuGameButton", "Buttons/GameButtons/backToMenuGameButton.png"},
                {"foldBallsGameButton", "Buttons/GameButtons/foldBallsGameButton.png"},
                {"no", "Other/noTexture.png"},
                {"ruby", "Currency/ruby.png"},
                {"star", "Currency/star.png"},
                {"dialog", "Dialogs/dialog.png"},
                {"loginDialog", "Dialogs/loginDialog.png"},
                {"boom", "GameOver/boom.png"},
                {"destroyedCannon", "GameOver/destroyedCannon.png"},
                {"explosion", "GameOver/explosion.png"},
                {"gameOver", "GameOver/gameOver.png"},
                {"storeMenu", "Menu/storeMenu.png"},
                {"background", "Other/background.png"},
                {"loading", "Other/loading.png"},
                {"point", "Other/point.png"},
                {"addBallPower", "Powers/addBall.png"},
                {"doublePower", "Powers/double.png"},
                {"removeBallPower", "Powers/removeBall.png"},
                {"spreaderPower", "Powers/spreader.png"},
                {"scroll", "Scroll/scroll.png"},
                {"upTextureScroll", "Scroll/upTextureScroll.png"},
                {"textFieldActive", "TextField/textFieldActive.png"},
                {"textFieldDeactive", "TextField/textFieldDeactive.png"},
                {"tip0", "Tips/tip0.png"},
        };
        int i = 0;
        for (String[] texture : all) {
            textures.put(texture[0], new Texture(texture[1]));
            if (bar != null)
                bar.showProgress(i, all.length);
            i++;
        }
    }

    public Texture get(String name) {
        Texture texture = textures.get(name);
        if (texture == null)
            throw new RuntimeException("Texture not found!");
        return texture;
    }

    public interface ProgressBar {
        void showProgress(int done, int all);
    }
}
