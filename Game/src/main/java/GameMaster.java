import javax.swing.JFrame;

/**
 * The GameMaster Class starts the game
 */
public class GameMaster {
    /**
     * Function main
     * Starts the game
     *
     * @param arg unused argument
     */
    public static void main(String[] arg) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Mouse Mirage");

        GameWindow gameWindow = new GameWindow();
        window.add(gameWindow);
        window.pack();      // fits the gameWindow into the JFrame

        //window.setLocationRelativeTo(null); //makes window displayed at screen center.
        window.setVisible(true);

        gameWindow.startGame();
    }
}