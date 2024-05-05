package org.example.kanmi.gameobject;

import org.example.kanmi.Game;
import org.example.kanmi.IntervalTimer;
import org.example.kanmi.Utils;

/**
 * A game object that moves inertially.
 */
public abstract class InertialGameObject extends GameObject {

    private IntervalTimer timer;

    @Override
    public void interact(GameObject other) {
        if (other instanceof BarrierObject) Utils.elasticBarrierCollision((BarrierObject) other, this);
        else Utils.elasticPointCollision(this, other);
    }
    @Override
    public void start(Game game) {
        super.start(game);
        timer = new IntervalTimer() {
            @Override
            public void handleInterval(long interval) {
                move(getDirection().multiply(interval));
            }
        };
    }
    @Override
    public void stop() {
        super.stop();
        timer.stop();
        timer = null;
    }
}
