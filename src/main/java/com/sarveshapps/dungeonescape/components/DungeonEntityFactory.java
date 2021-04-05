package com.sarveshapps.dungeonescape.components;

import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.sarveshapps.dungeonescape.DungeonEscapeApp.BLOCK_SIZE;
import static com.sarveshapps.dungeonescape.DungeonType.*;


/**
 * This class controls the entity factory to create all the entities in the game
 *
 *  @author Sarvesh Somasundaram
 *  @version 1.0
 *
 *  @see <a href=" https://github.com/Sarvesh1234567/FinalProjDungeonEscape ">Github Link to Project Repo</a>
 *  @see <a href=" https://github.com/AlmasB/FXGL ">JavaFX Game Library</a>
 *  @see <a href=" https://opengameart.org/ ">Textures, animations and backgrounds</a>
 *
 */

public class DungeonEntityFactory implements EntityFactory {

    /**
     * This method generates the information to spawn a new player on the game scene
     *
     * @param data spawndata
     * @return a new player entity
     */

    @Spawns("P") // key to generate a player from the text file
    public Entity newPlayer(SpawnData data) {
        var view = texture("Pirate.png"); // uses the texture for a pirate

        var e = entityBuilder(data) // creates an entity builder
                .type(PLAYER) // setting the type to PLAYER
                .bbox(new HitBox(new Point2D(4, 4), BoundingShape.box(32, 32))) // adds a hit box around the player
                .view(view)
                .with(new CollidableComponent(true)) // sets the player to be collidable with other objects
                .with(new CellMoveComponent(BLOCK_SIZE, BLOCK_SIZE, 200).allowRotation(false)) // allows the player to move in cells
                .with(new AStarMoveComponent(new LazyValue<>(() -> geto("grid")))) // adds the player to the A* grid
                .with(new PlayerComponent()) // ties player to the PlayerComponent movements
                .rotationOrigin(36/2.0, 30/2.0) // point that the object rotates around
                .build(); // adds to gameworld

        e.setLocalAnchorFromCenter(); // sets the anchor point to the center of the scene
        return e;
    }

    /**
     * This method generates the information to spawn a new wall on the game scene
     *
     * @param data spawndata
     * @return a new wall entity
     */

    @Spawns("1") // key to add wall from text file
    public Entity newBlock(SpawnData data) {
        var rect = new Rectangle(40, 40, Color.BLACK); // creates a rectangle with curved corners to act as walls
        rect.setArcWidth(15);
        rect.setArcHeight(15);
        rect.setStrokeWidth(0.25);
        rect.setStroke(Color.DARKGRAY);

        return entityBuilder(data)
                .type(WALL) // sets the type to WALL
                .with(new CollidableComponent(true)) //sets the wall to be collidable with the other entities
                .bbox(new HitBox(new Point2D(4, 4), BoundingShape.box(38, 38))) //adding a hitbox around the wall
                .viewWithBBox(rect) // adding the rectangle to the view
                .zIndex(-1)
                .build();
    }

    /**
     * This method generates the information to spawn a new floor on the game scene
     *
     * @param data spawndata
     * @return a new floor entity
     */

    @Spawns("0")
    public Entity newFloor(SpawnData data) {
        var view = texture("floor.jpg"); // declaring and instantiating a new texture for the floor object
        view.setTranslateX(5);
        view.setTranslateY(5);

        return entityBuilder(data)
                .type(FLOOR) // sets the type to FLOOR
                .bbox(new HitBox(new Point2D(5, 5), BoundingShape.box(30, 30))) // adds a bounding box and hitbox to the floor
                .view(view) // adds the texture to view
                .zIndex(-1)
                .build();
    }

    /**
     * This method generates the information to spawn a new enemy on the game scene
     *
     * @param data spawn data
     * @return a new enemy entity
     */

    @Spawns("E")
    public Entity newEnemy(SpawnData data) {
        var view = texture("mongbat.png"); // sets a texture for the enemy

        Entity enemy = entityBuilder(data)
                .type(ENEMY) // sets the type to ENEMY
                .bbox(new HitBox(new Point2D(4, 4), BoundingShape.box(40, 40))) // adds a hit box around the enemy
                .view(view)
                .with(new CollidableComponent(true)) // makes the ENEMY entity collidable
                .build();

        enemy.setLocalAnchorFromCenter();

        return enemy;
    }

    /**
     * This method generates the information to spawn a new exit on the game scene
     *
     * @param data spawn data
     * @return a new exit entity
     */

    @Spawns("F")
    public Entity newExit(SpawnData data) {
        var rect = new Rectangle(38, 38, Color.RED); // declares and instantiates a rectangle for the exit
        rect.setArcWidth(25);
        rect.setArcHeight(25);
        rect.setStrokeWidth(1);
        rect.setStroke(Color.ORANGE);

        return entityBuilder(data)
                .type(EXIT) // sets the type to EXIT
                .viewWithBBox(rect) // sets a bounding box on the exit
                .with(new CollidableComponent(true)) // makes the exit collidable
                .zIndex(-1)
                .build();
    }

    /**
     * This method generates the information to spawn a new magic bolt on the game scene
     *
     * @param data spawn data
     * @return a new magic entity
     */

    @Spawns("Magic")
    public Entity newMagic(SpawnData data) {
        var view = texture("shock.png"); // declares and instantiates the view to a magic bolt texture

        return entityBuilder(data)
                .type(MAGIC) // sets the type to MAGIC
                .bbox(new HitBox(new Point2D(5, 5), BoundingShape.box(30, 10))) // adds a bounding box to the magic bolt
                .view(view)
                .zIndex(-1)
                .with(new CollidableComponent(true)) // makes the magic collidable
                .with(new CellMoveComponent(BLOCK_SIZE, BLOCK_SIZE, 200).allowRotation(true)) // adds cellcomponent to magic so that it can move and it can rotate
                .with(new AStarMoveComponent(new LazyValue<>(() -> geto("grid")))) // adds the magic bolt to the grid on the gameWorld
                .with(new MagicComponent()) // adds a MagicComponent to the magic entity
                .rotationOrigin(36/2.0, 30/2.0) // origin of rotation
                .build();
    }

    /**
     * This method generates the information to spawn a new background on the game scene
     *
     * @param data spawndata
     * @return a new background entity
     */

    @Spawns("background")
    public Entity newBackground(SpawnData data) {
        return entityBuilder()
                .view(texture("newwallackground.jpg")) // sets the texture of background
                .zIndex(-1)
                .with(new IrremovableComponent()) // makes the component irremovable so that it is always persistent
                .build();
    }

}
