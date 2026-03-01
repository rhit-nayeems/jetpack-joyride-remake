package mainApp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 * Class: Coin
 * Purpose: implements the coin object
 */
public class Coin extends GameObject {

	public static final Color COIN_COLOR = Color.YELLOW;
	public static final double COIN_DIAMETER = 10;

	/**
	 * ensures: constructs the coin object
	 * @param gameComponent
	 * @param x
	 * @param y
	 */
	public Coin(GameComponent gameComponent, double x, double y) {
		super(gameComponent, x, y, COIN_DIAMETER, COIN_DIAMETER, COIN_COLOR);
	}

	/**
	 * ensures: draws the coin on the screen
	 */
	@Override
	public void drawOn(Graphics2D g2) {
		g2.setColor(Color.YELLOW);
		Ellipse2D.Double coin = new Ellipse2D.Double(this.getBoundingBox().x, this.getBoundingBox().y,
				this.getBoundingBox().getWidth(), this.getBoundingBox().getHeight());
		g2.setColor(this.getColor());
		g2.fill(coin);
		g2.setColor(Color.BLACK);
	}
}
