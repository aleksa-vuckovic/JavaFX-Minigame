package org.example.kanmi.collectibles;

import javafx.animation.*;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import org.example.kanmi.Game;
import org.example.kanmi.Utils;
import org.example.kanmi.gameobject.GameObject;

public abstract class Collectible extends GameObject {

    private boolean collected;
    public void setCollected() {collected = true;}
    public boolean isCollected() { return collected; }
    private Rotate rotation;
    private Timeline rotating;
    private Scale scale;

    public Collectible() {
        rotation = new Rotate(0, Rotate.Y_AXIS);
        scale = new Scale(1,1,1);
        getTransforms().addAll(
                rotation,
                scale
        );

        KeyValue kv1 = new KeyValue(rotation.angleProperty(), 0);
        KeyFrame kf1 = new KeyFrame(Duration.seconds(0), kv1);
        KeyValue kv2 = new KeyValue(rotation.angleProperty(), 360);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(5), kv2);
        rotating = new Timeline(kf1, kf2);
        rotating.setCycleCount(Integer.MAX_VALUE);
    }

    protected final void disappear() {
        rotating.setRate(10);
        vanish();
    }
    protected final void vanish() {
        Point3D center = Utils.center(getBoundsInLocal());
        scale.setPivotX(center.getX()); scale.setPivotY(center.getY()); scale.setPivotZ(center.getZ());
        KeyValue kv1 = new KeyValue(scale.xProperty(), 1, Interpolator.EASE_IN);
        KeyValue kv2 = new KeyValue(scale.yProperty(), 1, Interpolator.EASE_IN);
        KeyValue kv3 = new KeyValue(scale.zProperty(), 1, Interpolator.EASE_IN);
        KeyFrame kf1 = new KeyFrame(Duration.seconds(0), kv1, kv2, kv3);
        KeyValue kv4 = new KeyValue(scale.xProperty(), 0, Interpolator.EASE_IN);
        KeyValue kv5 = new KeyValue(scale.yProperty(), 0, Interpolator.EASE_IN);
        KeyValue kv6 = new KeyValue(scale.zProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(1), kv4, kv5, kv6);
        Timeline disappearing = new Timeline(kf1, kf2);
        disappearing.setDelay(Duration.seconds(1));
        disappearing.setOnFinished(actionEvent -> stop());
        disappearing.play();
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
}
