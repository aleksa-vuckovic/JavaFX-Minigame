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
import org.example.kanmi.indicators.EnergyIndicator;
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
    /**
     * Energy expenditure per millisecond of movement.
     */
    private double energyExpenditure;
    private ScoreIndicator scoreIndicator = new ScoreIndicator();
    private EnergyIndicator energyIndicator = new EnergyIndicator();
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

    public Player(double radius, double height, double speed, double energyExpenditure) {
        this.speed = speed;
        this.energyExpenditure = energyExpenditure;
        bounds = new Cylinder(radius, height);
        bounds.setMaterial(new PhongMaterial(Color.TRANSPARENT));
        head.setHeight(height/2);
        getChildren().addAll(bounds, head);
        getTransforms().addAll(rotation);
        position.setY(-height/2);
    }
    public static Player getRegularPlayer() {
        return new Player(25,100,0.1, 0.1/1000);
    }

    public void rotate(double dangle) {
        rotation.setAngle(rotation.getAngle() + dangle);
    }
    public void tilt(double dangle) {
        head.tilt(dangle);
    }
    @Override
    public Point3D getMotor() {
        double e = energyIndicator.getEnergy();
        e = 1-Math.pow(e-1, 6);
        return super.getMotor().multiply(e);
    }
    @Override
    public void start(Game game) {
        super.start(game);
        timer = new IntervalTimer() {
            @Override
            public void handleInterval(long interval) {
                if (!direction.equals(Point3D.ZERO)) energyIndicator.dec(interval*energyExpenditure);
                else energyIndicator.inc(interval*energyExpenditure*2);
            }
        };
        timer.start();
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
    public EnergyIndicator getEnergyIndicator() {
        return energyIndicator;
    }
}
