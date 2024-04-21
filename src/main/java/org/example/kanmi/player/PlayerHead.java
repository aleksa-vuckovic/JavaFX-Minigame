package org.example.kanmi.player;

import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.example.kanmi.gameobject.GameObject;

public class PlayerHead extends GameObject {

    private PerspectiveCamera camera = new PerspectiveCamera(true);
    private Translate height = new Translate(0,0,0);
    private Rotate tilt = new Rotate(0, Rotate.X_AXIS);
    public PlayerHead() {
        camera.setNearClip(0.1);
        camera.setFarClip(1000);
        camera.setFieldOfView(100);
        getChildren().addAll(camera);
        getTransforms().addAll(height, tilt);
    }

    @Override
    public void interact(GameObject other) {

    }

    public void setHeight(double height) {
        this.height.setY(-height);
    }
    public void tilt(double dtilt) {
        double cur = tilt.getAngle();
        cur+=dtilt;
        if (cur > 90 || cur < -90) return;
        tilt.setAngle(cur);
    }
    public PerspectiveCamera getCamera() {
        return camera;
    }

}
