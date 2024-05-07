package org.example.kanmi.gameobject;

import javafx.geometry.Point3D;
import org.example.kanmi.Utils;
import org.example.kanmi.collectibles.Collectible;
import org.example.kanmi.enemies.CanonBall;

/**
 * Represents a large mostly planar object
 * which cannot be approximated as a point in collisions.
 */
public abstract class BarrierObject extends GameObject {


    @Override
    public void interact(GameObject other) {
        if (other instanceof Collectible || other instanceof CanonBall) {
            other.interact(this);
            return;
        }
        if (other instanceof SelfMovingGameObject) Utils.obstacleBarrierCollision(this, (SelfMovingGameObject)other);
        else if (other instanceof MovingGameObject) Utils.elasticBarrierCollision(this, (MovingGameObject) other);

    }

    /**
     * @param impact The point in space being queried.
     * @return A vector normal on the surface of this barrier, and pointing outwards,
     * at the specified point, in game coordinates.
     */
    public abstract Point3D getNormal(Point3D impact);
}
