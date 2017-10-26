package com.windylee.scene.entity;

/**
 * Created by windylee on 4/20/17.
 */

public class ConnEvent {

    public static final int OPEN = 1;
    public static final int CLOSE = 2;

    private int type;
    private Scene mScene;
    private boolean isOpen;

    public ConnEvent(int type, Scene scene, boolean isOpen) {
        this.type = type;
        this.mScene = scene;
        this.isOpen = isOpen;
    }

    public int getType() {
        return type;
    }

    public Scene getScene() {
        return mScene;
    }

    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public String toString() {
        return "ConnEvent{" +
                "type=" + type +
                ", mScene=" + mScene +
                ", isOpen=" + isOpen +
                '}';
    }
}
