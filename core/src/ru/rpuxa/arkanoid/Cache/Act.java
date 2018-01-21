package ru.rpuxa.arkanoid.Cache;

import java.io.Serializable;
import java.util.ArrayList;

import ru.rpuxa.arkanoid.Main.Game;
import ru.rpuxa.arkanoid.Physics.Let;
import ru.rpuxa.arkanoid.Physics.Power;
import ru.rpuxa.arkanoid.Physics.Score;

public class Act implements Serializable {
    public SerializableLet[][] lets;
    public SerializableScore score;
    public ArrayList<SerializablePower> powers;
    private int lvl;
    private int countBalls;

    public Act(Game game) {
        lets = new SerializableLet[game.lets.length][game.lets[0].length];
        for (int i = 0; i < lets.length; i++) {
            for (int j = 0; j < lets[i].length; j++) {
                if (game.lets[i][j] != null && game.lets[i][j].count > 0)
                lets[i][j] = new SerializableLet(game.lets[i][j]);
            }
        }
        score = new SerializableScore(game.score);
        powers = new ArrayList<>(game.powers.size());
        for (Power power : game.powers)
            powers.add(new SerializablePower(power));
        lvl = game.lvl;
        countBalls = game.cannon.countBalls;
    }

    public void unpack(Game game) {
        for (int i = 0; i < lets.length; i++) {
            for (int j = 0; j < lets[i].length; j++) {
                SerializableLet let = lets[i][j];
                if (let != null)
                    game.lets[i][j] = new Let(let.x, let.y, let.count, game);
            }
        }
        game.score = new Score(score.pointsCollected, score.starsCollected);
        for (SerializablePower power : powers)
            game.powers.add(Power.getById(power.id, power.x, power.y));
        game.lvl = lvl;
        game.cannon.countBalls = countBalls;
    }

    private class SerializableLet implements Serializable {
        public int x, y;
        public double count;

        public SerializableLet(int x, int y, double count) {
            this.x = x;
            this.y = y;
            this.count = count;
        }

        public SerializableLet(Let let) {
            this(let.x, let.y, let.count);
        }
    }

    private class SerializableScore implements Serializable {
        public int pointsCollected, starsCollected;

        public SerializableScore(int pointsCollected, int starsCollected) {
            this.pointsCollected = pointsCollected;
            this.starsCollected = starsCollected;
        }

        public SerializableScore(Score score) {
            this(score.pointsCollected.getValue(), score.starsCollected.getValue());
        }
    }

    private class SerializablePower implements Serializable {
        public int x, y, id;

        public SerializablePower(int x, int y, int id) {
            this.x = x;
            this.y = y;
            this.id = id;
        }

        public SerializablePower(Power power) {
            this(power.x, power.y, power.id);
        }
    }
}
