package mainApp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * Class: Barrier
 * Purpose: implements the barrier object
 */
public class Barrier extends GameObject {
	
	private double rotation;
	private Color color;
	private Line2D.Double lineObj;
	protected double x2;
	protected double y2;
	private static final Color BARRIER_COLOR = Color.BLUE;
	private static final int BARRIER_WIDTH = 15;
	private static final double BARRIER_HEIGHT = 120;

	/**
	 * ensures: constructs a normal barrier
	 * @param gameComponent
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public Barrier(GameComponent gameComponent, double x1, double y1, double x2, double y2) {
		super(gameComponent, x1, y1, BARRIER_WIDTH, BARRIER_HEIGHT, BARRIER_COLOR);
		this.x2 = x2;
		this.y2 = y2;
		this.lineObj = new Line2D.Double(x1, y1, x2, y2);
	}

	/**
	 * ensures: constructs a barrier a different color (electric barrier, rotating barrier)
	 * @param gameComponent
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param color
	 */
	public Barrier(GameComponent gameComponent, double x1, double y1, double x2, double y2, Color color) {
		super(gameComponent, x1, y1, BARRIER_WIDTH, BARRIER_HEIGHT, color);
		this.x2 = x2;
		this.y2 = y2;
		this.lineObj = new Line2D.Double(x1, y1, x2, y2);
	}

	/**
	 * ensures: constructs the laser sub class object
	 * @param gameComponent
	 * @param y
	 * @param color
	 */
	public Barrier (GameComponent gameComponent, double y, Color color) {
		super(gameComponent, 0, y, 0, 0, color); //make new game object with height y, width, height, x of zero
		this.x2 = x2;
		this.y2 = y2;
		this.lineObj = new Line2D.Double(-500, -500, -600, -600);
	}
	/**
	 * ensures: draws the barrier on the screen
	 */
	@Override
	public void drawOn(Graphics2D g2) {
		this.lineObj = new Line2D.Double(this.getX(), this.getY(), this.x2, this.y2);
        g2.setColor(this.getColor());
        g2.setStroke(new BasicStroke(BARRIER_WIDTH));
        g2.draw(this.lineObj);       
    }
	
	/**
	 * @return Line2D.Double of barrier
	 */
	public Line2D.Double getLineObj() {
		return this.lineObj;
	}
}