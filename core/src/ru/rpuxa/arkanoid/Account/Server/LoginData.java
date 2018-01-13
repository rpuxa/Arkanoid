package ru.rpuxa.arkanoid.Account.Server;

        import java.io.Serializable;

public class LoginData implements Serializable {
    public String login;
    public byte[] password;
    public String id;

    public LoginData(String login, byte[] password, String id) {
        this.login = login;
        this.password = password;
        this.id = id;
    }

    public LoginData(String id) {
        this.id = id;
    }
}
