package clientold;

import serverold.Player;
import serverold.Server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.lang.Integer;

public class ClientMain {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println(
                    "Usage: java Player <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
                Socket server = new Socket(hostName, portNumber);
                Player player = new Player(server);
        ) {
            byte order = player.readByte(); // read 1

            byte[] myThrow = null;
            if (order == Server.FIRST) {
                myThrow = throwDice(player); // sends 1, reads 5
                readOpponentThrow(player); // reads 5
                rethrow(player, myThrow); // sends 5, reads 5
                readOpponentThrow(player); // reads 5
            }
            else {
                readOpponentThrow(player); // reads 5
                myThrow = throwDice(player); // sends 1, reads 5
                readOpponentThrow(player); // reads 5
                rethrow(player, myThrow); // sends 5, reads 5
            }

            if (player.readByte() == order) System.out.println("You've won!");
            else System.out.println("You LOST!");

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }

    public static void readOpponentThrow(Player player) throws IOException {
        byte[] theirThrow = player.read();
        System.out.printf("Your opponent threw: %d  %d  %d  %d  %d\n",
                theirThrow[0], theirThrow[1], theirThrow[2], theirThrow[3], theirThrow[4]);
    }

    public static byte[] throwDice(Player player) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hit enter when you're ready to throw...");
        scanner.nextLine();
        player.write((byte) 1);

        byte[] myThrow = player.read();
        System.out.printf("Your throw was: %d  %d  %d  %d  %d\n",
                myThrow[0], myThrow[1], myThrow[2], myThrow[3], myThrow[4]);

        return myThrow;
    }

    public static void rethrow(Player player, byte[] myThrow) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the indices you want to re-throw separated by spaces: ");
        String rethrow = scanner.nextLine();
        String [] parts = rethrow.split(" ");
        for (String part : parts) {
            myThrow[Integer.parseInt(part)] = -1;
        }
        player.write(myThrow);

        byte[] myThrow2 = player.read();
        System.out.printf("Your throw was: %d  %d  %d  %d  %d\n",
                myThrow2[0], myThrow2[1], myThrow2[2], myThrow2[3], myThrow2[4]);
    }
}
