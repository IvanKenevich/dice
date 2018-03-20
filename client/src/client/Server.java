package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Server {
    private static Server instance = null;

    private Socket socket;

    private InputStream in;
    private OutputStream out;

    // Exists only to defeat instantiation
    private Server() {}

    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    /**
     * @param host the host of the game server
     * @param port the port of the game server
     * @return -1 if the socket connection fails, 0 if not
     */
    public int initialize(String host, int port) {
        try {
            socket = new Socket(host, port);
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            return -1;
        }
        return 0;
    }

    public void write(byte data) throws IOException {
        out.write(data);
    }

    public void write(byte[] data) throws IOException {
        out.write(data);
    }

    public byte readByte() throws IOException {
        return read(1)[0];
    }

    public byte[] read() throws IOException {
        int length = 5;
        byte[] bytes = new byte[length];
        in.read(bytes, 0, length);
        return bytes;
    }

    public byte[] read(int length) throws IOException {
        byte[] bytes = new byte[length];
        in.read(bytes, 0, length);
        return bytes;
    }
}
