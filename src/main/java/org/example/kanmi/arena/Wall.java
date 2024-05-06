package org.example.kanmi.arena;

import javafx.geometry.Point3D;
import javafx.scene.paint.Material;
import javafx.scene.shape.Box;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import org.example.kanmi.Utils;
import org.example.kanmi.gameobject.BarrierObject;

public class Wall extends Obstacle {

    Box box;
    public Wall(double width, double length, double height) {
        box = new Box(width, height, length);
        box.setTranslateY(-height/2);
        getChildren().addAll(box);
    }
    public void setMaterial(Material mat) {
        box.setMaterial(mat);
    }

    @Override
    public Point3D getNormal(Point3D impact) {
        impact = box.sceneToLocal(impact);
        double x = box.getWidth()/2 - Math.abs(impact.getX());
        double y = box.getHeight()/2 - Math.abs(impact.getY());
        double z = box.getDepth()/2 - Math.abs(impact.getZ());
        Transform t = getLocalToSceneTransform();
        Point3D normal;
        if (x <= y && x <= z) {
            normal = Rotate.X_AXIS.multiply(impact.getX());
        }
        else if (y <= z) {
            normal = Rotate.Y_AXIS.multiply(impact.getY());
        }
        else {
            normal = Rotate.Z_AXIS.multiply(impact.getZ());
        }
        return Utils.transformVector(normal, t);
    }
}
