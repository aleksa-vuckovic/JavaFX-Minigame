package org.example.kanmi.gameobject;

import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import org.example.kanmi.Utils;

/**
 * Represents a large mostly planar object
 * which cannot be approximated as a point in collisions.
 */
public abstract class BarrierObject extends GameObject {


    @Override
    public void interact(GameObject other) {
        if (other instanceof InertialGameObject) Utils.elasticBarrierCollision(this, (InertialGameObject)other);
        else if (other instanceof SelfMovingGameObject) Utils.obstacleCollision(this, (SelfMovingGameObject)other);
    }

    /**
     * @param impact The point in space being queried.
     * @return A vector normal on the surface of this barrier, and pointing outwards,
     * at the specified point, in game coordinates.
     */
    public abstract Point3D getNormal(Point3D impact);
}
