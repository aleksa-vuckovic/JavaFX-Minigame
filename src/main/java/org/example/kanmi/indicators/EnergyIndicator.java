package org.example.kanmi.indicators;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

public class EnergyIndicator extends BarIndicator {
    public EnergyIndicator() {
        super(Color.DEEPSKYBLUE, Color.GREY, 200, 5);
    }
}
