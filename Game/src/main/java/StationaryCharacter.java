import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public abstract class StationaryCharacter{
    public BufferedImage image;
    protected String imagePath;
    protected Type type;



    /**
     * Abstract Constructor for StationaryCharacter
     * initializes image to null
     */
    public StationaryCharacter() {
        image = null;
    }

    /**
     * gets the type of StationaryCharacter
     * @return an int of type StationaryCharacter
     */
    public boolean getValue(){ return true;}
    public void setValue(){}


    /**
     * Loads the StationaryCharacter's image
     * Obtains the file from resources folder and initializes it for StationaryCharacter.
     * throws exception if file is not found.
     */
    public void getImage() {
        try {
            this.image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the StationaryCharacter's image on the paintComponent
     * if image has not been initialized draws a blank square otherwise draws the StationaryCharacter's image.
     * @param g2D graphic object
     * @param x the x position on the paintComponent
     * @param y the y position on the paintComponent
     */
    public void draw(Graphics2D g2D, int x, int y){
        if(image == null) {
            g2D.setColor(Color.green);
            g2D.fillRect(x * Cell.size, y * Cell.size, Cell.size, Cell.size);
        }else {
            g2D.drawImage(image, x * Cell.size, y * Cell.size, Cell.size, Cell.size, null);
        }
    }
}
