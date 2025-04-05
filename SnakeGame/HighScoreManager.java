import java.io.*;
import java.util.HashMap;

public class HighScoreManager {
    private static final String FILE_NAME = "highscores.txt";
    private static final String[] DIFFICULTIES = {"Easy", "Medium", "Hard"};
    @SuppressWarnings("FieldMayBeFinal")
    private HashMap<String, Integer> highScores;

    public HighScoreManager() {
        highScores = new HashMap<>();
        loadScores();
    }

    @SuppressWarnings("UnnecessaryTemporaryOnConversionFromString")
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
            // File might not exist initially, initialize scores to 0
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
