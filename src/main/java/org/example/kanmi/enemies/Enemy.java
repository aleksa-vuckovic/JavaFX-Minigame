package org.example.kanmi.enemies;

import javafx.geometry.Point3D;
import org.example.kanmi.Utils;
import org.example.kanmi.collectibles.Collectible;
import org.example.kanmi.gameobject.BarrierObject;
import org.example.kanmi.gameobject.GameObject;
import org.example.kanmi.gameobject.SelfMovingGameObject;
import org.example.kanmi.player.Player;

public class Enemy extends SelfMovingGameObject {

    private Point3D savedMotor = null;
    public void freeze() {
        if (savedMotor != null) return;
        savedMotor = getMotor();
        setMotor(Point3D.ZERO);
    }
    public boolean frozen() {
        return savedMotor != null;
    }
    public void unfreeze() {
        if (savedMotor == null) return;
        setMotor(savedMotor);
        savedMotor = null;
    }

    @Override
    public void interact(GameObject go) {
        if (go instanceof Collectible || go instanceof BarrierObject) { go.interact(this); return; }
        Point3D impact = getImpact(go);
        if (impact == null) return;
        if (go instanceof Player) {
            /*
            Utils.obstaclePointCollision(this, (Player)go);
            ((Player)go).enemyTouch();
            */
            Utils.elasticPointCollision(this, (Player)go);
            ((Player)go).enemyHit();
        }
        else if (go instanceof Enemy) {
            Utils.elasticPointCollision(this, (Enemy)go);
        }
    }
}
