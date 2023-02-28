import java.awt.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Board {

    // Attributes
    public static int columns = 16;
    public static int rows = 16;
    public int floorCount = 0;
    public int[][] floorCellList;
    public Cell[][] cells;
    public StationaryCharacter[][] sChar;
    private static Board theBoard = null;
    Player player = null;
    ArrayList<CatEnemy> cats = new ArrayList<>();
    static int maxCheese;
    static int[] exitDoorCoordinates = new int[2];
    GameWindow gameWindow;
    int gameWindowColumns;
    int gameWindowRows;

    //updater for instantiating water
    UpdateWater waterUpdater;


    // Constructor Singleton class

    /**
     * Constructor for Board
     * When first initialized this constructor creates a Cell 2D array and StationaryCharacter 2D array.
     * Then loads the boards Cells and StationaryCharacters
     *
     * @param columns the column size used to initialize the board
     * @param rows    the row size used to initialize the board
     * @param map     the map which is used to design the board
     */
     private Board(int columns, int rows, String map, GameWindow gw) {
         theBoard = this;
         System.out.println("creating a Board");
         cells = new Cell[columns][rows];
         sChar = new StationaryCharacter[columns][rows];
         floorCellList = new int[rows * columns][2];
         maxCheese = 0;
         gameWindow = gw;
         gameWindowColumns = columns;
         gameWindowRows = rows;
         getBoard(map);
         waterUpdater = new UpdateWater();
    }

    /**
     * Returns an instance of the singleton Board
     * This is a singleton constructor created to limit the number of created boards to 1.
     * If the board is uninitialized it will create a new board otherwise it will return the instance.
     *
     * @param columns the column size used to initialize the board
     * @param rows    the row size used to initialize the board
     * @param map     the map which is used to design the board
     * @return the singleton instance of the board
     */
    public static Board instance(int columns, int rows, String map, GameWindow gw) {
        theBoard = new Board(columns, rows, map, gw);
        return theBoard;
    }

    /**
     * Returns an instance of the singleton Board
     * If the board is uninitialized it will release a print statement otherwise it will return the instance.
     *
     * @return the singleton instance of the board
     */
    public static Board getInstance(){
        if(theBoard == null){
            System.out.print("Board not yet instantiated");
        }
        return theBoard;
    }

    /**
     * Instantiates random water droplet.
     * Water droplet instantiates at random intervals for random lifetimes
     */
    public void updateWater() {
       waterUpdater.updateWater();
    }

    /**
     * Destroys the stationary object.
     * The stationary objected will be destroyed at the given location.
     * The floor cell will be made available for use again.
     *
     * @param coordinates the coordinates of the water that will be destroyed
     */
    public void destroyStationary(int[] coordinates) {
        //adds the coordinates of the destroyed water to the list of empty floor cells
        floorCellList[floorCount][0] = coordinates[0];
        floorCellList[floorCount][1] = coordinates[1];
        floorCount++;

        sChar[coordinates[0]][coordinates[1]] = null;
    }

    public void addCat(int CellX, int CellY){
        cats.add(new CatEnemy(CellX, CellY));
    }

    public void clearCats() {
        cats.clear();
    }

    /**
     * loads the Board from a dedicated map file
     * when called this method scans the map file and fills cells[][] and sChar[][] with their
     * respective objects to give locations.
     *
     * @param map the map which is used to design the board
     */
    private synchronized void getBoard(String map) {
        System.out.println("Getting board");
        try {
            clearCats();
            //subject to change
            InputStream mapInput = getClass().getClassLoader().getResourceAsStream(map);
            Scanner myReader = new Scanner(mapInput);
            //x and y
            int x = 0;
            int y = 0;
            System.out.println("board initiate");
            while (myReader.hasNext()) {
                String[] cellType = myReader.nextLine().split("");
                for (String c : cellType) {
                    gameWindow.repaint();
                    switch (c) {
                        //wall
                        case "w":
                            System.out.print("w");
                            cells[x][y] = new Wall();
                            sChar[x][y] = null;
                            break;
                        //cracked wall
                        case "m":
                            System.out.print("m");
                            cells[x][y] = new CrackedWall();
                            sChar[x][y] = null;
                            break;
                        case " ":
                            System.out.print(" ");
                            cells[x][y] = new Floor();
                            sChar[x][y] = null;

                            //a list of empty floor cells
                            floorCellList[floorCount][0] = x;
                            floorCellList[floorCount][1] = y;
                            floorCount++;
                            break;
                        //water
                        case "h":
                            System.out.print("h");
                            cells[x][y] = new Floor();
                            sChar[x][y] = new WaterHealthBlock();
                            break;
                        //Stationary Enemy
                        case "e":
                            System.out.print("e");
                            cells[x][y] = new Floor();
                            sChar[x][y] = new StationaryEnemy();
                            break;
                        //Cheese
                        case "c":
                            System.out.print("c");
                            cells[x][y] = new Floor();
                            sChar[x][y] = new CheeseScoreBlock();
                            maxCheese++;
                            break;
                        //Cracked Wall
                        case "d":
                            System.out.print("d");
                            cells[x][y] = new CrackedWall();
                            sChar[x][y] = new ExitDoor();
                            exitDoorCoordinates[0] = x;
                            exitDoorCoordinates[1] = y;
                            break;
                        //Player
                        case "P":
                            System.out.print("P");
                            cells[x][y] = new Floor();
                            player = new Player(gameWindow, x, y);
                            break;
                        //Cat Enemy
                        case "C":
                            System.out.print("C");
                            cells[x][y] = new Floor();
                            addCat(x,y);
                            break;
                    }
                    x += 1;
                }
                y += 1;
                x = 0;
                System.out.println();
            }
            myReader.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        buildWalls();
    }

    /**
     * Draws the board onto a paintComponent
     * When called this method calls the draw method of every Cell and StationaryCharacter
     *
     * @param g2D graphics object
     */
    public void drawBoard(Graphics2D g2D) {
        if (cells != null) {
            for (int i = 0; i < gameWindowColumns; i++) {
                for (int j = 0; j < gameWindowRows; j++) {
                    cells[i][j].draw(g2D, i, j);
                    if (sChar[i][j] != null) {
                        sChar[i][j].draw(g2D, i, j);
                    }
                }
            }
        }
    }

    public void buildWalls() {
        if (cells != null) {
            for (int i = 0; i < columns; i++) {
                for (int j = 0; j < columns; j++) {
                    if (cells[i][j] instanceof Wall) {
                        if (cells[i][j].isCrackedWall) {
                            System.out.println("updating crackedwall image");
                            updateCrackedWallImage(i,j);
                        }else{
                            updateWallImage(i,j);
                        }
                    }
                }
            }
        }
    }

    public void updateWallImage(int i, int j) {
        String wallImageName = "";
        if ((j>0) && (cells[i][j-1] instanceof Floor)) {
            wallImageName += "top";
        }
        if ((j+1<rows) && (cells[i][j+1] instanceof Floor)) {
            wallImageName += "bottom";
        }
        if ((i>0) && (cells[i-1][j] instanceof Floor)) {
            wallImageName += "left";
        }
        if ((i+1<columns) && (cells[i+1][j] instanceof Floor)) {
            wallImageName += "right";
        }
        if(wallImageName.equals("")) {
            wallImageName = "wall";
        }
        ((Wall) cells[i][j]).setImage(wallImageName);
    }

    public void updateCrackedWallImage(int i, int j) {
        String wallImageName = "";
        if ((j>0) && ((cells[i][j-1] instanceof Floor) || (cells[i][j-1] instanceof CrackedWall))) {
            wallImageName += "top";
        }
        if ((j+1<rows) && ((cells[i][j+1] instanceof Floor)) || (cells[i][j+1] instanceof CrackedWall)) {
            wallImageName += "bottom";
        }
        if ((i>0) && ((cells[i-1][j] instanceof Floor) || (cells[i-1][j] instanceof CrackedWall))) {
            wallImageName += "left";
        }
        if ((i+1<columns) && ((cells[i+1][j] instanceof Floor) || (cells[i+1][j] instanceof CrackedWall))) {
            wallImageName += "right";
        }
        if(wallImageName.equals("")) {
            wallImageName = "crackedwall";
        }
        ((Wall) cells[i][j]).setImage(wallImageName);
    }
}
