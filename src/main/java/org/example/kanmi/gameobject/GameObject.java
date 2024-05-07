package org.example.kanmi.gameobject;

import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import org.example.kanmi.Game;
import org.example.kanmi.Utils;

/**
 * Represents any game object.
 * Each game object has a position inside the parent coordinate system.
 *  This is usually the scene coordinate system.
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

    public GameObject() {
        getTransforms().addAll(position);
    }

    /**
     * @return Object bounds in game coordinates.
     */
    public Bounds getBounds() {
        return localToScene(getBoundsInLocal());
    }

    /**
     * @return Object center in game coordinates.
     */
    public Point3D getCenter() {
        return Utils.center(getBounds());
    }
    /**
     * Checks if two objects are touching/intersecting.
     * @return The impact point in game coordinates, or null.
     */
    public Point3D getImpact(GameObject other) {
        try {
            Transform sceneToLocalTransform = getLocalToSceneTransform().createInverse();
            Affine affine = new Affine(sceneToLocalTransform);
            affine.append(other.getLocalToSceneTransform());
            Bounds intersect = Utils.intersection(getBoundsInLocal(), affine.transform(other.getBoundsInLocal()));
            if (intersect != null) return Utils.center(localToScene(intersect));
        } catch(Exception e) {}
        return null;
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

    /**
     * Called on each frame before the interactions, within
     * the main Game timer.
     * Should be used for updates whose order in relation to
     * interactions is important.
     * Otherwise, separate timers can be used for each game object.
     * @param interval Milliseconds since last update.
     */
    public void update(long interval) {}
}

/**
 * Interaction is a 2-way event, so to avoid implementing the same procedure in 2 classes,
 * an ordering among all types will be established.
 *
 * Collectible -> BarrierObject -> DumbEnemy -> Player
 *
 */
