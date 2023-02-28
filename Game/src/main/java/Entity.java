import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.*;
import java.util.Objects;
import java.awt.geom.AffineTransform; // for flipping and scaling the images
import java.awt.image.AffineTransformOp;

public abstract class Entity {
    // Attributes
    protected int x;
    protected int y;
    protected double scale;
    protected int size;
    protected int speed;
    protected int diagonalSpeed;
    protected int hitRadius;
    protected int hitBoxDecrease;
    protected boolean crackedWallIsCollision;
    protected boolean isColliding = false;
    protected boolean topLeftIsColliding = false;
    protected boolean topRightIsColliding = false;
    protected boolean bottomLeftIsColliding = false;
    protected boolean bottomRightIsColliding = false;

    protected Direction direction = Direction.Down;

    // for sprite animations
    protected int TailFrameCounter = 1;
    protected int currentTailFrame = 0;
    protected int currentLegFrame = 0;
    protected int numberOfLegFrames = 6;
    protected int numberOfTailFrames = 6;
    protected String imagePath;
    public BufferedImage body, tail, legs, bodyUp, bodyDown, bodyLeft, bodyRight;
    public BufferedImage[] legsDown = new BufferedImage[numberOfLegFrames];
    public BufferedImage[] legsUp = new BufferedImage[numberOfLegFrames];
    public BufferedImage[] legsLeft = new BufferedImage[numberOfLegFrames];
    public BufferedImage[] legsRight = new BufferedImage[numberOfLegFrames];
    public BufferedImage[] tailDown = new BufferedImage[numberOfTailFrames];
    public BufferedImage[] tailUp = new BufferedImage[numberOfTailFrames];
    public BufferedImage[] tailLeft = new BufferedImage[numberOfTailFrames];
    public BufferedImage[] tailRight = new BufferedImage[numberOfTailFrames];

    /**
     * Constructor for Entity
     * the images for Entity is set to null at initialization
     */
    public Entity() {
        body = null;
        legs = null;
        tail = null;
    }

    /**
     * Function getImage
     * will preprocess the different images for the instance of entity ready to be drawn on the screen
     */
    protected void getImage() {
        try {
            AffineTransform t = AffineTransform.getScaleInstance(-1, 1);
            t.translate(-Cell.size, 0);
            AffineTransformOp flipX = new AffineTransformOp(t, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

            t = AffineTransform.getScaleInstance(scale, scale);
            AffineTransformOp scale = new AffineTransformOp(t, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

            for(int i = 0; i < numberOfLegFrames; i++) {
                legsDown[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + "down/" + "legs" + i + ".png")));
                legsUp[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + "up/" + "legs" + i + ".png")));
                legsRight[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + "right/" + "legs" + i + ".png")));
                legsLeft[i] = flipX.filter(legsRight[i], null);
                legsDown[i] = scale.filter(legsDown[i], null);
                legsUp[i] = scale.filter(legsUp[i], null);
                legsRight[i] = scale.filter(legsRight[i], null);
                legsLeft[i] = scale.filter(legsLeft[i], null);
                //                legsLeft[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + "left/" + "legs" + i + ".png")));
            }
            for(int i = 0; i < numberOfTailFrames; i++) {
                tailDown[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + "down/" + "tail" + i + ".png")));
                tailUp[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + "up/" + "tail" + i + ".png")));
                tailRight[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + "right/" + "tail" + i + ".png")));
                tailLeft[i] = flipX.filter(tailRight[i], null);
                tailDown[i] = scale.filter(tailDown[i], null);
                tailUp[i] = scale.filter(tailUp[i], null);
                tailRight[i] = scale.filter(tailRight[i], null);
                tailLeft[i] = scale.filter(tailLeft[i], null);
