package org.example.kanmi.enemies;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import org.example.kanmi.Game;
import org.example.kanmi.IntervalTimer;
import org.example.kanmi.collectibles.Collectible;
import org.example.kanmi.gameobject.GameObject;
import org.example.kanmi.gameobject.MovingGameObject;
import org.example.kanmi.player.Player;

public class CanonBall extends MovingGameObject {


    private Sphere ball;
    private Canon parent;

    public CanonBall(double radius, Canon parent) {
        this.parent = parent;
        ball = new Sphere(radius);
        PhongMaterial mat = new PhongMaterial(Color.GRAY);
        mat.setSpecularPower(10);
        mat.setSpecularColor(Color.WHITESMOKE);
        ball.setMaterial(mat);
        getChildren().addAll(ball);
    }

    @Override
    public void interact(GameObject other) {
        if (other instanceof Collectible) {
            other.interact(this);
            return;
        }
        if (other == parent) return;
        if (getImpact(other) == null) return;
        stop();
        if (other instanceof Player) {
            ((Player)other).canonBallHit();
        }
    }

    @Override
    protected Point3D netForce() {
        return Point3D.ZERO; //No gravity, no air resistance
    }
}
