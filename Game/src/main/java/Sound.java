import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundFilePath = new URL[10];

    /**
     * Constructor for Sound
     */
    public Sound() {
        soundFilePath[0] = getClass().getResource("/gameMusic.wav");
    }

    /**
     * sets the chosen audio clip
     * picks the audio clip based the songNumber if not found throws exception
     *
     * @param songNumber the chosen audio clip
     */
    public void setFile(int songNumber) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFilePath[songNumber]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception ignored) {
        }
    }

    /**
     * plays the audio clip
     */
    public void play() {
        clip.start();
    }

    /**
     * loops the audio clip
     */
    public void loop() {
        clip.loop(clip.LOOP_CONTINUOUSLY);
    }

    /**
     * stops the audio clip
     */
    public void stop() {
        clip.stop();
    }
}
