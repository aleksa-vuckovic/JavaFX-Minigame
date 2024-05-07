package org.example.kanmi.collectibles;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.example.kanmi.Utils;
import org.example.kanmi.gameobject.GameObject;
import org.example.kanmi.player.Player;

public class Pill extends Collectible {

    private static double LENGTH = 40;
    private static double RADIUS = 10;
    Cylinder upper, lower;
    Sphere upperEnd, lowerEnd;
    public Pill() {
        upper = new Cylinder(RADIUS, LENGTH/2);
        upper.setTranslateY(-LENGTH/4);
        lower = new Cylinder(RADIUS, LENGTH/2);
        lower.setTranslateY(LENGTH/4);
        upperEnd = new Sphere(RADIUS);
        upperEnd.setTranslateY(-LENGTH/2);
        lowerEnd = new Sphere(RADIUS);
        lowerEnd.setTranslateY(LENGTH/2);
        PhongMaterial upperMat = new PhongMaterial(Color.PURPLE);
        PhongMaterial lowerMat = new PhongMaterial(Utils.lighter(Color.PURPLE, 0.8));
        upper.setMaterial(upperMat); upperEnd.setMaterial(upperMat);
        lower.setMaterial(lowerMat); lowerEnd.setMaterial(lowerMat);
        getChildren().addAll(upper, upperEnd, lower, lowerEnd);
        getTransforms().addAll(new Translate(0, -LENGTH, 0), new Rotate(45, Rotate.Z_AXIS));
    }

    @Override
    protected void collected(Player player) {
        player.getImmunityIndicator().inc();
    }
}
