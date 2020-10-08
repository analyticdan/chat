package client;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class LoginGUI {
    LoginGUI() {
        this.usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    LoginGUI.this.passwordField.requestFocus();
                }
            }
        });
        this.passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    LoginGUI.this.login();
                }
            }
        });
        this.loginButton.addActionListener(a -> this.login());
    }

    private void login() {
        String username = this.usernameField.getText();
        String password = new String(this.passwordField.getPassword()); // This is insecure.
        try {
            Client.login(username, password);
        } catch (IOException | SecurityException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    JPanel view;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
}
