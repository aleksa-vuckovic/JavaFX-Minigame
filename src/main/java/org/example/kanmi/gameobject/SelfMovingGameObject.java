package org.example.kanmi.gameobject;


import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import org.example.kanmi.Game;
import org.example.kanmi.IntervalTimer;
import org.example.kanmi.Utils;

/**
 * Movable object that has its own motor.
 */
public class SelfMovingGameObject extends GameObject {

    /**
     * Preferred direction and speed, in the local coordinate system.
     */
    protected Point3D motor = Point3D.ZERO;
    private Point3D getSceneMotor() {
        return getLocalToSceneTransform().transform(getMotor())
                .subtract(getLocalToSceneTransform().transform(Point3D.ZERO));
    }
    private IntervalTimer timer;

    public void setMotor(Point3D motor) { this.motor = motor; }
    public Point3D getMotor() {return motor;}

    @Override
    public void interact(GameObject other) {
        if (other instanceof BarrierObject) Utils.obstacleCollision((BarrierObject) other, this);
        else Utils.elasticPointCollision(this, other);
    }
    @Override
    public void start(Game game) {
        super.start(game);
        timer = new IntervalTimer() {
            @Override
            public void handleInterval(long interval) {
            move(getDirection().multiply(interval));
            //Reset direction until further interaction
            setDirection(getSceneMotor());
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
}
