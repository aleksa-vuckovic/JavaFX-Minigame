package org.example.kanmi.collectibles;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import org.example.kanmi.enemies.Enemy;
import org.example.kanmi.gameobject.GameObject;
import org.example.kanmi.player.Player;

public class Health extends Collectible {

    private static final double RADIUS = 20;
    private static final double THICKNESS = 10;

    public Health() {
        PhongMaterial mat = new PhongMaterial(Color.GREEN);
        Box h = new Box(RADIUS*2, THICKNESS, THICKNESS);
        Box v = new Box(THICKNESS, RADIUS*2, THICKNESS);
        h.setMaterial(mat); v.setMaterial(mat);
        h.setTranslateY(-RADIUS*1.3); v.setTranslateY(-RADIUS*1.3);
        getChildren().addAll(h, v);
    }
    @Override
    protected void collected(Player player) {
        player.getHealthIndicator().inc(0.25);
    }
}
