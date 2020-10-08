package client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class Client {
    private static JFrame frame;
    private static Socket socket;
    static ObjectOutputStream writer;
    static ObjectInputStream reader;

    public static void main(String[] args) {
        /* Setup JFrame. */
        Client.frame = new JFrame("MyChat");
        Client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Client.frame.setPreferredSize(new Dimension(300, 500));
        Client.frame.setVisible(true);
        /* Begin by showing connection page. */
        Client.frame.setContentPane((new ConnectionGUI()).view);
        Client.frame.pack();
    }

    static void connect(String ip, int port) throws IOException {
        /* Connect to socket. */
        Client.socket = new Socket(ip, port);
        /* If connection was successful, setup reader/writer. */
        Client.writer = new ObjectOutputStream(Client.socket.getOutputStream());
        Client.reader = new ObjectInputStream(Client.socket.getInputStream());
        /* Show login page. */
        Client.frame.setContentPane((new LoginGUI()).view);
        Client.frame.pack();
    }

    static void login(String username, String password) throws IOException, ClassNotFoundException {
        /* Prepare messageMap for login. */
        HashMap<String, String> messageMap = new HashMap<>();
        messageMap.put("username", username);
        messageMap.put("password", password);
        /* Send message map. */
        Client.writer.writeObject(messageMap);
        Client.writer.flush();
        /* Receive ack of success or failure. */
        messageMap = (HashMap<String, String>) Client.reader.readObject();
        /* If success, show chat page. */
        if (messageMap.get("ok") != null) {
            Client.frame.setContentPane((new ChatGUI()).view);
            Client.frame.pack();
        }
    }

    static boolean sendMessage(String message) {
        /* Build message map. */
        HashMap<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "message");
        messageMap.put("message", message);
        /* Send message map over socket. */
        try {
            Client.writer.writeObject(messageMap);
            Client.writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static Message getMessage() {
        /* Wait and receive a message map from the server. */
        HashMap<String, String> messageMap;
        try {
            messageMap = (HashMap<String, String>) Client.reader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        /* Parse message map. */
        return new Message(messageMap.get("time"), messageMap.get("sender"), messageMap.get("message"));
    }
}
