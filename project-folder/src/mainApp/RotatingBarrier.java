package mainApp;

import java.awt.Color;

/**
 * Class: RotatingBarrier 
 * Purpose: implements the rotating barrier object
 */
public class RotatingBarrier extends Barrier {

	public static final Color ROTATING_BARRIER_COLOR = Color.ORANGE;
	private static final double DEGREE_INCREMENT = 1;
	private double radius;
	private double dX;
	private double dY;
	private double degree;

	/**
	 * ensures: constructs the rotating barrier
	 * 
	 * @param gameComponent
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public RotatingBarrier(GameComponent gameComponent, double x1, double y1, double x2, double y2) {
		super(gameComponent, x1, y1, x2, y2, ROTATING_BARRIER_COLOR);
		this.dX = x2 - x1;
		this.dY = y2 - y1;
		this.radius = Math.sqrt(dX * dX + dY * dY);
		this.degree = 0;
	}

	/**
	 * ensures: updates the position of the barrier using angles
	 */
	public void update() {
		if (this.degree < 360) {
			this.x2 = this.getX() + this.dX * Math.cos(Math.toRadians(this.degree));
			this.y2 = this.getY() + this.dY * Math.sin(Math.toRadians(this.degree));
			this.degree = this.degree + DEGREE_INCREMENT;
		} else {
			this.degree = 0;
		}
	}
}
