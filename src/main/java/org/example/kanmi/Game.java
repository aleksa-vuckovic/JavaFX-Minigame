package org.example.kanmi;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.example.kanmi.arena.Arena;
import org.example.kanmi.gameobject.GameObject;
import org.example.kanmi.items.Coin;
import org.example.kanmi.player.Player;

import java.util.*;

public class Game extends Group {
    /**
     * How many degrees per pixel of mouse movement.
     */
    public static final double MOUSE_SENSITIVITY = 0.5;
    public static final double COIN_PERIOD = 5000;

    private Arena arena;
    private Player player;
    private List<GameObject> objects = new ArrayList<>();
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

    private void addRandomCoin() {
        Coin coin = new Coin();
        Point3D position = arena.getRandomLocation();
        coin.setPosition(position);
        objects.add(coin);
        getChildren().add(coin);
        coin.start(this);
        System.out.println("Added coin at position " + position);
    }
    public void start() {
        state = State.PLAYING;
        player.start(this);
        arena.start(this);
        for (int i = 0; i < 4; i++) addRandomCoin();
        timer = new IntervalTimer() {
            double coinInterval = 0;
            @Override
            public void handleInterval(long interval) {
                arena.interact(player);
                for (int i = 0; i < objects.size(); i++) objects.get(i).interact(player);

                //Removing stopped objects
                Iterator<GameObject> iter = objects.iterator();
                while(iter.hasNext()) {
                    GameObject go = iter.next();
                    if (go.stopped()) {
                        iter.remove();
                        getChildren().remove(go);
                    }
                }

                //Generate coins
                coinInterval += interval;
                if (coinInterval >= COIN_PERIOD) {
                    coinInterval -= COIN_PERIOD;
                    addRandomCoin();
                }
            }
        };
        timer.start();
    }

}
