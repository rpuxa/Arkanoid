package ru.rpuxa.arkanoid.Components;

import com.badlogic.gdx.graphics.Color;

public abstract class YesNoDialog extends Dialog {

    public YesNoDialog(String text) {
        super(1000, 618, "dialog", true);
        setChildren(
                new TextComponent(text, 60 - width / 2, height / 2 - 50, 40, Color.BLACK),
                new TextureButton(50 - width / 2 + 150, 60 - height / 2 + 185 / 2, 300, 185, "yesButton") {
                    @Override
                    public void onClick(int x, int y) {
                        YesNoDialog.super.dismiss();
                        onAccept();
                    }
                },
                new TextureButton(650 - width / 2 + 150, 60 - height / 2 + 185 / 2, 300, 185, "noButton") {
                    @Override
                    public void onClick(int x, int y) {
                        dismiss();
                    }
                }
        );
    }

    @Override
    public void dismiss() {
        super.dismiss();
        onCancel();
    }

    public abstract void onAccept();

    public void onCancel(){

    }
}
