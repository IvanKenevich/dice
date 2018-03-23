package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingClientMain {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Dice");
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[1];
        frame.setSize(gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(new DicePanel(frame.getSize()));
        frame.setVisible(true);
    }

    private static class DicePanel extends JPanel {
        final int SCREEN_WIDTH, SCREEN_HEIGHT;

        private JTextField hostField, portField;
        private JButton connectButton;

        private GameState gameState;

        public DicePanel(Dimension screen) {
            super(null);
            SCREEN_WIDTH = (int) screen.getWidth();
            SCREEN_HEIGHT = (int) screen.getHeight();

            initGUI();
            gameState = GameState.getInstance();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            switch (gameState.getCurrentState()) {
                case WAITING_CONNECT_INPUT:
                    drawConnectScreen(g);
                    break;
                case RECEIVING_ORDER:
                    break;
                case DISPLAYING_ORDER:
                    break;
                case WAITING_MYTHROW_INPUT:
                    break;
                case RECEIVING_MYTHROW:
                    break;
                case DISPLAYING_MYTHROW:
                    break;
                case RECEIVING_THEIRTHROW:
                    break;
                case DISPLAYING_THEIRTHROW:
                    break;
                case WAITING_MYRETHROW_INPUT:
                    break;
                case RECEIVING_MYRETHROW:
                    break;
                case DISPLAYING_MYRETHROW:
                    break;
                case RECEIVING_THEIRRETHROW:
                    break;
                case DISPLAYING_THEIRRETHROW:
                    break;
                case RECEIVING_WINNER:
                    break;
                case DISPLAYING_WINNER:
                    break;
                default:
                    break;
            }
        }

        private void initGUI() {
            hostField = new JTextField("ada.cs.pdx.edu");
            hostField.setSize(SCREEN_WIDTH, SCREEN_HEIGHT / 6);
            hostField.setLocation(SCREEN_WIDTH / 2 - hostField.getWidth() / 2, SCREEN_HEIGHT / 6);
            hostField.setHorizontalAlignment(JTextField.CENTER);
            add(hostField);
            hostField.setVisible(false);

            portField = new JTextField("1488");
            portField.setSize(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 6);
            portField.setLocation(SCREEN_WIDTH / 2 - portField.getWidth() / 2, (int) (hostField.getY() + hostField.getHeight() * 1.5));
            portField.setHorizontalAlignment(JTextField.CENTER);
            add(portField);
            portField.setVisible(false);

            Font hostport = new Font(Font.MONOSPACED, Font.PLAIN, hostField.getHeight() / 2);
            hostField.setFont(hostport);
            portField.setFont(hostport);

            connectButton = new JButton("GO");
            connectButton.setSize(SCREEN_WIDTH / 4, SCREEN_HEIGHT / 6);
            connectButton.setLocation(SCREEN_WIDTH / 2 - connectButton.getWidth() / 2, (int) (portField.getY() + portField.getHeight() * 1.5));
            connectButton.setBackground(Color.GRAY);
            connectButton.setFont(hostport);
            connectButton.addActionListener(e -> {
                hostField.setVisible(false);
                portField.setVisible(false);
                connectButton.setVisible(false);

                gameState.setHost(hostField.getText());
                gameState.setPort(Integer.parseInt(portField.getText()));
            });
            add(connectButton);
            connectButton.setVisible(false);
        }

        private void drawConnectScreen(Graphics g) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            hostField.setVisible(true);
            portField.setVisible(true);
            connectButton.setVisible(true);
        }
    }
}
