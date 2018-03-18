package server;

import java.io.IOException;
import java.util.Random;

public class Game implements AutoCloseable {
    private Player playerOne, playerTwo;
    public static final byte FIRST = 0, SECOND = 1;

    private Random random;

    public Game(Player playerOne, Player playerTwo) throws IllegalArgumentException {
        if (playerOne == null || playerTwo == null)
            throw new IllegalArgumentException("One of the Player objects was null.");
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        this.random = new Random();
    }

    /**
     * Commences a new game and blocks until it is finished.
     *
     * @throws IOException if a socket connection fails during the game
     */
    public void start() throws IOException {
        // DECIDING WHO GOES FIRST
        byte turn = (byte) (random.nextBoolean() ? FIRST : SECOND);

        // if turn is 0, keep playerOne and playerTwo the same
        // otherwise, switch them
        if (turn != 0) {
            Player temp = playerOne;
            playerOne = playerTwo;
            playerTwo = temp;
        }

        // TELLING PLAYERS WHO GOES FIRST
        try {
            playerOne.sendTurn(FIRST);
            playerTwo.sendTurn(SECOND);
        } catch (IOException e) {
            throw new IOException("Couldn't send turn to one of the players.\nException: " + e.getMessage());
        }

        // PLAYER 1 ROLLS, PLAYER 2 RECEIVES
        byte[] roll = null;
        try {
            roll = playerOne.roll();
        } catch (IOException e) {
            throw new IOException("Couldn't get playerOne's roll.\nException: " + e.getMessage());
        }
        try {
            playerTwo.sendOpponentThrow(roll);
        } catch (IOException e) {
            throw new IOException("Couldn't send playerOne's roll to playerTwo.\nException: " + e.getMessage());
        }

        // PLAYER 2 ROLLS, PLAYER 1 RECEIVES
        try {
            roll = playerTwo.roll();
        } catch (IOException e) {
            throw new IOException("Couldn't get playerTwo's roll.\nException: " + e.getMessage());
        }
        try {
            playerOne.sendOpponentThrow(roll);
        } catch (IOException e) {
            throw new IOException("Couldn't send playerTwo's roll to playerOne.\nException: " + e.getMessage());
        }

        // PLAYER 1 RE-THROWS, PLAYER 2 RECEIVES
        try {
            roll = playerOne.reroll();
        } catch (IOException e) {
            throw new IOException("Problem re-rolling playerOne's hand.\nException: " + e.getMessage());
        }
        try {
            playerTwo.sendOpponentThrow(roll);
        } catch (IOException e) {
            throw new IOException("Couldn't send playerOne's re-roll to playerTwo.\nException: " + e.getMessage());
        }

        // PLAYER 2 RE-THROWS, PLAYER 1 RECEIVES
        try {
            roll = playerTwo.reroll();
        } catch (IOException e) {
            throw new IOException("Problem re-rolling playerTwo's hand.\nException: " + e.getMessage());
        }
        try {
            playerOne.sendOpponentThrow(roll);
        } catch (IOException e) {
            throw new IOException("Couldn't send playerTwo's re-roll to playerOne.\nException: " + e.getMessage());
        }

        // DETERMINE WINNER AND SEND IT TO BOTH
        byte winner = determineWinner();
        playerOne.sendTurn(winner);
        playerTwo.sendTurn(winner);
    }

    /**
     * Incredibly complicated algorithm for determining which combination yields a higher score
     * @return the winner
     */
    private byte determineWinner() {
        byte sum1 = 0, sum2 = 0;
        byte[] player1 = playerOne.getHand(),
               player2 = playerTwo.getHand();

        for (int i = 0; i < player1.length; i++) {
            sum1 += player1[i];
            sum2 += player2[i];
        }
        // if sums are equal, player 1 still wins
        return (sum1 > sum2 ? FIRST : SECOND);
    }


    @Override
    public void close() throws IOException {
        playerOne.close();
        playerTwo.close();
    }
}