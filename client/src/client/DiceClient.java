package client;

public interface DiceClient {
    int connect(String host, int port);
    byte[] throwDice();
    byte[] rethrowDice(byte[] mask);
    byte[] getOpponentThrow();
    byte[] getOpponentRethrow();
}
