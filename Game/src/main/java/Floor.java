import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Floor extends Cell {
    public boolean isOccupied;

    /**
     * Constructor for Floor
     * sets initial isOccupied to false and sets the image path to be loaded
     */
    public Floor() {
        super();
        isOpaque = false;
        isOccupied = false;
        imagePath = "/floor.png";
        getImage();
    }


}
