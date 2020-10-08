package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {
    static final BlockingQueue<Message> broadcastQueue = new LinkedBlockingQueue<>();

    static final Set<ClientHandler> clientHandlers = new HashSet<>();

    static final Logger logger = new Logger();
    static final boolean logVerbose = false;
    static final boolean log = true;

    public static void main(String[] args) throws IOException {
        Thread broadcastThread = new Thread(() -> {
            while (true) {
                /* Block, waiting for a new broadcast message. */
                Message message;
                try {
                    message = Server.broadcastQueue.take();
                } catch (InterruptedException e) {
                    continue;
                }
                /* Log. */
                Server.logger.logNewMessage(message);
                /* Populate serializable map (to be send to clients). */
                HashMap<String, String> messageMap = new HashMap<>();
                messageMap.put("time", message.time);
                messageMap.put("sender", message.sender);
                messageMap.put("message", message.message);
                /* Put map in queue to be sent to clients. */
                synchronized (Server.clientHandlers) {
                    for (ClientHandler clientHandler : Server.clientHandlers) {
                        clientHandler.addToOutQueue(messageMap);
                    }
                }
            }
        });
        broadcastThread.start();

        ServerSocket serverSocket = new ServerSocket(8080);
        while (true) {
            /* Wait for clients to connect. */
            Socket client = serverSocket.accept();
            /* Log. */
            if (Server.log) {
                Server.logger.logNewConnection(client.getInetAddress());
            }
            /* Initialize client handler. */
            ClientHandler clientHandler = new ClientHandler(client);
            synchronized (Server.clientHandlers) {
                Server.clientHandlers.add(clientHandler);
            }
            new Thread(clientHandler::start).start();
        }
    }

    static boolean isValidUser(String username, String password) {
        // TODO: actually validate user.
        return username != null && password != null;
    }
}
