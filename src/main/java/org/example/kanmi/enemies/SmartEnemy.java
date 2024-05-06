package org.example.kanmi.enemies;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.example.kanmi.Game;
import org.example.kanmi.IntervalTimer;
import org.example.kanmi.misc.Cone;

public class SmartEnemy extends Enemy {

    private static double RADIUS = 20;
    private static double HEIGHT = 100;
    private static double PRICK_COUNT = 50;
    private Rotate rotate;

    public SmartEnemy() {
        PhongMaterial mat = new PhongMaterial(Color.RED);
        mat.setDiffuseMap(new Image("spikes.jpeg"));

        Cylinder cylinder = new Cylinder(RADIUS, HEIGHT);
        cylinder.setMaterial(mat);
        cylinder.setTranslateY(-HEIGHT/2);
        getChildren().add(cylinder);

        float prickRadius = (float)HEIGHT/10;
        float prickHeight = prickRadius*2;
        for (int i = 0; i < PRICK_COUNT; i++) {
            Cone prick = new Cone(prickRadius, prickHeight);
            prick.getTransforms().addAll(
                    new Rotate(Math.random()*360, Rotate.Y_AXIS),
                    new Translate(0, -prickRadius-Math.random()*(HEIGHT-2*prickRadius), -RADIUS*0.7),
                    new Rotate(90, Rotate.X_AXIS)
            );
            prick.setMaterial(mat);
            getChildren().add(prick);
        }

        rotate = new Rotate(0, Rotate.Y_AXIS);
        getTransforms().add(rotate);
        setMotor(new Point3D(0, 0, 1).multiply(0.4*Game.PLAYER_SPEED*FRICTION_CONST));
        setMass(100);
    }
    @Override
    public void update(long interval) {
        super.update(interval);
        rotate.setAngle(0);
        Point3D direction = sceneToLocal(game.getPlayer().getCenter()).normalize();
        direction = new Point3D(direction.getX(), 0, direction.getZ());
        double angle = direction.angle(0,0,1);
        angle *= Math.signum(direction.getX());
        rotate.setAngle(angle);
    }
}
