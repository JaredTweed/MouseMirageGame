//package GameTest;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class GameWindowTest {
    @Test
    public void aTest() {
        System.out.println("Running Unit test for GameWindow class");

        assertEquals(0, 0);
        assertTrue( true );
    }
    @Test
    public void passLevelTest(){
        GameWindow gw = new GameWindow();

        gw.currentLevel = 0;
        int currentLevel = 0;
        while (gw.currentLevel < gw.levels.numberOfLevels - 1) {
            assertEquals(currentLevel, gw.currentLevel);
            gw.gameState = GameState.Play;
            gw.levelPassed = false;
            gw.finishLevel();
            currentLevel++;
            assertTrue(gw.levelPassed);
            assertEquals(currentLevel, gw.currentLevel);
            assertEquals(GameState.End, gw.gameState);
            assertFalse(gw.allLevelsFinished);
        }

        assertEquals(currentLevel, gw.currentLevel);
        gw.gameState = GameState.Play;
        gw.levelPassed = false;
        gw.finishLevel();
        assertTrue(gw.levelPassed);
        assertEquals(0, gw.currentLevel);
        assertEquals(GameState.End, gw.gameState);
        assertTrue(gw.allLevelsFinished);
    }
}
