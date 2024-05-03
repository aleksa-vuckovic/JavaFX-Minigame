package org.example.kanmi.player;

import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.example.kanmi.Game;
import org.example.kanmi.gameobject.GameObject;
import org.example.kanmi.IntervalTimer;
import org.example.kanmi.gameobject.SelfMovingGameObject;
import org.example.kanmi.indicators.ScoreIndicator;

import java.util.HashSet;
import java.util.Set;

public class Player extends SelfMovingGameObject {

    private final Cylinder bounds;
    private final PlayerHead head = new PlayerHead();
    /**
     * Speed in pixels per millisecond.
     */
    private double speed;
    private ScoreIndicator scoreIndicator = new ScoreIndicator();
    private IntervalTimer timer;
    private final Rotate rotation = new Rotate(0, Rotate.Y_AXIS);

    private final Set<KeyCode> controls = new HashSet<>();
    public void keyEvent(KeyEvent keyEvent) {
        if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) controls.add(keyEvent.getCode());
        else if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED) controls.remove(keyEvent.getCode());
        Point3D direction = Point3D.ZERO;
        for (KeyCode code: controls) {
            switch (code) {
                case W -> direction = direction.add(new Point3D(0, 0, 1));
                case S -> direction = direction.add(new Point3D(0, 0, -1));
                case A -> direction = direction.add(new Point3D(-1, 0, 0));
                case D -> direction = direction.add(new Point3D(1, 0, 0));
            }
        }
        motor = direction.normalize().multiply(speed);
    }

    public Player(double radius, double height, double speed) {
        this.speed = speed;
        bounds = new Cylinder(radius, height);
        bounds.setMaterial(new PhongMaterial(Color.TRANSPARENT));
        head.setHeight(height/2);
        getChildren().addAll(bounds, head);
        getTransforms().addAll(rotation);
        position.setY(-height/2);
    }
    public static Player getRegularPlayer() {
        return new Player(25,100,0.1);
    }

    public void rotate(double dangle) {
        rotation.setAngle(rotation.getAngle() + dangle);
    }
    public void tilt(double dangle) {
        head.tilt(dangle);
    }
    @Override
    public void stop() {
        super.stop();
        timer.stop();
        timer = null;
    }
    public PerspectiveCamera getCamera() {
        return head.getCamera();
    }

    public ScoreIndicator getScoreIndicator() {
        return scoreIndicator;
    }
}
