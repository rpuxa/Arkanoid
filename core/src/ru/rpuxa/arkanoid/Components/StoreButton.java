package ru.rpuxa.arkanoid.Components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.rpuxa.arkanoid.Account.Account;
import ru.rpuxa.arkanoid.Account.SecurityInt;
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
import static ru.rpuxa.arkanoid.Main.Visual.textureBank;

public class StoreButton extends ConfirmButton {

    private BallInfo product0 = null;
    private CannonInfo product1 = null;
    private int id;
    private double platformWidth, platformHeight;
    private double barrelWidth, barrelHeight;
    private double[] platformRotateCenter, barrelRotateCenter;
    boolean sold, selected;
    private Text speedText, damageText, moneyText;
    private StoreButtonGroup group;
    private Texture speed, damage, star, ruby, soldTexture, selectedTexture;

    private StoreButton(int num, Info info, boolean sold) {
        super(315, 1700 - num * 300, 600, 250, "storeButton");
        this.sold = sold;
        id = info.id;
        speedText = new Text("x" + info.speed, (int) (12d / 200 * width), Color.BLACK);
        damageText = new Text("x" + info.damage, (int) (12d / 200 * width), Color.BLACK);
        moneyText = new Text("" + info.cost, (int) (12d / 200 * width), Color.BLACK);
        speed = textureBank.get("speed");
        damage = textureBank.get("damage");
        star = textureBank.get("star");
        ruby = textureBank.get("ruby");
        soldTexture = textureBank.get("sold");
        selectedTexture = textureBank.get("selected");
    }

    public void setGroup(StoreButtonGroup group) {
        this.group = group;
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

    public static Scroll createBallButtons() {
        StoreButton[] buttons = new StoreButton[BallInfo.balls.length];
        StoreButtonGroup group = new StoreButtonGroup();
        for (int i = 0; i < buttons.length; i++) {
            BallInfo info = BallInfo.balls[i];
            boolean sold = Game.account.haveItem(info.id);
            buttons[i] = createButton(i, info, sold);
            buttons[i].selected = buttons[i].id == Game.account.ballSelected.getValue();
            buttons[i].setGroup(group);
            group.add(buttons[i]);
        }
        return createScroll(buttons);
    }

    public static Scroll createCannonButtons() {
        StoreButton[] buttons = new StoreButton[CannonInfo.cannons.length];
        StoreButtonGroup group = new StoreButtonGroup();
        for (int i = 0; i < buttons.length; i++) {
            CannonInfo info = CannonInfo.cannons[i];
            boolean sold = Game.account.haveItem(info.id);
            buttons[i] = createButton(i, info, sold);
            buttons[i].selected = buttons[i].id == Game.account.cannonSelected.getValue();
            buttons[i].setGroup(group);
            group.add(buttons[i]);
        }
        return createScroll(buttons);
    }

    private static Scroll createScroll(StoreButton[] buttons) {
        int shift = buttons.length * 300 - (Visual.WIDTH - 450) - 1040;
        if (shift < 0)
            shift = 0;
        return new Scroll(0, Visual.WIDTH - 450, "scroll", "upTextureScroll", shift,
                buttons);
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
                   Game.account.addMoney(((product0 == null) ? product1.currency : product0.currency), -((product0 == null) ? product1.cost : product0.cost));
                   sold = true;
                   Visual.message.send("Sold!");
               } else {
                   Visual.message.send("Not enough money!");
               }
                Game.loading.stopLoading();
            }
        }).start();
    }

    @Override
    public void onClick(int x, int y) {
        super.onClick(x, y);
        if (showButtons() && visibleButtons) {
            group.setVisualButtonsFalse();
            visibleButtons = true;
        }
        if (sold) {
            group.setSelectedFalse();
            selected = true;
            if (product0 == null) {
                Game.account.cannonSelected = new SecurityInt(id);
                Visual.game.cannon.info = CannonInfo.get(id);
            } else {
                Game.account.ballSelected = new SecurityInt(id);
            }
        }
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
        if (selected) {
            batch.draw(selectedTexture,
                    x + parent.getX() + (float) (soldX * width),
                    y + parent.getY() + (float) (soldY * height),
                    (float) (soldWidth  * width),
                    (float) (soldHeight * height)
            );
        } else if (sold)
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
