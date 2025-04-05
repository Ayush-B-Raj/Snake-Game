import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private final int UNIT_SIZE = 30;
    private final int GRID_COLS = 20;
    private final int GRID_ROWS = 20;
    private final int GAME_WIDTH = GRID_COLS * UNIT_SIZE;
    private final int GAME_HEIGHT = GRID_ROWS * UNIT_SIZE;
    private final int PANEL_WIDTH = 400;

    private final int[] x = new int[GRID_COLS * GRID_ROWS];
    private final int[] y = new int[GRID_COLS * GRID_ROWS];

    private int snakeLength = 5;
    private int foodX, foodY;
    private int score = 0;
    private boolean running = false;
    private char direction = 'R';
    private Timer timer;
    private Random random;
    private GameWindow gameWindow;
    private Color snakeColor;
    private Color foodColor;
    private JButton exitButton;
    private int gameSpeed;
    private int level = 1;
    private int levelUpThreshold = 50;

    private HighScoreManager highScoreManager;
    private String difficulty;

    public SnakeGame(GameWindow gameWindow, Color snakeColor, Color foodColor, int gameSpeed, String difficulty) {
        this.setPreferredSize(new Dimension(1050, 800));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        this.gameWindow = gameWindow;
        this.snakeColor = snakeColor;
        this.foodColor = foodColor;
        this.gameSpeed = gameSpeed;
        this.difficulty = difficulty;
        this.highScoreManager = new HighScoreManager();
        random = new Random();

        setupExitButton();
        showStartScreen();
    }

    private void setupExitButton() {
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setBounds(GAME_WIDTH + 500, 20, 100, 40);
        exitButton.addActionListener(e -> gameWindow.showMenu());

        this.setLayout(null);
        this.add(exitButton);
    }

    private void showStartScreen() {
        running = false;
        repaint();
    }

    public void startGame() {
        resetGame();
        running = true;
        spawnFood();
        timer = new Timer(gameSpeed, this);
        timer.start();
        repaint();
    }

    private void resetGame() {
        snakeLength = 5;
        score = 0;
        direction = 'R';
        level = 1;
        running = false;

        for (int i = 0; i < snakeLength; i++) {
            x[i] = 150 - (i * UNIT_SIZE);
            y[i] = 150;
        }
    }

    private void spawnFood() {
        do {
            foodX = random.nextInt(GRID_COLS) * UNIT_SIZE;
            foodY = random.nextInt(GRID_ROWS) * UNIT_SIZE;
        } while (isFoodOnSnake());
    }

    private boolean isFoodOnSnake() {
        for (int i = 0; i < snakeLength; i++) {
            if (x[i] == foodX && y[i] == foodY) return true;
        }
        return false;
    }

    private void levelUp() {
        level++;
        gameSpeed = Math.max(30, gameSpeed - 10);
        timer.setDelay(gameSpeed);
        SoundPlayer.play("levelup.wav");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawUI(g);
        if (running) drawGame(g);
        else drawStartScreen(g);
    }

    private void drawUI(Graphics g) {
        int centerX = (getWidth() - (GAME_WIDTH + PANEL_WIDTH)) / 2;
        int centerY = (getHeight() - GAME_HEIGHT) / 2;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.WHITE);
        g.drawRect(centerX, centerY, GAME_WIDTH, GAME_HEIGHT);

        g.setColor(Color.BLACK);
        g.fillRect(centerX + GAME_WIDTH + 10, centerY, PANEL_WIDTH, GAME_HEIGHT);

        g.setColor(Color.WHITE);
        g.drawRect(centerX + GAME_WIDTH + 10, centerY, PANEL_WIDTH, GAME_HEIGHT);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        String title = "SNAKE GAME";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (getWidth() - titleWidth) / 2, 50);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, centerX + GAME_WIDTH + 30, centerY + 80);
        g.drawString("Level: " + level, centerX + GAME_WIDTH + 30, centerY + 120);
        g.drawString("High Score(" + difficulty + "): " + highScoreManager.getHighScore(difficulty),
                centerX + GAME_WIDTH + 30, centerY + 160);

        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("HOW TO PLAY:", centerX + GAME_WIDTH + 30, centerY + 210);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Press SPACE to Start", centerX + GAME_WIDTH + 30, centerY + 240);
        g.drawString("Use Arrow Keys to Move", centerX + GAME_WIDTH + 30, centerY + 270);
    }

    private void drawGame(Graphics g) {
        int centerX = (getWidth() - (GAME_WIDTH + PANEL_WIDTH)) / 2;
        int centerY = (getHeight() - GAME_HEIGHT) / 2;

        g.setColor(foodColor);
        g.fillOval(centerX + foodX, centerY + foodY, UNIT_SIZE, UNIT_SIZE);

        for (int i = 0; i < snakeLength; i++) {
            g.setColor((i == 0) ? snakeColor : snakeColor.darker());
            g.fillRect(centerX + x[i], centerY + y[i], UNIT_SIZE, UNIT_SIZE);
        }
    }

    private void drawStartScreen(Graphics g) {
        String text = "Press SPACE to Start";
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();
    
        // Move it slightly to the left (e.g., 40 pixels)
        int centerX = (getWidth() - textWidth) / 2 - 200;
        int centerY = (getHeight() - textHeight) / 2 + metrics.getAscent();
    
        g.drawString(text, centerX, centerY);
    }
    
    private void move() {
        for (int i = snakeLength; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U' -> y[0] -= UNIT_SIZE;
            case 'D' -> y[0] += UNIT_SIZE;
            case 'L' -> x[0] -= UNIT_SIZE;
            case 'R' -> x[0] += UNIT_SIZE;
        }
    }

    private void checkCollision() {
        for (int i = snakeLength; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                timer.stop();
                SoundPlayer.play("gameover.wav");
                highScoreManager.updateScore(difficulty, score);
            }
        }

        if (x[0] < 0 || x[0] >= GAME_WIDTH || y[0] < 0 || y[0] >= GAME_HEIGHT) {
            running = false;
            timer.stop();
            SoundPlayer.play("gameover.wav");
            highScoreManager.updateScore(difficulty, score);
        }

        if (x[0] == foodX && y[0] == foodY) {
            snakeLength++;
            score += 10;
            SoundPlayer.play("eat.wav");

            if (score >= level * levelUpThreshold) {
                levelUp();
            }

            spawnFood();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
        }
        repaint();
    }

    @Override public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> { if (direction != 'R') direction = 'L'; }
            case KeyEvent.VK_RIGHT -> { if (direction != 'L') direction = 'R'; }
            case KeyEvent.VK_UP -> { if (direction != 'D') direction = 'U'; }
            case KeyEvent.VK_DOWN -> { if (direction != 'U') direction = 'D'; }
            case KeyEvent.VK_SPACE -> { if (!running) startGame(); }
        }
    }
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    // Inner class for high score management
    class HighScoreManager {
        private static final String FILE_NAME = "highscores.txt";
        private static final String[] DIFFICULTIES = {"Easy", "Medium", "Hard"};
        private HashMap<String, Integer> highScores;

        public HighScoreManager() {
            highScores = new HashMap<>();
            loadScores();
        }

        private void loadScores() {
            try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        highScores.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
                    }
                }
            } catch (IOException e) {
                for (String diff : DIFFICULTIES) {
                    highScores.put(diff, 0);
                }
            }
        }

        public void saveScores() {
            try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
                for (String diff : DIFFICULTIES) {
                    pw.println(diff + ":" + getHighScore(diff));
                }
            } catch (IOException e) {
                System.out.println("Failed to save high scores.");
            }
        }

        public int getHighScore(String difficulty) {
            return highScores.getOrDefault(difficulty, 0);
        }

        public void updateScore(String difficulty, int score) {
            if (score > getHighScore(difficulty)) {
                highScores.put(difficulty, score);
                saveScores();
            }
        }
    }
}
