package org.example.kanmi.player;

import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import org.example.kanmi.Game;
import org.example.kanmi.gameobject.GameObject;
import org.example.kanmi.gameobject.SelfMovingGameObject;
import org.example.kanmi.indicators.EnergyIndicator;
import org.example.kanmi.indicators.HealthIndicator;
import org.example.kanmi.indicators.ScoreIndicator;

import java.util.HashSet;
import java.util.Set;

public class Player extends SelfMovingGameObject {

    private static long JUMP_INTERVAL = 1000;
    private static double HEALTH_LOSS = 0.2/1000;
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
    private HealthIndicator healthIndicator = new HealthIndicator();
    private final Rotate rotation = new Rotate(0, Rotate.Y_AXIS);

    private final Set<KeyCode> controls = new HashSet<>();
    public void keyEvent(KeyEvent keyEvent) {
        if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
            controls.add(keyEvent.getCode());
            if (keyEvent.getCode() == KeyCode.SPACE) jump();
        }
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
        setMotor(direction.normalize().multiply(speed*FRICTION_CONST));
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
         Player ret = new Player(15,100, Game.PLAYER_SPEED, 0.1/1000);
         ret.setMass(100);
         return ret;
    }

    public void rotate(double dangle) {
        rotation.setAngle(rotation.getAngle() + dangle);
    }
    public void tilt(double dangle) {
        head.tilt(dangle);
    }
    @Override
    public Point3D getMotor() {
        double e = energyIndicator.getValue();
        e = 1-Math.pow(e-1, 6);
        return super.getMotor().multiply(e);
    }
    @Override
    public void update(long interval) {
        super.update(interval);
        tillJump -= interval;
        if (getMotor().magnitude() > 0.001) energyIndicator.dec(interval*energyExpenditure);
        else energyIndicator.inc(interval*energyExpenditure*2);
        /*healthIndicator.dec(enemies*HEALTH_LOSS*interval);
        enemies = 0;
        if (healthIndicator.getValue() <= 0) game.gameOver();*/
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
    public HealthIndicator getHealthIndicator() {
        return healthIndicator;
    }

    @Override
    public void interact(GameObject other) {
        other.interact(this);
    }


    private long tillJump = 0;
    public void jump() {
        if (tillJump > 0) return;
        tillJump = JUMP_INTERVAL;
        Point3D dir = getDirection();
        dir = new Point3D(dir.getX(), dir.getY() - 0.5, dir.getZ());
        setDirection(dir);
    }


    private int enemies = 0;
    public void enemyTouch() {
        enemies += 1;
    }
    public void enemyHit() {
        healthIndicator.dec(0.2);
        if (healthIndicator.getValue() <= 0) game.gameOver();
    }
}
