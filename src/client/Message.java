package client;

import java.time.LocalDateTime;

class Message {

    Message(String time, String sender, String message) {
        this.time = LocalDateTime.now().toString();
        this.sender = sender;
        this.message = message;
    }

    final String time;
    final String sender;
    final String message;
}
