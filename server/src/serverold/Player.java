package serverold;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ServerSocket;

public class Player implements AutoCloseable {
    private Socket socket;
    private InputStream is;
    private OutputStream os;

    /**
     * @param server the serverold socket to listen to for connections
     * @throws IOException when the socket fails to connect
     */
    public Player(ServerSocket server) throws IOException {
        socket = server.accept();

        is = socket.getInputStream();
        os = socket.getOutputStream();
    }

    public Player(Socket socket) throws IOException {
        this.socket = socket;

        is = socket.getInputStream();
        os = socket.getOutputStream();
    }

    public void write(byte data) throws IOException {
        os.write(data);
    }

    public void write(byte[] data) throws IOException {
        os.write(data);
    }

    public byte readByte() throws IOException {
        return read(1)[0];
    }

    public byte[] read() throws IOException {
        int length = 5;
        byte[] bytes = new byte[length];
        is.read(bytes, 0, length);
        return bytes;
    }

    public byte[] read(int length) throws IOException {
        byte[] bytes = new byte[length];
        is.read(bytes, 0, length);
        return bytes;
    }

    public void close() throws IOException {
        is.close();
        os.close();

        socket.close();
    }
}
