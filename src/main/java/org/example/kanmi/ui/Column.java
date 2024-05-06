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

    public static class HorizontalAlignment {
        private enum Type {LEFT, CENTER, RIGHT}
        public static HorizontalAlignment left() { return new HorizontalAlignment(Type.LEFT); }
        public static HorizontalAlignment center() { return new HorizontalAlignment(Type.CENTER); }
        public static HorizontalAlignment right() { return new HorizontalAlignment(Type.RIGHT); }
        Type type;
        private double width = 0;
        HorizontalAlignment(Type type) {
            this.type = type;
        }
        public void setWidth(double width) { this.width = width; }
        public double getX(Node node) {
            Bounds bounds = node.getBoundsInLocal();
            if (type == Type.LEFT) return 0 - bounds.getMaxX();
            else if (type == Type.CENTER) return width/2 - bounds.getCenterX();
            else return width - bounds.getMaxX();
        }
    }
    public static class VerticalArrangement {

        private enum Type {TOP, CENTER, BOTTOM}
        private Type type;
        private double spacing;
        private double height;
        public static VerticalArrangement top(double spacing) {
            return new VerticalArrangement(Type.TOP, spacing);
        }
        public static VerticalArrangement center(double spacing) {
            return new VerticalArrangement(Type.CENTER, spacing);
        }
        public static VerticalArrangement bottom(double spacing) {
            return new VerticalArrangement(Type.BOTTOM, spacing);
        }

        public void setHeight(double height) { this.height = height; }
        public VerticalArrangement(Type type, double spacing) {
            this.type = type;
            this.spacing = spacing;
        }

        public List<Double> getY(List<Node> nodes) {
            double totalHeight = 0;
            for (Node node: nodes) totalHeight += node.getBoundsInLocal().getHeight() + spacing;
            totalHeight += spacing;

            double cur;
            if (type == Type.TOP) cur = spacing;
            else if (type == Type.CENTER) cur = height/2 - totalHeight/2 + spacing;
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
