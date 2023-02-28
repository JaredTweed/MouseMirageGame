public class ExitDoor extends StationaryCharacter {
    public boolean locked;

    /**
     * Constructor for ExitDoor
     * sets the image path and locked to true
     */
    public ExitDoor() {
        super();
        type = Type.EXIT_DOOR;
        locked = true;
        imagePath = "/exitDoor.png";
        getImage();
    }

    /**
     * sets the locked value to be false
     * the function set it to false and sets the exit door to unlocked image
     */
    public void setValue() {
        locked = false;
        imagePath = "/exitDoorUnlocked.png";
        getImage();
    }

    /**
     * returns if state of the lock
     *
     * @return if true return locked else return unlocked
     */
    public boolean getValue() {
        return locked;
    }
}
