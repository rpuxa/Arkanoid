package ru.rpuxa.arkanoid.Account.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

public class Connection {
    public static final int SERVER_PORT = 7158;
    public static final String SERVER_ADDRESS = "5.23.55.105";

    public static Command[] sendCommand(Command command) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(command);
            out.flush();
            return (Command[]) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Command[] sendCommand(int id, Object object) {
        return sendCommand(new Command(id, object));
    }

    public static Command[] sendCommand(int id, Object object, String from) {
        return sendCommand(new Command(id, object, from));
    }

    public static Command[] sendCommand(int id) {
        return sendCommand(id, null);
    }
}
