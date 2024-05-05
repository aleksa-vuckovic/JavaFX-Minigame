package org.example.kanmi.arena;

import javafx.geometry.Point3D;
import javafx.scene.paint.Material;
import javafx.scene.shape.Box;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import org.example.kanmi.gameobject.BarrierObject;

public class Wall extends BarrierObject {

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
        /*
        double px = 2*impact.getX()/box.getWidth();
        double py = 2*impact.getY()/box.getHeight();
        double pz = 2*impact.getZ()/box.getDepth();
        if (Math.abs(px) >= Math.abs(py) && Math.abs(px) >= Math.abs(pz)) {
            //System.out.println("X AXIS");
            return Rotate.X_AXIS.multiply(Math.signum(px));
        }
        else if (Math.abs(py) >= Math.abs(pz)) {
            System.out.println("Y AXIS");
            return Rotate.Y_AXIS.multiply(Math.signum(py));
        }
        else {
            System.out.println("Z AXIS");
            return Rotate.Z_AXIS.multiply(Math.signum(pz));
        }
        */
        double x = box.getWidth()/2 - Math.abs(impact.getX());
        double y = box.getHeight()/2 - Math.abs(impact.getY());
        double z = box.getDepth()/2 - Math.abs(impact.getZ());
        if (x <= y && x <= z) {
            return Rotate.X_AXIS.multiply(impact.getX());
        }
        else if (y <= z) {
            return Rotate.Y_AXIS.multiply(impact.getY());
        }
        else return Rotate.Z_AXIS.multiply(impact.getZ());
    }
}
