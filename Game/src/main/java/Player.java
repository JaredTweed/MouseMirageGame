public class Player extends Entity {
    // Attributes
    GameWindow gameWindow;
    static int water;
    static int cheeseCollected;
    int waterDrainTime;

    {
        scale = 0.7;
        size = (int) Math.floor(Cell.size * scale);
        hitRadius = (int) Math.floor(size * 0.8) / 2;
        hitBoxDecrease = (size / 2) - hitRadius;
        crackedWallIsCollision = false;
        speed = 4;
        diagonalSpeed = (int) Math.ceil((double) speed / Math.sqrt(2)); // a normalized speed for diagonal movement
        water = 10;
        cheeseCollected = 0;
        waterDrainTime = 180;
        imagePath = "/mouse/";
        getImage();
    }

    /**
     * Constructor for Player
     * sets the players speed, water, drain time and coordinates
     *
     * @param gw    is the game window that player updates death cause and level pass for
     * @param CellX is the initial column player is placed on the board
     * @param CellY is the initial row player is placed on the board
     */
    public Player(GameWindow gw, int CellX, int CellY) {
        super(); //this causes the player to inherit the entity's constructor

        // places the Cat in the center of Cell
        x = CellX * Cell.size + Cell.size / 2 - size / 2;
        y = CellY * Cell.size + Cell.size / 2 - size / 2;
        gameWindow = gw;
    }

    /**
     * Checks the players key press and moves player if possible
     * finds the players tile relative to the board and then gets the key press
     * the checks for walls and tiles
     *
     * @param keyH the key pressed
     */
    public void keyMove(KeyHandler keyH) {
        //The coordinates will only change if the destination is not in a cell that has collision
        if (keyH.upPressed) {
            moveUp();
        } else if (keyH.downPressed) {
            moveDown();
        }
        if (keyH.leftPressed) {
            moveLeft();
        } else if (keyH.rightPressed) {
            moveRight();
        }

        nextTail();
        checkStationary();
        preventWindowEscape();
    }

    /**
     * Checks what item the player is standing on
     * based on the item type the player will gain no effect, gain cheese, take damage, or end the game
     *
     */
    private void checkStationary() {
        int[] cellCoordinates = new int[2];
        cellCoordinates[0] = (int) Math.floor((double) trueX() / (double) Cell.size);
        cellCoordinates[1] = (int) Math.floor((double) trueY() / (double) Cell.size);
        Board board = Board.getInstance();

        if (board.sChar[cellCoordinates[0]][cellCoordinates[1]] != null) {
            StationaryCharacter item = board.sChar[cellCoordinates[0]][cellCoordinates[1]];
            if (item.type == Type.WATER) {
                board.destroyStationary(cellCoordinates);
                water++;
                waterDrainTime += 180;
            }
            if (item.type == Type.CHEESE) {
                board.destroyStationary(cellCoordinates);
                cheeseCollected++;
                if (cheeseCollected == Board.maxCheese) {
                    board.sChar[Board.exitDoorCoordinates[0]][Board.exitDoorCoordinates[1]].setValue();
                }
            }
            if (item.type == Type.TRAP) {
                board.destroyStationary(cellCoordinates);
                GameWindow.deathCause = DeathCause.TRAP;
                harm(StationaryEnemy.damage);
            }
            if (item.type == Type.EXIT_DOOR && (!item.getValue())) {
                gameWindow.finishLevel();
            }
        }
    }

    /**
     * Harms the player water based on the amount of damage
     *
     * @param damage amount of damage given to the player
     */
    public void harm(int damage) {
        assert (damage >= 0);
        water -= damage;
        if (water < 0) {
            water = 0;
        }
    }

    /**
     * Drains the player's water every few seconds
     * players water/health is decreased by 1 if the waterDrainTime is zero
     * otherwise decreases the waterDrainTime
     */
    public void drain() {
        if (waterDrainTime <= 0) {
            GameWindow.deathCause = DeathCause.DEHYDRATION;
            this.harm(1);
            waterDrainTime = 180;
        }
        waterDrainTime -= 1;
    }
}
