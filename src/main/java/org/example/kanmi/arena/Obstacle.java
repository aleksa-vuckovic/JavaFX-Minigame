package org.example.kanmi.arena;

import javafx.scene.paint.Material;
import org.example.kanmi.gameobject.BarrierObject;

public abstract class Obstacle extends BarrierObject {
    public abstract void setMaterial(Material mat);
}
