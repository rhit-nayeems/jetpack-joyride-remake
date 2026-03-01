package mainApp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Class: Laser Purpose: Used to control lasers in the game
 */
public class Laser extends Barrier {

	private static final Color LASER_COLOR = Color.RED;
	private static final double LASER_WIDTH = 20;
	private static final double LASER_HEIGHT = 5;
	private static final double LASER_DELAY = 120;
	private static final double LASER_TIME = 100;

	private double initialY;
	private double delay;
	private double counter;
	private double laserCounter;

	/**
	 * ensures: constructs Laser object
	 * 
	 * @param gameComponent
	 * @param delay
	 * @param y
	 */
	public Laser(GameComponent gameComponent, double delay, double y) {
		super(gameComponent, y, LASER_COLOR);
		this.initialY = y;
		this.delay = delay;
		this.counter = 0;
		this.laserCounter = 0;
	}

	/**
	 * ensures: laser is drawn onto the screen
	 */
	@Override
	public void drawOn(Graphics2D g2) {
		g2.setColor(LASER_COLOR);
		g2.fillRect((int) this.getX(), (int) this.getY() - (int) this.getHeight() / 2, (int) this.getWidth(),
				(int) this.getHeight());
	}

	/**
	 * ensures: updates the laser's current condition (charging, active, or
	 * inactive)
	 */
	@Override
	public void update() {
		counter++;
		if (counter > LASER_DELAY + delay && laserCounter < LASER_TIME) {// full lazer
			this.setHeight(20);
			laserCounter++;
		} else if (counter > delay && counter < LASER_DELAY + delay) {// show lazer powering up
			if (counter % 10 == 0) {
				this.setHeight(10);
				this.setWidth(1000);
			} else {
				this.setHeight(3);
				this.setWidth(1000);
			}
		} else if (counter > LASER_DELAY + delay + LASER_TIME) {// no lazer
			this.setWidth(0);
			this.setHeight(0);
		}
	}

	/**
	 * returns: the bounding box of the laser if active, or an empty bounding box if
	 * inactive
	 */
	@Override
	public Rectangle2D.Double getBoundingBox() {
		if (counter > LASER_DELAY + delay && laserCounter < LASER_TIME) { // checks if laser is active
			return new Rectangle2D.Double(this.getX(), this.getY() - this.getHeight() / 2, getWidth(), getHeight());
		} else {
			return new Rectangle2D.Double(0, 0, 0, 0);
		}
	}
}