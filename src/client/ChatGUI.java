package client;

import javax.swing.*;
import java.awt.event.*;

public class ChatGUI {

    ChatGUI() {
        this.scrollBar = this.scrollPane.getVerticalScrollBar();
        this.textArea.setLineWrap(true);
        /* Set button press/enter key press to send message. */
        this.submitButton.addActionListener(e -> this.sendMessage());
        this.textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ChatGUI.this.sendMessage();
                }
            }
        });
        /* Start thread to receive new messages. */
        Thread reader = new Thread(() -> {
            while (true) {
                this.getMessage();
            }
        });
        reader.start();
    }

    private void sendMessage() {
        /* Get message text from text box.*/
        String message = textField.getText();
        if (message == null || message.equals("")) {
            return;
        }
        /* Clear text box on successful send. */
        if (Client.sendMessage(message)) {
            textField.setText("");
        }
    }

    private void getMessage() {
        Message message = Client.getMessage();
        if (message != null) {
            this.textArea.append(String.format("[%s] %s: %s\n", message.time, message.sender, message.message));
            this.scrollBar.setValue(this.scrollBar.getMaximum());
        }
    }

    JPanel view;
    private JScrollPane scrollPane;
    private JScrollBar scrollBar;
    private JTextArea textArea;
    private JTextField textField;
    private JButton submitButton;
}
