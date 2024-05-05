package org.example.kanmi;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import org.example.kanmi.arena.Arena;
import org.example.kanmi.enemies.DumbEnemy;
import org.example.kanmi.enemies.SmartEnemy;
import org.example.kanmi.gameobject.GameObject;
import org.example.kanmi.indicators.EnergyIndicator;
import org.example.kanmi.indicators.TimeIndicator;
import org.example.kanmi.collectibles.Coin;
import org.example.kanmi.collectibles.Energy;
import org.example.kanmi.misc.Cone;
import org.example.kanmi.player.Player;
import org.example.kanmi.ui.Button;
import org.example.kanmi.ui.Column;

import java.util.*;

public class Game extends Scene {
    /**
     * How many degrees per pixel of mouse movement.
     */
    private static final double MOUSE_SENSITIVITY = 0.5;
    public static final double PLAYER_SPEED = 0.1;
    private class ItemGenerator extends IntervalTimer {
        public interface Producer {GameObject produce();}
        private final long period;
        private final int initial;
        private final Producer producer;
        private long cur = 0;

        public ItemGenerator(long period, int initial, Producer producer) {
            this.period = period;
            this.initial = initial;
            this.producer = producer;
        }
        @Override
        public void handleInterval(long interval) {
            cur += interval;
            if (cur < period) return;
            cur -= period;
            generate();
        }
        public void init() {
            for (int i = 0; i < initial; i++) generate();
        }
        public void generate() {
            GameObject go = producer.produce();
            Point3D position = arena.getRandomLocation();
            go.setPosition(position);
            add(go);
            go.start(Game.this);
        }
    }
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
    private List<ItemGenerator> generators = List.of(
            new ItemGenerator(5000, 4, () -> {
                double r = Math.random();
                if (r < 0.2) return Coin.blueCoin();
                else if (r < 0.5) return Coin.greenCoin();
                else return Coin.goldCoin();
            }),
            new ItemGenerator(12000, 4, Energy::new),
            new ItemGenerator(Long.MAX_VALUE, 3, DumbEnemy::new),
            new ItemGenerator(Long.MAX_VALUE, 1, SmartEnemy::new)
    );
    private final TimeIndicator timeIndicator = new TimeIndicator();
    public enum State {
        PLAYING, PENDING_STOP, STOPPED
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
        AmbientLight light = new AmbientLight(Color.WHITE);
        root3D.getChildren().add(light);
    }

    private void remove(GameObject go) {
        root3D.getChildren().remove(go);
        objects.remove(go);
    }
    private void add(GameObject go) {
        root3D.getChildren().add(go);
        objects.add(go);
    }

    public void setArena(Arena arena) {
        remove(this.arena);
        this.arena = arena;
        add(arena);
    }
    public Arena getArena() { return arena; }
    public void setPlayer(Player player) {
        remove(this.player);
        if (this.player != null) {
            root2D.getChildren().remove(this.player.getScoreIndicator());
            root2D.getChildren().remove(this.player.getEnergyIndicator());
        }
        this.player = player;
        add(player);
        root2D.getChildren().add(player.getScoreIndicator());
        EnergyIndicator ei = player.getEnergyIndicator();
        ei.setCentered(WIDTH);
        root2D.getChildren().add(ei);
        scene3D.setCamera(player.getCamera());
    }
    public Player getPlayer() { return player; }

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
        timeIndicator.start();
        for (ItemGenerator gen: generators) {
            gen.init();
            gen.start();
        }
        timer = new IntervalTimer() {
            @Override
            public void handleInterval(long interval) {
                for (int i = 0; i < objects.size(); i++)
                    for (int j = i + 1; j < objects.size(); j++)
                        objects.get(i).interact(objects.get(j));
                //Removing stopped objects
                Iterator<GameObject> iter = objects.iterator();
                while(iter.hasNext()) {
                    GameObject go = iter.next();
                    if (go.stopped()) {
                        iter.remove();
                        root3D.getChildren().remove(go);
                    }
                }
                if (state == State.PENDING_STOP) Game.this.stop();
            }
        };
        timer.start();
    }

    public void gameOver() {
        state = State.PENDING_STOP;
    }

    private void stop() {
        state = State.STOPPED;
        for (GameObject go: objects) go.stop();
        for (ItemGenerator ig: generators) ig.stop();
        timeIndicator.stop();
        timer.stop();
        int score = player.getScoreIndicator().getScore();

        Column resultScreen = new Column(WIDTH, HEIGHT, Column.HorizontalAlignment.CENTER, Column.VerticalArrangement.center(20));
        Text gameOverText = new Text("GAME OVER");
        Text scoreText = new Text("Score: " + score);
        gameOverText.setFont(Font.font("Arial", 40));
        scoreText.setFont(Font.font("Arial", 20));
        Button back = new Button("Back", null);
        resultScreen.setBackround(Utils.changeOpacity(Color.WHITE, 0.5f));
        resultScreen.addAll(gameOverText, scoreText, back);
        root2D.getChildren().add(resultScreen);
    }

}
