import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    //input handling.
    public boolean upPressed, downPressed, leftPressed, rightPressed, selectPressed, pausePressed;
    int framesUntilKeyCanType = 0;
    int framesUntilKeyCanSelect = 0;
    GameWindow gameWindow;

    /**
     * Constructor that finds the gameWindow.
     *
     * @param gw is the gameWindow of which uses the key handler.
     */
    KeyHandler(GameWindow gw){
        gameWindow = gw;
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
            upPressed = true;
            gameWindow.theCursor.cursorInUse = false;
        }
        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
            downPressed = true;
            gameWindow.theCursor.cursorInUse = false;
        }
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
            selectPressed = true;
            gameWindow.theCursor.cursorInUse = false;
        }
        if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_E) {
            pausePressed = true;
            gameWindow.theCursor.cursorInUse = false;
        }
        if (code == KeyEvent.VK_M) {
            gameWindow.toggleMusic();
        }

        // Debugging
        if (code == KeyEvent.VK_C) {
            System.out.printf("player: (%d, %d)\n",gameWindow.theBoard.player.trueX(),gameWindow.theBoard.player.trueY());
        }
        if (code == KeyEvent.VK_I) {
            gameWindow.theBoard.cats.get(0).direction = Direction.Up;
        }
        if (code == KeyEvent.VK_K) {
            gameWindow.theBoard.cats.get(0).direction = Direction.Down;
        }
        if (code == KeyEvent.VK_J) {
            gameWindow.theBoard.cats.get(0).direction = Direction.Left;
        }
        if (code == KeyEvent.VK_L) {
            gameWindow.theBoard.cats.get(0).direction = Direction.Right;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
            selectPressed = false;
        }
        if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_E) {
            pausePressed = false;
        }
    }

    /**
     * Resets cool down until typing is allowed.
     */
    public void keyTypeCoolDownReset() {
        framesUntilKeyCanType = 11;
    }

    /**
     * Resets cool down until selection is allowed.
     */
    public void selectionCoolDownReset() {
        framesUntilKeyCanSelect = 12;
    }

    /**
     * Allows cool down to occur after a key is typed.
     * The cool down allows individual typing to occur without forcing the action to be completed for every millisecond the key is pressed.
     */
    public void coolDownKeyType() {
        if (framesUntilKeyCanType <= 0) {
            framesUntilKeyCanType = 0;
        } else {
            framesUntilKeyCanType -= 1;
        }

        if (framesUntilKeyCanSelect <= 0) {
            framesUntilKeyCanSelect = 0;
        } else {
            framesUntilKeyCanSelect -= 1;
        }
    }

    /**
     * Returns a boolean indicating whether typing is allowed again.
     *
     * @return whether typing is allowed.
     */
    public boolean isKeyTypeCooledDown() {
        return framesUntilKeyCanType <= 0;
    }

    /**
     * Returns a boolean indicating whether selection is allowed again.
     *
     * @return whether selection is allowed.
     */
    public boolean isSelectionCooledDown() {
        return framesUntilKeyCanSelect <= 0;
    }
}