//                tailLeft[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + "left/" + "tail" + i + ".png")));
            }
            bodyDown = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + "down/" + "body.png")));
            bodyUp = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + "up/" + "body.png")));
            bodyRight = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + "right/" + "body.png")));
            bodyLeft = flipX.filter(bodyRight, null);
            bodyDown = scale.filter(bodyDown, null);
            bodyUp = scale.filter(bodyUp, null);
            bodyRight = scale.filter(bodyRight, null);
            bodyLeft = scale.filter(bodyLeft, null);
            body = bodyDown;
            tail = tailDown[0];
            legs = legsDown[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Function changeDirectionTo
     *
     * @param newDirection the direction the entity will turn to face
     */
    protected void changeDirectionTo(Direction newDirection) {
        direction = newDirection;
        updateSprite();
    }

    /**
     * Function draw
     * Draws the current image entities
     *
     * @param g2D the Graphics2D for drawing pngs of Entities
     */
    public void draw(Graphics2D g2D) {
        if ((body != null) && (legs != null) && (tail != null)) {
            g2D.drawImage(body, x, y,null);
            g2D.drawImage(legs, x, y,null);
            g2D.drawImage(tail, x, y,null);
        }
    }

    public void updateSprite() {
        switch (direction) {
            case Down:
                body = bodyDown;
                legs = legsDown[currentLegFrame];
                tail = tailDown[currentTailFrame];
                break;
            case Up:
                body = bodyUp;
                legs = legsUp[currentLegFrame];
                tail = tailUp[currentTailFrame];
                break;
            case Left:
                body = bodyLeft;
                legs = legsLeft[currentLegFrame];
                tail = tailLeft[currentTailFrame];
                break;
            case Right:
                body = bodyRight;
                legs = legsRight[currentLegFrame];
                tail = tailRight[currentTailFrame];
                break;
        }
    }

    /**
     * Function nextTail()
     * updates the entities current image once every second
     *
     */
    public void nextTail(){
        TailFrameCounter++;
        // sprite counter is incremented 60 times a second == FPS
        if(TailFrameCounter > GameWindow.FPS/numberOfTailFrames) {
            // entities sprites are looped through once every second
            currentTailFrame = (currentTailFrame + 1) % numberOfTailFrames;
            TailFrameCounter = 1;
            updateSprite();
        }
//        System.out.printf("next sprite %d, %d \n", currentSprite, spriteCounter);
    }

    /**
     * Function nextLegs()
     * updates the entities current image once every second
     *
     */
    public void nextLegs() {
        currentLegFrame = (currentLegFrame + 1) % numberOfLegFrames;
        updateSprite();
    }

    /**
     * Function inCollisionCell
     * checks if player is in a collision
     *
     * @param X            pixel column position of Entity
     * @param Y            pixel row position of Entity
     */
    protected boolean inCollisionCell(int X, int Y) {
        //if in the index range of the 2D list

        int cellX = (int) Math.floor((double) X / Cell.size);
        int cellY = (int) Math.floor((double) Y / Cell.size);
        Board board = Board.getInstance();

        if (0 <= cellX && cellX <= Board.columns && 0 <= cellY && cellY <= Board.rows) {
            //checking if in floor
            if (board.cells[cellX][cellY].collision) {
                isColliding = true;
                return true;
            }

            if (crackedWallIsCollision && board.cells[cellX][cellY].isCrackedWall) {
                isColliding = true;
                return true;
            }
        }
        isColliding = false;
        return false;
    }

    /**
     * Function moveUp
     * will move the player up if there are no obstructions
     */
    protected void moveUp() {
        moveUpBy(speed);
    }

    /**
     * Function moveUpBy
     * will move the player up by specified velocity if there are no obstructions
     *
     * @param velocity is the number of pixels that entity will be displaced
     */
    protected void moveUpBy(int velocity) {
        if(velocity == 0) {
            return;
        }
        nextLegs();
        topLeftIsColliding = inCollisionCell(x + hitBoxDecrease, y - velocity + hitBoxDecrease);
        topRightIsColliding = inCollisionCell( x + size - hitBoxDecrease, y - velocity + hitBoxDecrease);

        if(topLeftIsColliding || topRightIsColliding) {
            if(!topLeftIsColliding) {
                x -= speed/2;
            }
            if(!topRightIsColliding) {
                x += speed/2;
            }
        }else {
            // move entity as expected
            changeDirectionTo(Direction.Up);
            y -= velocity;
        }
    }

    /**
     * Function moveDown
     * will move the player down if there are no obstructions
     */
    protected void moveDown() {
        moveDownBy(speed);
    }

    /**
     * Function moveDownBy
     * will move the player down by specified velocity if there are no obstructions
     *
     * @param velocity is the number of pixels that entity will be displaced
     */
    protected void moveDownBy(int velocity) {
        if(velocity == 0) {
            return;
        }
        nextLegs();
        bottomLeftIsColliding = inCollisionCell(x + hitBoxDecrease, y + velocity + size - hitBoxDecrease);
        bottomRightIsColliding = inCollisionCell(x + size - hitBoxDecrease, y + velocity + size - hitBoxDecrease);

        if(bottomLeftIsColliding || bottomRightIsColliding) {
            // move entity to prevent getting stuck
            if(!bottomLeftIsColliding) {
                x -= speed/2;
            }
            if(!bottomRightIsColliding) {
                x += speed/2;
            }
        }else {
            // move entity as expected
            changeDirectionTo(Direction.Down);
            y += velocity;
        }
    }

    /**
     * Function moveLeft
     * will move the player left if there are no obstructions
     */
    protected void moveLeft() {
        moveLeftBy(speed);
    }

    /**
     * Function moveLeftBy
     * will move the player left by specified velocity if there are no obstructions
     *
     * @param velocity is the number of pixels that entity will be displaced
     */
    protected void moveLeftBy(int velocity) {
        if(velocity == 0) {
            return;
        }
        nextLegs();
        topLeftIsColliding = inCollisionCell(x - velocity + hitBoxDecrease, y + hitBoxDecrease);
        bottomLeftIsColliding = inCollisionCell(x - velocity + hitBoxDecrease, y + size - hitBoxDecrease);

        if(topLeftIsColliding || bottomLeftIsColliding) {
            // move entity to prevent getting stuck
            if(!topLeftIsColliding) {
                y -= speed/2;
            }
            if(!bottomLeftIsColliding) {
                y += speed/2;
            }
        }else {
            // move entity as expected
            changeDirectionTo(Direction.Left);
            x -= velocity;
        }
    }

    /**
     * Function moveRight
     * Will move the player right if there are no obstructions
     */
    protected void moveRight() {
        moveRightBy(speed);
    }


    /**
     * Function moveRightBy
     * will move the player right by specified velocity if there are no obstructions
     *
     * @param velocity is the number of pixels that entity will be displaced
     */
    protected void moveRightBy(int velocity) {
        if(velocity == 0) {
            return;
        }
        nextLegs();
        topRightIsColliding = inCollisionCell(x + velocity + size - hitBoxDecrease, y + hitBoxDecrease);
        bottomRightIsColliding = inCollisionCell(x + velocity + size - hitBoxDecrease, y + size - hitBoxDecrease);

        if(topRightIsColliding || bottomRightIsColliding) {
            // move entity to prevent getting stuck
            if(!topRightIsColliding) {
                y -= speed/2;
            }
            if(!bottomRightIsColliding) {
                y += speed/2;
            }
        }else {
            // move entity as expected
            changeDirectionTo(Direction.Right);
            x += velocity;
        }
    }

    /**
     * Function preventWindowEscape
     * Will move the player if the new position exists in the board
     *
     */
    protected void preventWindowEscape() {
        double playerCellX = (double) x / Cell.size;
        double playerCellY = (double) y / Cell.size;
        int collisionCount = 0;

        //preventing player from going through right edge
        if ((int) Math.floor(playerCellX) > Board.columns - 2) {
            isColliding = true;
            collisionCount++;
            x = Cell.size * (Board.columns - 1) - 1;
        }
        //preventing player from going through bottom edge
        if ((int) Math.floor(playerCellY) > Board.rows - 2) {
            isColliding = true;
            collisionCount++;
            y = Cell.size * (Board.rows - 1) - 1;
        }
        //preventing player from going through left edge
        if (x <= 0) {
            isColliding = true;
            collisionCount++;
            x = 1;
        }
        //preventing player from going through top edge
        if (y <= 0) {
            isColliding = true;
            collisionCount++;
            y = 1;
        }

        if (collisionCount == 0) {
            isColliding = false;
        }
    }

    // Getters
    /**
     * Function getX
     *
     * @return x the entities x position
     */
    public int getX() {
        return x;
    }

    /**
     * Function getY
     *
     * @return y the entities y position
     */
    public int getY() {
        return y;
    }

    /**
     * Function trueX
     *
     * @return x the position for the center of the entity
     */
    public int trueX() {
        return x + size/2;
    }

    /**
     * Function trueY
     *
     * @return y the position for the center of the entity
     */
    public int trueY() {
        return y + size/2;
    }
}
