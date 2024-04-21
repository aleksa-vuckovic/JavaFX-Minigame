package org.example.kanmi;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.example.kanmi.arena.Arena;
import org.example.kanmi.player.Player;

import java.util.HashSet;
import java.util.Set;

public class Game extends Group {
    /**
     * How many degrees per pixel of mouse movement.
     */
    public static final double MOUSE_SENSITIVITY = 0.5;

    private Arena arena;
    private Player player;
    private IntervalTimer timer;
    public enum State {
        PLAYING, STOPPED
    }
    private State state = State.STOPPED;

    public Game() {

    }


    public void setArena(Arena arena) {
        getChildren().remove(arena);
        this.arena = arena;
        getChildren().add(arena);
    }
    public Arena getArena() { return arena; }
    public void setPlayer(Player player) {
        getChildren().remove(player);
        this.player = player;
        getChildren().add(player);
    }

    public void onKeyEvent(KeyEvent keyEvent) {
        if (state != State.PLAYING) return;
        player.keyEvent(keyEvent);
    }
    private Point2D mouse = Point2D.ZERO;
    public void onMouseEvent(MouseEvent mouseEvent) {
        if (state != State.PLAYING) return;
        Point2D newMouse = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
        if (mouse != Point2D.ZERO) {
            double dx = (mouse.getY() - newMouse.getY());
            double dy = (mouse.getX() - newMouse.getX());
            player.rotate(-dy*MOUSE_SENSITIVITY);
            player.tilt(dx*MOUSE_SENSITIVITY);
        }
        mouse = newMouse;
    }

    public void start() {
        state = State.PLAYING;
        player.start(this);
        arena.start(this);
        timer = new IntervalTimer() {
            @Override
            public void handleInterval(long interval) {
                arena.interact(player);
            }
        };
        timer.start();
    }

}
