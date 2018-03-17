package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientExample {

    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println(
                    "Usage: java Player <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        byte [] data = new byte[1];
        data[0] = 0b111;

        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                OutputStream out = echoSocket.getOutputStream();
                InputStream in = echoSocket.getInputStream();
        ) {
            out.write(data);
            System.out.println("Read in "+in.read(data)+" bytes. Here they are: ");
            System.out.println(Byte.toString(data[0]));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}