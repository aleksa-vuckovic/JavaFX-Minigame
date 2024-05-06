package org.example.kanmi.gameobject;


import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import org.example.kanmi.Game;
import org.example.kanmi.IntervalTimer;
import org.example.kanmi.Utils;

/**
 * Movable object that has its own moving force, with an opposing
 * force proportional to the current speed of the object (and in the opposite direction).
 * The maximum speed is achieved when these forces cancel out, or Fmotor = k*V.
 * k is set to 0.01.
 */
public abstract class SelfMovingGameObject extends MovingGameObject {

    /**
     * Preferred direction and speed, in the local coordinate system.
     */
    private Point3D motor = Point3D.ZERO;

    private Point3D getSceneMotor() {
        return getLocalToSceneTransform().transform(getMotor())
                .subtract(getLocalToSceneTransform().transform(Point3D.ZERO));
    }
    public void setMotor(Point3D motor) { this.motor = motor; }
    public Point3D getMotor() {return motor;}
    @Override
    public Point3D netForce() {
        return super.netForce().add(getSceneMotor());
    }

}
