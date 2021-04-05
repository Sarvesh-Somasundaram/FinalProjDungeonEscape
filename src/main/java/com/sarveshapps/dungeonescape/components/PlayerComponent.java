package com.sarveshapps.dungeonescape.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.sarveshapps.dungeonescape.DungeonEscapeApp;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.sarveshapps.dungeonescape.DungeonType.PLAYER;
import static com.sarveshapps.dungeonescape.components.PlayerComponent.MoveDirection.*;

/**
 *  This class creates a new Component for the player
 *
 *  @author Sarvesh Somasundaram
 *  @version 1.0
 *
 *  @see <a href=" https://github.com/Sarvesh1234567/FinalProjDungeonEscape ">Github Link to Project Repo</a>
 *  @see <a href=" https://github.com/AlmasB/FXGL ">JavaFX Game Library</a>
 *  @see <a href=" https://opengameart.org/ ">Textures, animations and backgrounds</a>
 *
 */

public class PlayerComponent extends Component {

    /**
     * This is all the directions that the player can travel in
     */
    enum MoveDirection {
        UP, RIGHT, DOWN, LEFT, IDLE
    }

    private CellMoveComponent moveComponent; // all the components needed to move through the gameWorld grid
    private AStarMoveComponent astar;

    /**
     * This method gets the player entity from the gameWorld
     *
     * @return player entity
     */
    public Entity getPlayer() {
        return getGameWorld().getSingleton(PLAYER);
    }


    private MoveDirection currentMoveDir = IDLE; // setting the inital values for the movement direction
    private MoveDirection nextMoveDir = RIGHT;

    /**
     *  This method sets the move direction to up
     */
    public void up() {
        nextMoveDir = UP;
    }

    /**
     *  This method sets the move direction to down
     */
    public void down() {
        nextMoveDir = DOWN;
    }

    /**
     *  This method sets the move direction to left
     */
    public void left() {
        nextMoveDir = LEFT;
    }

    /**
     *  This method sets the move direction to right
     */
    public void right() {
        nextMoveDir = RIGHT;
    }

    /**
     *  This method sets the move direction to idle
     */
    public void stop() {
        nextMoveDir = IDLE;
    }

    /**
     * This method gets the player entity and teleports the player to that location if that cell is walkable on
     *
     * @param x teleport x coordinate
     * @param y teleport y coordinate
     * @param playerX players current x coordinate
     * @param playerY players current y coordinate
     */
    public void teleport(int x, int y, int playerX, int playerY) {
        // this statement checks if the cell the player is teleporting to is walkable on and if that cell is within 3 blocks of the players current location
        if (astar.getGrid().get(x, y).getState().isWalkable() && astar.getGrid().get(x, y).distance(astar.getGrid().get(playerX, playerY)) < 3) {
            getPlayer().setPosition(x*40, y*40); // sets the new position
            DungeonEscapeApp.teleportCounter++; // increments the teleportation counter
        }
    }


    /**
     * This method evaluates the current and next movement directions to move the object in the proper direction
     *
     * @param tpf
     */

    @Override
    public void onUpdate(double tpf) {
        var x = moveComponent.getCellX(); // getting the x and y cell position of the player
        var y = moveComponent.getCellY();


        if (astar.isMoving()) // if its moving then ignore any input
            return;

        switch (nextMoveDir) { // change the current movement direction to the next movement direction if the cell that the player is moving to is walkable
            case UP:
                if (astar.getGrid().getUp(x, y).filter(c -> c.getState().isWalkable()).isPresent()) {
                    currentMoveDir = nextMoveDir;
                }
                break;
            case RIGHT:
                if (astar.getGrid().getRight(x, y).filter(c -> c.getState().isWalkable()).isPresent()) {
                    currentMoveDir = nextMoveDir;
                }
                break;
            case DOWN:
                if (astar.getGrid().getDown(x, y).filter(c -> c.getState().isWalkable()).isPresent()) {
                    currentMoveDir = nextMoveDir;
                }
                break;
            case LEFT:
                if (astar.getGrid().getLeft(x, y).filter(c -> c.getState().isWalkable()).isPresent()) {
                    currentMoveDir = nextMoveDir;
                }
                break;
            case IDLE:
                currentMoveDir = nextMoveDir;
                break;
        }

        switch (currentMoveDir) { // use the new currentMoveDir to move the magic entity up, down, left, right, or idle
            case UP:
                astar.moveToUpCell();
                break;
            case RIGHT:
                astar.moveToRightCell();
                break;
            case DOWN:
                astar.moveToDownCell();
                break;
            case LEFT:
                astar.moveToLeftCell();
                break;
            case IDLE: // if idle the player stops moving
                astar.stopMovement();
                break;
        }
    }

}
