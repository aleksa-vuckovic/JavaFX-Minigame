package org.example.kanmi.indicators;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import org.example.kanmi.IntervalTimer;
import org.example.kanmi.ui.TextBox;

import java.time.format.DateTimeFormatter;

public class TimeIndicator extends Group {
    private static final double WIDTH = 100.0;
    private static final double HEIGHT = 50.0;
    TextBox text;
    private long time = 0;
    private IntervalTimer timer;
    private Translate position;

    private String format(long timeNano) {
        long secs = timeNano/1000000000;
        long mins = secs/60;
        secs %= 60;
        return "%02d:%02d".formatted(mins, secs);
    }

    public TimeIndicator() {
        text = new TextBox("00:00", 10f, Font.font("Arial", 24f), 0f);
        text.setWidth(WIDTH);
        text.setHeight(HEIGHT);
        text.setTranslateX(-WIDTH);
        getChildren().addAll(text);
        this.position = new Translate();
        getTransforms().addAll(this.position);
        this.timer = new IntervalTimer(IntervalTimer.Precision.NANO) {
            @Override
            public void handleInterval(long interval) {
                time += interval;
                text.setContent(format(time));
            }
        };
    }

    public void start() {
        timer.start();
    }
    public void stop() {
        timer.stop();
    }
    public void reset() {
        time = 0;
        text.setContent(format(time));
    }

    public void setPosition(Point2D position) {
        this.position.setX(position.getX());
        this.position.setY(position.getY());
    }
}
