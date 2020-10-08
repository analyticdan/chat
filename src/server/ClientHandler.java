package server;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class ClientHandler {
    private Socket socket;

    private boolean running;
    private BlockingQueue<HashMap<String, String>> outQueue;

    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Thread writer, reader;

    private String username;

    ClientHandler(Socket socket) {
        this.socket = socket;
        this.running = true;
        this.outQueue = new LinkedBlockingQueue<>();

        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e){
            this.shutdown();
            return;
        }

        this.writer = new Thread(() -> {
            while (this.running) {
                try {
                    writeMessage();
                } catch (IOException e) {
                    if (Server.logVerbose) {
                        e.printStackTrace();
                    }
                    this.shutdown();
                } catch (InterruptedException e) {
                    // Do nothing.
                }
            }
        });

        this.reader = new Thread(() -> {
            while (this.running) {
                try {
                    readMessage();
                } catch (IOException | ClassNotFoundException e) {
                    if (Server.logVerbose) {
                        e.printStackTrace();
                    }
                    this.shutdown();
                }
            }
        });
    }

    void start() {
        /* Get the client to login. */
        while (this.username == null) {
            try {
                this.verify();
            } catch (IOException | ClassNotFoundException e) {
                if (Server.logVerbose) {
                    e.printStackTrace();
                }
                this.shutdown();
                return;
            }
        }
        /* Log. */
        if (Server.log) {
            Server.logger.logNewLogin(this.username);
        }
        /* Start sending and receiving messages. */
        this.reader.start();
        this.writer.start();
    }

    void addToOutQueue(HashMap<String, String> m) {
        this.outQueue.offer(m);
    }

    private void verify() throws IOException, ClassNotFoundException {
        HashMap<String, String> messageMap = (HashMap<String, String>) this.ois.readObject();
        String username = messageMap.get("username");
        String password = messageMap.get("password");

        messageMap.clear();
        if (Server.isValidUser(username, password)) {
            messageMap.put("ok", "not-null");
            this.username = username;
        }

        this.oos.writeObject(messageMap);
        this.oos.flush();
    }

    private void readMessage() throws IOException, ClassNotFoundException {
        /* Fetch message map from socket and write to broadcast queue. */
        HashMap<String, String> messageMap = (HashMap<String, String>) this.ois.readObject();
        String message = messageMap.get("message");
        if (message != null) {
            Server.broadcastQueue.offer(new Message(this.username, message));
        }
    }

    private void writeMessage() throws IOException, InterruptedException {
        /* Fetch message maps from the queue and write them to the socket. */
        HashMap<String, String> messageMap = this.outQueue.take();
        this.oos.writeObject(messageMap);
        this.oos.flush();
    }

    private synchronized void shutdown() {
        if (this.running) {
            /* Remove this from list of client handlers. */
            synchronized (Server.clientHandlers) {
                Server.clientHandlers.remove(this);
            }
            /* Close socket. */
            try {
                this.socket.close();
            } catch (IOException e) {
                // Do nothing.
            }
            /* Complete shutdown. */
            this.running = false;
            /* Log. */
            if (Server.log) {
                Server.logger.logEndedConnection(this.socket.getInetAddress());
            }
        }
    }
}
