package mainApp;

import java.util.Random;

/**
 * Class: RandomMissile 
 * Purpose: implement the randomly moving missile
 */
public class RandomMissile extends Missile {

	private Random r;
	private int count;
	private int direction;

	/**
	 * ensures: constructs the RandomMissile object and a new random object
	 * 
	 * @param gameComponent
	 * @param x
	 * @param y
	 */
	public RandomMissile(GameComponent gameComponent, double x, double y) {
		super(gameComponent, x, y);
		r = new Random();
		count = 0;
		direction = 1; // positive is up and negative is down
	}

	/**
	 * ensures: updates the missile's position; y position is based on a random
	 * number
	 */
	public void update() {
		if (count > 5) {
			direction = r.nextInt(2) * 2 - 1; // yields either 1 or -1
			count = 0;
		}
		super.update();

		if (direction == 1 && this.getY() < 480) {
			this.setY(this.getY() + r.nextDouble() * 3 * direction);
		} else if (direction == -1 && this.getY() > 20) {
			this.setY(this.getY() + r.nextDouble() * 3 * direction);
		}
		count++;
	}
}
