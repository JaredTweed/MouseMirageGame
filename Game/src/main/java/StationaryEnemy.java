

public class StationaryEnemy extends StationaryCharacter {
    static int damage;

    /**
     * Constructor of StationaryEnemy
     * sets itself to be active and sets the image path to be loaded.
     */
    StationaryEnemy() {
        type = Type.TRAP;
        damage = 5;
        imagePath = "/mouseTrap.png";
        getImage();
    }
}
