package ru.rpuxa.arkanoid.Components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.rpuxa.arkanoid.Account.Account;
import ru.rpuxa.arkanoid.Physics.Text;

import static ru.rpuxa.arkanoid.Main.Visual.textureBank;

public class MoneyBar implements Child {
    int x, y;
    int width, height;
    Parent parent;
    int currency;
    Account account;
    Text text;
    Texture background, star, ruby;

    public MoneyBar(String back, int x, int y, int width, int height, int currency) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.currency = currency;
        text = new Text("", 40, Color.BLACK);
        background = textureBank.get(back);
        star = textureBank.get("star");
        ruby = textureBank.get("ruby");
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public boolean collision(int x, int y) {
        return false;
    }

    @Override
    public void touchUp() {

    }

    @Override
    public void touchDragged(int x, int y) {

    }

    @Override
    public boolean firstTouch(int x, int y) {
        return false;
    }

    @Override
    public void render(SpriteBatch batch, double delta) {
        if (account != null) {
            batch.draw(background, x + parent.getX(), y + parent.getY(), width, height);
            batch.draw((currency == 0) ? star : ruby, x + parent.getX() + 20, y + parent.getY() + height / 8, 3 * height / 4, 3 * height / 4);
            text.text = "" + account.money[currency].getValue();
            text.x = x + height + parent.getX() + 10;
            text.y = height / 2 + 15 + parent.getY() + y;
            text.render(batch);
        }
    }

    @Override
    public void setParent(Parent parent) {
        this.parent = parent;
    }
}
