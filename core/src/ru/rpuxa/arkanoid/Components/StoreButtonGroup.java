package ru.rpuxa.arkanoid.Components;

import java.util.ArrayList;

public class StoreButtonGroup {
    ArrayList<StoreButton> buttons;

    public StoreButtonGroup() {
        buttons = new ArrayList<>();
    }

    public void add(StoreButton button) {
        buttons.add(button);
    }

    public void setSelectedFalse() {
        for (StoreButton button : buttons)
            button.selected = false;
    }

    public void setVisualButtonsFalse() {
        for (StoreButton button : buttons)
            button.visibleButtons = false;
    }
}
