package org.example.kanmi.ui;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Column extends Group {

    public enum HorizontalAlignment {
        LEFT(), CENTER(), RIGHT();
        private double width = 0;
        public void setWidth(double width) { this.width = width; }
        public double getX(Node node) {
            Bounds bounds = node.getBoundsInLocal();
            if (this == LEFT) return 0 - bounds.getMaxX();
            else if (this == CENTER) return width/2 - bounds.getCenterX();
            else return width - bounds.getMaxX();
        }
    }
    public static class VerticalArrangement {

        private enum Side {TOP, CENTER, BOTTOM}
        private Side side;
        private double spacing;
        private double height;
        public static VerticalArrangement top(double spacing) {
            return new VerticalArrangement(Side.TOP, spacing);
        }
        public static VerticalArrangement center(double spacing) {
            return new VerticalArrangement(Side.CENTER, spacing);
        }
        public static VerticalArrangement bottom(double spacing) {
            return new VerticalArrangement(Side.BOTTOM, spacing);
        }

        public void setHeight(double height) { this.height = height; }
        public VerticalArrangement(Side side, double spacing) {
            this.side = side;
            this.spacing = spacing;
        }

        public List<Double> getY(List<Node> nodes) {
            double totalHeight = 0;
            for (Node node: nodes) totalHeight += node.getBoundsInLocal().getHeight() + spacing;
            totalHeight += spacing;

            double cur;
            if (side == Side.TOP) cur = spacing;
            else if (side == Side.CENTER) cur = height/2 - totalHeight/2 + spacing;
            else cur = height - totalHeight + spacing;

            List<Double> result = new ArrayList<>();
            for (Node node: nodes) {
                result.add(cur);
                cur += node.getBoundsInLocal().getHeight();
                cur += spacing;
            }
            return result;
        }
    }

    double width;
    double height;
    HorizontalAlignment alignment;
    VerticalArrangement arrangement;
    Rectangle background;

    public Column(double width, double height, HorizontalAlignment alignment, VerticalArrangement arrangement) {
        this.width = width;
        this.height = height;
        this.alignment = alignment;
        alignment.setWidth(width);
        this.arrangement = arrangement;
        arrangement.setHeight(height);
        background = new Rectangle(width, height, Color.TRANSPARENT);
        getChildren().add(background);
    }

    public void add(Node node) {
        getChildren().add(node);
        recalculate();
    }
    public void addAll(Node... nodes) {
        getChildren().addAll(nodes);
        recalculate();
    }
    private void recalculate() {
        getChildren().remove(background);
        List<Double> ys = arrangement.getY(getChildren());
        Iterator<Double> yIter = ys.iterator();
        Iterator<Node> nodeIter = getChildren().iterator();
        while (yIter.hasNext()) {
            double y = yIter.next();
            Node node = nodeIter.next();
            node.setTranslateY(y);
            node.setTranslateX(alignment.getX(node));
        }
        if (background != null) getChildren().add(0, background);
    }

    public void setBackround(Color color) {
        background.setFill(color);
    }
}
