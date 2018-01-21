package ru.rpuxa.arkanoid.Physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.rpuxa.arkanoid.Account.SecurityInt;
import ru.rpuxa.arkanoid.Main.Visual;

import static ru.rpuxa.arkanoid.Main.Visual.textureBank;

public class Score {
    public SecurityInt pointsCollected, starsCollected;
    Text score, points, stars;
    Texture star;

    public Score() {
        int x = Visual.WIDTH / 2 - 30;
        int y = 145;
        this.pointsCollected = new SecurityInt(0);
        this.starsCollected = new SecurityInt(0);
        score = new Text("SCORE:", x, y, 30);
        points = new Text("000000", x - 100, y - 30, 60);
        stars = new Text("000", 780, 65, 45);
        star = textureBank.get("star");
    }

    public Score(int pointsCollected, int starsCollected) {
        this();
        this.pointsCollected = new SecurityInt(pointsCollected);
        this.starsCollected = new SecurityInt(starsCollected);
        addPoint(pointsCollected);
        addStar(starsCollected);
    }

    public void addPoint(int sc) {
        points.text = "";
        pointsCollected.add(sc);
        String text = "00000" + pointsCollected.getValue();
        for (int i = 5; i >= 0; i--) {
            points.text += text.charAt(text.length() - i - 1);
        }
    }

    public void addStar() {
        addStar(1);
    }

    public void addStar(int c) {
        stars.text = "";
        starsCollected.add(c);
        String text = "00" + starsCollected.getValue();
        for (int i = 2; i >= 0; i--) {
            stars.text += text.charAt(text.length() - i - 1);
        }
    }

    public void render(SpriteBatch batch) {
        points.render(batch);
        score.render(batch);
        batch.draw(star, 802, 90, 50, 50);
        stars.render(batch);
    }
}
