package org.example.kanmi.items;

import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import org.example.kanmi.Game;
import org.example.kanmi.gameobject.GameObject;
import org.example.kanmi.player.Player;

public class Energy extends GameObject {
    private static final double LENGTH = 20;
    private static final double THICKNESS = 5;

    Rotate rotation;
    Timeline rotating;
    boolean collected = false;

    public Energy() {
        double t = 1/(2*Math.sqrt(2));
        Box[] boxes = new Box[3];
        boxes[0] = new Box(LENGTH, THICKNESS, THICKNESS);
        boxes[0].getTransforms().addAll(new Rotate(45, Rotate.Z_AXIS), new Translate(0, t*LENGTH));
        boxes[1] = new Box(LENGTH, THICKNESS, THICKNESS);
        boxes[2] = new Box(LENGTH, THICKNESS, THICKNESS);
        boxes[2].getTransforms().addAll(new Rotate(45, Rotate.Z_AXIS), new Translate(0, -t*LENGTH));
        PhongMaterial mat = new PhongMaterial(Color.WHITESMOKE);
        for (Box box: boxes) box.setMaterial(mat);
        getChildren().addAll(boxes);
        rotation = new Rotate(0, Rotate.Y_AXIS);
        getTransforms().addAll(
                new Translate(0,-LENGTH*1.3,0),
                rotation
        );

        KeyValue kv1 = new KeyValue(rotation.angleProperty(), 0);
        KeyFrame kf1 = new KeyFrame(Duration.seconds(0), kv1);
        KeyValue kv2 = new KeyValue(rotation.angleProperty(), 360);
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
            ((Player) other).getEnergyIndicator().inc(0.33);
            collected = true;
            rotating.setRate(10);
            ScaleTransition disappearing = new ScaleTransition(Duration.seconds(1), this);
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
