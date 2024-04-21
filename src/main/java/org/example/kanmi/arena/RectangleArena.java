package org.example.kanmi.arena;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import org.example.kanmi.gameobject.GameObject;

public class RectangleArena extends Arena {

    private Wall ground;
    private Wall[] walls;
    private Box innerSpace;

    private Wall[] obstacles;

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

        obstacles = new Wall[10];
        Image stoneImage = new Image("stone.jpg");
        PhongMaterial mat = new PhongMaterial(Color.GRAY);
        mat.setDiffuseMap(stoneImage);
        for (int i = 0; i < 10; i++) {
            obstacles[i] = new Wall(width/20, length/20, wallHeight*0.7);
            obstacles[i].setPosition(getRandomLocation());
            obstacles[i].setMaterial(mat);
        }
        getChildren().addAll(obstacles);
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
        for (Wall wall: obstacles) wall.interact(other);
    }

    @Override
    public Point3D getNormal(Point3D impact) {
        return null; //not used
    }

    @Override
    public Point3D getRandomLocation() {
        double x = (Math.random()-0.5)*innerSpace.getWidth();
        double z = (Math.random()-0.5)*innerSpace.getDepth();
        return localToScene(new Point3D(x, 0, z));
    }
}
