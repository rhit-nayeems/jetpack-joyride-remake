package mainApp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Class: Missile Purpose: Generates and controls missile behavior
 */
public class Missile extends GameObject {

	private static final Color MISSILE_COLOR = Color.ORANGE;
	private static final double MISSILE_WIDTH = 20;
	private static final double MISSILE_HEIGHT = 5;
	private static final double MISSILE_INITIAL_X = 965;
	private static final double MISSILE_X_VELOCITY = -3;

	private double initialY;
	private double delay;
	private double counter;

	/**
	 * ensures: constructs missile
	 * 
	 * @param gameComponent
	 * @param delay
	 * @param y
	 */
	public Missile(GameComponent gameComponent, double delay, double y) {
		super(gameComponent, MISSILE_INITIAL_X, y, MISSILE_X_VELOCITY, 0, MISSILE_WIDTH, MISSILE_HEIGHT, MISSILE_COLOR);
		this.initialY = y;
		this.delay = delay;
		this.counter = 0;
	}

	/**
	 * ensures: draws missile on screen
	 */
	@Override
	public void drawOn(Graphics2D g2) {
		Rectangle2D.Double missile = new Rectangle2D.Double(this.getX(), this.getY(), this.getWidth(),
				this.getHeight());
		g2.setColor(MISSILE_COLOR);
		g2.fill(missile);
	}

	/**
	 * ensures: updates missile's position
	 */
	@Override
	public void update() {
		counter++;
		if (counter > delay) {
			super.update();
			if (this.isOffScreen()) {
				flyBack();
			}
		}
	}

	/**
	 * ensures: puts the missile at its spawn when it goes off screen
	 */
	public void flyBack() {
		this.setX(MISSILE_INITIAL_X);
		this.setY(this.initialY);
	}
}
