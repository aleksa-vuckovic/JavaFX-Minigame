package org.example.kanmi.arena;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import org.example.kanmi.gameobject.BarrierObject;
import org.example.kanmi.gameobject.GameObject;

public abstract class Arena extends BarrierObject {

    /**
     * The ground surface of an arena should always be at y=0 by default.
     */
    public Arena() {}

    public static Arena getRegularArena() {
        Image concreteImage = new Image("concrete.jpg");
        Image brickImage = new Image("brick.jpg");
        Arena arena = new RectangleArena(1000, 1000);
        PhongMaterial mat = new PhongMaterial(Color.GREY);
        mat.setDiffuseMap(concreteImage);
        arena.setGroundMaterial(mat);
        mat = new PhongMaterial(Color.INDIANRED);
        mat.setDiffuseMap(brickImage);
        arena.setWallMaterial(mat);
        return arena;
    }

    public abstract void setGroundMaterial(Material mat);
    public abstract void setWallMaterial(Material mat);


    /**
     * @return A random location on the surface of the arena.
     */
    public abstract Point3D getRandomLocation();

}
