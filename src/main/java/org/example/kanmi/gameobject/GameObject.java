package org.example.kanmi.gameobject;

import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Translate;
import org.example.kanmi.Game;
import org.example.kanmi.Utils;

/**
 * Represents any game object.
 * Each game object has a position inside the parent coordinate system.
 *  This is usually the scene coordinate system.
 * Each game object has a mass.
 * Each game object has a motion vector, in units of pixels per millisecond.
 * Each game object has reference to the containing Game object.
 * Each game object has a lifecycle, starting with start(Game), and ending with stop().
 * Each game object can interact with another.
 */
public abstract class GameObject extends Group {
    protected Game game;
    protected Translate position = new Translate();
    public void setPosition(Point3D pos) {
        position.setX(pos.getX());
        position.setY(pos.getY());
        position.setZ(pos.getZ());
    }
    protected double mass = Double.MAX_VALUE;
    public double getMass() { return mass; }
    public void setMass(double mass) { this.mass = mass; }
    public void move(Point3D amount) {
        position.setX(position.getX() + amount.getX());
        position.setY(position.getY() + amount.getY());
        position.setZ(position.getZ() + amount.getZ());
    }
    /**
     * The current direction and speed of movement,
     * in pixels per second.
     */
    protected Point3D direction = Point3D.ZERO;
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
        /*
        try {
            dir = getLocalToSceneTransform().inverseTransform(dir);
            Point3D ref = getLocalToSceneTransform().inverseTransform(Point3D.ZERO);
            direction = dir.subtract(ref);
        } catch(Exception e) {}
        */
         direction = dir;
    }
    public GameObject() {
        getTransforms().addAll(position);
    }
    public Bounds getBounds() {
        return localToScene(getBoundsInLocal());
    }
    public Point3D getCenter() {
        return Utils.center(getBounds());
    }
    /**
     * Checks if two objects are touching/intersecting.
     * @return The impact point in game coordinates, or null.
     */
    public Point3D getImpact(GameObject other) {
        Bounds intersect = Utils.intersection(getBounds(), other.getBounds());
        if (intersect != null) return Utils.center(intersect);
        else return null;
    }
    public boolean contains(Point3D point) {
        return getBounds().contains(point);
    }
    public boolean contains(GameObject other) {
        return getBounds().contains(other.getBounds());
    }

    /**
     * By default, the interaction is an elastic point collision.
     *
     */
    public abstract void interact(GameObject other);


    /**
     * This method should be called when the object joins the game.
     * @param game The game this object is part of.
     */
    public void start(Game game) {
        this.game = game;
    }

    /**
     * Should be called once the object is not part of the game anymore.
     */
    public void stop() {
        this.game = null;
    }

    public boolean stopped() {
        return this.game == null;
    }
}
