package serverold;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

public class Server implements AutoCloseable {
    private int port;
    private Random random;
    private ServerSocket serverSocket;
    private Player playerOne, playerTwo;

    public static final byte FIRST = 0, SECOND = 1;

    public Server(int port) throws IOException {
        this.port = port;

        playerOne = playerTwo = null;

        serverSocket = new ServerSocket(this.port);

        random = new Random();
    }

    public void listen() throws IOException {
        playerOne = new Player(serverSocket);
        playerTwo = new Player(serverSocket);
    }

    public void startGame() throws IOException {
        // DECIDING WHO GOES FIRST
        byte turn = random.nextBoolean() ? FIRST : SECOND;

        // if turn is 0, keep playerOne and playerTwo the same
        // otherwise, switch them
        if (turn != 0) {
            Player temp = playerOne;
            playerOne = playerTwo;
            playerTwo = temp;
        }
        // TELLING PLAYERS WHO GOES FIRST
        playerOne.write(FIRST);
        playerTwo.write(SECOND);

        // PLAYER 1 THROWS AND PLAYER 2 RECEIVES
        playerOne.readByte();
        byte[] result = new byte[5];
        for (int i = 0; i < result.length; i++) {
            result[i] = -1;
        }
        rollDice(result);
        playerOne.write(result);
        playerTwo.write(result);

        // PLAYER 2 THROWS AND PLAYER 1 RECEIVES
        playerTwo.readByte();
        for (int i = 0; i < result.length; i++) {
            result[i] = -1;
        }
        rollDice(result);
        playerTwo.write(result);
        playerOne.write(result);

        // FIND OUT WHICH ONES PLAYER 1 WANTS TO RE-THROW, SEND RESULT TO BOTH
        byte[] final_result1  = playerOne.read();
        rollDice(final_result1);
        playerOne.write(final_result1);
        playerTwo.write(final_result1);

        // FIND OUT WHICH ONES PLAYER 2 WANTS TO RE-THROW, SEND RESULT TO BOTH
        byte[] final_result2  = playerTwo.read();
        rollDice(final_result2);
        playerTwo.write(final_result2);
        playerOne.write(final_result2);

        // DETERMINE WHO WON
        byte winner = findWinner(final_result1, final_result2);
        playerOne.write(winner);
        playerTwo.write(winner);
    }

    @Override
    public void close() throws IOException {
        playerOne.close();
        playerTwo.close();

        serverSocket.close();
    }

    private void rollDice(byte[] hand) {
        for (int i = 0; i < hand.length; i++) {
            if (hand[i] == -1) hand[i] = (byte) (random.nextInt(5) + 1);
        }
    }

    private byte findWinner(byte[] player1, byte[] player2) {
        byte sum1 = 0, sum2 = 0;
        for (int i = 0; i < player1.length; i++) {
            sum1 += player1[i];
            sum2 += player2[i];
        }
        // if sums are equal, player 1 still wins
        return (sum1 > sum2 ? FIRST : SECOND);
    }
}