//package GameTest;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;


public class EntityTest {
    @Test
    public void aTest() {
        System.out.println("Running Unit test for Entity class");

        assertEquals(0, 0);
        assertTrue( true );
    }

    @Test
    public void imageTest() {
        System.out.println("Running Unit test for Entity class");
        GameWindow gw = new GameWindow();
        CatEnemy cat = new CatEnemy(1,1);
        Player player = new Player(gw,4,4);
        assertNotNull(player.body);
        assertNotNull(player.tail);
        assertNotNull(player.legs);
        assertNotNull(cat.body);
        assertNotNull(cat.tail);
        assertNotNull(cat.legs);
    }

    @Test
    public void positionTest(){
        CatEnemy cat = new CatEnemy(1,1);
        assertEquals(Cell.size, cat.getX());
        assertEquals(Cell.size, cat.getY());
    }

    @Test
    public void movementTest(){
        GameWindow gw = new GameWindow();
        Board board = Board.instance(16,16,"mapTest.txt",gw);
        CatEnemy cat = board.cats.get(0);
        assertEquals(11*Cell.size, cat.getX());
        assertEquals(8*Cell.size, cat.getY());

        cat.moveDownBy(Cell.size);

        assertEquals(11*Cell.size, cat.getX());
        assertEquals(9*Cell.size, cat.getY());

        cat.moveRightBy(Cell.size);

        assertEquals(12*Cell.size, cat.getX());
        assertEquals(9*Cell.size, cat.getY());

        cat.moveUpBy(Cell.size);

        assertEquals(12*Cell.size, cat.getX());
        assertEquals(8*Cell.size, cat.getY());

        cat.moveLeftBy(Cell.size);
        cat.moveLeftBy(Cell.size);
        cat.moveLeftBy(Cell.size);
        cat.moveLeftBy(Cell.size);
        cat.moveLeftBy(Cell.size);

        assertEquals(7*Cell.size, cat.getX());
        assertEquals(8*Cell.size, cat.getY());

        cat.moveLeftBy(Cell.size);

        assertEquals(7*Cell.size, cat.getX());
        assertEquals(8*Cell.size, cat.getY());
    }
}
