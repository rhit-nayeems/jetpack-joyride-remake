package mainApp;

/**
 * Class: SinMissile
 * Purpose: implements the sin wave missile
 */
public class SinMissile extends Missile {

	private double yPlane;
	private double amplitude;
	private int updateCounter;

	/**
	 * ensures: constructs the sin wave missile
	 *
	 * @param gameComponent
	 * @param x
	 * @param y
	 */
	public SinMissile(GameComponent gameComponent, double x, double y) {
		super(gameComponent, x, y);
		yPlane = this.getY();
		this.amplitude = 100;
		this.updateCounter = 0;
	}

	/**
	 * ensures: updates the missile's position based on a sin wave function
	 */
	public void update() {
		double freq = 0.05 * getDifficultySpeedMultiplier();
		this.setY(yPlane + amplitude * Math.sin(updateCounter * freq));
		super.update();
		this.updateCounter++;
	}
}
