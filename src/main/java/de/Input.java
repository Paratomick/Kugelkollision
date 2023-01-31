package de;

import javafx.scene.Scene;

public class Input {

    public static final int KEY_A = 65;
    public static final int KEY_C = 67;
    public static final int KEY_D = 68;
    public static final int KEY_G = 71;
    public static final int KEY_S = 83;
    public static final int KEY_W = 87;

    public static final int MOUSE_LEFT_BUTTON = 1;
    public static final int MOUSE_RIGHT_BUTTON = 2;

    private static Input input;

    private boolean[] keysDown = new boolean[300];
    private boolean[] keysPressed = new boolean[300];
    private boolean[] mouseDown = new boolean[50];
    private double mX = 0, mY = 0;

    Scene scene;

    public Input(Scene scene) {
        this.scene = scene;
        Input.input = this;

        scene.setOnKeyPressed(keyEvent -> {
            int i = keyEvent.getCode().getCode();
            if(i < 300)
                keysDown[i] = true;
        });
        scene.setOnKeyReleased(keyEvent -> {
            int i = keyEvent.getCode().getCode();
            if(i < 300) {
                keysDown[i] = false;
                keysPressed[i] = true;
            }
        });
        scene.setOnMousePressed(mouseEvent -> {
            switch (mouseEvent.getButton().name()) {
                case "PRIMARY": mouseDown[MOUSE_LEFT_BUTTON] = true;
                case "SECONDARY": mouseDown[MOUSE_RIGHT_BUTTON] = true;
            }
        });
        scene.setOnMouseReleased(mouseEvent -> {
            switch (mouseEvent.getButton().name()) {
                case "PRIMARY": mouseDown[MOUSE_LEFT_BUTTON] = false;
                case "SECONDARY": mouseDown[MOUSE_RIGHT_BUTTON] = false;
            }
        });
        scene.setOnMouseMoved(mouseEvent -> {
            mX = mouseEvent.getX();
            mY = mouseEvent.getY();
        });
    }

    public static Input getInput() {
        return input;
    }

    public boolean isKeyDown(int key) {
        return keysDown[key];
    }

    public boolean isKeyPressed(int key) {
        boolean b = keysPressed[key];
        keysPressed[key] = false;
        return b;
    }

    public boolean isMouseButtonDown(int mouseKey) {
        return mouseDown[mouseKey];
    }

    public double getMouseX() {
        return mX;
    }

    public double getMouseY() {
        return mY;
    }
}
