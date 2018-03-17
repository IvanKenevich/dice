package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

public class Player implements AutoCloseable {
    // connection to the actual player
    private Socket client;

    private byte[] hand;

    private InputStream in;
    private OutputStream out;

    private Random random;

    private final int HAND_SIZE = 5;

    /**
     * @param client the socket to send and receive information from
     * @throws IOException if the socket connection fails
     */
    public Player(Socket client) throws IOException {
        this.client = client;

        hand = new byte[HAND_SIZE];

        in = client.getInputStream();
        out = client.getOutputStream();

        random = new Random();
    }

    /**
     * Waits for the player to send a byte, rolls the Player's hand,
     * and sends the result to the socket.
     *
     * @return the result of the roll
     */
    public byte[] roll() throws IOException {
        readByte();

        rollDice();
        out.write(hand);

        return hand;
    }

    /**
     * Waits for the player to send the indices of the dice they wish to reroll,
     * rerolls them, and sends the result to the socket
     *
     * @return the result of the reroll
     */
    public byte[] reroll() throws IOException {
        try {
            rerollDice(read());
        } catch (IOException e) {
            throw new IOException("Error while reading to socket during reroll:" + e.getMessage());
        }
        try {
            out.write(hand);
        } catch (IOException e) {
            throw new IOException("Error while writing to socket during reroll:" + e.getMessage());
        }

        return hand;
    }

    /**
     * Sends the Player's turn to the socket.
     *
     * @param turn which turn it is
     */
    public void sendTurn(byte turn) throws IOException {
        out.write(turn);
    }

    public void sendOpponentThrow(byte[] opponentThrow) throws IOException {
        out.write(opponentThrow);
    }

    private byte readByte() throws IOException {
        return read(1)[0];
    }

    private byte[] read() throws IOException {
        int length = HAND_SIZE;
        byte[] bytes = new byte[length];
        in.read(bytes, 0, length);
        return bytes;
    }

    private byte[] read(int length) throws IOException {
        byte[] bytes = new byte[length];
        in.read(bytes, 0, length);
        return bytes;
    }

    private void rollDice() {
        for (int i = 0; i < hand.length; i++) {
            hand[i] = (byte) (random.nextInt(5) + 1);
        }
    }

    private void rerollDice(byte[] flags) {
        for (int i = 0; i < hand.length; i++) {
            if (flags[i] == -1) hand[i] = (byte) (random.nextInt(5) + 1);
        }
    }

    public byte[] getHand() {
        return hand;
    }

    public void close() throws IOException {
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            throw new IOException("Failed to close the input and/or output stream for a Player.\nException: " + e.getMessage());
        }
    }
}
