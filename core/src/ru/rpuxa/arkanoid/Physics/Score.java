package ru.rpuxa.arkanoid.Physics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Score {
    int count;
    Text score, textCount;

    public Score(int x, int y) {
        score = new Text("SCORE:", x, y, 30);
        textCount = new Text("000000", x - 100, y - 30, 60);
    }

    public void addScore(int sc) {
        textCount.text = "";
        count += sc;
        String text = "00000" + count;
        for (int i = 5; i >= 0; i--) {
            textCount.text += text.charAt(text.length() - i - 1);
        }
    }

    public void render(SpriteBatch batch) {
        textCount.render(batch);
        score.render(batch);
    }
}
