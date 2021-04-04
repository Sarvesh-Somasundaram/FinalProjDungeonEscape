package com.sarveshapps.dungeonescape;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.text.TextLevelLoader;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.sarveshapps.dungeonescape.components.DungeonEntityFactory;
import com.sarveshapps.dungeonescape.components.MagicComponent;
import com.sarveshapps.dungeonescape.components.PlayerComponent;
import com.sarveshapps.dungeonescape.uielements.GameLoadingScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.sarveshapps.dungeonescape.DungeonType.*;
import static com.sarveshapps.dungeonescape.components.LevelMaker.LevelGen;

/**
 *
 *  @author Sarvesh Somasundaram
 *
 *  Github Link to Project Repo:
 *  JavaFX Game Library: https://github.com/AlmasB/FXGL
 *  Textures, animations and backgrounds from https://opengameart.org/
 *
 *  This is the main class that controls the game application, scenes,
 *  settings, components, and inputs and actions
 */

public class DungeonEscapeApp extends GameApplication {

    private static final int MAX_LEVEL = 4;
    private static final int STARTING_LEVEL = 0;

    public static final int BLOCK_SIZE = 40;
    public static final int MAP_SIZE = 17;

    public static final int NUM_TELEPORTS = 5;
    public static int teleportCounter = 0;


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(MAP_SIZE * BLOCK_SIZE);
        settings.setHeight(MAP_SIZE * BLOCK_SIZE);
        settings.setTitle("Dungeon Escape");
        settings.setVersion("1.0");
        settings.setManualResizeEnabled(true);
        settings.setPreserveResizeRatio(true);
        settings.setSceneFactory(new SceneFactory() {
            @NotNull
            @Override
            public LoadingScene newLoadingScene() {
                return new GameLoadingScene();
            }
        });
    }

    public Entity getPlayer() {
        return getGameWorld().getSingleton(PLAYER);
    }


    public PlayerComponent getPlayerComponent() {
        return getPlayer().getComponent(PlayerComponent.class);
    }

    public Entity getMagic() {
        return spawn("Magic", getPlayer().getX(), getPlayer().getY() + 5);
    }

    public MagicComponent getMagicComponent() {
        return getMagic().getComponent(MagicComponent.class);
    }


    @Override
    protected void initInput() {

        getInput().addAction(new UserAction("Up") {
            @Override
            protected void onAction() {
                getPlayerComponent().up();
            }

            @Override
            protected void onActionEnd() {
                getPlayerComponent().stop();
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Down") {
            @Override
            protected void onAction() {
                getPlayerComponent().down();
            }

            @Override
            protected void onActionEnd() {
                getPlayerComponent().stop();
            }
        }, KeyCode.S);

        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                getPlayerComponent().left();
            }

            @Override
            protected void onActionEnd() {
                getPlayerComponent().stop();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                getPlayerComponent().right();
            }

            @Override
            protected void onActionEnd() {
                getPlayerComponent().stop();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Shoot Up") {
            @Override
            protected void onActionEnd() {
                getMagicComponent().up();
            }
        }, KeyCode.UP);

        getInput().addAction(new UserAction("Shoot Down") {
            @Override
            protected void onActionEnd() {
                getMagicComponent().down();
            }
        }, KeyCode.DOWN);

        getInput().addAction(new UserAction("Shoot Right") {
            @Override
            protected void onActionEnd() {
                getMagicComponent().right();
            }
        }, KeyCode.RIGHT);

        getInput().addAction(new UserAction("Shoot Left") {
            @Override
            protected void onActionEnd() {
                getMagicComponent().left();
            }
        }, KeyCode.LEFT);

        getInput().addAction(new UserAction("Teleport") {
            @Override
            protected void onActionEnd() {
                if (teleportCounter == NUM_TELEPORTS) {
                    showMessage("You are out of Teleports!");
                }

                else {
                    int xCellVal = (int)getInput().getMouseXWorld()/40;
                    int yCellVal = (int)getInput().getMouseYWorld()/40;
                    int playerX = (int)getPlayer().getX()/40;
                    int playerY = (int)getPlayer().getY()/40;
                    getPlayerComponent().teleport(xCellVal, yCellVal, playerX, playerY);
                }
            }
        }, MouseButton.PRIMARY);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("level", STARTING_LEVEL);
    }

    @Override
    protected void initGame() {
        getGameScene().setBackgroundColor(Color.BLACK);
        getGameScene().setBackgroundRepeat("floor.jpg");
        getGameWorld().addEntityFactory(new DungeonEntityFactory());

        LevelGen();
        setLevel(geti("level"));
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(PLAYER, ENEMY, (p, e) -> onPlayerKilled());

        onCollision(PLAYER, EXIT, (p, f) -> nextLevel());

        onCollisionBegin(MAGIC, ENEMY, (m, e) -> onEnemyKilled(m, e));

        onCollisionEnd(MAGIC, WALL, (m, w) -> onHitWall(m));

    }

    @Override
    protected void onUpdate(double tpf) {
        if (requestNewGame) {
            requestNewGame = false;
            setLevel(geti("level"));
        }
    }


    private void nextLevel() {
        if (geti("level") == MAX_LEVEL) {
            showMessage("You won the game!");
            return;
        }

        inc("level", +1);
        setLevel(geti("level"));
    }

    private void setLevel(int levelNum) {
        Level level = getAssetLoader().loadLevel("levels" + levelNum + ".txt", new TextLevelLoader(40, 40, ' '));
        getGameWorld().setLevel(level);

        // init the A* underlying grid and mark cells where blocks are as not walkable
        AStarGrid grid = AStarGrid.fromWorld(getGameWorld(), MAP_SIZE, MAP_SIZE, BLOCK_SIZE, BLOCK_SIZE, (type) -> {
            if (type == WALL)
                return CellState.NOT_WALKABLE;

            return CellState.WALKABLE;
        });

        set("grid", grid);
        set("score", 0);
        set("teleports", NUM_TELEPORTS);
    }

    private boolean requestNewGame = false;

    public void onPlayerKilled() {
        requestNewGame = true;
    }

    public void onHitWall(Entity magic) {
        magic.removeFromWorld();
    }

    public void onEnemyKilled(Entity magic, Entity enemy) {
        magic.removeFromWorld();
        enemy.removeFromWorld();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
