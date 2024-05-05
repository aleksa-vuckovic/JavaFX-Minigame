package org.example.kanmi.collectibles;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.example.kanmi.enemies.Enemy;
import org.example.kanmi.gameobject.GameObject;
import org.example.kanmi.player.Player;

public class Energy extends Collectible {
    private static final double LENGTH = 20;
    private static final double THICKNESS = 5;

    public Energy() {
        double t = 1/(2*Math.sqrt(2));
        Box[] boxes = new Box[3];
        boxes[0] = new Box(LENGTH, THICKNESS, THICKNESS);
        boxes[0].getTransforms().addAll(new Translate(0, (t-1.3)*LENGTH), new Rotate(45, Rotate.Z_AXIS));
        boxes[1] = new Box(LENGTH, THICKNESS, THICKNESS);
        boxes[1].setTranslateY(-1.3*LENGTH);
        boxes[2] = new Box(LENGTH, THICKNESS, THICKNESS);
        boxes[2].getTransforms().addAll(new Translate(0, (-t-1.3)*LENGTH), new Rotate(45, Rotate.Z_AXIS));
        PhongMaterial mat = new PhongMaterial(Color.WHITESMOKE);
        for (Box box: boxes) box.setMaterial(mat);
        getChildren().addAll(boxes);
    }


    @Override
    public void interact(GameObject other) {
        if (isCollected()) return;
        if (getImpact(other) == null) return;
        if (other instanceof Player) {
            ((Player) other).getEnergyIndicator().inc(0.33);
            setCollected();
            disappear();
        }
        else if (other instanceof Enemy) {
            setCollected();
            vanish();
        }
    }
}
