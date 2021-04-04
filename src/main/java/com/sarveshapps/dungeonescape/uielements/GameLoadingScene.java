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
 * @author Sarvesh Somasundaram
 */
public class GameLoadingScene extends LoadingScene {

    public GameLoadingScene() {
        var bg = new Rectangle(getAppWidth(), getAppHeight(), Color.BLACK);

        var text = getUIFactoryService().newText("Loading Game", Color.WHITE, 46.0);
        centerText(text, getAppWidth() / 2.0, getAppHeight() / 3.0  + 25);

        var hbox = new HBox(5);

        for (int i = 0; i < 3; i++) {
            var textDot = getUIFactoryService().newText(".", Color.WHITE, 46.0);

            hbox.getChildren().add(textDot);

            animationBuilder(this)
                    .autoReverse(true)
                    .delay(Duration.seconds(i * 0.75))
                    .repeatInfinitely()
                    .fadeIn(textDot)
                    .buildAndPlay();
        }

        hbox.setTranslateX(getAppWidth() / 2.0 - 20);
        hbox.setTranslateY(getAppHeight() / 2.0);

        var playerTexture = texture("Pirate.png").subTexture(new Rectangle2D(0, 0, 32, 30));
        playerTexture.setTranslateX(getAppWidth() / 2.0 - 32/2.0);
        playerTexture.setTranslateY(getAppHeight() / 2.0 - 42/2.0);

        animationBuilder(this)
                .duration(Duration.seconds(2))
                .repeatInfinitely()
                .autoReverse(true)
                .interpolator(Interpolators.EXPONENTIAL.EASE_IN_OUT())
                .rotate(playerTexture)
                .from(0)
                .to(360)
                .buildAndPlay();

        getContentRoot().getChildren().setAll(bg, text, hbox, playerTexture);
    }
}
