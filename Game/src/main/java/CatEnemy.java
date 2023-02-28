import java.util.*;

public class CatEnemy extends Entity {
    int wanderSpeed = 1;
    boolean hasTarget = false;
    int targetX = 0;
    int targetY = 0;
    int actionLockCounter;
    int timeUntilNewDirection;

    //Default Directions
    Direction currentDirection = Direction.Down;
    Direction previousDirection = Direction.Down;
    boolean movesDiagonal;

    // initialization block
    {
        scale = 1;
        speed = 4;
        size = (int) Math.floor(scale * Cell.size);
        hitRadius = (int) Math.floor(size * 0.8) / 2;
        hitBoxDecrease = (size / 2) - hitRadius;
        crackedWallIsCollision = true;
        imagePath = "/cat/";
        getImage();
    }

    /**
     * Constructor for CatEnemy
     * The CatEnemy is given a movement speed and position and images are process when initialized
     *
     * @param CellX is the initial Cell column
     * @param CellY is the initial Cell row
     */
    public CatEnemy(int CellX, int CellY) {
        super();
        x = CellX * Cell.size + Cell.size / 2 - size / 2;
        y = CellY * Cell.size + Cell.size / 2 - size / 2;
    }

    /**
     * Function attack
     * deals damage to player if the player is in attack range
     *
     * @param player the player to attack
     */
    public void attack(Player player) {
        if (canAttack(player)) {
            player.harm(1);
            GameWindow.deathCause = DeathCause.CAT;
        }
    }

    /**
     * Function canAttack
     *
     * @param player the player to check if in range
     * @return true if player is in attack range else false
     */
    private boolean canAttack(Player player) {
        return distanceTo(player) < hitRadius + player.hitRadius;
    }

    /**
     * Function setTargetAs
     * records the location of a player
     *
     * @param player the player to target
     */
    private void setTargetAs(Player player) {
        hasTarget = true;
        targetX = player.trueX();
        targetY = player.trueY();
        actionLockCounter = 0;
    }

    /**
     * Function targetIsSet
     * forgets the target after 120 calls about 2 seconds
     *
     * @return true if this entity has a target
     */
    private boolean targetIsSet() {
        actionLockCounter++;
        if (actionLockCounter > 120) {
            hasTarget = false;
        }
        return hasTarget;
    }

    /**
     * Function move
     * Determines how the cat will move
     *
     * @param player the player to be chased or attacked if possible
     */
    public void move(Player player) {
        nextTail();
        if (canSee(player)) {
            setTargetAs(player);
            chaseTarget();
            attack(player);
        } else if (targetIsSet()) {
            chaseTarget();
        } else {
            wander();
        }
    }

    /**
     * Function chaseTarget
     * makes the cat chase the target
     */
    public void chaseTarget() {
        int trueX = trueX();
        int trueY = trueY();

        int run = targetX - trueX;
        int rise = targetY - trueY;
        int distanceCatToTarget = (int) Math.sqrt(rise * rise + run * run);

        if (distanceCatToTarget < 1) {
            distanceCatToTarget = 1;
        }
        // <run,rise> this has length speed
        run = (int) Math.floor((double) speed * run / distanceCatToTarget);
        rise = (int) Math.floor((double) speed * rise / distanceCatToTarget);

        if (distanceCatToTarget >= speed) {
            if (run > 0) {
                moveRightBy(run);
            } else {
                moveLeftBy(-run);
            }
        }

        if (distanceCatToTarget >= speed) {
            if (rise > 0) {
                moveDownBy(rise);
            } else {
                moveUpBy(-rise);
            }
        }
    }

    /**
     * Function randomDirection
     * sets a random direction
     *
     * @param excluded is a direction that will not be returned.
     * @return an enum of a random direction.
     */
    public Direction randomDirection(Direction excluded) {
        previousDirection = currentDirection;

        if(excluded == null) {
            int r = new Random().nextInt(4);
            if (r == 0) {
                currentDirection = Direction.Up;
            } else if (r == 1) {
                currentDirection = Direction.Right;
            } else if (r == 2) {
                currentDirection = Direction.Down;
            } else {
                currentDirection = Direction.Left;
            }
        } else {
            int r = new Random().nextInt(3);
            if (r == 0) {
                if (Direction.Up != excluded) {
                    currentDirection = Direction.Up;
                } else {
                    currentDirection = Direction.Left;
                }
            } else if (r == 1) {
                if (Direction.Right != excluded) {
                    currentDirection = Direction.Right;
                } else {
                    currentDirection = Direction.Left;
                }
            } else {
                if (Direction.Down != excluded) {
                    currentDirection = Direction.Down;
                } else {
                    currentDirection = Direction.Left;
                }
            }
        }

        //make Cat stationary if it has the same or opposite direction as before
        if (excluded == null) {
            if (previousDirection == currentDirection) {
                currentDirection = Direction.None;
            } else if (currentDirection == Direction.Up && previousDirection == Direction.Down) {
                currentDirection = Direction.None;
            } else if (currentDirection == Direction.Left && previousDirection == Direction.Right) {
                currentDirection = Direction.None;
            } else if (currentDirection == Direction.Down && previousDirection == Direction.Up) {
                currentDirection = Direction.None;
            } else if (currentDirection == Direction.Right && previousDirection == Direction.Left) {
                currentDirection = Direction.None;
            }
        }
        return currentDirection;
    }


