package serverold;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println(
                    "Usage: java Server <port number>");
            System.exit(1);
        }

        try (
                Server server = new Server(Integer.parseInt(args[0]));
        ) {
            server.listen();

            server.startGame();

        } catch (IOException e) {
            System.err.println("Exception caught when trying to listen on port "
                    + Integer.parseInt(args[0]) + " or listening for a connection");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
