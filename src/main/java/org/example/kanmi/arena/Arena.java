package org.example.kanmi.arena;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.util.Pair;
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
        Arena arena = new RectangleArena(1500, 1500);
        PhongMaterial mat = new PhongMaterial(Color.GREY);
        mat.setDiffuseMap(concreteImage);
        arena.setGroundMaterial(mat);
        mat = new PhongMaterial(Color.INDIANRED);
        mat.setDiffuseMap(brickImage);
        arena.setWallMaterial(mat);
        arena.setObstacleMaterial(mat);
        return arena;
    }

    public abstract void setGroundMaterial(Material mat);
    public abstract void setWallMaterial(Material mat);
    public abstract void setObstacleMaterial(Material mat);


    /**
     * @return A random location on the surface of the arena.
     */
    public abstract Point3D getRandomLocation();
    /**
     *
     * @return A random location on the wall surface, and a normal on the wall, pointing inwards.
     */
    public abstract Pair<Point3D, Point3D> getRandomWallLocation();

}
