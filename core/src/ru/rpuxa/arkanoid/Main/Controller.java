package ru.rpuxa.arkanoid.Main;

import ru.rpuxa.arkanoid.Components.Dialog;
import ru.rpuxa.arkanoid.Components.GameButton;
import ru.rpuxa.arkanoid.Components.Touchable;
import ru.rpuxa.arkanoid.Physics.Score;
import ru.rpuxa.arkanoid.Skins.BallInfo;

public class Controller {
    private boolean cannonTouched;
    private boolean componentTouched;
    private int[] firstTouch;
    private int firstTouchCannonPos;
    private int[] lastTouch;
    private boolean[] canShoot;
    private Game game;

    public Controller(boolean[] canShoot, Game game) {
        this.canShoot = canShoot;
        this.game = game;
    }

    public void touchDown(int x, int y) {
        firstTouch = new int[]{x, y};
        for (Touchable[] touchables : new Touchable[][]{game.gameButtons, game.menus, new Touchable[]{game.dialog}})
            if (!game.gameStarted || touchables[0] instanceof Dialog || touchables[0] instanceof GameButton)
            for (Touchable touchable : touchables) {
                if (touchable.collision(x, y)) {
                    componentTouched = true;
                    touchable.firstTouch(x, y);
                    return;
                }
            }
        if (canShoot[0])
            if (game.gameStarted && x >= game.cannon.x && x <= game.cannon.x + game.cannon.platformWidth && y >= game.cannon.y && y <= game.cannon.platformHeight + game.cannon.y) {
                firstTouchCannonPos = game.cannon.x;
                cannonTouched = true;
            } else {
                lastTouch = new int[]{x, y};
                if (game.gameStarted) {
                    game.cannon.setAngle(lastTouch, game);
                    game.aiming = true;
                }
            }
    }

    public void touchUp() {
        for (Touchable[] touchables : new Touchable[][]{game.gameButtons, game.menus, new Touchable[]{game.dialog}})
            for (Touchable touchable : touchables) {
                touchable.touchUp();
            }
        if (lastTouch != null) {
            if (game.gameStarted) {
                if (canShoot[0]) {
                    if (game.cannon.setAngle(lastTouch, game))
                        game.cannon.shoot(
                                BallInfo.get(Game.account.ballSelected.getValue())
                        );
                    canShoot[0] = false;
                }
                lastTouch = null;
            }
        }
        if (!game.gameStarted && !componentTouched) {
            if (game.gameLosed) {
                canShoot[0] = true;
                game.lvl = 0;
                game.cannon.countBalls = 0;
                game.score = new Score();
                game.nextLevel();
            }
            game.gameStarted = true;
        }
        game.aiming = false;
        cannonTouched = false;
        componentTouched = false;
    }

    public void touchDragged(int x, int y) {
        if (componentTouched) {
            for (Touchable[] touchables : new Touchable[][]{game.gameButtons, game.menus, new Touchable[]{game.dialog}})
                for (Touchable touchable : touchables) {
                    touchable.touchDragged(x, y);
            }
        } else if (cannonTouched && canShoot[0]) {
            game.cannon.x = firstTouchCannonPos - (firstTouch[0] - x);
            if (game.cannon.x + game.cannon.platformWidth > Visual.WIDTH)
                game.cannon.x = Visual.WIDTH - game.cannon.platformWidth;
            else if (game.cannon.x < 0)
                game.cannon.x = 0;
        } else if (game.gameStarted && canShoot[0]) {
            lastTouch = new int[]{x, y};
            game.cannon.setAngle(lastTouch, game);
        }
    }
}
