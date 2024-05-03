package org.example.kanmi.indicators;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Translate;
import org.example.kanmi.IntervalTimer;
import org.example.kanmi.ui.TextBox;

public class ScoreIndicator extends Group {

    private static final double WIDTH = 100.0;
    private static final double HEIGHT = 50.0;
    private static final double ARC = 0.3f;
    TextBox text;
    private int score = 0;
    private Translate position;

    private String format(int score) {
        String ret = Integer.toString(score);
        while (ret.length() < 4) ret = "0" + ret;
        return ret;
    }

    public ScoreIndicator() {
        text = new TextBox(format(0), 0f, Font.font("Monospace", 24f), ARC);
        text.setWidth(WIDTH);
        text.setHeight(HEIGHT);
        text.setBackground(Color.BLACK);
        text.setColor(Color.YELLOW);
        text.setBorder(Color.YELLOW, 2f);
        text.setTranslateX(-ARC*WIDTH/2);
        text.setTranslateY(-ARC*HEIGHT/2);
        getChildren().addAll(text);
        this.position = new Translate();
        getTransforms().addAll(this.position);
    }

    public void setPosition(Point2D position) {
        this.position.setX(position.getX());
        this.position.setY(position.getY());
    }

    public void inc() {
        setScore(score + 1);
    }
    public void setScore(int score) {
        this.score = score;
        text.setContent(format(score));
    }
    public int getScore() {
        return this.score;
    }

}
