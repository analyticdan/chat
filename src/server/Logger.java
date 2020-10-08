package server;

import java.net.InetAddress;

class Logger {

    void logNewConnection(InetAddress address) {
        System.out.println("New connection from: " + address);
    }

    void logEndedConnection(InetAddress address) {
        System.out.println("Ended connection from: " + address);
    }

    void logNewLogin(String username) {
        System.out.println(String.format("\"%s\" has logged in", username));
    }

    void logNewMessage(Message m) {
        System.out.println(String.format("[%s] \"%s\" sent a message: %s", m.time, m.sender, m.message));
    }
}
