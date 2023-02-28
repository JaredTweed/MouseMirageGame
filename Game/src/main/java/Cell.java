import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public abstract class Cell {
    public static int size = 48;
    public boolean isOpaque;

    public boolean isCrackedWall = false;
    public boolean collision;
    public BufferedImage image;
    protected String imagePath;

    /**
     * Constructor for Cell
     * Sets the initial collision to false and image to null
     */
    public Cell() {
        this.collision = false;
        this.image = null;
    }

    /**
     * Loads the cells image
     * Obtains the file from resources folder and initializes it for cell.
     * throws exception if file is not found.
     */
    public void getImage() {
        try {
            this.image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the cell's image on the paintComponent
     * if image has not been initialized draws a blank square otherwise draws the cell's image.
     *
     * @param g2D graphic object
     * @param x   the x position on the paintComponent
     * @param y   the y position on the paintComponent
     */
    public void draw(Graphics2D g2D, int x, int y) {

        if (image == null) {
            g2D.setColor(Color.green);
            g2D.fillRect(x * 48, y * 48, 48, 48);
        } else {
            g2D.drawImage(image, x * 48, y * 48, size, size, null);
        }

    }

}