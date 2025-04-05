import java.awt.*;
import javax.swing.*;

public class GameWindow {
    private final JFrame frame;
    private StartMenu startMenu;
    private SnakeGame snakeGame;

    public GameWindow() {
        frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(true);

        startMenu = new StartMenu(this);
        frame.add(startMenu);
        frame.setVisible(true);
    }

    // Updated to include difficulty
    public void startGame(Color snakeColor, Color foodColor, int speed, String difficulty) {
        frame.remove(startMenu);
        snakeGame = new SnakeGame(this, snakeColor, foodColor, speed, difficulty);
        frame.add(snakeGame);
        frame.revalidate();
        frame.repaint();
        snakeGame.requestFocus();
    }

    public void showMenu() {
        frame.remove(snakeGame);
        startMenu = new StartMenu(this);
        frame.add(startMenu);
        frame.revalidate();
        frame.repaint();
    }
}
