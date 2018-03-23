package client;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class GameState {
    private static GameState instance = null;

    private static final int HAND_SIZE = 5;

    public static final byte FIRST = 0, SECOND = 1;
    private byte myOrder;
    private byte winner;

    private State currentState;
    private Queue<State> mySequence;

    private byte[] myHand;
    private byte[] myMask; // for rethrowing
    private byte[] opponentHand;

    private String host;
    private int port;

    /**
     * WAITING means that you expect some input from the player
     * RECEIVING means that you are reading data from the server
     * DISPLAYING means that you present data to the player
     *
     * SENDING is NOT a word, because it doesn't require a 'change of scene'
     * a ServerCommunicator will do 'SENDING' inside of RECEIVING
     *
     * ... i guess sending can be added to modularize the code in ServerCommunicator
     * and it can just be ignored by the 'front end'
      */
    public enum State {
        WAITING_CONNECT_INPUT, RECEIVING_ORDER, DISPLAYING_ORDER,           // CONNECT
        WAITING_MYTHROW_INPUT, RECEIVING_MYTHROW, DISPLAYING_MYTHROW,       // THROW
        RECEIVING_THEIRTHROW, DISPLAYING_THEIRTHROW,                        // READ
        WAITING_MYRETHROW_INPUT, RECEIVING_MYRETHROW, DISPLAYING_MYRETHROW, // RETHROW
        RECEIVING_THEIRRETHROW, DISPLAYING_THEIRRETHROW,                    // READ RETHROW
        RECEIVING_WINNER, DISPLAYING_WINNER                                 // FINAL
    }
    private static final State[] initialSequence = {
            State.WAITING_CONNECT_INPUT, State.RECEIVING_ORDER, State.DISPLAYING_ORDER
    };
    private static final State[] playerOneSequence = {
            State.WAITING_MYTHROW_INPUT, State.RECEIVING_MYTHROW, State.DISPLAYING_MYTHROW,
            State.RECEIVING_THEIRTHROW, State.DISPLAYING_THEIRTHROW,
            State.WAITING_MYRETHROW_INPUT, State.RECEIVING_MYRETHROW, State.DISPLAYING_MYRETHROW,
            State.RECEIVING_THEIRRETHROW, State.DISPLAYING_THEIRRETHROW,
            State.RECEIVING_WINNER, State.DISPLAYING_WINNER
    };
    private static final State[] playerTwoSequence = {
            State.RECEIVING_THEIRTHROW, State.DISPLAYING_THEIRTHROW,
            State.WAITING_MYTHROW_INPUT, State.RECEIVING_MYTHROW, State.DISPLAYING_MYTHROW,
            State.RECEIVING_THEIRRETHROW, State.DISPLAYING_THEIRRETHROW,
            State.WAITING_MYRETHROW_INPUT, State.RECEIVING_MYRETHROW, State.DISPLAYING_MYRETHROW,
            State.RECEIVING_WINNER, State.DISPLAYING_WINNER
    };
    
    // Exists only to defeat instantiation
    private GameState() {}
    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
            instance.setInitialSequence();
        }
        return instance;
    }

    private void setInitialSequence() {
        mySequence = new LinkedList<>(Arrays.asList(initialSequence));
        step();
    }

    private int chooseSequence(byte order) {
        if (order == FIRST) {
            mySequence.addAll(new LinkedList<>(Arrays.asList(playerOneSequence)));
        }
        else if (order == SECOND) {
            mySequence.addAll(new LinkedList<>(Arrays.asList(playerTwoSequence)));
        }
        else return -1;
        return 0;
    }

    public int step() {
        try {
            currentState = mySequence.remove();
        } catch (NoSuchElementException e) {
            return -1;
        }
        return 0;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setMyHand(byte [] values) {
        myHand = values;
    }

    public byte [] getMyHand() {
        return myHand;
    }

    public void setOpponentHand(byte [] values) {
        opponentHand = values;
    }

    public byte [] getOpponentHand() {
        return opponentHand;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public byte getMyOrder() {
        return myOrder;
    }

    public void setMyOrder(byte myOrder) {
        this.myOrder = myOrder;
        chooseSequence(myOrder);
    }

    public int getHAND_SIZE() {
        return HAND_SIZE;
    }

    public byte[] getMyMask() {
        return myMask;
    }

    public void setMyMask(byte[] myMask) {
        this.myMask = myMask;
    }

    public byte getWinner() {
        return winner;
    }

    public void setWinner(byte winner) {
        this.winner = winner;
    }
}
