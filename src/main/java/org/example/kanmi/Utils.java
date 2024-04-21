package org.example.kanmi;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.util.Pair;
import org.example.kanmi.gameobject.BarrierObject;
import org.example.kanmi.gameobject.GameObject;
import org.example.kanmi.gameobject.InertialGameObject;
import org.example.kanmi.gameobject.SelfMovingGameObject;

public class Utils {

    /**
     * @return The bounds representing the intersection of b1 and b2.
     */
    private static Pair<Double, Double> lineIntersection(double a1, double b1, double a2, double b2) {
        if (a1 > a2) {
            double tmp = a1; a1 = a2; a2 = tmp;
            tmp = b1; b1 = b2; b2 = tmp;
        }
        if (a2 > b1) return null;
        else if (b2 > b1) return new Pair<>(a2, b1 - a2);
        else return new Pair<>(a2, b2 - a2);

    }
    public static Bounds intersection(Bounds b1, Bounds b2) {
        if (!b1.intersects(b2)) return null;
        Pair<Double, Double> xIntersect = lineIntersection(b1.getMinX(), b1.getMaxX(), b2.getMinX(), b2.getMaxX());
        Pair<Double, Double> yIntersect = lineIntersection(b1.getMinY(), b1.getMaxY(), b2.getMinY(), b2.getMaxY());
        Pair<Double, Double> zIntersect = lineIntersection(b1.getMinZ(), b1.getMaxZ(), b2.getMinZ(), b2.getMaxZ());
        return new BoundingBox(xIntersect.getKey(), yIntersect.getKey(), zIntersect.getKey(), xIntersect.getValue(), yIntersect.getValue(), zIntersect.getValue());
    }
    public static Point3D center(Bounds b) {
        return new Point3D(b.getCenterX(), b.getCenterY(), b.getCenterZ());
    }

    public static double radius(Bounds b) {
        return Math.sqrt(b.getHeight()*b.getHeight() + b.getWidth()*b.getWidth() + b.getDepth()*b.getDepth());
    }

    /**
     * Returns a transform which transforms startVector into alignment with goalVector.
     * @return
     */
    public static Rotate alignTransform(Point3D startVector, Point3D goalVector) {
        //find the rotation vector
        double angle = Math.acos(startVector.normalize().dotProduct(goalVector.normalize()));
        //find the axis of rotation (as perpendicular to the plane)
        Point3D axis = startVector.crossProduct(goalVector);
        return new Rotate(Math.toDegrees(angle), axis);
    }


    public static void elasticPointCollision(GameObject a, GameObject b) {
        Bounds intersect = Utils.intersection(a.getBounds(), b.getBounds());
        if (intersect == null) return;
        double c1 = (a.getMass() - b.getMass())/(a.getMass() + b.getMass());
        double c2 = 2*b.getMass()/(a.getMass() + b.getMass());
        double c3 = 2*a.getMass()/(a.getMass() + b.getMass());
        double c4 = -c1;
        Point3D dir = a.getDirection().multiply(c1).add(b.getDirection().multiply(c2));
        Point3D dirOther = a.getDirection().multiply(c3).add(b.getDirection().multiply(c4));
        a.setDirection(dir);
        b.setDirection(dirOther);
        double rad = Utils.radius(intersect);
        a.move(dir.normalize().multiply(rad));
        b.move(dirOther.normalize().multiply(rad));
    }

    public static void elasticBarrierCollision(BarrierObject a, GameObject b) {
        Point3D impact = a.getImpact(b);
        if (impact == null) return;
        Point3D direction = b.getDirection();
        Point3D normal = a.getNormal(impact);
        Transform align = Utils.alignTransform(normal, Rotate.X_AXIS);
        direction = align.transform(direction);
        direction = new Point3D(Math.abs(direction.getX()), direction.getY(), direction.getZ());
        try {
            direction = align.inverseTransform(direction);
            b.setDirection(direction);
        } catch(Exception e) {}
    }

    public static void obstacleCollision(BarrierObject a, GameObject b) {
        Point3D impact = a.getImpact(b);
        if (impact == null) return;


        Point3D direction = b.getDirection();
        Point3D normal = a.getNormal(impact);
        Transform align = Utils.alignTransform(normal, Rotate.X_AXIS);
        direction = align.transform(direction);
        if (direction.getX() < 0) direction = new Point3D(0, direction.getY(), direction.getZ());
        try {
            direction = align.inverseTransform(direction);
            b.setDirection(direction);
        } catch(Exception e) {}
    }


}
