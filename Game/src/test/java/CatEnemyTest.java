//package GameTest;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CatEnemyTest {
    CatEnemy cat = new CatEnemy(4, 10);

    @Test
    public void attributeTest(){
        assertTrue("Cat Size must be larger than zero",cat.size > 0);
        assertTrue("hit Radius must be larger than zero", cat.hitRadius > 0);
        assertTrue("hit Radius must be smaller than size",cat.hitRadius < cat.size/2);
        assertTrue("hit Box must be larger than zero",cat.hitBoxDecrease > 0);
        assertTrue("hit Box must be smaller than size",cat.hitBoxDecrease < cat.size/2);
        assertTrue("cracked walls must be collision object for cats",cat.crackedWallIsCollision);
        assertNotNull("image must always be initialized", cat.body);
        assertNotNull("image must always be initialized", cat.tail);
        assertNotNull("image must always be initialized", cat.legs);

    }

    @Test
    public void CanSeeTest() {

        GameWindow gameWindow = new GameWindow();
        Board board = Board.instance(16,16,"mapTest3.txt",gameWindow);
        CatEnemy cat = board.cats.get(0);
        cat.direction = Direction.Left;
        Player player = board.player;

        assertTrue(cat.canSee(player));
        player.moveUpBy(Cell.size);
        assertTrue(cat.canSee(player));
        player.moveUpBy(Cell.size);
        assertTrue(cat.canSee(player));
        player.moveUpBy(Cell.size);
        assertTrue(cat.canSee(player));

        // move behind first wall
        player.moveUpBy(Cell.size);
        assertFalse(cat.canSee(player));

        player.moveUpBy(Cell.size);
        assertTrue(cat.canSee(player));

        // move behind second wall
        player.moveUpBy(Cell.size);
        assertFalse(cat.canSee(player));

        // move into cracked wall
        player.moveRightBy(Cell.size);
        assertFalse(cat.canSee(player));

        // creep closer to edge of cracked wall
        player.moveRightBy(Cell.size/2);
        assertTrue(cat.canSee(player));

        // peek out of cracked wall
        player.moveRightBy(Cell.size/2);
        assertTrue(cat.canSee(player));

        player.moveLeftBy(Cell.size*2);
        player.moveUpBy(Cell.size);
        assertTrue(cat.canSee(player));

        // move behind third wall
        player.moveUpBy(Cell.size);
        assertFalse(cat.canSee(player));

        player.moveUpBy(Cell.size);
        assertTrue(cat.canSee(player));
        player.moveUpBy(Cell.size);
        assertTrue(cat.canSee(player));
        player.moveUpBy(Cell.size);
        assertTrue(cat.canSee(player));
        player.moveUpBy(Cell.size);
        assertTrue(cat.canSee(player));
        player.moveUpBy(Cell.size);
        assertTrue(cat.canSee(player));

        // return player to start and repeat by going down
    }

    @Test
    public void isFaceingTest(){
        GameWindow gameWindow = new GameWindow();
        Board board = Board.instance(16,16,"mapTest6.txt", gameWindow);
        CatEnemy cat = board.cats.get(0);
        Player player = board.player;

//        check left
        cat.direction = Direction.Right;
        assertTrue(cat.isFacing(player));
        cat.direction = Direction.Left;
        assertFalse(cat.isFacing(player));

        // check Left up
        cat.moveUpBy(Cell.size);
        assertFalse(cat.isFacing(player));
        cat.direction = Direction.Right;
        assertTrue(cat.isFacing(player));
        cat.direction = Direction.Down;
        assertTrue(cat.isFacing(player));
        cat.direction = Direction.Left;
        assertFalse(cat.isFacing(player));

//        check Up
        cat.moveRightBy(Cell.size);
        cat.direction = Direction.Down;
        assertTrue(cat.isFacing(player));
        cat.direction = Direction.Up;
        assertFalse(cat.isFacing(player));

//        check Right Up
        cat.moveRightBy(Cell.size);
        assertFalse(cat.isFacing(player));
        cat.direction = Direction.Down;
        assertTrue(cat.isFacing(player));
        cat.direction = Direction.Left;
        assertTrue(cat.isFacing(player));
        cat.direction = Direction.Up;
        assertFalse(cat.isFacing(player));

//        check right
        cat.moveDownBy(Cell.size);
        cat.direction = Direction.Left;
        assertTrue(cat.isFacing(player));
        cat.direction = Direction.Right;
        assertFalse(cat.isFacing(player));

//        check Right Down
        cat.moveDownBy(Cell.size);
        assertFalse(cat.isFacing(player));
        cat.direction = Direction.Right;
        assertFalse(cat.isFacing(player));
        cat.direction = Direction.Left;
        assertTrue(cat.isFacing(player));
        cat.direction = Direction.Up;
        assertTrue(cat.isFacing(player));

//        check Down
        cat.moveLeftBy(Cell.size);
        cat.direction = Direction.Up;
        assertTrue(cat.isFacing(player));
        cat.direction = Direction.Down;
        assertFalse(cat.isFacing(player));

//        check Left Down
        cat.moveLeftBy(Cell.size);
        assertFalse(cat.isFacing(player));
        cat.direction = Direction.Down;
        assertFalse(cat.isFacing(player));
        cat.direction = Direction.Right;
        assertTrue(cat.isFacing(player));
        cat.direction = Direction.Up;
        assertTrue(cat.isFacing(player));
    }

    @Test
    public void moveTest() {
        GameWindow gameWindow = new GameWindow();
        Board board = Board.instance(16,16,"mapTest5.txt", gameWindow);
        CatEnemy cat = board.cats.get(0);
        Player player = board.player;
        cat.direction = Direction.Right;
        assertEquals(player.trueY(), cat.trueY());

        while(Player.water > 0) {
            System.out.println(cat.trueX());
            cat.direction = Direction.Right;
            assertEquals(Direction.Right, cat.direction);
            cat.move(player);
            assertEquals(Direction.Right, cat.direction);
        }

        cat.wander();

//        System.out.println(x + "x   y" + y);
    }


}
