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
	private double degree;
	private double initialAngleRadians;

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
		double dX = x2 - x1;
		double dY = y2 - y1;
		this.radius = Math.sqrt(dX * dX + dY * dY);
		this.initialAngleRadians = Math.atan2(dY, dX);
		this.degree = 0;
	}

	/**
	 * ensures: updates endpoint position on a circular trajectory around (x1, y1)
	 */
	public void update() {
		double angleRadians = this.initialAngleRadians + Math.toRadians(this.degree);
		this.x2 = this.getX() + this.radius * Math.cos(angleRadians);
		this.y2 = this.getY() + this.radius * Math.sin(angleRadians);
		this.degree = (this.degree + DEGREE_INCREMENT) % 360;
	}
}
