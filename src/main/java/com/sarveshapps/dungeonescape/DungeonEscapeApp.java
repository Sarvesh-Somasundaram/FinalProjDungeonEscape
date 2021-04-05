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
 *  This is the main class that controls the game application, scenes,
 *  settings, components, and inputs and actions
 *
 *  @author Sarvesh Somasundaram
 *  @version 1.0
 *
 *  @see <a href=" https://github.com/Sarvesh1234567/FinalProjDungeonEscape ">Github Link to Project Repo</a>
 *  @see <a href=" https://github.com/AlmasB/FXGL ">JavaFX Game Library</a>
 *  @see <a href=" https://opengameart.org/ ">Textures, animations and backgrounds</a>
 *
 */

/*
    See README on more info on running the project if your having trouble launching the game
 */

public class DungeonEscapeApp extends GameApplication {

    /**
     * This is the number of levels the game generates
     */
    private static final int MAX_LEVEL = 4;

    /**
     * This is the level the game starts on
     */
    private static final int STARTING_LEVEL = 0;

    /**
     * This is the size of each cell component in pixels
     */
    public static final int BLOCK_SIZE = 40;
    /**
     * This is the number of cells in the height and width of the map
     */
    public static final int MAP_SIZE = 17;

    /**
     * This is the total number of teleports allowed in the game
     */
    public static final int NUM_TELEPORTS = 5;
    /**
     * This is a counter which updates how many times the user has teleported
     */
    public static int teleportCounter = 0;


