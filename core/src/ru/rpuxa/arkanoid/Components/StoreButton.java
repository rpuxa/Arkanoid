package ru.rpuxa.arkanoid.Components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.rpuxa.arkanoid.Account.Server.Connection;
import ru.rpuxa.arkanoid.Account.Server.Constants;
import ru.rpuxa.arkanoid.Main.Game;
import ru.rpuxa.arkanoid.Main.Visual;
import ru.rpuxa.arkanoid.Physics.Text;
import ru.rpuxa.arkanoid.Skins.BallInfo;
import ru.rpuxa.arkanoid.Skins.CannonInfo;
import ru.rpuxa.arkanoid.Skins.Info;

import static ru.rpuxa.arkanoid.Account.Account.STARS;
import static ru.rpuxa.arkanoid.Main.Visual.imei;

public class StoreButton extends ConfirmButton {

    private BallInfo product0 = null;
    private CannonInfo product1 = null;
    private int id;
    private double platformWidth, platformHeight;
    private double barrelWidth, barrelHeight;
    private double[] platformRotateCenter, barrelRotateCenter;
    private boolean sold;
    private Text speedText, damageText, moneyText;

    static Texture speed, damage, star, ruby, soldTexture;

    private StoreButton(int num, Info info, boolean sold) {
        super(315, 1700 - num * 300, 600, 250, "storeButton.png");
        this.sold = sold;
        id = info.id;
        speedText = new Text("x" + info.speed, (int) (12d / 200 * width), Color.BLACK);
        damageText = new Text("x" + info.damage, (int) (12d / 200 * width), Color.BLACK);
        moneyText = new Text("" + info.cost, (int) (12d / 200 * width), Color.BLACK);
        if (speed == null) {
            speed = new Texture("speed.png");
            damage = new Texture("damage.png");
            star = new Texture("star.png");
            ruby = new Texture("ruby.png");
            soldTexture = new Texture("sold.png");
        }
    }

    private static final double productX = -212d / 500, productY = -40d / 200, productXCenter = -204d / 500;
    private static final double productWidth = 100d / 500, productHeight = 90d / 200;

    private static StoreButton createButton(int num, BallInfo info, boolean sold) {
        StoreButton button = new StoreButton(num, info, sold);
        button.product0 = info;
        return button;
    }

    private static StoreButton createButton(int num, CannonInfo info, boolean sold) {
        StoreButton button = new StoreButton(num, info, sold);
        button.product1 = info;
        button.platformWidth = productWidth;
        button.platformHeight = info.texture[1].getHeight() * button.platformWidth / info.texture[1].getWidth() * button.width / button.height;
        button.barrelWidth = info.texture[0].getWidth() * button.platformWidth / info.texture[1].getWidth();
        button.barrelHeight = info.texture[1].getHeight() * button.barrelWidth /  info.texture[1].getWidth() * button.width / button.height;
        button.platformRotateCenter = new double[] {
                info.rotatePlatformCenter[0] * button.platformWidth / info.texture[1].getWidth(),
                info.rotatePlatformCenter[1] * button.platformHeight / info.texture[1].getHeight()
        };
        button.barrelRotateCenter = new double[] {
                info.rotateBarrelCenter[0] * button.barrelWidth / info.texture[0].getWidth(),
                info.rotateBarrelCenter[1] * button.barrelHeight / info.texture[0].getHeight()
        };
        return button;
    }

    public static StoreButton[] createBallButtons() {
        StoreButton[] buttons = new StoreButton[BallInfo.balls.length];
        for (int i = 0; i < buttons.length; i++) {
            BallInfo info = BallInfo.balls[i];
            boolean sold = Game.account.haveItem(info.id);
            buttons[i] = createButton(i, info, sold);
        }
        return buttons;
    }

