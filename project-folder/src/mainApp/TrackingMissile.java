package mainApp;

/**
 * Class: TrackingMissile
 * Purpose: implement the tracking missile
 */
public class TrackingMissile extends Missile {

	/**
	 * ensures: constructs the tracking missile using the normal missile constructor
	 *
	 * @param gameComponent
	 * @param x
	 * @param y
	 */
	public TrackingMissile(GameComponent gameComponent, double x, double y) {
		super(gameComponent, x, y);
	}

	/**
	 * ensures: updates the missile's position
	 */
	public void update() {
		double trackSpeed = 0.5 * getDifficultySpeedMultiplier();
		if (this.gameComponent.getHero().getY() < this.getY()) {
			this.yVelocity = -trackSpeed;
		} else if (this.gameComponent.getHero().getY() > this.getY()) {
			this.yVelocity = trackSpeed;
		} else {
			this.yVelocity = 0;
		}
		super.update();
	}
}
