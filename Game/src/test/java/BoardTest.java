//package GameTest;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BoardTest {

    @Test
    public void aTest() {
        System.out.println("Running Unit test for board class");

        assertEquals(0, 0);
        assertTrue( true );
    }

    @Test
    public void checkBoardStationaryCharacter(){
        GameWindow gw = new GameWindow();
        Board board = Board.instance(16,16,"mapTest.txt",gw);
        assertEquals(3,Board.maxCheese);
        assertTrue(board.sChar[1][3] instanceof CheeseScoreBlock);
        assertTrue(board.sChar[4][7] instanceof StationaryEnemy);
        assertTrue(board.sChar[12][14] instanceof WaterHealthBlock);
//        assertTrue(Board.sChar[15][1] instanceof ExitDoor);
    }

    @Test
    public void checkBoardCells(){
        GameWindow gw = new GameWindow();
        Board board = Board.instance(16,16,"mapTest.txt",gw);
        assertTrue(board.cells[3][4] instanceof Wall);
        assertFalse(board.cells[4][4] instanceof Wall);

        assertTrue(board.cells[8][12] instanceof Floor);
        assertFalse(board.cells[8][15] instanceof Floor);

        assertTrue(board.cells[6][10] instanceof CrackedWall);
        assertFalse(board.cells[6][11] instanceof CrackedWall);
    }

    @Test
    public void checkBoardEntity(){
        GameWindow gw = new GameWindow();
        Board board = Board.instance(16,16,"mapTest.txt",gw);

        assertEquals((int) (1.5*Cell.size), board.player.trueX());
        assertEquals((int) (14.5*Cell.size),board.player.trueY());

        assertFalse(board.cats.isEmpty());

        assertEquals((int) (11.5*Cell.size), board.cats.get(0).trueX());
        assertEquals((int) (8.5*Cell.size), board.cats.get(0).trueY());
    }

    @Test
    public void levelTest(){
        GameWindow gw = new GameWindow();

        int currentLevel = 0;
        while(currentLevel < gw.levels.numberOfLevels) {
            Board board = Board.instance(16,16,gw.levels.getLevel(currentLevel), gw);
            currentLevel++;
        }
    }
}
