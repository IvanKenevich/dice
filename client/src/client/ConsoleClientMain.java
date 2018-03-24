package client;

import java.util.Scanner;

public class ConsoleClientMain {
    private static ServerCommunicator serverCommunicator;
    private static GameState gameState;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        serverCommunicator = ServerCommunicator.getInstance();
        gameState = GameState.getInstance();

        start();
    }

    private static void start() {
        do {
            switch (gameState.getCurrentState()) {
                case WAITING_CONNECT_INPUT:
                    System.out.print("Enter host (ex: ada.cs.pdx.edu): ");
                    String host = scanner.nextLine();
                    System.out.print("Enter port (ex: 1488): ");
                    int port = Integer.parseInt(scanner.nextLine());
                    gameState.setHost(host);
                    gameState.setPort(port);
                    //gameState.step();
                    break;
                case DISPLAYING_ORDER:
                    System.out.println("You're going " +
                            (gameState.getMyOrder() == GameState.FIRST ? "FIRST" : "SECOND"));
                    //gameState.step();
                    break;
                case WAITING_MYTHROW_INPUT:
                    System.out.print("Hit enter when you are ready to throw...");
                    scanner.nextLine();
                    //gameState.step();
                    break;
                case DISPLAYING_MYTHROW:
                    byte [] myThrow = gameState.getMyHand();
                    System.out.printf("Your throw was: %d  %d  %d  %d  %d\n",
                            myThrow[0], myThrow[1], myThrow[2], myThrow[3], myThrow[4]);
                    //gameState.step();
                    break;
                case DISPLAYING_THEIRTHROW:
                    byte[] theirThrow = gameState.getOpponentHand();
                    System.out.printf("Your opponent threw: %d  %d  %d  %d  %d\n",
                            theirThrow[0], theirThrow[1], theirThrow[2], theirThrow[3], theirThrow[4]);
                    //gameState.step();
                    break;
                case WAITING_MYRETHROW_INPUT:
                    System.out.print("Enter the indices you want to re-throw separated by spaces: ");
                    String rethrow = scanner.nextLine();
                    String[] parts = rethrow.split(" ");
                    byte[] result = new byte[5];
                    for (String part : parts) {
                        result[Integer.parseInt(part)] = -1;
                    }
                    gameState.setMyMask(result);
                    //gameState.step();
                    break;
                case DISPLAYING_MYRETHROW:
                    myThrow = gameState.getMyHand();
                    System.out.printf("Your re-throw was: %d  %d  %d  %d  %d\n",
                            myThrow[0], myThrow[1], myThrow[2], myThrow[3], myThrow[4]);
                    //gameState.step();
                    break;
                case DISPLAYING_THEIRRETHROW:
                    theirThrow = gameState.getOpponentHand();
                    System.out.printf("Your opponent re-threw: %d  %d  %d  %d  %d\n",
                            theirThrow[0], theirThrow[1], theirThrow[2], theirThrow[3], theirThrow[4]);
                    //gameState.step();
                    break;
                case DISPLAYING_WINNER:
                    System.out.println((gameState.getWinner() == gameState.getMyOrder() ? "You won!" : "You lost!"));
                    //gameState.step();
                    break;
                default:
                    serverCommunicator.update();
                    break;
            }
        } while(gameState.step() != -1);
    }
}