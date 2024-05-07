package org.example.kanmi.gameobject;

import javafx.geometry.Point3D;
import org.example.kanmi.Game;
import org.example.kanmi.IntervalTimer;

/**
 * A non-stationary game object, with a mass and a motion vector.
 * Subject to the gravitational force, in the positive Y direction,
 * And air friction, of a magnitude proportional to the speed,
 * and of an opposite direction, or -kV, where k is set to 1.
 */
public abstract class MovingGameObject extends GameObject {

    public static double FRICTION_CONST = 0.3;

    private double mass = Double.MAX_VALUE;
    public double getMass() { return mass; }
    public void setMass(double mass) { this.mass = mass; }
    public void move(Point3D amount) {
        position.setX(position.getX() + amount.getX());
        position.setY(position.getY() + amount.getY());
        position.setZ(position.getZ() + amount.getZ());
    }
    /**
     * The current direction and speed of movement,
     * in pixels (centimeters) per second.
     */
    private Point3D direction = Point3D.ZERO;
    /**
     * @return Direction (and speed) in game coordinate system.
     */
    public Point3D getDirection() {
        return direction;
    }
    /**
     * Set direction, given in game coordinate system.
     */
    public void setDirection(Point3D dir) {
        direction = dir;
    }
    @Override
    public void update(long interval) {
        super.update(interval);
        //Move in the direction calculated after previous interaction cycle
        move(getDirection().multiply(interval));
        //Update direction based on force
        setDirection(getDirection().add(netForce().multiply(1/getMass()).multiply(interval)));
    }

    /**
     * Should return the net force acting on this object,
     * in units of kg pixel per millisecond squared.
     */
    protected Point3D netForce() {
        return new Point3D(0, (double)981/1000000*getMass(), 0).subtract(getDirection().multiply(FRICTION_CONST));
    }

}
