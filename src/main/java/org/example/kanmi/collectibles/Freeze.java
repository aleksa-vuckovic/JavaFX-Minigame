package org.example.kanmi.collectibles;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import org.example.kanmi.enemies.Enemy;
import org.example.kanmi.gameobject.GameObject;
import org.example.kanmi.player.Player;

public class Freeze extends Collectible {

    private static double RADIUS = 20;
    private static double THICKNESS = 5;

    public Freeze() {
        PhongMaterial mat = new PhongMaterial(Color.LIGHTBLUE);
        Box[] arms = new Box[3];
        for (int i = 0; i < 3; i++) {
            arms[i] = new Box(RADIUS*2, THICKNESS, THICKNESS);
            arms[i].setRotationAxis(Rotate.Z_AXIS);
            arms[i].setRotate((double)i*360/3);
            arms[i].setTranslateY(-RADIUS*1.3);
            arms[i].setMaterial(mat);
        }
        getChildren().addAll(arms);
    }
    @Override
    protected void collected(Player player) {
        game.freeze();
    }
}
