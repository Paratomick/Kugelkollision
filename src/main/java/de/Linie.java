package de;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Linie {

	private double xStart;
	private double yStart;
	private double xEnd;
	private double yEnd;
	
	private double mass;
	private double lWidth;
	private boolean akt1, akt2;
	private double angel;

	public void init() {
		this.angel = (double) Math.atan(Math.abs(yStart - yEnd) / Math.abs(xStart - xEnd));
	}
	
	public Linie(double xStart, double yStart, double xEnd, double yEnd) {
		this.xStart = xStart;
		this.yStart = yStart;
		this.xEnd = xEnd;
		this.yEnd = yEnd;
		
		if(lWidth == 0) lWidth = 1;

		akt1 = false;
		akt2 = false;
	}
	
	public Linie(double xStart, double yStart, double xEnd, double yEnd, double mass, double width) {
		this(xStart, yStart, xEnd, yEnd);
		
		this.mass = mass;
		this.lWidth = width;
	}
	
	public Linie(double mass, double width) {
		xStart = 0;
		yStart = 0;
		xEnd = 0;
		yEnd = 0;
		
		this.mass = mass;
		this.lWidth = width;
		
		akt1 = true;
		akt2 = true;
	}
	
	public boolean update(double delta) {
		Input input = Input.getInput();
		
		if(input.isKeyDown(Input.KEY_C)) {
			return false;
		}
		
		if(akt1) {
			xStart = input.getMouseX();
			yStart = input.getMouseY();
		}
		
		if(akt2) {
			xEnd = input.getMouseX();
			yEnd = input.getMouseY();
		}
		
		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			akt1 = false;
		}
		
		if(input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
			akt2 = false;
		}
			
		return true;
	}
	
	public void render(GraphicsContext g) {
		g.setStroke(Color.RED);
		g.setLineWidth(lWidth);
		g.strokeLine(xStart, yStart, xEnd, yEnd);
	}

	public double xStart() {
		return xStart;
	}

	public double yStart() {
		return yStart;
	}

	public double xEnd() {
		return xEnd;
	}

	public double yEnd() {
		return yEnd;
	}

	public double getMass() {
		return mass;
	}

	public double getWidth() {
		return lWidth;
	}

	public double getAngel() {
		return angel;
	}
}
