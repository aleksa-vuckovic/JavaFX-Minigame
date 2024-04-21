package org.example.kanmi.arena;

import javafx.geometry.Point3D;
import javafx.scene.paint.Material;
import javafx.scene.shape.Box;
import org.example.kanmi.gameobject.GameObject;

public class RectangleArena extends Arena {

    private Wall ground;
    private Wall[] walls;
    private Box innerSpace;

    public RectangleArena(double width, double length,
                          double wallThickness, double wallHeight) {
        ground = new Wall(width, length, 2);
        getChildren().addAll(ground);
        innerSpace = new Box(width, 100000, length);

        walls = new Wall[4];
        double hWallWidth = width;
        double hWallLength = wallThickness;
        walls[0] = new Wall(hWallWidth, wallThickness, wallHeight);
        walls[0].setTranslateZ(-wallThickness/2-length/2);
        walls[1] = new Wall(hWallWidth, wallThickness, wallHeight);
        walls[1].setTranslateZ(wallThickness/2+length/2);

        double lWallWidth = wallThickness;
        double lWallLength = length + 2*hWallLength;
        walls[2] = new Wall(lWallWidth, lWallLength, wallHeight);
        walls[2].setTranslateX(-wallThickness/2-width/2);
        walls[3] = new Wall(lWallWidth, lWallLength, wallHeight);
        walls[3].setTranslateX(wallThickness/2+width/2);

        getChildren().addAll(walls);
    }
    public RectangleArena(double width, double length) {
        this(width, length, 50, 100);
    }

    @Override
    public void setGroundMaterial(Material mat) {
        ground.setMaterial(mat);
    }

    @Override
    public void setWallMaterial(Material mat) {
        for (Wall wall: walls) wall.setMaterial(mat);
    }

    /**
     * Return true if the object is within the arena's bounds walls on the sides,
     * floor below and virtually unlimited above.
     */
    @Override
    public boolean contains(GameObject go) {
        return innerSpace.getBoundsInParent().contains(go.getBounds());
    }

    @Override
    public void interact(GameObject other) {
        for (Wall wall: walls) wall.interact(other);
    }

    @Override
    public Point3D getNormal(Point3D impact) {
        return null; //not used
    }

}
