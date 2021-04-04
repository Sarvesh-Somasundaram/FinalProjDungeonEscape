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
 * @author Sarvesh Somasundaram
 */
public class PlayerComponent extends Component {

    enum MoveDirection {
        UP, RIGHT, DOWN, LEFT, IDLE
    }

    private CellMoveComponent moveComponent;
    private AStarMoveComponent astar;

    public Entity getPlayer() {
        return getGameWorld().getSingleton(PLAYER);
    }


    private MoveDirection currentMoveDir = IDLE;
    private MoveDirection nextMoveDir = RIGHT;

    public void up() {
        nextMoveDir = UP;
    }

    public void down() {
        nextMoveDir = DOWN;
    }

    public void left() {
        nextMoveDir = LEFT;
    }

    public void right() {
        nextMoveDir = RIGHT;
    }

    public void stop() {
        nextMoveDir = IDLE;
    }

    public void teleport(int x, int y, int playerX, int playerY) {
        if (astar.getGrid().get(x, y).getState().isWalkable() && astar.getGrid().get(x, y).distance(astar.getGrid().get(playerX, playerY)) < 3) {
            getPlayer().setPosition(x*40, y*40);
            DungeonEscapeApp.teleportCounter++;
        }
    }


    @Override
    public void onUpdate(double tpf) {
        var x = moveComponent.getCellX();
        var y = moveComponent.getCellY();


        if (astar.isMoving())
            return;

        switch (nextMoveDir) {
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

        switch (currentMoveDir) {
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
            case IDLE:
                astar.stopMovement();
                break;
        }
    }

}
