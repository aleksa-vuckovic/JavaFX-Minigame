package org.example.kanmi;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.example.kanmi.arena.Arena;
import org.example.kanmi.gameobject.GameObject;
import org.example.kanmi.indicators.EnergyIndicator;
import org.example.kanmi.indicators.ScoreIndicator;
import org.example.kanmi.indicators.TimeIndicator;
import org.example.kanmi.items.Coin;
import org.example.kanmi.player.Player;

import java.util.*;

public class Game extends Scene {
    /**
     * How many degrees per pixel of mouse movement.
     */
    private static final double MOUSE_SENSITIVITY = 0.5;
    private static final double COIN_PERIOD = 5000;
    private static final double WIDTH = 800;
    private static final double HEIGHT = 500;

    private final Group root3D = new Group();
    private final Group root2D = new Group();
    private final SubScene scene3D = new SubScene(root3D, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
    private final SubScene scene2D = new SubScene(root2D, WIDTH, HEIGHT);
    private Arena arena;
    private Player player;
    private List<GameObject> objects = new ArrayList<>();
    private IntervalTimer timer;
    private final TimeIndicator timeIndicator = new TimeIndicator();
    public enum State {
        PLAYING, STOPPED
    }
    private State state = State.STOPPED;

    public Game() {
        super(new Group(), WIDTH, HEIGHT);
        Group root = new Group();
        root.getChildren().addAll(scene3D, scene2D);
        super.setRoot(root);

        addEventHandler(KeyEvent.ANY, this::onKeyEvent);
        addEventHandler(MouseEvent.ANY, this::onMouseEvent);
        root2D.getChildren().add(timeIndicator);
        timeIndicator.setPosition(new Point2D(WIDTH, 0));
    }


    public void setArena(Arena arena) {
        root3D.getChildren().remove(this.arena);
        this.arena = arena;
        root3D.getChildren().add(arena);
    }
    public Arena getArena() { return arena; }
    public void setPlayer(Player player) {
        if (this.player != null) {
            root3D.getChildren().remove(this.player);
            root2D.getChildren().remove(this.player.getScoreIndicator());
            root2D.getChildren().remove(this.player.getEnergyIndicator());
        }
        this.player = player;
        root3D.getChildren().add(player);
        root2D.getChildren().add(player.getScoreIndicator());
        EnergyIndicator ei = player.getEnergyIndicator();
        ei.setCentered(WIDTH);
        root2D.getChildren().add(ei);
        scene3D.setCamera(player.getCamera());
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
        root3D.getChildren().add(coin);
        coin.start(this);
    }
    public void start() {
        state = State.PLAYING;
        player.start(this);
        arena.start(this);
        timeIndicator.start();
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
                        root3D.getChildren().remove(go);
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
