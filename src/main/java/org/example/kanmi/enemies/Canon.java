package org.example.kanmi.enemies;

import javafx.animation.ScaleTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import org.example.kanmi.Game;
import org.example.kanmi.IntervalTimer;
import org.example.kanmi.Utils;
import org.example.kanmi.gameobject.BarrierObject;
import org.example.kanmi.misc.Arc3D;

public class Canon extends BarrierObject {

    private static final double CANONBALL_SPEED = 0.3;
    private static final long PERIOD = 5000;

    private Cylinder body;
    private Cylinder entry;
    private Arc3D hoop;
    private Point3D exitPoint;
    private IntervalTimer timer;




    /**
     * The canon is facing towards the positive Z axis, with its center at 0,0,0.
     */
    public Canon(double radius, double length) {
        body = new Cylinder(radius, length);
        hoop = new Arc3D(radius*1.3, radius*0.8, 0, 360, length*0.3, 50);
        entry = new Cylinder(radius*0.8, length*0.3);
        body.getTransforms().addAll(new Rotate(90, Rotate.X_AXIS));
        entry.getTransforms().addAll(new Translate(0, 0, length/2+length*0.15), new Rotate(90, Rotate.X_AXIS));
        hoop.getTransforms().addAll(new Translate(0, 0, length/2+length*0.15));
        PhongMaterial mat = new PhongMaterial(Color.GRAY);
        body.setMaterial(mat);
        hoop.setMaterial(mat);
        entry.setMaterial(new PhongMaterial(Color.BLACK));
        getChildren().addAll(body, hoop, entry);
        exitPoint = Utils.center(entry.getBoundsInParent());
    }


    @Override
    public void start(Game game) {
        super.start(game);
        timer = new IntervalTimer() {
            long time = PERIOD;
            @Override
            public void handleInterval(long interval) {
                time -= interval;
                if (time <= 0) {
                    time = PERIOD;
                    CanonBall ball = fire();
                    game.add(ball);
                    ball.start(game);
                }
            }
        };
        timer.start();
    }
    @Override
    public void stop() {
        super.stop();
        timer.stop();
    }

    public CanonBall fire() {
        CanonBall ball = new CanonBall(20, this);//body.getRadius());
        //ball.setPosition(new Point3D(0, -100, 0));
        ball.setPosition(localToScene(exitPoint));
        ball.setDirection(Utils.transformVector(Rotate.Z_AXIS, getLocalToSceneTransform()).normalize().multiply(CANONBALL_SPEED));
        ScaleTransition trigger = new ScaleTransition(Duration.millis(200), hoop);
        trigger.setFromX(1); trigger.setFromY(1); trigger.setFromZ(1);
        trigger.setToX(1.2); trigger.setToY(1.15); trigger.setToZ(1.15);
        trigger.setAutoReverse(true);
        trigger.setCycleCount(2);
        trigger.play();
        return ball;
    }

    @Override
    public Point3D getNormal(Point3D impact) {
        impact = sceneToLocal(impact);
        Bounds bounds = getBoundsInLocal();
        double x = bounds.getMaxX() - Math.abs(impact.getX());
        double y = bounds.getMaxY() - Math.abs(impact.getY());
        double z = bounds.getMaxZ()/2 - Math.abs(impact.getZ()-bounds.getMaxZ()/2);
        x = Math.min(x,y);
        Transform t = getLocalToSceneTransform();
        Point3D normal;
        if (x <= z) {
            normal = impact.subtract(0, 0, impact.getZ());
        }
        else {
            normal = Rotate.Z_AXIS.multiply(impact.getZ()-bounds.getMaxZ()/2);
        }
        return Utils.transformVector(normal, t);
    }



}
