package org.example.kanmi.arena;

import javafx.geometry.Point3D;
import javafx.scene.paint.Material;
import org.example.kanmi.gameobject.GameObject;

public class PWall extends Obstacle {

    Wall left;
    Wall right;
    Wall upper;
    public PWall(double width, double height, double thickness) {
        left = new Wall(thickness, thickness, height);
        right = new Wall(thickness, thickness, height);
        upper = new Wall(width - 2*thickness, thickness, thickness);
        left.setTranslateX(thickness/2 - width/2);
        right.setTranslateX(-thickness/2 + width/2);
        upper.setTranslateY(-height+thickness);
        getChildren().addAll(left, upper, right);
    }

    @Override
    public void setMaterial(Material mat) {
        left.setMaterial(mat);
        right.setMaterial(mat);
        upper.setMaterial(mat);
    }

    @Override
    public Point3D getNormal(Point3D impact) {
        return null; //Unused
    }

    @Override
    public void interact(GameObject other) {
        left.interact(other);
        upper.interact(other);
        right.interact(other);
    }
}
