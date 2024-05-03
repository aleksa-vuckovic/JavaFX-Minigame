package org.example.kanmi.ui;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.example.kanmi.Utils;

public class TextBox extends Group {

    Text text;
    Rectangle rect;
    Double width, height;
    Paint color;
    Paint background;
    double pad, arc;
    public TextBox(String content, double pad, Font font, double arc) {
        text = new Text(content);
        rect = new Rectangle();
        this.pad = pad;
        this.arc = arc;
        this.color = Color.BLACK;
        this.background = Color.WHITE;
        text.setFont(font);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(1f);
        rect.setStrokeType(StrokeType.OUTSIDE);
        getChildren().addAll(rect, text);
        this.recalculate();
    }

    public void setOnClick(Runnable onClick) {
        if (onClick != null) {
            this.setOnMouseClicked(mouseEvent -> onClick.run());
            this.setOnMousePressed(mouseEvent -> {
                rect.setFill(Color.LIGHTGRAY);
            });
            this.setOnMouseReleased(mouseEvent -> {
                rect.setFill(background);
            });
        }
        else {
            this.setOnMouseClicked(null);
            this.setOnMousePressed(null);
            this.setOnMouseReleased(null);
        }
    }

    public void setContent(String content) {
        text.setText(content);
        this.recalculate();
    }
    public void setColor(Paint color) {
        this.color = color;
        recalculate();
    }
    public void setBorder(Paint color, double width) {
        rect.setStroke(color);
        rect.setStrokeWidth(width);
    }
    public void setBackground(Paint color) {
        this.background = color;
        recalculate();
    }
    public void setWidth(Double width) {
        this.width = width;
        recalculate();
    }
    public void setHeight(Double height) {
        this.height = height;
        recalculate();
    }

    private void recalculate() {
        text.setFill(color);
        rect.setFill(background);
        double textWidth = text.getLayoutBounds().getWidth();
        double textHeight = Utils.getTextHeight(text);
        if (width == null) {
            rect.setWidth(textWidth + 2*pad);
            text.setTranslateX(pad);
        } else {
            rect.setWidth(width);
            double space = (width - textWidth)/2;
            text.setTranslateX(space > 0 ? space: 0);
        }
        if (height == null) {
            rect.setHeight(textHeight + 2*pad);
            text.setTranslateY(textHeight + pad);
        } else {
            rect.setHeight(height);
            double space = (height - textHeight)/2;
            text.setTranslateY(space > 0 ? space + textHeight: textHeight);
        }

        rect.setArcWidth(textWidth*arc);
        rect.setArcHeight(textHeight*arc);
    }
    public String getContent() {
        return text.getText();
    }
}
