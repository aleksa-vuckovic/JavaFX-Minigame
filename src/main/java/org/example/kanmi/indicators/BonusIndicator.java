package org.example.kanmi.indicators;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.example.kanmi.Utils;

public class BonusIndicator extends Group {

    private static double OUTER_RADIUS = 20;
    private static double INNER_RADIUS = 15;

    private Text text;
    private Circle inner;
    private Arc outer;
    long max;
    Long value;

    public BonusIndicator(Color color, long max) {
        this.max = max;
        outer = new Arc(OUTER_RADIUS, OUTER_RADIUS, OUTER_RADIUS, OUTER_RADIUS, 90, 360);
        outer.setType(ArcType.ROUND);
        outer.setFill(color);
        inner = new Circle(OUTER_RADIUS, OUTER_RADIUS, INNER_RADIUS);
        inner.setFill(Utils.lighter(color, 0.8));
        text = new Text("0");
        text.setTranslateX(OUTER_RADIUS-INNER_RADIUS);
        text.setTranslateY(OUTER_RADIUS-INNER_RADIUS);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 12));
    }

    /**
     * @return True if the indicator state is changed.
     */
    public boolean inc() {
        boolean ret = value == null;
        value = max;
        update();
        return ret;
    }
    public boolean dec(long amount) {
        if (value == null) return false;
        value -= amount;
        if (value <= 0) {
            value = null;
            update();
            return true;
        }
        update();
        return false;
    }
    public boolean isActive() { return value != null; }

    private void update() {
        if (value == null) {
            getChildren().clear();
            return;
        }
        if (getChildren().isEmpty()) getChildren().addAll(outer, inner, text);
        outer.setLength((double)value/max*360);
        text.setText(Integer.toString((int)(value/1000)));
        Point3D textCenter = Utils.center(text.getBoundsInLocal());
        text.setTranslateX(OUTER_RADIUS - textCenter.getX());
        text.setTranslateY(OUTER_RADIUS - textCenter.getY());
    }

    public void reset() {
        value = null;
        update();
    }
}
