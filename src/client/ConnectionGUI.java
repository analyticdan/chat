package client;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.UnknownHostException;

public class ConnectionGUI {

    ConnectionGUI() {
        this.ipField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ConnectionGUI.this.portField.requestFocus();
                }
            }
        });
        this.portField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ConnectionGUI.this.connect();
                }
            }
        });
        this.connectButton.addActionListener(a -> this.connect());
    }

    private void connect() {
        try {
            Client.connect(ipField.getText(), Integer.parseInt(portField.getText()));
        } catch (UnknownHostException e) {
            this.messageLabel.setText("Please enter a valid internet address.");
        } catch (IllegalArgumentException e) {
            this.messageLabel.setText("Please enter a valid port number.");
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    JPanel view;
    private JTextField ipField;
    private JTextField portField;
    private JButton connectButton;
    private JLabel messageLabel;
}
