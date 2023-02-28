public class WaterHealthBlock extends StationaryCharacter {

    /**
     * Constructor for WaterHealthBlock.
     * The constructor sets the image path to be loaded.
     */
    WaterHealthBlock() {
        super();
        type = Type.WATER;
        imagePath = "/waterDrop.png";
        getImage();
    }
}
