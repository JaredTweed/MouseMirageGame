//package GameTest;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class GameTest {
//
    /**
     * Rigorous Test :-)
     */
    @Test
    public void aTest()
    {
        System.out.println("Running Unit test for Board class");
        GameWindow gw = new GameWindow();
        int numC = 16;
        int numR = 16;
        Board board = Board.instance(numC,numR,"mapTest.txt", gw);

        // a bunch of tests
        assertEquals(numC, numR);
        assertTrue( true );
    }
}

