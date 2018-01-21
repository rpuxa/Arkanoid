package ru.rpuxa.arkanoid.Account;

import ru.rpuxa.arkanoid.Account.Server.*;
import ru.rpuxa.arkanoid.Components.*;
import ru.rpuxa.arkanoid.Main.Game;
import ru.rpuxa.arkanoid.Main.Visual;

import static ru.rpuxa.arkanoid.Main.Visual.imei;

public class LoginDialog extends Dialog implements Constants {

    TextField login
            , password,
            confirmPassword;
    TextureButton confirm, change;
    boolean isLogin;
    Game game;


    public LoginDialog(Game game) {
        super(1000, 900, "loginDialog", false);
        this.game = game;
        setLogin(Connection.sendCommand(LOGIN_ACCOUNT, new LoginData(imei)));
        int width = super.width, x = -width / 2 + 50, widthText = width - 100, heightText = 80;
        int height = super.height;
        super.setChildren(
                confirm = new TextureButton(-300, -300, 250, 190, "confirmButton") {
                    @Override
                    public void onClick(int x, int y) {
                        check();
                    }
                },
                login = new TextField(x , height / 2 - (int) (158d / 618 * height), widthText, heightText, "textFieldActive", "textFieldDeactive", "login", "Enter your login"),
                password = new TextField(x, height / 2 - (int) (300d / 618 * height), widthText, heightText, "textFieldActive", "textFieldDeactive", "password", "Enter your pass"),
                confirmPassword = new TextField(x, height / 2 - (int) (440d / 618 * height), widthText, heightText, "textFieldActive", "textFieldDeactive", "confirm password", "Confirm your pass"),
                change = new TextureButton(200, -300, 375, 190, "loginOrRegButton") {
                    @Override
                    public void onClick(int x, int y) {
                        confirmPassword.disabled = !isLogin;
                        isLogin = !isLogin;
                    }
                }
        );
    }

    private void check() {
        String login = this.login.getText();
        if (login.length() < 4) {
            Visual.message.send("Login is too short");
            return;
        }
        if (login.length() > 16) {
            Visual.message.send("Login is too long");
            return;
        }
        String pass = password.getText();
        if (pass.length() < 4) {
            Visual.message.send("Password is too short");
            return;
        }
        if (pass.length() > 30) {
            Visual.message.send("Password is too long");
            return;
        }
        if (!isLogin) {
            String confirmPass = confirmPassword.getText();
            if (!pass.equals(confirmPass)) {
                Visual.message.send("Passwords do not match");
                return;
            }
        }
        setLogin(Connection.sendCommand(new Command((isLogin) ? LOGIN_ACCOUNT : REGISTER_ACCOUNT, new LoginData(login, pass, imei))));
    }

    private void setLogin(Command[] commands) {
        int id = commands[0].id;
        if (id == LOGIN_SUCCESSFUL) {
            Game.account = new Account((ServerAccount) commands[0].data);
            Visual.message.send("Login/Register successful!");
            game.loginSuccessful();
            dismiss();
        } else if (id == WRONG_LOGIN_DATA) {
            Visual.message.send("Wrong login or password");
        } else if (id == LOGIN_ALREADY_EXISTS)
            Visual.message.send("Login already exists");
    }
}
