import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;

/**
 * The GameWindow class is where the pixel size for the window as well as for game objects is specified and initialized
 */
public class GameWindow extends JPanel implements Runnable {
    final int tileSize = 48; // a 48X48 pixel tile for board and character objects
    final int columns = 16;
    final int rows = 16;
    final int windowWidth = tileSize * columns; // 768 pixels
    final int windowHeight = tileSize * rows; // 768 pixels

    Cursor theCursor;
    Board theBoard;
    GameUI theUI;
    Thread gameThread;
    KeyHandler keyH = new KeyHandler(this);
    Sound sound = new Sound();
    boolean musicIsPlaying;

    //Game State
    public GameState gameState = GameState.Title;

    enum ProgressWarning {PlayWithoutLoad, UnlockAllLevels, LevelsWithoutLoad, ResetProgress}

    ProgressWarning warning;

    enum SaveMenuSelected {LoadProgress, UnlockLevels, ResetProgress, MainMenu, SaveGame}

    SaveMenuSelected saveSelect = null;

    //Level processing
    Levels levels = new Levels();
    int currentLevel = 0;
    boolean levelPassed = false;
    boolean allLevelsFinished = false;
    public static DeathCause deathCause = DeathCause.DEHYDRATION;

    //Menu Select
    int optionSelected = 0;
    boolean isPaused = false;
    int levelScreen = 0;
    int numLevelScreens = (levels.numberOfLevels + 3) / 4;

    //Loading icon
    public LoadingIcon loadingIcon = new LoadingIcon(windowWidth / 2, windowHeight / 2);
    boolean isLoading = true;

