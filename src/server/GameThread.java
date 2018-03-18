package server;

import java.io.IOException;

public class GameThread extends Thread{
    private Player playerOne, playerTwo;

    public GameThread(Player playerOne, Player playerTwo) {
        super("Game Session Thread");
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public void run() {
        try (
                Game game = new Game(playerOne, playerTwo);
        ) {
            game.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
