package org.example.kanmi.items;

import javafx.animation.*;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import org.example.kanmi.Game;
import org.example.kanmi.gameobject.GameObject;
import org.example.kanmi.player.Player;

public class Coin extends GameObject {

    private static final double RADIUS = 15;
    private static final double THICKNESS = 5;

    Cylinder cyl;
    Rotate cylRotation;
    Timeline rotating;
    boolean collected = false;

    public Coin() {
        cyl = new Cylinder(RADIUS, THICKNESS);
        Image goldImage = new Image("gold.jpg");
        PhongMaterial mat = new PhongMaterial(Color.YELLOW);
        mat.setDiffuseMap(goldImage);
        cyl.setMaterial(mat);
        cylRotation = new Rotate(0, Rotate.Y_AXIS);
        cyl.getTransforms().addAll(
                new Translate(0,-RADIUS*1.3,0),
                cylRotation,
                new Rotate(90, Rotate.X_AXIS)
        );
        getChildren().addAll(cyl);

        KeyValue kv1 = new KeyValue(cylRotation.angleProperty(), 0);
        KeyFrame kf1 = new KeyFrame(Duration.seconds(0), kv1);
        KeyValue kv2 = new KeyValue(cylRotation.angleProperty(), 360);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(5), kv2);
        rotating = new Timeline(kf1, kf2);
        rotating.setCycleCount(Integer.MAX_VALUE);
    }

    @Override
    public void start(Game game) {
        super.start(game);
        rotating.play();
    }

    @Override
    public void stop() {
        super.stop();
        rotating.stop();
        rotating = null;
    }

    @Override
    public void interact(GameObject other) {
        if (collected) return;
        if (other instanceof Player && getImpact(other) != null) {
           collected = true;
           rotating.setRate(10);
           ScaleTransition disappearing = new ScaleTransition(Duration.seconds(1), cyl);
           disappearing.setDelay(Duration.seconds(1));
           disappearing.setFromX(1); disappearing.setFromY(1); disappearing.setFromZ(1);
           disappearing.setToX(0); disappearing.setToY(0); disappearing.setToZ(0);
           disappearing.setInterpolator(Interpolator.EASE_IN);
           disappearing.setOnFinished(actionEvent -> {
               stop();
           });
           disappearing.play();
        }
    }
}
