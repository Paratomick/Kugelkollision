package de;

import com.sun.javafx.geom.Vec2d;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Umgebung {

    private int anzahlFelder = 1;

    private ArrayList<Kugel> kugeln;
    private List<Kugel> neueKugeln;
    private List<Kugel> veralteteKugeln;
    private List<Kugel>[][] grid;

    public static final int GRID_SIZE = 26;

    public static final int ANZAHL_KUGELN = 10000;

    public static final double RADIUS_KUGEL = 2;
    public static final double MASSE_KUGEL = 2;
    public static final int RADIUS_HAUPTKUGEL = 40;
    public static final double MASSE_HAUPTKUGEL = 10000;

    public static final double MIN_COLLISION_QUAD = RADIUS_KUGEL + RADIUS_HAUPTKUGEL;

    private static Random random = new Random();

    Canvas canvas;
    GraphicsContext g;

    public Umgebung(StackPane root) {
        canvas = new Canvas();
        root.getChildren().add(canvas);
        g = canvas.getGraphicsContext2D();
        g.setFill(Color.YELLOW);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void init() {
        kugeln = new ArrayList<>();
        neueKugeln = new ArrayList<>();
        veralteteKugeln = new ArrayList<>();
        grid = new ArrayList[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = new ArrayList<>(ANZAHL_KUGELN);
            }
        }

        double[] gridScale = {canvas.getWidth() / GRID_SIZE, canvas.getHeight() / GRID_SIZE};
        Kugel nK;
        nK = new Kugel(this, 50, 50, RADIUS_HAUPTKUGEL, MASSE_HAUPTKUGEL);
        nK.setColor(Color.RED);
        nK.infected = true;
        neueKugeln.add(nK);
        grid[(int) Math.max(0, Math.min(nK.x / gridScale[0], GRID_SIZE - 1))][(int) Math.max(0, Math.min(nK.y / gridScale[1], GRID_SIZE - 1))].add(nK);

        for (int i = 0; i < ANZAHL_KUGELN; i++) {
            double radiusKugeln = random.nextDouble() * (RADIUS_KUGEL * 0.6) + RADIUS_KUGEL * 0.4;
            nK = new Kugel(this, random.nextDouble() * (canvas.getWidth() - radiusKugeln * 2) + radiusKugeln, random.nextDouble() * (canvas.getHeight() - radiusKugeln * 2) + radiusKugeln, radiusKugeln, MASSE_KUGEL);
            nK.setColor(new Color(1, 1, 1, 0.5));
            neueKugeln.add(nK);
            int gx = (int) Math.max(0, Math.min(nK.x / gridScale[0], GRID_SIZE - 1));
            int gy = (int) Math.max(0, Math.min(nK.y / gridScale[1], GRID_SIZE - 1));
            nK.setGrid(gx, gy);
            grid[gx][gy].add(nK);
        }
    }

    public void update(double delta) {

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j].clear();
            }
        }

        kugeln.forEach(kugel -> {
			int gx = (int) Math.max(0, Math.min(kugel.x / 100, GRID_SIZE - 1));
			int gy = (int) Math.max(0, Math.min(kugel.y / 100, GRID_SIZE - 1));
			grid[gx][gy].add(kugel);
			kugel.setGrid(gx, gy);
        });

        kugeln.parallelStream().forEach(k1 -> {
        	int[] g = k1.getGrid();
			for (int dx = g[0]==0?0:-1; dx <= (g[0]==(GRID_SIZE-1)?0:1); dx++) {
				for (int dy = g[1]==0?0:-1; dy <= (g[1]==(GRID_SIZE-1)?0:1); dy++) {
					for (Kugel k2 : grid[g[0] + dx][g[1] + dy]) {
						if (!k1.equals(k2)) collisionKK(k1, k2);
					}
				}
			}
        });

        kugeln.parallelStream().forEach(kugel -> {
            if (!kugel.update(delta)) {
                veralteteKugeln.add(kugel);
            }
        });

        kugeln.addAll(neueKugeln);
        neueKugeln.clear();
        kugeln.removeAll(veralteteKugeln);
        veralteteKugeln.clear();
    }

    public void render() {
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Kugel e : kugeln) {
            e.render(g);
        }
    }

    public double getWidth() {
        return canvas.getWidth();
    }

    public double getHeight() {
        return canvas.getHeight();
    }

    public static Random getRandom() {
        return random;
    }

    public boolean collisionKK(Kugel k1, Kugel k2) {

        if (k1.kollisionKugelKugel(k2)) {
            if (k1.infected && !k2.infected) {
                //new Color(1, 1, 1, 0.5);
                //k2.setColor(Color.WHITE);
            }
            double length = Math.sqrt((k2.x - k1.x) * (k2.x - k1.x) + (k2.y - k1.y) * (k2.y - k1.y));
            double normalx = (k2.x - k1.x) / length;
            double normaly = (k2.y - k1.y) / length;

            double overlapplength = k2.radius + k1.radius - length;
            double masses = k1.getMass() + k2.getMass();
            k1.addMovement(-normalx * overlapplength * k2.getMass() / masses, -normaly * overlapplength * k2.getMass() / masses);

            double f = (-(1 + k1.epsilon) * ((k2.getSpeedX() - k1.getSpeedX()) * normalx + (k2.getSpeedY() - k1.getSpeedY()) * normaly)) / (1 / k1.getMass() + 1 / k2.getMass());

            k1.addForce(-f / k1.getMass() * normalx, -f / k1.getMass() * normaly);

            return true;
        }
        return false;
    }
}
