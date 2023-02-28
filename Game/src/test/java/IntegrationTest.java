//package GameTest;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class IntegrationTest {
    @Test
    public void aTest() {
        System.out.println("Running integration tests");

        assertEquals(0, 0);
        assertTrue( true );
    }

    @Test
    public void checkStationaryTest() {
        GameWindow gw = new GameWindow();
        gw.currentLevel = 0;
        gw.gameState = GameState.Play;
        Board board = Board.instance(16,16,"mapTest4.txt",gw);
        Player player = board.player;
        KeyHandler keyH = new KeyHandler(gw);
        keyH.upPressed = true;
        assertEquals(0, Player.cheeseCollected);
        int i;
        while(player.trueY() > 13.5*Cell.size) {
            player.keyMove(keyH);
        }
        assertEquals(1, Player.cheeseCollected);
        assertEquals(10, Player.water);
        while(player.trueY() > 12.5*Cell.size) {
            player.keyMove(keyH);
        }
        assertEquals(1, Player.cheeseCollected);
        assertEquals(5, Player.water);
        while(player.trueY() > 11.5*Cell.size) {
            player.keyMove(keyH);
        }
        assertEquals(6, Player.water);

        assertEquals(0, gw.currentLevel);
        while((gw.gameState == GameState.Play) && (player.trueY() > 10.5*Cell.size)) {
            System.out.println(gw.currentLevel);
            player.keyMove(keyH);
        }
        assertTrue(gw.levelPassed);
        assertEquals(1, gw.currentLevel);
    }

    @Test
    public void crackedWallTest(){
        GameWindow gw = new GameWindow();
        Board board = Board.instance(16,16,"mapTest2.txt",gw);
        Player player = board.player;
        CatEnemy cat = board.cats.get(0);
        assertEquals((int) (6.5*Cell.size),player.trueX());
        player.moveRightBy(Cell.size);
        assertEquals((int) (7.5*Cell.size),player.trueX());
        player.moveLeftBy(Cell.size);

        assertEquals((int) (8.5*Cell.size),cat.trueX());
        cat.moveLeftBy(Cell.size);
        assertEquals((int) (8.5*Cell.size),cat.trueX());
    }
}
