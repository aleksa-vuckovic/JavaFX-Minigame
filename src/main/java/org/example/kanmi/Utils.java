package org.example.kanmi;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.*;
import javafx.util.Pair;
import org.example.kanmi.gameobject.*;

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
    public static Transform alignTransform(Point3D startVector, Point3D goalVector) {
        startVector = startVector.normalize();
        goalVector = goalVector.normalize();
        if (startVector.equals(Point3D.ZERO) || goalVector.equals(Point3D.ZERO) || startVector.equals(goalVector)) return new Translate();
        if (startVector.multiply(-1).equals(goalVector)) return new Scale(-1,-1,-1);
        //find the rotation vector
        double angle = Math.acos(startVector.dotProduct(goalVector));
        //find the axis of rotation (as perpendicular to the plane)
        Point3D axis = startVector.crossProduct(goalVector);
        return new Rotate(Math.toDegrees(angle), axis);
    }


    public static void elasticPointCollision(MovingGameObject a, MovingGameObject b) {
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
        double rad = Math.min(Math.min(intersect.getWidth(), intersect.getHeight()), intersect.getDepth());//Utils.radius(intersect);
        a.move(dir.normalize().multiply(rad));
        b.move(dirOther.normalize().multiply(rad));
    }

    public static void elasticBarrierCollision(BarrierObject a, MovingGameObject b) {
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

    public static void obstacleBarrierCollision(BarrierObject a, MovingGameObject b) {
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
        } catch(Exception e) {e.printStackTrace();}
    }

    public static void obstaclePointCollision(MovingGameObject a, MovingGameObject b) {
        Bounds intersect = Utils.intersection(a.getBounds(), b.getBounds());
        if (intersect == null) return;
        Point3D normal = a.getCenter().subtract(b.getCenter());
        Transform align = Utils.alignTransform(normal, Rotate.X_AXIS);
        Point3D aDirection = align.transform(a.getDirection());
        Point3D bDirection = align.transform(b.getDirection());
        if (aDirection.getX() < 0) aDirection = new Point3D(0, aDirection.getY(), aDirection.getZ());
        if (bDirection.getX() > 0) bDirection = new Point3D(0, bDirection.getY(), bDirection.getZ());
        try {
            aDirection = align.inverseTransform(aDirection);
            bDirection = align.inverseTransform(bDirection);
            a.setDirection(aDirection);
            b.setDirection(bDirection);
        } catch(Exception e) {e.printStackTrace();}
    }

    public static double getTextHeight(Text text) {
        return text.getBoundsInLocal().getHeight()*0.8;
    }

    public static Color changeOpacity(Color color, float opacity) {
        return Color.color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }

    public static Point2D getCirclePoint(int divisions, int i, double radius, double offset) {
        double angle = 360.0*i/divisions;
        angle += offset*360.0/divisions;
        return new Rotate(angle).transform(radius, 0);
    }

    public static Point3D transformVector(Point3D vector, Transform transform) {
        return transform.transform(vector).subtract(transform.transform(Point3D.ZERO));
    }

}
