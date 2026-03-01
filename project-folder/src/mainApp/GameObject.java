package mainApp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Class: GameObject
 * Purpose: superclass of all objects in the game: missiles, barriers, coins, hero
 */
public abstract class GameObject {
	
	private double x, y;
	private double width;
	private double height;
	protected GameComponent gameComponent;
	protected double xVelocity;
	protected double yVelocity;
	private Color color;

	/**
	 * ensures: constructs objects that move: missiles, hero, moving barrier
	 * 
	 * @param gameComponent
	 * @param x
	 * @param y
	 * @param xVel
	 * @param yVel
	 * @param width
	 * @param height
	 * @param color
	 */
	public GameObject(GameComponent gameComponent, double x, double y, double xVel, double yVel, double width,
			double height, Color color) {
		this.x = x;
		this.y = y;
		this.gameComponent = gameComponent;
		this.width = width;
		this.height = height;
		this.xVelocity = xVel;
		this.yVelocity = yVel;
		this.color = color;

	}

	/**
	 * ensures: constructs objects that don't move: barriers, coins
	 * 
	 * @param gameComponent
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 */
	public GameObject(GameComponent gameComponent, double x, double y, double width, double height, Color color) {
		this.x = x;
		this.y = y;
		this.gameComponent = gameComponent;
		this.width = width;
		this.height = height;
		this.xVelocity = 0;
		this.yVelocity = 0;
		this.color = color;
	}

	/**
	 * ensures: each subclass must have a drawOn method
	 * @param g2
	 */
	public abstract void drawOn(Graphics2D g2);

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getWidth() {
		return this.width;
	}

	public double getHeight() {
		return this.height;
	}

	public Color getColor() {
		return this.color;
	}

	/**
	 * ensures: updates the x and y positions based on velocity
	 */
	public void update() {
		this.x += this.xVelocity;
		this.y += this.yVelocity;
	}

	/**
	 * ensures: reverses the x and y direction of an object
	 */
	public void reverseDirection() {
		this.xVelocity = -this.xVelocity;
		this.yVelocity = -this.yVelocity;
	}

	/**
	 * ensures: reverses just the x direction of an object
	 */
	public void reverseXDirection() {
		this.xVelocity = -this.xVelocity;
	}

	/**
	 * ensures: reverses just the y direction of an object
	 */
	public void reverseYDirection() {
		this.yVelocity = -this.yVelocity;
	}

	/**
	 * @return hit box for collision purposes
	 */
	public Rectangle2D.Double getBoundingBox() {
		return new Rectangle2D.Double(this.x, this.y, getWidth(), getHeight());
	}

	/**
	 * ensures: determines if this object is overlapping with another object
	 * @param other
	 * @return boolean overlaps
	 */
	public boolean overlaps(GameObject other) {
		return getBoundingBox().intersects(other.getBoundingBox());
	}

	/**
	 * ensures: determines is object is on or off screen
	 * @return boolean on or off screen
	 */
	public boolean isOffScreen() {
		boolean xLow = x < 0;
		boolean xHigh = x + width > gameComponent.getWidth();
		boolean yLow = y < 0;
		boolean yHigh = y + height > gameComponent.getHeight();
		return xLow || xHigh || yLow || yHigh;
	}

	/**
	 * ensures: if object is at the x limit
	 * @return true or false
	 */
	public boolean atXLimits() {
		boolean xLow = x < 0;
		boolean xHigh = x + width >= gameComponent.getWidth();
		return xLow || xHigh;
	}
	/**
	 * ensures: if object is at the y limit
	 * @return true or false
	 */
	public boolean atYLimits() {
		boolean yLow = y < 0;
		boolean yHigh = y + height > gameComponent.getHeight();
		return yLow || yHigh;
	}
	public void setHeight(double height) {
		this.height = height;
	}

	public void setWidth(double width) {
		this.width = width;
	}
}
