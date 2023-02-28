import java.util.Random;

public class UpdateWater {
    //variables for instantiating water
    private double waterLifetime = 0;
    private double timeUntilWater = 0;
    private int[] waterCoordinates = new int[2];
    private boolean dropExists = false;
    Board board;

    public UpdateWater(){
        board = Board.getInstance();
    }
    /**
     * Instantiates random water droplet.
     * Water droplet instantiates at random intervals for random lifetimes
     */
    public void updateWater() {
        if (dropExists) {
            if (waterLifetime <= 0) {
                //destroy current water
                board.destroyStationary(waterCoordinates);
                dropExists = false;

                //set time until next water instantiates
                timeUntilWater = setDoubleRange(1, 5);
            } else {
                waterLifetime -= (double) 1 / 60;
            }
        } else {
            if (timeUntilWater <= 0) {
                //instantiates new water with a given lifetime
                waterLifetime = setDoubleRange(1.5, 3);
                waterCoordinates = generateRandomWater();

                dropExists = true;
            } else {
                timeUntilWater -= (double) 1 / 60;


            }
        }
    }

    /**
     * returns a random double between the given range.
     *
     * @param rangeMin the lowest possible value for the returned double.
     * @param rangeMax the highest possible value for the returned double.
     * @return the double between the range.
     */
    private double setDoubleRange(double rangeMin, double rangeMax) {
        //Generates a random value between a range of possible seconds from water death and birth
        return rangeMin + (rangeMax - rangeMin) * (new Random()).nextDouble();
    }

    /**
     * Instantiates water at a random location.
     * The water instantiation will occur in a random floor cell.
     *
     * @return the coordinates of the instantiated water.
     */
    private int[] generateRandomWater() {
        int[] coordinates = new int[2];

        //random coordinates from a list of all the empty floor cells. The upper-bound (exclusive) is the floorCount.
        int randomCell = (new Random()).nextInt(board.floorCount);
        coordinates[0] = board.floorCellList[randomCell][0];
        coordinates[1] = board.floorCellList[randomCell][1];


        //removes the random cell from the list of empty floor cells
        board.floorCellList[randomCell] = board.floorCellList[board.floorCount - 1];
        board.floorCount--;

        //instantiate the water
        if (board.sChar[coordinates[0]][coordinates[1]] == null) {
            board.sChar[coordinates[0]][coordinates[1]] = new WaterHealthBlock();
        } else {
            System.out.println("WATER INSTANTIATION FAILED");
        }

        return coordinates;
    }
}
