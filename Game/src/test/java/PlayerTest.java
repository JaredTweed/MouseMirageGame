//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;


public class PlayerTest {
    private Player player;
    private GameWindow gameWindow;
    private Board board;

//    @BeforeEach
    public void setup() {
        player = new Player(gameWindow,1,1);
    }

//    @Test
//    void testExceptionIsThrown() {
//        assertThrows(IllegalArgumentException.class, () -> Player.multiply(1000, 5));
//    }

    @Test
    public void aTest() {

        System.out.println("Running Unit test for player class");
        // a bunch of tests
        assertEquals(0, 0);
        assertTrue( true );
    }

    @Test
    public void playerHealthTest(){
        GameWindow gw = new GameWindow();
        Player p = new Player(gw,1,1);
        //damage of a mouse trap
        p.harm(5);
        assertEquals(5, Player.water);
        p.harm(0);
        assertEquals(5, Player.water);
        p.harm(5);
        assertEquals(0, Player.water);
    }

    @Test
    public void playerDrainTest(){
        GameWindow gw = new GameWindow();
        Player p = new Player(gw,1,1);
        for(int i = 0; i < 180; i++) {
            p.drain();
        }
        assertEquals(10, Player.water);
        p.drain();
        assertEquals(9, Player.water);
        p.drain();
        assertEquals(9, Player.water);
    }

}


