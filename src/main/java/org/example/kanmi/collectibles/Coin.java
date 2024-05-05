package org.example.kanmi.collectibles;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.example.kanmi.enemies.Enemy;
import org.example.kanmi.gameobject.GameObject;
import org.example.kanmi.player.Player;

public class Coin extends Collectible {

    private static final double RADIUS = 15;
    private static final double THICKNESS = 5;

    Cylinder cyl;
    Rotate cylRotation;
    int points;

    public Coin(PhongMaterial mat, int points) {
        this.points = points;
        cyl = new Cylinder(RADIUS, THICKNESS);

        cyl.setMaterial(mat);
        cylRotation = new Rotate(0, Rotate.Y_AXIS);
        cyl.getTransforms().addAll(new Translate(0,-RADIUS*1.3,0), new Rotate(90, Rotate.X_AXIS));
        getChildren().addAll(cyl);
    }
    public static Coin goldCoin() {
        PhongMaterial mat = new PhongMaterial(Color.YELLOW);
        mat.setDiffuseMap(new Image("gold.jpg"));
        return new Coin(mat, 1);
    }
    public static Coin greenCoin() {
        PhongMaterial mat = new PhongMaterial(Color.GREEN);
        return new Coin(mat, 3);
    }
    public static Coin blueCoin() {
        PhongMaterial mat = new PhongMaterial(Color.BLUE);
        return new Coin(mat, 5);
    }

    @Override
    public void interact(GameObject other) {
        if (isCollected()) return;
        if (getImpact(other) == null) return;
        if (other instanceof Player) {
            ((Player) other).getScoreIndicator().inc(points);
            setCollected();
            disappear();
        }
        else if (other instanceof Enemy) {
            setCollected();
            vanish();
        }

    }
}
