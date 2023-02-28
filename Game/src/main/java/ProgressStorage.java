import java.io.*;

public class ProgressStorage {
    static int numLevelsUnlocked = 1;
    static int savedDataChecker;

    /**
     * Function saveGame
     * This saves the game's progress to be used in later sessions.
     */
    public static void saveGame() {
        try {
            FileOutputStream saveFileOutput = new FileOutputStream("progress.sav");
            ObjectOutputStream saveOutputData = new ObjectOutputStream(saveFileOutput);
            saveOutputData.writeObject(numLevelsUnlocked);
            saveOutputData.flush();
            saveOutputData.close();
            System.out.println("Game Saved");
        } catch (Exception e) {
            System.out.print("Serialization Error! Can't save data.\n" + e.getClass() + ": " + e.getMessage() + "\n");
        }
    }

    /**
     * Function loadGame
     * This loads the game's last saved progress; even if it was saved in a previous play session.
     */
    public static void loadGame() {
        try {
            FileInputStream saveFileInput = new FileInputStream("progress.sav");
            ObjectInputStream savedInputData = new ObjectInputStream(saveFileInput);
            numLevelsUnlocked = (int) savedInputData.readObject();
            savedInputData.close();
            System.out.println("Game Loaded");
        } catch (Exception e) {
            System.out.print("Serialization Error! Can't save data.\n" + e.getClass() + ": " + e.getMessage() + "\n");
        }
    }

    /**
     * Function progressInSavedGame
     * This returns the number of levels completed in the game's saved progress.
     */
    public static int progressInSavedGame() {
        int levelsUnlocked;
        try {
            FileInputStream saveFileInput = new FileInputStream("progress.sav");
            ObjectInputStream savedInputData = new ObjectInputStream(saveFileInput);
            levelsUnlocked = (int) savedInputData.readObject();
            savedInputData.close();
            return levelsUnlocked;
        } catch (Exception e) {
            System.out.print("Serialization Error! Can't save data.\n" + e.getClass() + ": " + e.getMessage() + "\n");
            return 1;
        }
    }

    /**
     * Function isAlreadySaved
     * This returns a boolean stating whether the current state of the game is the state that is saved and can be loaded.
     *
     * @return true if the game is already saved.
     */
    public static boolean isAlreadySaved() {
        try {
            FileInputStream saveFileInput = new FileInputStream("progress.sav");
            ObjectInputStream savedInputData = new ObjectInputStream(saveFileInput);
            savedDataChecker = (int) savedInputData.readObject();
            savedInputData.close();

            if (savedDataChecker == numLevelsUnlocked) {
                return true;
            }

            System.out.println("isAlreadySaved:\nsavedDataChecker: " + savedDataChecker + "\nLevels Unlocked: " + numLevelsUnlocked);
        } catch (Exception e) {
            System.out.print("Serialization Error! Can't save data.\n" + e.getClass() + ": " + e.getMessage() + "\n");
        }

        return false;
    }

    /**
     * Function hasSavedProgressAvailable
     * This returns a boolean stating whether the saved version of the game has progress.
     *
     * @return true if the game has a saved version with progress.
     */
    public static boolean hasSavedProgressAvailable() {
        try {
            FileInputStream saveFileInput = new FileInputStream("progress.sav");
            ObjectInputStream savedInputData = new ObjectInputStream(saveFileInput);
            savedDataChecker = (int) savedInputData.readObject();
            savedInputData.close();
            if (savedDataChecker != 1) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
