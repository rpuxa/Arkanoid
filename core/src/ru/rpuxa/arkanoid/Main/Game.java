package ru.rpuxa.arkanoid.Main;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Random;

import ru.rpuxa.arkanoid.Account.Account;
import ru.rpuxa.arkanoid.Account.LoginDialog;
import ru.rpuxa.arkanoid.Components.Child;
import ru.rpuxa.arkanoid.Components.Dialog;
import ru.rpuxa.arkanoid.Components.GameButton;
import ru.rpuxa.arkanoid.Components.GameOver;
import ru.rpuxa.arkanoid.Components.Menu;
import ru.rpuxa.arkanoid.Components.MoneyBar;
import ru.rpuxa.arkanoid.Components.Scroll;
import ru.rpuxa.arkanoid.Components.StoreButton;
import ru.rpuxa.arkanoid.Components.Submenu;
import ru.rpuxa.arkanoid.Components.YesNoDialog;
import ru.rpuxa.arkanoid.Physics.AimLine;
import ru.rpuxa.arkanoid.Physics.Ball;
import ru.rpuxa.arkanoid.Physics.Boom;
import ru.rpuxa.arkanoid.Physics.Cannon;
import ru.rpuxa.arkanoid.Physics.Edge;
import ru.rpuxa.arkanoid.Physics.Explosion;
import ru.rpuxa.arkanoid.Physics.Let;
import ru.rpuxa.arkanoid.Physics.LineSegment;
import ru.rpuxa.arkanoid.Physics.Number;
import ru.rpuxa.arkanoid.Physics.Power;
import ru.rpuxa.arkanoid.Physics.Score;
import ru.rpuxa.arkanoid.Skins.BallInfo;
import ru.rpuxa.arkanoid.Cache.CacheMaster;
import ru.rpuxa.arkanoid.Skins.CannonInfo;

import static ru.rpuxa.arkanoid.Main.Visual.textureBank;

public class Game {
    Texture background, tip;
    public Let[][] lets;
    public ArrayList<Edge> letEdges;
    public ArrayList<Ball> balls;
    public ArrayList<Power> powers;
    public Cannon cannon;
    public GameButton[] gameButtons;
    public Menu[] menus;
    public Dialog dialog;
    Controller controller;
    Boom boom;
    Explosion explosion;
    AimLine aimLine;
    public Score score;
    GameOver gameOver;
    private boolean[] canShoot = {true};
    public boolean aiming = false;
    public int lvl;
    boolean gameStarted, menuOpened, gameLosed;

    public static Loading loading;
    public static Account account;

    Game() {
        gameOver = new GameOver("gameOver");
        explosion = new Explosion(30, "explosion", 6, 8);
        tip = textureBank.get("tip0");
        boom = new Boom();
        Number.setTextures();
        loading = new Loading("loading");
        balls = new ArrayList<>();
        score = new Score();
        lets = new Let[8][14];
        powers = new ArrayList<>();
        letEdges = new ArrayList<>();
        gameButtons = new GameButton[] {
                new GameButton(995, 80, 120, 120, "accelerationGameButton") {

                    static final double MULTIPLY_SPEED = 2;

                    @Override
                    public boolean firstTouch(int x, int y) {
                        boolean b = super.firstTouch(x, y);
                        if (firstTouch) {
                            Ball.SPEED *= MULTIPLY_SPEED;
                        }
                        return b;
                    }

                    @Override
                    public void touchUp() {
                        if (firstTouch)
                            Ball.SPEED /= MULTIPLY_SPEED;
                        super.touchUp();
                    }

                    @Override
                    public void onClick(int x, int y) {}
                },
                new GameButton(250, 80, 120, 120, "foldBallsGameButton") {
                    @Override
                    public void onClick(int x, int y) {
                        foldBalls();
                    }
                },
                new GameButton(81, 80, 120, 120, "backToMenuGameButton") {
                    @Override
                    public void onClick(int x, int y) {
                        dialog = new YesNoDialog("Quit game?? Are you sure??") {
                            @Override
                            public void onAccept() {
                                balls.clear();
                                cannon.setStartPos();
                                gameStarted = false;
                                cannon.isDestroyed = false;
                                gameOver.dismiss();
                            }
                        };
                    }
                }
        };
        menus = new Menu[]{
                new Menu(Visual.WIDTH - 200, "storeMenu", "storeMenuButton", true, 200,
                        storeSubmenu = new Submenu(
                                175, 0, Visual.WIDTH - 450, Visual.HEIGHT,
                                new Texture[]{textureBank.get("no"), textureBank.get("no"), textureBank.get("no")},
                                new Child[0][0],
                                new Submenu.ShiftButton[]{
                                        new Submenu.ShiftButton(97, 1815, 130, 121),
                                        new Submenu.ShiftButton(97, 1620, 130, 121),
                                        new Submenu.ShiftButton(97, 1418, 130, 121),
                                }
                        ),
                        moneyBarStar = new MoneyBar("dialog", 100, 40, 300, 80, 0),
                        moneyBarRuby = new MoneyBar("dialog", 480, 40, 300, 80, 1)
                )
        };
        controller = new Controller(canShoot, this);
        cannon = new Cannon(CannonInfo.get(1), balls);
        gameStarted = false;
        background = textureBank.get("background");
        aimLine = new AimLine(cannon);
        Let.setTextures();
        if (!CacheMaster.loadAct(this))
            nextLevel();
        updateEdges();
        if (Visual.ONLINE_MODE)
            dialog = new LoginDialog(this);
        else {
            account = new Account();
            dialog = new Dialog();
            loginSuccessful();
        }
    }

