import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class LoadingIcon {
    private final int size = 96;
    private final int numberOfFrames = 12;
    private int currentFrame;
    private int x;
    private int y;

    public BufferedImage[] frame = new BufferedImage[numberOfFrames];

    LoadingIcon(int X, int Y) {
        x = X - size/2;
        y = Y - size/2;
        // sets x and y to be the location on the screen for the top left pixel of the icon
        getImages();
    }

    public BufferedImage getNextFrame(){
        nextFrame();
        return frame[currentFrame];
    }

    private void nextFrame(){
        currentFrame++;
        if(currentFrame >= numberOfFrames) {
            currentFrame = 0;
        }
    }

    public void draw(Graphics2D g2D) {
        g2D.drawImage(getNextFrame(), x, y, size, size, null);
    }

    private void getImages() {
        try {
            for(int i = 0; i < numberOfFrames; i++) {
                String imagePath = "/loader/";
                frame[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + "frame" + i + ".png")));
            }
            currentFrame = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
