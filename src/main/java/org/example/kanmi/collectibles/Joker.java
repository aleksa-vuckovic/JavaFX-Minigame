package org.example.kanmi.collectibles;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.example.kanmi.misc.Arc3D;
import org.example.kanmi.player.Player;

public class Joker extends Collectible {

    private static double HEIGHT = 30;
    private static double WIDTH = 10;
    private static double DEPTH = 5;
    private static double RADIUS = 5;


    public Joker() {
        Arc3D arc = new Arc3D(RADIUS+WIDTH, RADIUS, 180, 180, DEPTH, 50);
        Box body = new Box(WIDTH, HEIGHT, DEPTH);
        body.getTransforms().addAll(
                new Rotate(-35, Rotate.X_AXIS),
                new Translate(WIDTH/2 + RADIUS, -HEIGHT/2 - RADIUS*3)
        );
        arc.getTransforms().addAll(
                new Rotate(-35, Rotate.X_AXIS),
                new Translate(0, -RADIUS*3)
        );

        PhongMaterial material = new PhongMaterial(Color.WHITE);
        material.setDiffuseMap(new Image("colorful.jpeg"));
        arc.setMaterial(material);
        body.setMaterial(material);

        getChildren().addAll(arc, body);
    }


    @Override
    protected void collected(Player player) {
        double r = Math.random();
        if (r < 0.4) {
            int points = (int)(Math.random()*10) + 1;
            player.getScoreIndicator().inc(points);
        }
        else if (r < 0.6) {
            player.getEnergyIndicator().dec(0.2);
            player.getHealthIndicator().inc(0.2);
        }
        else if (r < 0.8) {
            player.getHealthIndicator().dec(0.2);
            player.getEnergyIndicator().inc(0.2);
            if (player.getHealthIndicator().getValue() <= 0) game.gameOver();
        }
        else if (r < 0.9) game.freeze();
        else player.getImmunityIndicator().inc();
    }
}
