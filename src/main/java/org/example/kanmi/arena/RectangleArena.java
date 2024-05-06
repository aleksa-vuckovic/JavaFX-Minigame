package org.example.kanmi.arena;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import org.example.kanmi.gameobject.GameObject;

public class RectangleArena extends Arena {

    private final static double OBSTACLE_HEIGHT = 75;

    private Wall ground;
    private Wall[] walls;
    private Box innerSpace;

    private Obstacle[] obstacles;

    public RectangleArena(double width, double length,
                          double wallThickness, double wallHeight) {
        ground = new Wall(width, length, 30); ground.setTranslateY(30);
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

        obstacles = new Obstacle[12];
        Image stoneImage = new Image("stone.jpg");
        PhongMaterial mat = new PhongMaterial(Color.GRAY);
        mat.setDiffuseMap(stoneImage);
        for (int i = 0; i < 12; i++) {
            double p = Math.random();
            if (p < 1.0/3) obstacles[i] = new Wall(OBSTACLE_HEIGHT, OBSTACLE_HEIGHT, OBSTACLE_HEIGHT);
            else if (p < 2.0/3) obstacles[i] = new LWall(OBSTACLE_HEIGHT*2, OBSTACLE_HEIGHT*1.6, OBSTACLE_HEIGHT*0.8);
            else obstacles[i] = new PWall(OBSTACLE_HEIGHT, OBSTACLE_HEIGHT*0.8, OBSTACLE_HEIGHT*0.2);
            obstacles[i].setPosition(getRandomLocation());
            obstacles[i].getTransforms().add(new Rotate(Math.random()*360, Rotate.Y_AXIS));
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
    @Override
    public void setObstacleMaterial(Material mat) { for (Obstacle obstacle: obstacles) obstacle.setMaterial(mat);}

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
        ground.interact(other);
        for (Wall wall: walls) wall.interact(other);
        for (Obstacle obstacle: obstacles) obstacle.interact(other);
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
