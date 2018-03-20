package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server implements AutoCloseable {
    private int port;
    private ServerSocket socket;
    private Stack<Player> players;

    private Logger logger;

    public Server (int port) {
        this.port = port;

        this.logger = Logger.getLogger(Server.class.getName());
        logger.setLevel(Level.INFO);

        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
        players = new Stack<>();
    }

    public void listen() {
        while (true) {
            try {
               players.push(new Player(socket.accept()));
            } catch (IOException e) {
                logger.severe(e.getMessage());
            }
            if (players.size() == 2) new GameThread(players.pop(), players.pop()).start();
        }
    }


    @Override
    public void close() throws IOException {
        socket.close();
    }
}
