package com.sarveshapps.dungeonescape.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;

import static com.sarveshapps.dungeonescape.components.MagicComponent.MagicDirection.*;


/**
 *  This class creates a new Component for the magic bolt
 *
 *  @author Sarvesh Somasundaram
 *  @version 1.0
 *
 *  @see <a href=" https://github.com/Sarvesh1234567/FinalProjDungeonEscape ">Github Link to Project Repo</a>
 *  @see <a href=" https://github.com/AlmasB/FXGL ">JavaFX Game Library</a>
 *  @see <a href=" https://opengameart.org/ ">Textures, animations and backgrounds</a>
 *
 */

public class MagicComponent extends Component {


    /**
     * This is all the directions that the magic bolt can travel in
     */
    enum MagicDirection {
        UP, RIGHT, DOWN, LEFT
    }

    private CellMoveComponent moveComponent; // all the components needed to move through the gameWorld grid
    private AStarMoveComponent astar;

    private MagicDirection currentMoveDir = UP; // setting the inital values for the movement direction
    private MagicDirection nextMoveDir = RIGHT;

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
     * This method evaluates the current and next movement directions to move the object in the proper direction
     *
     * @param tpf
     */
    @Override
    public void onUpdate(double tpf) {
        var x = moveComponent.getCellX(); // getting the x and y cell position of the magic bolt
        var y = moveComponent.getCellY();


        if (astar.isMoving()) // if its moving then ignore any input
            return;

        switch (nextMoveDir) { // change the current movement direction to the next movement direction if the cell that you are moving to is walkable
            case UP:
                if (astar.getGrid().getUp(x, y).filter(c -> c.getState().isWalkable()).isPresent())
                    currentMoveDir = nextMoveDir;
                break;
            case RIGHT:
                if (astar.getGrid().getRight(x, y).filter(c -> c.getState().isWalkable()).isPresent())
                    currentMoveDir = nextMoveDir;
                break;
            case DOWN:
                if (astar.getGrid().getDown(x, y).filter(c -> c.getState().isWalkable()).isPresent())
                    currentMoveDir = nextMoveDir;
                break;
            case LEFT:
                if (astar.getGrid().getLeft(x, y).filter(c -> c.getState().isWalkable()).isPresent())
                    currentMoveDir = nextMoveDir;
                break;
        }

        switch (currentMoveDir) { // use the new currentMoveDir to move the magic entity up, down, left, or right
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
        }
    }
}
