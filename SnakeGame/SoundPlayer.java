// SoundPlayer.java
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class SoundPlayer {
    public static void play(String soundFileName) {
        try {
            File file = new File("sounds/" + soundFileName);
            if (!file.exists()) {
                System.err.println("Sound file not found: " + file.getAbsolutePath());
                return;
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing sound: " + soundFileName);
            e.printStackTrace();
        }
    }
}
