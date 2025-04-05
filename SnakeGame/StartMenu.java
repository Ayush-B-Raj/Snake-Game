import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class StartMenu extends JPanel {
    private GameWindow gameWindow;
    private Color snakeColor = Color.GREEN;
    private Color foodColor = Color.RED;
    private int gameSpeed = 100; // Default speed (Medium)
    private String difficulty = "Medium"; // Default difficulty

    public StartMenu(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel title = new JLabel("SNAKE GAME", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 60));
        title.setForeground(Color.YELLOW);
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setLayout(new GridLayout(5, 1, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 300, 50, 300));

        // Difficulty Buttons
        JPanel difficultyPanel = new JPanel();
        difficultyPanel.setBackground(Color.BLACK);
        difficultyPanel.setLayout(new GridLayout(1, 3, 10, 0));

        JButton easyButton = createStyledButton("Easy");
        JButton mediumButton = createStyledButton("Medium");
        JButton hardButton = createStyledButton("Hard");

        easyButton.addActionListener(e -> {
            gameSpeed = 150;      // Slower
            difficulty = "Easy";
        });
        mediumButton.addActionListener(e -> {
            gameSpeed = 100;      // Normal
            difficulty = "Medium";
        });
        hardButton.addActionListener(e -> {
            gameSpeed = 60;       // Faster
            difficulty = "Hard";
        });

        difficultyPanel.add(easyButton);
        difficultyPanel.add(mediumButton);
        difficultyPanel.add(hardButton);

        // Start Game Button
        JButton startButton = createStyledButton("Start Game");
        startButton.addActionListener(e -> 
            gameWindow.startGame(snakeColor, foodColor, gameSpeed, difficulty)
        );

        // Snake Color Button
        JButton snakeColorButton = createStyledButton("Change Snake Color");
        snakeColorButton.addActionListener(this::changeSnakeColor);

        // Food Color Button
        JButton foodColorButton = createStyledButton("Change Food Color");
        foodColorButton.addActionListener(this::changeFoodColor);

        // Quit Button
        JButton quitButton = createStyledButton("Quit Game");
        quitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(difficultyPanel);
        buttonPanel.add(startButton);
        buttonPanel.add(snakeColorButton);
        buttonPanel.add(foodColorButton);
        buttonPanel.add(quitButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 22));
        button.setFocusPainted(false);
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        return button;
    }

    private void changeSnakeColor(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(this, "Choose Snake Color", snakeColor);
        if (newColor != null) {
            snakeColor = newColor;
        }
    }

    private void changeFoodColor(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(this, "Choose Food Color", foodColor);
        if (newColor != null) {
            foodColor = newColor;
        }
    }
}
