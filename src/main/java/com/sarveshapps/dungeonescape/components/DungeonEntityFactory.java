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


public class DungeonEntityFactory implements EntityFactory {

    @Spawns("P")
    public Entity newPlayer(SpawnData data) {
        var view = texture("Pirate.png");

        var e = entityBuilder(data)
                .type(PLAYER)
                .bbox(new HitBox(new Point2D(4, 4), BoundingShape.box(32, 32)))
                .view(view)
                .with(new CollidableComponent(true))
                .with(new CellMoveComponent(BLOCK_SIZE, BLOCK_SIZE, 200).allowRotation(false))
                .with(new AStarMoveComponent(new LazyValue<>(() -> geto("grid"))))
                .with(new PlayerComponent())
                .rotationOrigin(36/2.0, 30/2.0)
                .build();

        e.setLocalAnchorFromCenter();
        return e;
    }

    @Spawns("1")
    public Entity newBlock(SpawnData data) {
        var rect = new Rectangle(40, 40, Color.BLACK);
        rect.setArcWidth(15);
        rect.setArcHeight(15);
        rect.setStrokeWidth(0.25);
        rect.setStroke(Color.DARKGRAY);

        return entityBuilder(data)
                .type(WALL)
                .with(new CollidableComponent(true))
                .viewWithBBox(rect)
                .zIndex(-1)
                .build();
    }

    @Spawns("0")
    public Entity newFloor(SpawnData data) {
        var view = texture("floor.jpg");
        view.setTranslateX(5);
        view.setTranslateY(5);

        return entityBuilder(data)
                .type(FLOOR)
                .bbox(new HitBox(new Point2D(5, 5), BoundingShape.box(30, 30)))
                .view(view)
                .zIndex(-1)
                .build();
    }

    @Spawns("E")
    public Entity newEnemy(SpawnData data) {
        var view = texture("mongbat.png");

        Entity enemy = entityBuilder(data)
                .type(ENEMY)
                .bbox(new HitBox(new Point2D(4, 4), BoundingShape.box(40, 40)))
                .view(view)
                .with(new CollidableComponent(true))
                .build();

        enemy.setLocalAnchorFromCenter();

        return enemy;
    }

    @Spawns("F")
    public Entity newExit(SpawnData data) {
        var rect = new Rectangle(38, 38, Color.RED);
        rect.setArcWidth(25);
        rect.setArcHeight(25);
        rect.setStrokeWidth(1);
        rect.setStroke(Color.ORANGE);

        return entityBuilder(data)
                .type(EXIT)
                .viewWithBBox(rect)
                .with(new CollidableComponent(true))
                .zIndex(-1)
                .build();
    }

    @Spawns("Magic")
    public Entity newMagic(SpawnData data) {
        var view = texture("shock.png");

        return entityBuilder(data)
                .type(MAGIC)
                .bbox(new HitBox(new Point2D(5, 5), BoundingShape.box(30, 10)))
                .view(view)
                .zIndex(-1)
                .with(new CollidableComponent(true))
                .with(new CellMoveComponent(BLOCK_SIZE, BLOCK_SIZE, 200).allowRotation(true))
                .with(new AStarMoveComponent(new LazyValue<>(() -> geto("grid"))))
                .with(new MagicComponent())
                .rotationOrigin(36/2.0, 30/2.0)
                .build();
    }

    @Spawns("background")
    public Entity newBackground(SpawnData data) {
        return entityBuilder()
                .view(texture("newwallackground.jpg"))
                .zIndex(-1)
                .with(new IrremovableComponent())
                .build();
    }

}