    /**
     * This is the method that initializes all the settings of the application such as app width, height, and title as
     * well as resizability and loading screen
     *
     * @param settings Game settings used by the game library
     */
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(MAP_SIZE * BLOCK_SIZE); //  setting the width and height of the map to be the number of cells times the pixels per cell
        settings.setHeight(MAP_SIZE * BLOCK_SIZE);
        settings.setTitle("Dungeon Escape"); // setting the title of the app
        settings.setVersion("1.0"); // setting the version of the app to 1.0
        settings.setManualResizeEnabled(true); // making the window resizable while preserving the aspect ratio
        settings.setPreserveResizeRatio(true);
        settings.setSceneFactory(new SceneFactory() { // setting a new scene
            @NotNull
            @Override
            public LoadingScene newLoadingScene() { // Launching a new loading scene
                return new GameLoadingScene();
            }
        });
    }

    /**
     * This method gets a single player entity from the gameWorld
     *
     * @return a Player entity
     */
    public Entity getPlayer() {
        return getGameWorld().getSingleton(PLAYER);
    }


    /**
     * This method gets the playerComponent of a player entity from getPlayer() method
     *
     * @return a playerComponent of a player entity
     */
    public PlayerComponent getPlayerComponent() {
        return getPlayer().getComponent(PlayerComponent.class);
    }

    /**
     * This method spawns a single Magic entity at the player's x and y location
     *
     * @return a Magic entity
     */
    public Entity getMagic() {
        return spawn("Magic", getPlayer().getX(), getPlayer().getY() + 5);
    }

    /**
     * This method gets the playerComponent of a magic entity from getMagic() method
     *
     * @return a magicComponent for a magic entity
     */
    public MagicComponent getMagicComponent() {
        return getMagic().getComponent(MagicComponent.class);
    }


    /**
     * This method initializes the inputs for the game to control the player
     */
    @Override
    protected void initInput() {

        getInput().addAction(new UserAction("Up") { // Adds the up action to move player up
            @Override
            protected void onAction() {
                getPlayerComponent().up();
            }

            @Override
            protected void onActionEnd() {
                getPlayerComponent().stop();
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Down") { // Adds the down action to move player down
            @Override
            protected void onAction() {
                getPlayerComponent().down();
            }

            @Override
            protected void onActionEnd() {
                getPlayerComponent().stop();
            }
        }, KeyCode.S);

        getInput().addAction(new UserAction("Left") { // Adds the left action to move player left
            @Override
            protected void onAction() {
                getPlayerComponent().left();
            }

            @Override
            protected void onActionEnd() {
                getPlayerComponent().stop();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Right") { // Adds the right action to move player right
            @Override
            protected void onAction() {
                getPlayerComponent().right();
            }

            @Override
            protected void onActionEnd() {
                getPlayerComponent().stop();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Shoot Up") { // Adds the up action to shoot magic up
            @Override
            protected void onActionEnd() {
                getMagicComponent().up();
            }
        }, KeyCode.UP);

        getInput().addAction(new UserAction("Shoot Down") { // Adds the down action to shoot magic down
            @Override
            protected void onActionEnd() {
                getMagicComponent().down();
            }
        }, KeyCode.DOWN);

        getInput().addAction(new UserAction("Shoot Right") { // Adds the right action to shoot magic right
            @Override
            protected void onActionEnd() {
                getMagicComponent().right();
            }
        }, KeyCode.RIGHT);

        getInput().addAction(new UserAction("Shoot Left") { // Adds the left action to shoot magic left
            @Override
            protected void onActionEnd() {
                getMagicComponent().left();
            }
        }, KeyCode.LEFT);

        getInput().addAction(new UserAction("Teleport") { // Adds the teleport action to teleport player
            @Override
            protected void onActionEnd() {
                if (teleportCounter == NUM_TELEPORTS) { // if the number of allowed teleports is reached, then the player does not teleport
                    showMessage("You are out of Teleports!");
                }

                else {
                    /*
                        Gets the clicked cell's x and y values and converts to cell coordinates and gets the player's
                        x and y values and converts to cell coordinates by dividing by 40.
                        THen calls on the teleport function in PlayerComponent with the x and y coordinates as input
                     */
                    int xCellVal = (int)getInput().getMouseXWorld()/40;
                    int yCellVal = (int)getInput().getMouseYWorld()/40;
                    int playerX = (int)getPlayer().getX()/40;
                    int playerY = (int)getPlayer().getY()/40;
                    getPlayerComponent().teleport(xCellVal, yCellVal, playerX, playerY);
                }
            }
        }, MouseButton.PRIMARY);
    }

    /**
     * This method initializes the game variable for the level.
     *
     * @param vars value of STARTING_LEVEL
     */
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("level", STARTING_LEVEL);
    }

    /**
     * This method initializes the game itself including background, adding entities, and generating and starting the levels.
     */
    @Override
    protected void initGame() {
        getGameScene().setBackgroundColor(Color.BLACK); // sets the background color and repeating floor image
        getGameScene().setBackgroundRepeat("floor.jpg");
        getGameWorld().addEntityFactory(new DungeonEntityFactory()); // adds the DungeonEntityFactory tp the gameWorld to be able to add entities

        LevelGen(); // Calls on LevelGen to generate 5 new random levels in text files
        setLevel(geti("level")); // sets the level to the starting level
    }

    /**
     * This method initializes the collision physics for the game.
     */
    @Override
    protected void initPhysics() {
        onCollisionBegin(PLAYER, ENEMY, (p, e) -> onPlayerKilled()); // lambda function to call onPlayerKilled when player is killed by enemy

        onCollision(PLAYER, EXIT, (p, f) -> nextLevel()); // lambda function to call nextLevel() when player beats level

        onCollisionBegin(MAGIC, ENEMY, (m, e) -> onEnemyKilled(m, e)); // lambda function to call onEnemyKilled() when enemy is killed by player

        onCollisionEnd(MAGIC, WALL, (m, w) -> onHitWall(m)); // lambda function to call onHitWall() when magic bolt hits wall

    }

    /**
     * This method is called when the game updates when the player is killed to restart the level
     *
     * @param tpf
     */
    @Override
    protected void onUpdate(double tpf) {
        if (requestNewGame) { // if player is killed then requestNewGame is set to True and onUpdate, the level will be restarted
            requestNewGame = false;
            setLevel(geti("level")); // sets the same level that the player died on
        }
    }


    /**
     * This method checks if the game is beat or increases the level variable and sets a new variable using setLevel()
     */
    private void nextLevel() {
        if (geti("level") == MAX_LEVEL) { // if all levels are beaten
            showMessage("You won the game!");
            return;
        }

        inc("level", +1); // increases level and starts the new level
        setLevel(geti("level"));
    }

    /**
     * This method loads the new level text file into a A* grid in the game and starts the level
     *
     * @param levelNum level that the player is on, from 0-4
     */
    private void setLevel(int levelNum) {
        Level level = getAssetLoader().loadLevel("levels" + levelNum + ".txt", new TextLevelLoader(40, 40, ' '));
        getGameWorld().setLevel(level); // loads the level text file that levelNum is on and sets that level in the gameWorld

        // initializes the A* underlying grid on the gameWorld and marks cells where cells are as not walkable
        AStarGrid grid = AStarGrid.fromWorld(getGameWorld(), MAP_SIZE, MAP_SIZE, BLOCK_SIZE, BLOCK_SIZE, (type) -> {
            if (type == WALL) // if the type is WALL or 1 instead of Floor (0)
                return CellState.NOT_WALKABLE;

            return CellState.WALKABLE;
        });

        set("grid", grid); // sets "grid" var to grid for DungeonEntityFactory PLAYER entity
        set("teleports", NUM_TELEPORTS); // resets the number of teleports allowed
    }

    /**
     * This boolean is indicative of whether the player has died or not
     */
    private boolean requestNewGame = false;

    /**
     * This method is called when the player has been killed and sets requestNewGame to true
     */
    public void onPlayerKilled() {
        requestNewGame = true;
    }

    /**
     * This method is called when a magic entity hits the wall
     *
     * @param magic magic entity
     */
    public void onHitWall(Entity magic) {
        magic.setVisible(false); // sets the magic bolt to invisible so that it doesn't stay on the game Scene
    }

    /**
     * This method is called when an enemy is killed by magic
     *
     * @param magic magic entity
     * @param enemy enemy entity
     */
    public void onEnemyKilled(Entity magic, Entity enemy) {
        magic.removeFromWorld(); // removes the magic and enemy entities from the game scene so that the player can move around
        enemy.removeFromWorld();

    }

    /**
     * Launches the game
     * 
     * @param args launch arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
