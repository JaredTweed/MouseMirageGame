import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameUI {
    GameWindow gameWindow;
    Graphics2D g2D;
    private final BufferedImage cheese;
    private final BufferedImage water;
    final BufferedImage menu;
    private final BufferedImage endMenu;
    Font cheesyFont;
    Font cheesyFontSmall;
    Font titleFont;

    //Coordinates for Random Buttons
    int numOptions = 6;
    int[] optionLeftEdge = new int[numOptions];
    int[] optionYCell = new int[numOptions];
    String[] option = new String[numOptions + 1];
    String[] text = new String[numOptions];


    /**
     * Constructor for the games UI
     * initializes a Font and the images of each respective UI element
     *
     * @param gw       the game window
     * @param graphics graphics object
     */
    public GameUI(GameWindow gw, Graphics2D graphics) {
        gameWindow = gw;
        g2D = graphics;
        cheesyFont = new Font("Comic Sans MS", Font.BOLD, 25);
        cheesyFontSmall = new Font("Comic Sans MS", Font.BOLD, 20);
        titleFont = new Font("Comic Sans MS", Font.BOLD, 60);
        this.cheese = getImage("/cheese.png");
        this.water = getImage("/waterDrop.png");
        this.menu = getImage("/startScreen.png");
        this.endMenu = getImage("/endScreen.png");

        optionYCell[0] = 5;
        optionYCell[1] = 6;
        optionYCell[2] = 7;
        optionYCell[3] = 8;
        optionYCell[4] = 9;
        optionYCell[5] = 10;
    }

    /**
     * Draws the text options and menu that appears in the game menus.
     * when called obtains initialized font and prints the text to the middle of the screen.
     *
     * @param option1 draws this text in the first option spot.
     * @param option2 draws this text in the second option spot.
     * @param option3 draws this text in the third option spot.
     * @param option4 draws this text in the fourth option spot.
     * @param option5 draws this text in the fifth option spot.
     * @param option6 draws this text in the sixth option spot.
     */
    public void menuFormat(String option1, String option2, String option3, String option4, String option5, String option6) {
        g2D.setColor(Color.BLACK);
        g2D.setFont(titleFont);
        g2D.drawString("Mouse Mirage", getXForCenteredText("Mouse Mirage"), 2 * Cell.size);

        g2D.setFont(cheesyFont);
        g2D.setColor(Color.BLACK);

        /*option[0] means that no option is selected. option[0] should only occur
        when switching between menus or when the mouse is in use. */
        option[1] = option1;
        option[2] = option2;
        option[3] = option3;
        option[4] = option4;
        option[5] = option5;
        option[6] = option6;

        for (int i = 0; i < numOptions; i++) {
            //draws all the options
            if (option[i + 1] != null) {
                optionLeftEdge[i] = getXForCenteredText(option[i + 1]);
                g2D.drawString(option[i + 1], optionLeftEdge[i], optionYCell[i] * Cell.size);
            }

            //draws which option is selected
            if (gameWindow.optionSelected == i + 1) {
                g2D.drawString(">", optionLeftEdge[i] - 20, optionYCell[i] * Cell.size);
            }
        }
    }

    /**
     * Draws the text non-selectable that appears in the game menus.
     * when called obtains initialized font and prints the text to the middle of the screen.
     *
     * @param option1  draws this text in the first option spot.
     * @param option2  draws this text in the second option spot.
     * @param option3  draws this text in the third option spot.
     * @param option4  draws this text in the fourth option spot.
     * @param option5  draws this text in the fifth option spot.
     * @param option6  draws this text in the sixth option spot.
     * @param withLock draws a lock over this text if this is true.
     */
    public void menuTextFormat(String option1, String option2, String option3, String option4, String option5, String option6, Boolean withLock) {
        g2D.setFont(cheesyFont);
        g2D.setColor(Color.DARK_GRAY);

        text[0] = option1;
        text[1] = option2;
        text[2] = option3;
        text[3] = option4;
        text[4] = option5;
        text[5] = option6;

        for (int i = 0; i < numOptions; i++) {
            //draws all the options
            if (text[i] != null) {
                optionLeftEdge[i] = getXForCenteredText(text[i]);
                g2D.drawString(text[i], optionLeftEdge[i], optionYCell[i] * Cell.size);

                if (withLock) {
                    g2D.drawImage(getImage("/exitDoor.png"), gameWindow.windowWidth / 2 - (Cell.size / 2), optionYCell[i] * Cell.size - 33, Cell.size, Cell.size, null);
                }
            }


        }
    }

    /**
     * draws the UI of the game in progress
     * when called prints and draws the Players score and health
     */
    public void drawUI() {
        g2D.setFont(cheesyFont);
        g2D.setColor(Color.YELLOW);
        g2D.drawImage(cheese, 0, 0, Cell.size, Cell.size, null);
        g2D.drawString("" + Player.cheeseCollected, Cell.size, Cell.size - 5);
        g2D.setColor(Color.BLUE);
        for (int i = 0; i < Player.water; i++) {
            g2D.drawImage(water, (15 - i) * Cell.size, 0, Cell.size, Cell.size, null);
        }
    }

    /**
     * draws the end screen and tallies score
     * When called print and draws the players score based on players factors
     */
    public void drawEnd() {
        g2D.setFont(titleFont);
        g2D.setColor(Color.BLACK);

        if (!gameWindow.levelPassed) {
            g2D.drawImage(endMenu, 0, 0, 16 * Cell.size, 16 * Cell.size, null);
            g2D.drawString("Game Over", getXForCenteredText("Game Over"), 6 * Cell.size);

            g2D.setFont(cheesyFont);
            if (gameWindow.deathCause == DeathCause.TRAP) {
                g2D.drawString("You were killed by a trap", 5 * Cell.size - 10, 7 * Cell.size);
            } else if (gameWindow.deathCause == DeathCause.CAT) {
                g2D.drawString("The cat ate you", 5 * Cell.size - 10, 7 * Cell.size);
            } else if (gameWindow.deathCause == DeathCause.DEHYDRATION) {
                g2D.drawString("You ran out of water", 5 * Cell.size - 10, 7 * Cell.size);
            }

            drawScore(8);

            g2D.setFont(cheesyFont);
            g2D.drawString("Press space to retry level", 5 * Cell.size - 10, 10 * Cell.size);
        } else if (gameWindow.allLevelsFinished) {
            g2D.drawImage(menu, 0, 0, 16 * Cell.size, 16 * Cell.size, null);
            g2D.drawString("Game", 5 * Cell.size + 15, 6 * Cell.size - 24);
            g2D.drawString("Complete", 5 * Cell.size - 10, 7 * Cell.size - 24);

            g2D.setFont(cheesyFont);
            g2D.drawString("Congratulations you won! ", 5 * Cell.size - 10, 8 * Cell.size - 24);

            drawScore(8);

            g2D.setFont(cheesyFont);
            g2D.drawString("Press space to play again", 5 * Cell.size - 10, 10 * Cell.size);
        } else {
            g2D.drawImage(menu, 0, 0, 16 * Cell.size, 16 * Cell.size, null);
            g2D.drawString("Level " + trueCurrentLevel(), 5 * Cell.size + 15, 6 * Cell.size);
            g2D.drawString("Complete", 5 * Cell.size - 10, 7 * Cell.size);

            drawScore(8);

            g2D.setFont(cheesyFont);
            g2D.drawString("Press space to play next level", 4 * Cell.size + 12, 10 * Cell.size);
        }
    }

    /**
     * Draws the score of the level
     * Breaks the score down into 3 parts the cheese collected, the water remaining, and total score. Then draws onto the screen
     *
     * @param y the starting y cell where the score should be drawn.
     */
    private void drawScore(int y) {
        g2D.setFont(cheesyFontSmall);
        g2D.drawString("Cheese Collected: " + Player.cheeseCollected + "/" + Board.maxCheese, 5 * Cell.size - 10, y * Cell.size);
        g2D.drawString("Water Remaining:   " + Player.water, 5 * Cell.size - 10, (y + 1) * Cell.size - 24);
        g2D.drawString("Level " + trueCurrentLevel() + " Score: " + ((Player.water * 1000) + (1000 * Player.cheeseCollected / Board.maxCheese)), 5 * Cell.size - 10, (y + 1) * Cell.size);
    }

    /**
     * Finds the actual current level.
     * This finds the level number for the level that is being played or the level that was just ended.
     *
     * @return returns the actual current level.
     */
    public int trueCurrentLevel() {
        int currentLevel;
        if (gameWindow.allLevelsFinished) {
            currentLevel = gameWindow.levels.numberOfLevels;
        } else if (gameWindow.levelPassed) {
            currentLevel = gameWindow.currentLevel;
        } else {
            currentLevel = gameWindow.currentLevel + 1;
        }

        return currentLevel;
    }

    /**
     * Finds the starting X coordinate to center the text.
     * This X value can be used in as the X coordinate in the text's location.
     *
     * @param text the text that will be centered.
     * @return returns the X coordinate that should be used to place the text in the center.
     */
    public int getXForCenteredText(String text) {
        int textLength;
        if (text != null) {
            textLength = (int) g2D.getFontMetrics().getStringBounds(text, g2D).getWidth();
        } else {
            textLength = 0;
        }
        return gameWindow.windowWidth / 2 - textLength / 2;
    }

    /**
     * Loads the image from the image's path
     * Obtains the file from resources folder and initializes it for StationaryCharacter.
     * throws exception if file is not found.
     *
     * @param imagePath string representing the image's path
     * @return returns the image if found and null if image is unavailable
     */
    public BufferedImage getImage(String imagePath) {
        try {
            return ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
