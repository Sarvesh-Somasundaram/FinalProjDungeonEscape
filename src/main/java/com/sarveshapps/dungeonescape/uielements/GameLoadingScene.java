package com.sarveshapps.dungeonescape.uielements;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.LoadingScene;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * This class controls and creates the Game Loading Scene
 *
 *  @author Sarvesh Somasundaram
 *  @version 1.0
 *
 *  @see <a href=" https://github.com/Sarvesh1234567/FinalProjDungeonEscape ">Github Link to Project Repo</a>
 *  @see <a href=" https://github.com/AlmasB/FXGL ">JavaFX Game Library</a>
 *  @see <a href=" https://opengameart.org/ ">Textures, animations and backgrounds</a>
 *
 */
public class GameLoadingScene extends LoadingScene {

    /**
     * This method creates the loading scene
     */
    public GameLoadingScene() {
        var bg = new Rectangle(getAppWidth(), getAppHeight(), Color.BLACK); // declares and initializes a Black rectangle to fill the screen

        var text = getUIFactoryService().newText("Loading Game", Color.WHITE, 46.0); // declares and initializes text to be in the center of the screen
        centerText(text, getAppWidth() / 2.0, getAppHeight() / 3.0  + 25);

        var hbox = new HBox(5);

        for (int i = 0; i < 3; i++) { // loop to animate an ellipses after "Loading Game"
            var textDot = getUIFactoryService().newText(".", Color.WHITE, 46.0);

            hbox.getChildren().add(textDot); // using hbox to add to children

            animationBuilder(this)
                    .autoReverse(true)
                    .delay(Duration.seconds(i * 0.75))
                    .repeatInfinitely()
                    .fadeIn(textDot)
                    .buildAndPlay();
        }

        hbox.setTranslateX(getAppWidth() / 2.0 - 20);
        hbox.setTranslateY(getAppHeight() / 2.0);

        var playerTexture = texture("Pirate.png").subTexture(new Rectangle2D(0, 0, 32, 30)); // declares and initializes an image of the main character, a pirate
        playerTexture.setTranslateX(getAppWidth() / 2.0 - 32/2.0);
        playerTexture.setTranslateY(getAppHeight() / 2.0 - 42/2.0);

        animationBuilder(this) // animates the pirate to turn in a circle
                .duration(Duration.seconds(2))
                .repeatInfinitely()
                .autoReverse(true)
                .interpolator(Interpolators.EXPONENTIAL.EASE_IN_OUT())
                .rotate(playerTexture)
                .from(0)
                .to(360)
                .buildAndPlay();

        getContentRoot().getChildren().setAll(bg, text, hbox, playerTexture); // adds all the components to the content scene
    }
}
