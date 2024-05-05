package org.example.kanmi.enemies;

import javafx.geometry.Point3D;
import org.example.kanmi.Utils;
import org.example.kanmi.collectibles.Collectible;
import org.example.kanmi.gameobject.BarrierObject;
import org.example.kanmi.gameobject.GameObject;
import org.example.kanmi.gameobject.SelfMovingGameObject;
import org.example.kanmi.player.Player;

public class Enemy extends SelfMovingGameObject {

    @Override
    public void interact(GameObject go) {
        if (go instanceof Collectible || go instanceof BarrierObject) { go.interact(this); return; }
        Point3D impact = getImpact(go);
        if (impact == null) return;
        if (go instanceof Player) {
            game.gameOver();
        }
        else if (go instanceof Enemy) {
            Utils.elasticPointCollision(this, go);
        }
    }
}
