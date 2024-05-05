package org.example.kanmi.enemies;

import javafx.geometry.Point3D;
import javafx.scene.effect.Light;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.example.kanmi.Game;
import org.example.kanmi.IntervalTimer;
import org.example.kanmi.Utils;
import org.example.kanmi.collectibles.Collectible;
import org.example.kanmi.gameobject.BarrierObject;
import org.example.kanmi.gameobject.GameObject;
import org.example.kanmi.gameobject.SelfMovingGameObject;
import org.example.kanmi.misc.Cone;
import org.example.kanmi.player.Player;

public class DumbEnemy extends Enemy {

    private static double RADIUS = 20;
    private static double HEIGHT = 100;
    private static double PRICK_COUNT = 50;
    private Rotate rotate;
    private IntervalTimer timer;
    public DumbEnemy() {
        PhongMaterial mat = new PhongMaterial(Color.RED);
        mat.setDiffuseMap(new Image("spikes.jpeg"));

        Cylinder cylinder = new Cylinder(RADIUS, HEIGHT);
        cylinder.setMaterial(mat);
        cylinder.setTranslateY(-HEIGHT/2);
        getChildren().add(cylinder);

        float prickRadius = (float)HEIGHT/10;
        float prickHeight = prickRadius*2;
        for (int i = 0; i < PRICK_COUNT; i++) {
            Cone prick = new Cone(prickRadius, prickHeight);
            prick.getTransforms().addAll(
                    new Rotate(Math.random()*360, Rotate.Y_AXIS),
                    new Translate(0, -prickRadius-Math.random()*(HEIGHT-2*prickRadius), -RADIUS*0.7),
                    new Rotate(90, Rotate.X_AXIS)
            );
            prick.setMaterial(mat);
            getChildren().add(prick);
        }

        rotate = new Rotate(0, Rotate.Y_AXIS);
        getTransforms().add(rotate);
        setMotor(new Point3D(0, 0, 0.05));
        setMass(100);

    }

    @Override
    public void start(Game game) {
        super.start(game);
        timer = new IntervalTimer() {
            long passed = 0;
            @Override
            public void handleInterval(long interval) {
                passed += interval;
                if (passed < 2000) return;
                passed -= 2000;
                double angle = Math.random()*360;
                rotate.setAngle(angle);
            }
        };
        timer.start();
    }

    @Override
    public void stop() {
        super.stop();
        timer.stop();
    }

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
/**
 * Bio jednom jedan decak Janko. Vole je da plese. u JEDNOM PLESNOM TURNIRU OSVOJIO JE TROFEJ. Medjutim,
 * kada je stigao kuci video je da je u trofeju mali patuljak. Mama i Tata su mu cestitali i kupili mu plesni podijum.
 * Ali on se toliko umprio da je legao da spava. Sanjao je jedan cudan san. Kako je vilenjak dao Janku mapu,
 * potragu za maglom. Marko je iskocio brzo kroz prozor, uzeo ranac koji je bio u basti, uzeo flasicu koja je bila u rancu,
 * i npunio je vodom koja je bila iz cesme, a cesma je bila u dvoristu. Krenuo je u potragu, ali je bila toliko duga da je
 * morao da popije malo vode, i onda je krenuo. Video je maglu i usao u nju i cuo glas mame, "Marko marko, ajde da jedes".
 * Marko je, posto je nije video, jako otvorio oci, i onda se probudio jer je jako otvorio oci, orvorio je stvarno oci,
 * i probudio se. Otisao je kod mame, za sto, i krenuo da jede. Mama kada je pitala Marka, gde su ti napocare, rekao je
 * sad cu da odem kada pojedem rucak. I kada je pojeo rucak stavio je naocare. Pogledao je u trofej i shvatio je
 * da je samo posto nije imao naocare video patuljka.
 */