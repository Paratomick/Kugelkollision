package de;

import com.sun.javafx.geom.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Kugel {

	public static final double GRAVITY = 0;//.00005f;
	public static final double CENTER_PULL = .0f;//005f;
	public static final double SIDE_REFLECTION = -0.9;

    public double x;
    public double y;
    public double radius;
    public boolean infected = false;

    public double fx;
    public double fy;

    public int[] grid;

    private double mass;
    private Color color;
    private double speed;
    private double speedx;
    private double speedy;

    public double epsilon = 0f;

    private double forcex, forcey;
    private double maxforcex, maxforcey;
    private double movex, movey;

    private Umgebung umgebung;

    public Kugel(Umgebung umgebung, double x, double y, double radius, double mass) {

        this.umgebung = umgebung;
        this.forcex = 0;
        this.forcey = 0;

        this.x = x;
        this.y = y;
        this.radius = radius;

        this.grid = new int[2];

        this.mass = mass;

        this.speed = .001f;
        speedx = 0;
        speedy = 0;
    }

    public void setGrid(int gx, int gy) {
        this.grid[0] = gx;
        this.grid[1] = gy;
    }

    public int[] getGrid() {
        return grid;
    }

    public boolean update(double delta) {

        //speedx *= 0.99f;
        //speedy *= 0.99f;

        if (color == Color.RED) {
            Input input = Input.getInput();
            if (input.isKeyDown(Input.KEY_A)) speedx += speed * -delta;
            if (input.isKeyDown(Input.KEY_D)) speedx += speed * delta;
            if (input.isKeyDown(Input.KEY_S)) speedy += speed * delta;
            if (input.isKeyDown(Input.KEY_W)) speedy += speed * -delta;
        } else {
            double length = Math.sqrt((umgebung.getWidth()/2 - x) * (umgebung.getWidth()/2 - x) + (umgebung.getHeight()/2 - y) * (umgebung.getHeight()/2 - y));
            double normalx = (umgebung.getWidth()/2 - x) / length;
            double normaly = (umgebung.getHeight()/2 - y) / length;
            speedx += normalx * CENTER_PULL;
            speedy += normaly * CENTER_PULL;
        }

        speedx += Math.max(-maxforcex, Math.min(forcex, maxforcex));
        speedy += Math.max(-maxforcey, Math.min(forcey, maxforcey));
        forcex = 0; forcey = 0; maxforcex = 0; maxforcey = 0;

        speedy += delta * radius * GRAVITY;

        x += movex;
        y += movey;
        movex = 0;
        movey = 0;

        x += speedx * delta;
        y += speedy * delta;

        if (x < -radius) {
            x += umgebung.getWidth();
            //x = radius;
            //speedx *= SIDE_REFLECTION;
        }

        if (x > umgebung.getWidth() + radius) {
            x -= umgebung.getWidth();
            //x = umgebung.getWidth() - radius;
            //speedx *= SIDE_REFLECTION;
        }

        if (y < -radius) {
            y += umgebung.getHeight();
            //y = radius;
            //speedy *= SIDE_REFLECTION;
        }

        if (y > umgebung.getHeight() + radius) {
            y -= umgebung.getHeight();
            //y = umgebung.getHeight() - radius;
            //speedy *= SIDE_REFLECTION;
        }

        return true;
    }

    public void render(GraphicsContext g) {
        g.setFill(color);
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    public boolean kollisionKugelKugel(Kugel k) {
        return (((x - k.x) * (x - k.x) + (y - k.y) * (y - k.y) <= (radius + k.radius) * (radius + k.radius)));
    }

    public void addForce(double vx, double vy) {
        forcex += vx;
        forcey += vy;
        if (Math.abs(vx) > maxforcex) maxforcex = Math.abs(vx);
        if (Math.abs(vy) > maxforcey) maxforcey = Math.abs(vy);
    }

    public void addMovement(double vx, double vy) {
        movex += vx;
        movey += vy;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public double getSpeedX() {
        return speedx;
    }

    public double getSpeedY() {
        return speedy;
    }

    public double getMass() {
        return mass;
    }
}
