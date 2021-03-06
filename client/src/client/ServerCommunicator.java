package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerCommunicator {
    private static ServerCommunicator instance = null;

    private Socket socket;

    private InputStream in;
    private OutputStream out;

    private GameState gameState;
    // Exists only to defeat instantiation
    private ServerCommunicator() {}

    public static ServerCommunicator getInstance() {
        if (instance == null) {
            instance = new ServerCommunicator();
            instance.linkGameState();
        }
        return instance;
    }

    /**
     * @return -1 if the socket connection fails, 0 if not
     */
    public int initialize() {
        try {
            socket = new Socket(gameState.getHost(), gameState.getPort());
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            return -1;
        }
        return 0;
    }

    public void update() {
        switch (gameState.getCurrentState()) {
            case WAITING_CONNECT_INPUT:
                break;
            case RECEIVING_ORDER:
                initialize();
                gameState.setMyOrder(readByte());
                //gameState.step();
                break;
            case DISPLAYING_ORDER:
                break;
            case WAITING_MYTHROW_INPUT:
                break;
            case RECEIVING_MYTHROW:
                write((byte)1);
                gameState.setMyHand(read());
                //gameState.step();
                break;
            case DISPLAYING_MYTHROW:
                break;
            case RECEIVING_THEIRTHROW:
                gameState.setOpponentHand(read());
                //gameState.step();
                break;
            case DISPLAYING_THEIRTHROW:
                break;
            case WAITING_MYRETHROW_INPUT:
                break;
            case RECEIVING_MYRETHROW:
                write(gameState.getMyMask());
                gameState.setMyHand(read());
                //gameState.step();
                break;
            case DISPLAYING_MYRETHROW:
                break;
            case RECEIVING_THEIRRETHROW:
                gameState.setOpponentHand(read());
                //gameState.step();
                break;
            case DISPLAYING_THEIRRETHROW:
                break;
            case RECEIVING_WINNER:
                gameState.setWinner(readByte());
                //gameState.step();
                break;
            case DISPLAYING_WINNER:
                break;
            default:
                break;
        }
    }

    private void linkGameState() {
        gameState = GameState.getInstance();
    }

    private void write(byte data) {
        try {
            out.write(data);
        } catch (IOException e) {

        }
    }

    private void write(byte[] data) {
        try {
            out.write(data);
        } catch (IOException e) {

        }
    }

    private byte readByte() {
        return read(1)[0];
    }

    private byte[] read() {
        try {
            int length = gameState.getHAND_SIZE();
            byte[] bytes = new byte[length];
            in.read(bytes, 0, length);
            return bytes;
        } catch (IOException e) {

        }
        return new byte[gameState.getHAND_SIZE()];
    }

    private byte[] read(int length) {
        try {
            byte[] bytes = new byte[length];
            in.read(bytes, 0, length);
            return bytes;
        } catch (IOException e) {

        }
        return new byte[length];
    }
}