    public static StoreButton[] createCannonButtons() {
        StoreButton[] buttons = new StoreButton[CannonInfo.cannons.length];
        for (int i = 0; i < buttons.length; i++) {
            CannonInfo info = CannonInfo.cannons[i];
            boolean sold = Game.account.haveItem(info.id);
            buttons[i] = createButton(i, info, sold);
        }
        return buttons;
    }

    @Override
    public boolean firstTouch(int x, int y) {
        if (sold)
            visibleButtons = false;
        super.firstTouch(x, y);
        return false;
    }

    @Override
    boolean showButtons() {
        return !sold;
    }

    @Override
    public void onAccept() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Game.loading.startLoading();
               if ((boolean) Connection.sendCommand(Constants.BUY_ITEM, id, imei)[0].data){
                   Game.account.addItem(id);
                   sold = true;
                   Visual.message.send("Sold!");
               } else {
                   Visual.message.send("Not enough money!");
               }
                Game.loading.stopLoading();
            }
        }).start();
    }

    private static final double parametersX = -70d / 500, parametersHeight = 31d / 200, parametersWidth = 31d / 500;
    private static final double parameter0Y = 40d / 200, parameter1Y = -13d / 200, parameter2Y = -65d / 200;
    private static final double soldX = 45d / 500, soldY = -120d / 200, soldWidth = 200d / 500, soldHeight = 150d / 200;
    private static final double textX = -30d / 500;

    @Override
    public void render(SpriteBatch batch, double delta) {
        super.render(batch, delta);
        double width = (double) textureWidth,
                height = (double) textureHeight;
        Info info;
        if (product0 == null) {
            batch.draw(product1.texture[1], x + parent.getX() + (float) (productX * width), (float) (productY * height) + y + parent.getY(), (float) (platformWidth * width), (float) (platformHeight * height));
            batch.draw(product1.texture[0], x + parent.getX() + (float) ((productX + platformRotateCenter[0] - barrelRotateCenter[0]) * width), (float)((productY + platformRotateCenter[1] - barrelRotateCenter[1]) * height) + y + parent.getY(), (float) (barrelWidth * width), (float) (barrelHeight * height));
            info = product1;
        } else {
            batch.draw(product0.texture[0], x + parent.getX() + (float) (productXCenter * width), y + parent.getY() + (float) (-productHeight / 2 * height), (float) (9d / 50  * width), (float) (9d / 20 * height));
            info = product0;
        }
        batch.draw((info.currency == STARS) ? star : ruby,
                x + parent.getX() + (float) (parametersX * width),
                y + parent.getY() + (float) (parameter0Y * height),
                (float) (parametersWidth  * width),
                (float) (parametersHeight * height)
                );
        batch.draw(damage,
                x + parent.getX() + (float) (parametersX * width),
                y + parent.getY() + (float) (parameter1Y * height),
                (float) (parametersWidth  * width),
                (float) (parametersHeight * height)
                );
        batch.draw(speed,
                x + parent.getX() + (float) (parametersX * width),
                y + parent.getY() + (float) (parameter2Y * height),
                (float) (parametersWidth  * width),
                (float) (parametersHeight * height)
        );
        if (sold)
            batch.draw(soldTexture,
                    x + parent.getX() + (float) (soldX * width),
                    y + parent.getY() + (float) (soldY * height),
                    (float) (soldWidth  * width),
                    (float) (soldHeight * height)
                    );
        speedText.x = x + parent.getX() + (int) (textX * width);
        speedText.y = y + parent.getY() + (int) ((parameter2Y + 27d / 200) * height);
        speedText.render(batch);

        damageText.x = x + parent.getX() + (int) (textX * width);
        damageText.y = y + parent.getY() + (int) ((parameter1Y + 27d / 200) * height);
        damageText.render(batch);

        moneyText.x = x + parent.getX() + (int) (textX * width);
        moneyText.y = y + parent.getY() + (int) ((parameter0Y + 27d / 200) * height);
        moneyText.render(batch);
    }
}
