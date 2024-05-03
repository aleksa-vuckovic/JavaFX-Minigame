package org.example.kanmi.indicators;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

public class EnergyIndicator extends Group {

    private static final double WIDTH = 200.0;
    private static final double HEIGHT = 5.0;
    private Color foreColor = Color.DEEPSKYBLUE;
    private Color backColor = Color.WHITE;
    private double energy = 1.0;
    private Rectangle rect = new Rectangle(WIDTH, HEIGHT);
    public EnergyIndicator() {
        setEnergy(1);
        getChildren().add(rect);
    }

    public double getEnergy() {
        return energy;
    }
    public void setEnergy(double energy) {
        if (energy < 0) energy = 0;
        if (energy > 1) energy = 1;
        this.energy = energy;

        LinearGradient lg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop[]{new Stop(0, foreColor), new Stop(energy, foreColor), new Stop(energy, backColor), new Stop(1, backColor)});
        rect.setFill(lg);
    }
    public void dec(double amount) {
        setEnergy(energy - amount);
    }
    public void inc(double amount) {
        setEnergy(energy + amount);
    }

    public void setCentered(double screenWidth) {
        setTranslateX(screenWidth/2 - WIDTH/2);
        setTranslateY(HEIGHT*3);
    }
}
