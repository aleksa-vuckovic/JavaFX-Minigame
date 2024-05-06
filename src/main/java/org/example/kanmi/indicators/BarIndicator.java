package org.example.kanmi.indicators;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

public class BarIndicator extends Group {

    private double width;
    private double height;
    private Color foreColor;
    private Color backColor;
    private double value = 1.0;
    private Rectangle rect;
    public BarIndicator(Color foreColor, Color backColor, double width, double height) {
        rect = new Rectangle(width, height);
        this.width = width;
        this.height = height;
        this.foreColor = foreColor;
        this.backColor = backColor;
        setValue(1);
        getChildren().add(rect);
    }

    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        if (value < 0) value = 0;
        if (value > 1) value = 1;
        this.value = value;

        LinearGradient lg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop[]{new Stop(0, foreColor), new Stop(value, foreColor), new Stop(value, backColor), new Stop(1, backColor)});
        rect.setFill(lg);
    }
    public void dec(double amount) {
        setValue(value - amount);
    }
    public void inc(double amount) {
        setValue(value + amount);
    }

    public void setCentered(double screenWidth) {
        setTranslateX(screenWidth/2 - width/2);
        setTranslateY(height*3);
    }
}
