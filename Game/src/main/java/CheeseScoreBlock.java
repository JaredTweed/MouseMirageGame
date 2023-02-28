

public class CheeseScoreBlock extends StationaryCharacter {

    /**
     * Constructor for CheeseScoreBlock.
     * The constructor sets the image path to be loaded.
     */
    CheeseScoreBlock() {
        super();
        type = Type.CHEESE;
        imagePath = "/cheese.png";
        getImage();
    }
}