    /**
     * Function Wander
     * This is the cat default movement before seeing the player and after losing track of player
     */
    public void wander() {
        int chaseSpeed = speed;
        speed = wanderSpeed;
        timeUntilNewDirection--;

        if (timeUntilNewDirection <= 0 || isColliding) {
            timeUntilNewDirection = 70;

            if(isColliding) {
                currentDirection = randomDirection(currentDirection);
            } else {
                currentDirection = randomDirection(null);
            }

            movesDiagonal = new Random().nextBoolean();
        }

        if (false) { //Change this to "if (movesDiagonal) {" to reimplement diagonal wander movement
            /* I removed diagonal wander movement because I changed to wandering system to
            minimize wall collisions (diagonal movement prevented the entity from getting cornered). Also,
            the animation looks better without diagonal wander movement.*/
            if (currentDirection == Direction.Up) {
                moveUp();
                moveRight();
            }
            if (currentDirection == Direction.Down) {
                moveDown();
                moveLeft();
            }
            if (currentDirection == Direction.Left) {
                moveLeft();
                moveUp();
            }
            if (currentDirection == Direction.Right) {
                moveRight();
                moveDown();
            }
        } else {
            if (currentDirection == Direction.Up) {
                moveUp();
            }
            if (currentDirection == Direction.Down) {
                moveDown();

            }
            if (currentDirection == Direction.Left) {
                moveLeft();

            }
            if (currentDirection == Direction.Right) {
                moveRight();
            }

        }

        speed = chaseSpeed;
    }

    /**
     * Function canSee
     * The cat will track the player coordinate to know when it can see the player and then start chasing
     *
     * @param player Player class to be tracked
     * @return The status of seen or unseen
     */
    public boolean canSee(Player player) {
        // coordinates represent the center of the entity in cells not pixels
        double playerX = (double) player.trueX() / Cell.size;
        double playerY = (double) player.trueY() / Cell.size;
        double catX = (double) trueX() / Cell.size;
        double catY = (double) trueY() / Cell.size;
        double deltaY = playerY - catY;
        double deltaX = playerX - catX;
        Board board = Board.getInstance();
        int distanceCatToPlayer = (int) distanceTo(player);
        if (distanceCatToPlayer < 1) {
            // assume cat can see mouse if close
            return true;
        } else if (!isFacing(player)) {
            return false;
        }

        double precision = 0.3; // number of cells between checking for opaque objects on board

        // stretch vector from cat to player to be of length == precision
        deltaY = (deltaY * precision) / (distanceCatToPlayer);
        deltaX = (deltaX * precision) / (distanceCatToPlayer);

        // check if player is in a cracked wall or peeking out from one
        if ((board.cells[(int) playerX][(int) playerY].isOpaque) && (board.cells[(int) (playerX - deltaX / 2)][(int) (playerY - deltaY / 2)].isOpaque)) {
            return false;
        }

        // walk along a straight path from cat to player checking for opaque board objects
        double X = catX + deltaX;
        double Y = catY + deltaY;
        while (distanceTo(X, Y) < distanceCatToPlayer) {
            if (board.cells[(int) X][(int) Y].isOpaque) {
                return false;
            }
            X += deltaX;
            Y += deltaY;
        }
        return true;
    }

    /**
     * Function isFacing
     * The cat will track the player coordinate to know when it can see the player and then start chasing
     *
     * @param player Player class to be tracked
     * @return The status of true for seen or false for unseen
     */
    public boolean isFacing(Player player) {
        double deltaY = player.trueY() - trueY();
        double deltaX = player.trueX() - trueX();

        switch (direction) {
            case Down:
                if (deltaY < 0) {
                    return false;
                }
                break;
            case Up:
                if (deltaY > 0) {
                    return false;
                }
                break;
            case Left:
                if (deltaX > 0) {
                    return false;
                }
                break;
            case Right:
                if (deltaX < 0) {
                    return false;
                }
                break;
        }
        return true;
    }

    // Helper methods

    /**
     * Function distanceTo
     *
     * @param player Player class find the distance to
     * @return The distance from this Cat to the player in pixels
     */
    private double distanceTo(Player player) {
        return Math.sqrt(Math.pow(player.trueX() - this.trueX(), 2) + Math.pow(player.trueY() - this.trueY(), 2));
    }

    /**
     * Function distanceTo
     *
     * @param X x Cell column to find the distance to
     * @param Y y Cell column to find the distance to
     * @return The distance from this Cat to the player in pixels
     */
    private double distanceTo(double X, double Y) {
        return Math.sqrt(Math.pow(X * Cell.size - this.trueX(), 2) + Math.pow(Y * Cell.size - this.trueY(), 2));
    }

}
