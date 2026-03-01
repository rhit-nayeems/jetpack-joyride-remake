package mainApp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class: Coin
 * Purpose: implements the coin object with a pulsing animation
 */
public class Coin extends GameObject {

	public static final Color COIN_COLOR = Color.YELLOW;
	public static final double COIN_DIAMETER = 10;

	private int animationTick;
	private double animationPhase;

	/**
	 * ensures: constructs the coin object
	 *
	 * @param gameComponent
	 * @param x
	 * @param y
	 */
	public Coin(GameComponent gameComponent, double x, double y) {
		super(gameComponent, x, y, COIN_DIAMETER, COIN_DIAMETER, COIN_COLOR);
		this.animationTick = 0;
		this.animationPhase = ThreadLocalRandom.current().nextDouble(0, Math.PI * 2);
	}

	@Override
	public void update() {
		this.animationTick++;
	}

	/**
	 * ensures: draws the coin on the screen
	 */
	@Override
	public void drawOn(Graphics2D g2) {
		double pulse = 1.0 + 0.2 * Math.sin(animationTick * 0.25 + animationPhase);
		double size = COIN_DIAMETER * pulse;
		double offset = (size - COIN_DIAMETER) / 2.0;
		Ellipse2D.Double coin = new Ellipse2D.Double(this.getX() - offset, this.getY() - offset, size, size);

		g2.setColor(this.getColor());
		g2.fill(coin);
		g2.setColor(new Color(255, 200, 40));
		g2.draw(coin);
	}
}