    /**
     * Constructor for GameWindow
     * sets the window size
     */
    public GameWindow() {
        theCursor = new Cursor();
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); //this line improves rendering performance
        this.addKeyListener(keyH);
        this.addMouseMotionListener(theCursor);
        this.addMouseListener(theCursor);
        this.setFocusable(true);
    }

    /**
     * Function startGame
     * creates and starts the main game thread
     */
    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start(); //this calls the run method
        ProgressStorage.numLevelsUnlocked = 1;
    }

    //FPS
    final static int FPS = 60;

    /**
     * Function run
     * initializes the board and starts the music for the game
     * starts the game loop and controls the games clock
     * in loop game objects and conditions updated
     */
    @Override
    public void run() {
        boolean boardIsInstantiated = false;

        playMusic(0);
        musicIsPlaying = true;

        double drawInterval = 1000000000f / FPS; //number of nanoseconds between each frame: 1000000000f == 1 second
        double startTime = System.nanoTime(); //system.nanoTime is the current time on the system
        double nextDrawTime = startTime + drawInterval;


        while (gameThread != null) {
            if (gameState == GameState.Play) {
                if (!boardIsInstantiated) {
                    System.out.println("Initializing board");
                    levelPassed = false;
                    theBoard = Board.instance(columns, rows, levels.getLevel(currentLevel), this);
                    boardIsInstantiated = true;
                }

                if (!isPaused) {
                    theBoard.player.drain();
                }
                if (Player.water <= 0) {
                    levelPassed = false;
                    gameState = GameState.End;
                }
            } else if (gameState == GameState.End || gameState == GameState.LevelSelect || gameState == GameState.Title) {
                if (boardIsInstantiated) {
                    System.out.println("destroying the board");
                    theBoard = null;
                    boardIsInstantiated = false;
                }
            }
            updateUI();
            repaint(); //this calls the paintComponent method which is part of the JPanel library.
//            System.out.printf("FPS: %.0f ", 1000000000f/(System.nanoTime() - startTime));

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000; //converting remaining time from nanoseconds to milliseconds

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                //waiting until next frame
                Thread.sleep((long) remainingTime); //Thread.sleep just needs to be surrounded with try&catch.

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * updates the positions for all animated characters and detects if the player has been caught
     */
    public void updateUI() {
        if (keyH != null) {
            keyH.coolDownKeyType();
        }

        if (!isPaused) {
            if (theBoard != null) {
                if (keyH != null && theBoard.player != null) {
                    theBoard.player.keyMove(keyH);
                }
                theBoard.updateWater();
                if (theBoard.cats != null && theBoard.player != null) {
                    for (CatEnemy aCat : theBoard.cats) {
                        aCat.move(theBoard.player);
                    }
                }
            }

        }
    }

    /**
     * Function paintComponent
     * Draws the gameScreen and all game objects while the game is running
     *
     * @param g graphics tool that is used to paint the canvas
     */
    public void paintComponent(Graphics g) { //
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        theUI = new GameUI(this, g2D);

        if (gameState == GameState.Title) {
            //Main menu UI
            g2D.drawImage(theUI.menu, 0, 0, 16 * tileSize, 16 * tileSize, null);
            if (ProgressStorage.numLevelsUnlocked == 1) {
                theUI.menuFormat(null, "Story Mode", "Level Select", "Load Saved Progress", "Quit Game", null);
            } else if (ProgressStorage.numLevelsUnlocked == levels.numberOfLevels) {
                theUI.menuFormat(null, "Replay Story", "Level Select", "Load Saved Progress", "Quit Game", null);
            } else {
                theUI.menuFormat(null, "Continue Story", "Level Select", "Load Saved Progress", "Quit Game", null);
            }

            menuNavigation();

            //Selecting your option
            if (keyH.isSelectionCooledDown() && (keyH.selectPressed || theCursor.isClicked)) {
                if (optionSelected == 2) {
                    isPaused = false;
                    if (ProgressStorage.numLevelsUnlocked < ProgressStorage.progressInSavedGame()) {
                        warning = ProgressWarning.PlayWithoutLoad;
                        gameState = GameState.Confirmation;
                    } else {
                        if (ProgressStorage.numLevelsUnlocked != levels.numberOfLevels) {
                            currentLevel = ProgressStorage.numLevelsUnlocked - 1;
                        } else {
                            currentLevel = 0;
                        }
                        gameState = GameState.Play;
                    }
                } else if (optionSelected == 3) {
                    if (ProgressStorage.numLevelsUnlocked < ProgressStorage.progressInSavedGame()) {
                        warning = ProgressWarning.LevelsWithoutLoad;
                        gameState = GameState.Confirmation;
                    } else {
                        gameState = GameState.LevelSelect;
                    }
                } else if (optionSelected == 4) {
                    gameState = GameState.SaveMenu;
                } else if (optionSelected == 5) {
                    System.exit(0);
                }

                theCursor.isClicked = false;
                optionSelected = 0;
                keyH.selectionCoolDownReset();
            }
        } else if (gameState == GameState.LevelSelect) {
            //Drawing the level select menu UI.
            g2D.drawImage(theUI.menu, 0, 0, 16 * tileSize, 16 * tileSize, null);
            String[] level = new String[4];
            String[] lockedLevel = new String[4];
            for (int i = 0; i < 4; i++) {
                if ((4 * levelScreen) + i < levels.numberOfLevels) {
                    if ((4 * levelScreen) + i >= ProgressStorage.numLevelsUnlocked) {
                        lockedLevel[i] = "Level " + ((4 * levelScreen) + i + 1);
                        level[i] = null;
                    } else {
                        lockedLevel[i] = null;
                        level[i] = "Level " + ((4 * levelScreen) + i + 1);
                    }
                } else {
                    level[i] = null;
                }
            }
            theUI.menuFormat(level[0], level[1], level[2], level[3], "Other Levels", "Main Menu");
            theUI.menuTextFormat(lockedLevel[0], lockedLevel[1], lockedLevel[2], lockedLevel[3], null, null, true);

            menuNavigation();

            if (keyH.isSelectionCooledDown() && (keyH.selectPressed || theCursor.isClicked)) {
                //Selecting the level
                for (int i = 1; i < 5; i++) {
                    if (optionSelected == i) {
                        currentLevel = ((4 * levelScreen) + i - 1);
                        levelScreen = 0; //sets the default level screen that will show upon return
                        isPaused = false;
                        gameState = GameState.Play;
                    }
                }

                if (optionSelected == 5) { //"other levels" selection
                    if (levelScreen < numLevelScreens - 1) {
                        levelScreen++;
                    } else {
                        levelScreen = 0;
                    }
                } else if (optionSelected == 6) { //main menu selection
                    levelScreen = 0; //sets the default level screen that will show upon return
                    gameState = GameState.Title;
                }

                optionSelected = 0;
                theCursor.isClicked = false;
                keyH.selectionCoolDownReset();
            }
        } else if (gameState == GameState.SaveMenu) {
            int saveGame = 1, loadProgress = 1, unlockLevels = 1, resetProgress = 1, mainMenu;

            //Save Menu UI
            g2D.drawImage(theUI.menu, 0, 0, 16 * tileSize, 16 * tileSize, null);
            if (ProgressStorage.numLevelsUnlocked == levels.numberOfLevels) {
                theUI.menuTextFormat(null, "All Levels Already Unlocked", null, null, null, null, false);
                theUI.menuFormat(null, null, "Reset Progress", "Main Menu", null, null);
                resetProgress = 3;
                mainMenu = 4;
            } else if (ProgressStorage.numLevelsUnlocked == 1 && ProgressStorage.hasSavedProgressAvailable()) {
                theUI.menuTextFormat(null, "It Has Only Just Begun", null, null, null, null, false);
                theUI.menuFormat(null, null, "Load Saved Progress", "Unlock All Levels", "Main Menu", null);
                loadProgress = 3;
                unlockLevels = 4;
                mainMenu = 5;
            } else if (ProgressStorage.numLevelsUnlocked == 1) {
                theUI.menuTextFormat(null, "Your Journey Awaits", null, null, null, null, false);
                theUI.menuFormat(null, null, "Unlock All Levels", "Main Menu", null, null);
                unlockLevels = 3;
                mainMenu = 4;
            } else if (!ProgressStorage.isAlreadySaved()) { //this outcome shouldn't happen
                theUI.menuFormat(null, "Load Saved Progress", "Unlock All Levels", "Reset Progress", "Main Menu", null);
                loadProgress = 2;
                unlockLevels = 3;
                resetProgress = 4;
                mainMenu = 5;
            } else {
                theUI.menuTextFormat(null, "Game Saved", null, null, null, null, false);
                theUI.menuFormat(null, null, "Unlock All Levels", "Reset Progress", "Main Menu", null);
                unlockLevels = 3;
                resetProgress = 4;
                mainMenu = 5;
            }

            menuNavigation();

            //Selecting your option
            if (keyH.isSelectionCooledDown() && (keyH.selectPressed || theCursor.isClicked)) {
                //Determining which button was clicked
                if (optionSelected == saveGame) {
                    saveSelect = SaveMenuSelected.SaveGame;
                } else if (optionSelected == loadProgress) {
                    saveSelect = SaveMenuSelected.LoadProgress;
                } else if (optionSelected == unlockLevels) {
                    saveSelect = SaveMenuSelected.UnlockLevels;
                } else if (optionSelected == resetProgress) {
                    saveSelect = SaveMenuSelected.ResetProgress;
                } else if (optionSelected == mainMenu) {
                    saveSelect = SaveMenuSelected.MainMenu;
                }

                //The action for each button
                if (saveSelect == SaveMenuSelected.SaveGame) {
                    ProgressStorage.saveGame();
                    if (keyH.selectPressed) {
                        optionSelected = mainMenu;
                    }
                } else if (saveSelect == SaveMenuSelected.LoadProgress) {
                    ProgressStorage.loadGame();
                    if (keyH.selectPressed) {
                        optionSelected = mainMenu;
                    }
                } else if (saveSelect == SaveMenuSelected.UnlockLevels) {
                    warning = ProgressWarning.UnlockAllLevels;
                    gameState = GameState.Confirmation;
                    optionSelected = 0;
                } else if (saveSelect == SaveMenuSelected.ResetProgress) {
                    warning = ProgressWarning.ResetProgress;
                    gameState = GameState.Confirmation;
                    optionSelected = 0;
                } else if (saveSelect == SaveMenuSelected.MainMenu) {
                    gameState = GameState.Title;
                    optionSelected = 0;
                }

                theCursor.isClicked = false;
                keyH.selectionCoolDownReset();
            }
        } else if (gameState == GameState.Confirmation) {
            int yes = 1, no = 1;

            //Confirmation Menu UI
            g2D.drawImage(theUI.menu, 0, 0, 16 * tileSize, 16 * tileSize, null);
            if (warning == ProgressWarning.UnlockAllLevels) {
                theUI.menuTextFormat("Are you sure?", "Your previous progress", "will be overwritten.", null, null, null, false);
                theUI.menuFormat(null, null, null, "Yes, I'm sure.", "No, never-mind.", null);
                yes = 4;
                no = 5;
            } else if (warning == ProgressWarning.ResetProgress) {
                theUI.menuTextFormat("Are you sure?", "All progress will be lost.", null, null, null, null, false);
                theUI.menuFormat(null, null, null, "Yes, I'm sure.", "No, never-mind.", null);
                yes = 4;
                no = 5;
            } else if (warning == ProgressWarning.PlayWithoutLoad) {
                theUI.menuTextFormat("Are you sure?", "Your saved progress", "will be overwritten", "upon level completion.", null, null, false);
                theUI.menuFormat(null, null, null, null, "Yes, I'm sure.", "No, never-mind.");
                yes = 5;
                no = 6;
            } else if (warning == ProgressWarning.LevelsWithoutLoad) {
                theUI.menuTextFormat("Are you sure?", "This action will overwrite", "your saved progress.", null, null, null, false);
                theUI.menuFormat(null, null, null, "Yes, I'm sure.", "No, never-mind.", null);
                yes = 4;
                no = 5;
            }

            menuNavigation();

            //Selecting your option
            if (keyH.isSelectionCooledDown() && (keyH.selectPressed || theCursor.isClicked)) {
                if (optionSelected == yes) {
                    if (warning == ProgressWarning.UnlockAllLevels) {
                        gameState = GameState.SaveMenu;
                        ProgressStorage.numLevelsUnlocked = levels.numberOfLevels;
                        ProgressStorage.saveGame();
                    } else if (warning == ProgressWarning.ResetProgress) {
                        gameState = GameState.SaveMenu;
                        ProgressStorage.numLevelsUnlocked = 1;
                        ProgressStorage.saveGame();
                    } else if (warning == ProgressWarning.PlayWithoutLoad) {
                        if (ProgressStorage.numLevelsUnlocked != levels.numberOfLevels) {
                            currentLevel = ProgressStorage.numLevelsUnlocked - 1;
                        } else {
                            currentLevel = 0;
                        }
                        gameState = GameState.Play;
                    } else if (warning == ProgressWarning.LevelsWithoutLoad) {
                        gameState = GameState.LevelSelect;
                        ProgressStorage.saveGame();
                    }
                } else if (optionSelected == no) {
                    if (warning == ProgressWarning.UnlockAllLevels || warning == ProgressWarning.ResetProgress) {
                        gameState = GameState.SaveMenu;
                    } else if (warning == ProgressWarning.PlayWithoutLoad || warning == ProgressWarning.LevelsWithoutLoad) {
                        gameState = GameState.Title;
                    }
                }

                theCursor.isClicked = false;
                optionSelected = 0;
                keyH.selectionCoolDownReset();
            }
        } else if (gameState == GameState.Play) {
            if (theBoard == null) {
                g2D.drawImage(theUI.menu, 0, 0, 16 * tileSize, 16 * tileSize, null);
                loadingIcon.draw(g2D);
                isPaused = true;
                isLoading = true;
            } else {
                if (isLoading) {
                    isPaused = false;
                    isLoading = false;
                }
                theBoard.drawBoard(g2D);
                theBoard.player.draw(g2D);
                for (CatEnemy aCat : theBoard.cats) {
                    aCat.draw(g2D);
                }
                theUI.drawUI();

                if (isPaused) {
                    if (theUI != null) {
                        g2D.drawImage(theUI.getImage("/map.png"), 0, 0, 16 * tileSize, 16 * tileSize, null);

                        theUI.menuFormat(null, "Resume Game", "Level Select", "Main Menu", null, null);

                        g2D.setColor(Color.BLACK);
                        g2D.setFont(theUI.titleFont);
                        g2D.drawString("Level " + theUI.trueCurrentLevel(), theUI.getXForCenteredText("Level " + (currentLevel + 1)), 10 * Cell.size);
                    }

                    menuNavigation();

                    if (keyH.pausePressed && keyH.isSelectionCooledDown()) {
                        isPaused = false;
                        optionSelected = 0;
                        keyH.selectionCoolDownReset();
                    }

                    //Selecting your option
                    if (keyH.isSelectionCooledDown() && (keyH.selectPressed || theCursor.isClicked)) {
                        if (optionSelected == 2) {
                            isPaused = false;
                        } else if (optionSelected == 3) {
                            gameState = GameState.LevelSelect;
                        } else if (optionSelected == 4) {
                            gameState = GameState.Title;
                        }

                        optionSelected = 0;
                        keyH.selectionCoolDownReset();
                        theCursor.isClicked = false;
                    }
                } else {
                    g2D.setFont(theUI.cheesyFont);
                    g2D.setColor(Color.BLACK);
                    g2D.drawString("Press Escape or E to Pause", theUI.getXForCenteredText("Press Escape or E to Pause"), (16 * tileSize) - 20);

                    if (keyH.isSelectionCooledDown() && keyH.pausePressed) {
                        isPaused = true;
                        keyH.selectionCoolDownReset();
                    }
                }
            }
        } else if (gameState == GameState.End) {
            theUI.drawEnd();
            if (keyH.isSelectionCooledDown() && keyH.selectPressed) {
                if (allLevelsFinished) {
                    gameState = GameState.Title;
                    allLevelsFinished = false;
                } else {
                    gameState = GameState.Play;
                }

                optionSelected = 0;
                keyH.selectionCoolDownReset();
            }

        }
        g2D.dispose(); //saves memory once drawing is done
    }

    /**
     * Allows the menu to be navigated.
     * The cursor can be used to navigate through the menu or the arrows/WASD keys.
     */
    private void menuNavigation() {
        //Choosing an option with arrows
        if (keyH.isKeyTypeCooledDown() && (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed)) {
            int potentialSelected = 0;
            if (optionSelected == 0) {
                // selected option when switching from mouse to keyboard.
                if (gameState == GameState.SaveMenu || gameState == GameState.Confirmation) {
                    navigateToBottomOfMenu();
                } else if (gameState == GameState.LevelSelect) {
                    optionSelected = 5;
                } else {
                    navigateToTopOfMenu();
                }
            } else if (keyH.downPressed) { //choosing the next option above.
                for (int i = theUI.numOptions; i > optionSelected; i--) {
                    if (theUI.option[i] != null) {
                        potentialSelected = i;
                    }
                }
                if (potentialSelected != 0) {
                    optionSelected = potentialSelected;
                }
            } else if (keyH.upPressed) { //choosing the next option below
                for (int i = 1; i < optionSelected; i++) {
                    if (theUI.option[i] != null) {
                        potentialSelected = i;
                    }
                }
                if (potentialSelected != 0) {
                    optionSelected = potentialSelected;
                }
            }
            keyH.keyTypeCoolDownReset();
        }

        //Choosing an option with the cursor
        if (theCursor.cursorInUse) {
            optionSelected = 0;
            for (int i = 0; i < theUI.numOptions; i++) {
                if (theCursor.x > theUI.optionLeftEdge[i] && theCursor.x < windowWidth - theUI.optionLeftEdge[i]
                        && Math.round((float) (theCursor.y + 10) / tileSize) == theUI.optionYCell[i]
                        && theUI.option[i + 1] != null) {
                    optionSelected = i + 1;
                }
            }
        } else if (theUI.option[optionSelected] == null) {
            /*Moves to the specified location of the menu when opening a
            menu area if already selected option is not available. */
            if (gameState == GameState.SaveMenu || gameState == GameState.Confirmation) {
                navigateToBottomOfMenu();
            } else if (gameState == GameState.LevelSelect) {
                optionSelected = 5;
            } else {
                navigateToTopOfMenu();
            }
        }
    }

    /**
     * Function navigateToTopOfMenu
     * Makes the selected option in the menu be the first option in the menu.
     */
    private void navigateToTopOfMenu() {
        if (!theCursor.cursorInUse) {
            int potentialSelected = 0;
            for (int i = theUI.numOptions; i > 0; i--) {
                if (theUI.option[i] != null) {
                    potentialSelected = i;
                }
            }
            if (potentialSelected != 0) {
                optionSelected = potentialSelected;
            }
        }
    }

    /**
     * Function navigateToBottomOfMenu
     * Makes the selected option in the menu be the last option in the menu.
     */
    private void navigateToBottomOfMenu() {
        if (!theCursor.cursorInUse) {
            int potentialSelected = 0;
            for (int i = 1; i <= 6; i++) {
                if (theUI.option[i] != null) {
                    potentialSelected = i;
                }
            }
            if (potentialSelected != 0) {
                optionSelected = potentialSelected;
            }
        }
    }

    /**
     * Function finishLevel
     * Enters the screen to show that the level was completed.
     * Also, sets the current level to be the next level or sets allLevelsFinished to true if the game is complete.
     */
    public void finishLevel() {
        System.out.printf("\npassing level %d ", currentLevel);
        assert (gameState == GameState.Play);
        if (!levelPassed) {
            levelPassed = true;
            if (currentLevel + 1 < levels.numberOfLevels) {
                currentLevel++;
            } else {
                currentLevel = 0;
                allLevelsFinished = true;
            }
            System.out.printf("next level is %d \n", currentLevel);
            if (ProgressStorage.numLevelsUnlocked < currentLevel + 1) {
                ProgressStorage.numLevelsUnlocked = currentLevel + 1;
                ProgressStorage.saveGame();
                System.out.printf("Unlocked level %d \n", ProgressStorage.numLevelsUnlocked);
            }
        }
        gameState = GameState.End;

    }

    /**
     * specifies the music to be played and begins the soundtrack
     *
     * @param i specifies the song to be played
     */
    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        musicIsPlaying = true;
        sound.loop();
    }

    /**
     * stops the music
     */
    public void toggleMusic() {
        if (sound != null) {
            if (musicIsPlaying) {
                sound.stop();
                musicIsPlaying = false;
            } else {
                sound.play();
                musicIsPlaying = true;
            }
        }
    }
}
