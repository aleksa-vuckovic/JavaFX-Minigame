package org.example.kanmi.arena;

import javafx.geometry.Point3D;
import javafx.scene.paint.Material;
import org.example.kanmi.gameobject.GameObject;

public class LWall extends Obstacle {

    Wall lower;
    Wall upper;
    public LWall(double width, double height, double thickness) {
        lower = new Wall(width, thickness, thickness);
        upper = new Wall(thickness, thickness, height-thickness);
        upper.setTranslateY(-thickness);
        upper.setTranslateX(-width/2+thickness/2);
        getChildren().addAll(lower, upper);
    }

    @Override
    public void setMaterial(Material mat) {
        lower.setMaterial(mat);
        upper.setMaterial(mat);
    }

    @Override
    public Point3D getNormal(Point3D impact) {
        return null; //Unused
    }

    @Override
    public void interact(GameObject other) {
        System.out.println("!!!!!!!!!!!!!11lower");
        lower.interact(other);
        upper.interact(other);
    }
}
