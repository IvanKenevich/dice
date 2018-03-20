package client;

import java.io.IOException;
import java.util.Scanner;

public class ConsoleClientMain {

    private static final ConsoleClientMain.GameState[] playerOneSequence = {
            GameState.THROW, GameState.READ, GameState.RETHROW, GameState.READRETHROW, GameState.FINAL
    };

    private static final ConsoleClientMain.GameState[] playerTwoSequence = {
            GameState.READ, GameState.THROW, GameState.READRETHROW, GameState.RETHROW, GameState.FINAL
    };

    private static final byte FIRST = 0, SECOND = 1;
    private static byte myTurn;

    private static Server server;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println(
                    "Usage: java ConsoleClientMain <host name> <port number>");
            System.exit(1);
        }

        server = Server.getInstance();

        connect(args[0], Integer.parseInt(args[1]));
        start();
    }

    private static void connect(String host, int port) {
        server.initialize(host, port);
    }

    private static void start() {
        ConsoleClientMain.GameState[] mySequence = chooseSequence();
        if (mySequence != null) {
            for (ConsoleClientMain.GameState state : mySequence) {
                switch (state) {
                    case THROW:
                        throwDice();
                        break;
                    case READ:
                        getOpponentThrow();
                        break;
                    case RETHROW:
                        rethrowDice();
                        break;
                    case READRETHROW:
                        getOpponentReThrow();
                        break;
                    case FINAL:
                        getWinner();
                        break;
                }
            }
        }
    }

    private static void throwDice() {
        try {
            System.out.println("Hit enter when you're ready to throw...");
            scanner.nextLine();
            server.write((byte) 1);

            byte[] myThrow = server.read();
            System.out.printf("Your throw was: %d  %d  %d  %d  %d\n",
                    myThrow[0], myThrow[1], myThrow[2], myThrow[3], myThrow[4]);
        } catch (IOException e) {
            System.err.print("Problem throwing dice: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void getOpponentThrow() {
        try {
            byte[] theirThrow = server.read();
            System.out.printf("Your opponent threw: %d  %d  %d  %d  %d\n",
                    theirThrow[0], theirThrow[1], theirThrow[2], theirThrow[3], theirThrow[4]);
        } catch (IOException e) {
            System.err.print("Problem reading opponent's throw: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void rethrowDice() {
        try {
            System.out.print("Enter the indices you want to re-throw separated by spaces: ");
            String rethrow = scanner.nextLine();
            String[] parts = rethrow.split(" ");
            byte[] result = new byte[5];
            for (String part : parts) {
                result[Integer.parseInt(part)] = -1;
            }
            server.write(result);

            result = server.read();
            System.out.printf("Your throw was: %d  %d  %d  %d  %d\n",
                    result[0], result[1], result[2], result[3], result[4]);
        } catch (IOException e) {
            System.err.print("Problem rethrowing dice: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void getOpponentReThrow() {
        try {
            byte[] theirThrow = server.read();
            System.out.printf("Your opponent re-threw: %d  %d  %d  %d  %d\n",
                    theirThrow[0], theirThrow[1], theirThrow[2], theirThrow[3], theirThrow[4]);
        } catch (IOException e) {
            System.err.print("Problem reading opponent's re-throw: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void getWinner() {
        try {
            if (server.readByte() == myTurn) System.out.println("You've won!");
            else System.out.println("You LOST!");
        } catch (IOException e) {
            System.err.print("Problem getting game's winner: " + e.getMessage());
            System.exit(1);
        }
    }

    private static ConsoleClientMain.GameState[] chooseSequence() {
        try {
            System.out.println("Waiting for opponent to throw...");
            myTurn = server.readByte();
            if (myTurn == SECOND) System.out.println("Waiting for opponent to throw...");
            return myTurn == FIRST ? playerOneSequence : playerTwoSequence;
        } catch (IOException e) {
            System.err.print("Problem getting player's sequence: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    private enum GameState {
        THROW, READ, RETHROW, READRETHROW, FINAL
    }
}