    public void addBalls(int count) {
        if (cannon.countBalls + count >= 0)
            cannon.countBalls += count;
    }

    public void updateEdges() {
        letEdges.clear();
        boolean[][] isLet = new boolean[8][10];

        int width = Visual.WIDTH, height = Visual.HEIGHT;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 10; j++) {
                isLet[i][j] = lets[i][j] != null && lets[i][j].count > 0;
            }
        }
        for (int y = 0; y < 14; y++) {
            for (int x = 0; x < 8; x++) {
                if (y < 10 && isLet[x][y])
                    continue;
                boolean[][] isLet3x3 = new boolean[3][3];
                for (int x1 = -1; x1 <= 1; x1++) {
                    for (int y1 = -1; y1 <= 1; y1++) {
                        isLet3x3[x1 + 1][y1 + 1] = x + x1 < 0 || x + x1 > 7 || y + y1 < 0 || y + y1 <= 9 && isLet[x + x1][y + y1];
                    }
                }
                updateEdges(isLet3x3, x * width / 8, height - (y + 1) * width / 8, width / 8, BallInfo.DIAMETER / 2, x, y);
            }
        }
    }

    private void updateEdges(boolean[][] isLet, int x, int y, int size, int radius, int x1, int y1) {
        if (isLet[1][2]) {
            int i = x1 , j = y1 + 1;
            letEdges.add(new Edge(new LineSegment(new double[][]{
                    {x, y + radius},
                    {x + size, y + radius}
            }), (i < 0 || j < 0 || i > 7 || j > 9) ? null : lets[i][j]));
        }
        if (isLet[1][0]) {
            int i = x1 , j = y1 - 1;
            letEdges.add(new Edge(new LineSegment(new double[][]{
                    {x, y + size - radius},
                    {x + size, y + size - radius}
            }), (i < 0 || j < 0 || i > 7 || j > 9) ? null : lets[i][j]));
        }
        if (isLet[0][1]) {
            int i = x1 - 1, j = y1;
            letEdges.add(new Edge(new LineSegment(new double[][]{
                    {x + radius, y + size},
                    {x + radius, y}
            }), (i < 0 || j < 0 || i > 7 || j > 9) ? null : lets[i][j]));
        }
        if (isLet[2][1]) {
            int i = x1 + 1, j = y1;
            letEdges.add(new Edge(new LineSegment(new double[][]{
                    {x + size - radius, y + size},
                    {x + size - radius, y}
            }), (i < 0 || j < 0 || i > 7 || j > 9) ? null : lets[i][j]));
        }
        if (isLet[0][0] && !isLet[0][1] && !isLet[1][0]) {
            int i = x1 - 1, j = y1 - 1;
            addCircle(x, y + size, radius, 3, (i < 0 || j < 0 || i > 7 || j > 9) ? null : lets[i][j]);
        }
        if (isLet[2][0] && !isLet[1][0] && !isLet[2][1]) {
            int i = x1 + 1, j = y1 - 1;
            addCircle(x + size, y + size, radius, 2, (i < 0 || j < 0 || i > 7 || j > 9) ? null : lets[i][j]);
        }
        if (isLet[2][2] && !isLet[2][1] && !isLet[1][2]) {
            int i = x1 + 1, j = y1 + 1;
            addCircle(x + size, y, radius, 1, (i < 0 || j < 0 || i > 7 || j > 9) ? null : lets[i][j]);
        }
        if (isLet[0][2] && !isLet[1][2] && !isLet[0][1]) {
            int i = x1 - 1, j = y1 + 1;

            addCircle(x, y, radius, 0, (i < 0 || j < 0 || i > 7 || j > 9) ? null : lets[i][j]);
        }
    }

    private void addCircle(int x, int y, int radius, int fourth, Let let) {
        int index = fourth * 5;
        for (int i = 0; i < 5; i++) {
            double angle0 = Math.PI / 10 * index, angle1 = Math.PI / 10 * (index + 1);
            letEdges.add(new Edge(new LineSegment(new double[][]{
                    {x + radius * Math.cos(angle0), y + radius * Math.sin(angle0)},
                    {x + radius * Math.cos(angle1), y + radius * Math.sin(angle1)}
            }), let));
            index++;
        }
    }

    private boolean getRandomBoolean(double percent, double percent1) {
        return new Random().nextDouble() < percent + percent - percent * percent1;
    }

    public void nextLevel() {
        lvl++;
        cannon.countBalls++;
        for (Power power : powers.toArray(new Power[0]))
            if (power.isUsed())
                powers.remove(power);
        powers.clear();
        for (int y = 10; y >= 0; y--) {
            for (int x = 0; x < 8; x++) {
                if (y == 0) {
                    lets[x][y] = null;
                } else
                    lets[x][y] = lets[x][y - 1];
            }
        }

        double countLets = 0;

        for (Let[] let0 : lets)
            for (Let let : let0)
                if (let != null) {
                    let.moveDown();
                    countLets++;
                }
        countLets /= 30;

        for (Let[] let1 : lets) {
            Let let = let1[10];
            if (let != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        canShoot[0] = false;
                        int x = cannon.x;
                        Random random = new Random();
                        for (int i = 0; i < 100; i++) {
                            cannon.x = (int) (x + random.nextDouble() * i / 4);
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException ignored) {
                            }
                        }
                        gameOver();
                    }
                }).start();
                return;
            }
        }

        int countInFirstRow = 0;
        for (int i = 0; i < 8; i++) {
            if (lets[i][1] != null)
                countInFirstRow++;
        }

        Random random = new Random();
        if (countInFirstRow <= 1) {
            for (int i = 0; i < 8; i++) {
                if (random.nextInt(10) >= 2 || i == 3 || i == 4)
                    spawnLet(i);
            }
        } else
            for (int i = 0; i < 8; i++) {
                int count = 0;
                if (lets[i][1] != null)
                    count++;
                if ((i != 0 && lets[i - 1][1] != null))
                    count++;
                if ((i != 7 && lets[i + 1][1] != null))
                    count++;
                if (count == 3) {
                    if (getRandomBoolean(0.9, countLets))
                        spawnLet(i);
                } else if (count == 2) {
                    if (getRandomBoolean(.4, countLets))
                        spawnLet(i);
                } else if (count == 1)
                    if (getRandomBoolean(.5, countLets))
                        spawnLet(i);
                    else if (getRandomBoolean(.1, countLets))
                        spawnLet(i);
            }

        boolean spreaderOrDoublePowerSeated = false, starSeated = false;
        for (int y = 0; y < 12; y++) {
            label:
            for (int x = 0; x < 8; x++) {
                if (y > 9 || lets[x][y] == null) {
                    if (random.nextInt(90) == 0) {
                        powers.add(Power.ADD_BALL.clone(x, y));
                    } else if (random.nextInt(180) == 0) {
                        powers.add(Power.REMOVE_BALL.clone(x, y));
                    } else if (!spreaderOrDoublePowerSeated && y > 5 && random.nextInt(30) == 0) {
                        for (int i = -1; i <= 1; i++) {
                            for (int j = -1; j <= 1; j++) {
                                try {
                                    if (lets[x + i][y + j] != null)
                                        continue label;
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    continue label;
                                }
                            }
                        }
                        if (random.nextBoolean())
                            powers.add(Power.SPREADER.clone(x, y));
                        else
                            powers.add(Power.DOUBLE_BALL.clone(x, y));
                        spreaderOrDoublePowerSeated = true;
                    } else if (!starSeated && random.nextInt(40) == 0) {
                        powers.add(Power.STAR.clone(x, y));
                        starSeated = true;
                    }
                }
            }
        }
        updateEdges();
        CacheMaster.saveAct(this);
    }

    private void spawnLet(int column) {
        Random random = new Random();
        spawnLet(column, (int) Math.round(Math.ceil(lvl * (1 + random.nextDouble() / 2 - .25))));

    }

    private void spawnLet(int column, int count) {
        lets[column][0] = new Let(column * Visual.WIDTH / 8 + Visual.WIDTH / 16, Visual.HEIGHT - Visual.WIDTH / 16, count, this);
    }

    void gameOver() {
        gameLosed = true;
        canShoot[0] = false;
        boom = new Boom(cannon.x + cannon.platformWidth / 2, cannon.y, this);
        explosion.explode(cannon.x + cannon.platformWidth / 2, cannon.y + cannon.platformHeight / 2, 600);
        try {
            Thread.sleep(300);
        } catch (InterruptedException ignored) {
        }
        cannon.isDestroyed = true;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
        gameOver.show();
    }

    void foldBalls() {
        for (Ball ball : balls.toArray(new Ball[0]))
            ball.fold = true;
    }

    private Submenu storeSubmenu;
    private MoneyBar moneyBarStar, moneyBarRuby;

    public void loginSuccessful() {
        storeSubmenu.setChildren(new Child[][]{
                {StoreButton.createCannonButtons()},
                {StoreButton.createBallButtons()},
                //{donate}
        });
        moneyBarStar.setAccount(account);
        moneyBarRuby.setAccount(account);
    }

    void render(SpriteBatch batch, double delta) {
        if (!balls.isEmpty())
            canShoot[0] = false;
        batch.draw(background, 0, 0);
        if (gameStarted)
        for (Power power : powers.toArray(new Power[0])) {
            power.render(batch);
        }
        for (Ball ball : balls.toArray(new Ball[0])) {
            if (ball.y <= 165 + BallInfo.DIAMETER / 2 || ball.y > Visual.HEIGHT || ball.x < 0 || ball.x > Visual.WIDTH)
                balls.remove(ball);
            else
                ball.render(batch, delta, this);
            if (balls.isEmpty()) {
                canShoot[0] = true;
                nextLevel();
            }
        }
        for (int i = 0; i < lets.length; i++) {
            for (int j = 0; j < lets[0].length; j++) {
                if (lets[i][j] != null && lets[i][j].isDestroyed())
                    lets[i][j] = null;
            }
        }
        for (Let[] let0 : lets)
            for (Let let : let0) {
                if (let != null)
                    let.render(batch, delta);
            }
        cannon.render(batch);
        if (aiming)
            aimLine.render(batch, lets);
        if (gameStarted)
            score.render(batch);
        if (!gameStarted) {
            batch.draw(tip, 0, 0);
        }
        gameOver.render(batch, delta);
        if (gameStarted) {
            for (GameButton button : gameButtons)
                button.render(batch, delta);
        } else {
            for (Menu menu : menus)
                menu.render(batch, delta);
        }
        boom.render(batch, delta);
        explosion.render(batch, delta);
        if (!dialog.isDismissed())
            dialog.render(batch, delta);
        loading.render(batch);
    }

    public static class Loading {
        Texture texture;
        boolean visible;

        Loading(String texture) {
            this.texture = textureBank.get(texture);
        }

        private void render(SpriteBatch batch) {
            if (visible)
                batch.draw(texture, 0, 0);
        }

        public void startLoading() {
            visible = true;
        }

        public void stopLoading() {
            visible = false;
        }
    }
}
