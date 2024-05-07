package org.example.kanmi;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.scene.transform.Rotate;
import javafx.util.Pair;
import org.example.kanmi.arena.Arena;
import org.example.kanmi.collectibles.*;
import org.example.kanmi.enemies.*;
import org.example.kanmi.gameobject.GameObject;
import org.example.kanmi.indicators.*;
import org.example.kanmi.misc.Arc3D;
import org.example.kanmi.misc.Cone;
import org.example.kanmi.player.Player;
import org.example.kanmi.ui.Button;
import org.example.kanmi.ui.Column;

import java.util.*;

public class Game extends Scene {
    public static final double PLAYER_SPEED = 0.1;
    public static final double PLAYER_MASS = 100;
    public static final double METER = 100;
    /**
     * How many degrees per pixel of mouse movement.
     */
    private static final double MOUSE_SENSITIVITY = 0.5;

    private class ItemGenerator extends IntervalTimer {
        public interface Producer {GameObject produce();}
        private final long period;
        private final double probability;
        private final int initial;
        private final Producer producer;
        private long cur = 0;

        public ItemGenerator(long period, double probability, int initial, Producer producer) {
            this.period = period;
            this.probability = probability;
            this.initial = initial;
            this.producer = producer;
        }
        public ItemGenerator(long period, int initial, Producer producer) {
            this(period, 1, initial, producer);
        }
        @Override
        public void handleInterval(long interval) {
            cur += interval;
            if (cur < period) return;
            cur -= period;
            if (probability >= 1 || Math.random() < probability) generate();
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
            new ItemGenerator(5000, 0.3, 10, Freeze::new),
            new ItemGenerator(Long.MAX_VALUE, 3, DumbEnemy::new),
            new ItemGenerator(Long.MAX_VALUE, 1, SmartEnemy::new),
            new ItemGenerator(10000, 0.2, 1, Health::new),
            new ItemGenerator(10000, 0.1, 1, Pill::new),
            new ItemGenerator(15000, 1, 10, Joker::new)
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
        root2D.getChildren().addAll(timeIndicator, freezeIndicator);
        timeIndicator.setTranslateX(WIDTH);
        freezeIndicator.setTranslateX(10); freezeIndicator.setTranslateY(100); freezeIndicator.reset();
        AmbientLight light = new AmbientLight(Color.WHITE);
        PointLight pointLight = new PointLight(Color.WHITE);
        pointLight.setTranslateY(-500);
        root3D.getChildren().add(pointLight);
        Sphere universe = new Sphere(1200);
        PhongMaterial mat = new PhongMaterial(Color.WHITE);
        mat.setSelfIlluminationMap(new Image("nightsky.jpg"));
        universe.setMaterial(mat);
        universe.setCullFace(CullFace.NONE);
        root3D.getChildren().add(universe);
    }

    public void remove(GameObject go) {
        root3D.getChildren().remove(go);
        objects.remove(go);
    }
    public void add(GameObject go) {
        root3D.getChildren().add(go);
        objects.add(go);
    }
    private void add(GameObject go, int i) {
        root3D.getChildren().add(go);
        objects.add(i, go);
    }

    public void setArena(Arena arena) {
        remove(this.arena);
        this.arena = arena;
        add(arena, 0);
    }
    public Arena getArena() { return arena; }
    public void setPlayer(Player player) {
        remove(this.player);
        if (this.player != null) {
            root2D.getChildren().remove(this.player.getScoreIndicator());
            root2D.getChildren().remove(this.player.getEnergyIndicator());
            root2D.getChildren().remove(this.player.getHealthIndicator());
            root2D.getChildren().remove(this.player.getImmunityIndicator());
        }
        this.player = player;
        add(player);
        root2D.getChildren().add(player.getScoreIndicator());
        EnergyIndicator ei = player.getEnergyIndicator();
        ei.setCentered(WIDTH); ei.setTranslateY(30);
        root2D.getChildren().add(ei);
        HealthIndicator hi = player.getHealthIndicator();
        hi.setCentered(WIDTH); ei.setTranslateY(10);
        root2D.getChildren().add(hi);
        ImmunityIndicator ii = player.getImmunityIndicator();
        ii.setTranslateX(10);
        ii.setTranslateY(150);
        root2D.getChildren().add(ii);
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
        for (int i = 0; i < 2; i++) {
            Canon canon = new Canon(25, 40);
            Pair<Point3D, Point3D> loc = arena.getRandomWallLocation();
            canon.setPosition(loc.getKey());
            canon.getTransforms().add(Utils.alignTransform(Rotate.Z_AXIS, loc.getValue()));
            add(canon);
        }
        timeIndicator.start();
        for (GameObject go: objects) go.start(this);
        for (ItemGenerator gen: generators) {
            gen.init();
            gen.start();
        }
        timer = new IntervalTimer() {
            @Override
            public void handleInterval(long interval) {
                for (GameObject go: objects) go.update(interval);
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
                if (freezeIndicator.dec(interval))
                    for (GameObject go: objects)
                        if (go instanceof Enemy)
                            ((Enemy) go).unfreeze();
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

        Column resultScreen = new Column(WIDTH, HEIGHT, Column.HorizontalAlignment.center(), Column.VerticalArrangement.center(20));
        Text gameOverText = new Text("GAME OVER");
        Text scoreText = new Text("Score: " + score);
        gameOverText.setFont(Font.font("Arial", 40));
        scoreText.setFont(Font.font("Arial", 20));
        Button back = new Button("Back", null);
        resultScreen.setBackround(Utils.changeOpacity(Color.WHITE, 0.5f));
        resultScreen.addAll(gameOverText, scoreText, back);
        root2D.getChildren().add(resultScreen);
    }


    private FreezeIndicator freezeIndicator = new FreezeIndicator();
    public void freeze() {
        if (freezeIndicator.inc())
            for (GameObject go: objects)
                if (go instanceof Enemy)
                    ((Enemy) go).freeze();
    }

}
