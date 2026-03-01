package mainApp;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;

public class Hero extends GameObject {

	// TODO: Implement horizontal and vertical movement

	private static final double HERO_WIDTH = 30;
	private static final double HERO_HEIGHT = 40;
	private static final Color HERO_COLOR = Color.BLACK;
	private int numLives;
	private int numCoins;
	private Robot robot;
	private Toolkit toolkit;
	private BufferedImage barryImage;
	private double bounceTravel;
	private long lastDeathTime = 0;
	private static final long DEATH_COOLDOWN = 1000;
	

	public Hero(GameComponent gameComponent, double xVel, double yVel) {
		super(gameComponent, 0, 410, xVel, yVel, HERO_WIDTH, HERO_HEIGHT, HERO_COLOR);
		numLives = 6;
		numCoins = 0;
		this.bounceTravel = 0;
		try {
			robot = new Robot();
			toolkit = Toolkit.getDefaultToolkit();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			barryImage = ImageIO.read(new File("barry.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void addCoin() {
		numCoins++;
	}

	public void die() {
		// fixes losing two lives at once by cooling down
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastDeathTime < DEATH_COOLDOWN) {
			return;
		}
		lastDeathTime = currentTime;
		numLives--;

		System.out.println("Lost Life! " + numLives + " lives remaining.");
		if (numLives <= 0) {
			numLives = 0;
			gameComponent.setGameOver(true);
		} else {
			this.bounceTravel = 0;
			this.xVelocity = 1;
			this.numCoins = 0;
			// trigger the reset key
			robot.keyPress(KeyEvent.VK_R);
			robot.delay(100);
			robot.keyRelease(KeyEvent.VK_R);
			// System.out.println("Robot pressed key");
		}
	}

	public void flyUp() {
		this.yVelocity = -2;
	}

	public void stopFlying() {
		this.yVelocity = 1;
	}

	public void bounceBack() {
		if (this.bounceTravel < 50) {
			this.xVelocity = -1;
		}
	}

	@Override
	public void drawOn(Graphics2D g2) {
		if (barryImage != null) {
			g2.drawImage(barryImage, (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight(), null);
		} else {
			g2.setColor(HERO_COLOR);
			g2.fill(this.getBoundingBox());
		}
	}

	@Override
	public void update() {
		super.update();
		if (this.bounceTravel >= 50) {
			this.xVelocity = 1;
			this.bounceTravel = 0;
		}
		if (this.xVelocity == -1) {
			this.bounceTravel++;
		}

		if (atXLimits() && !atYLimits()) {
			this.reverseXDirection();
			super.update();
			this.reverseXDirection();
		}
		if (atYLimits() && !atXLimits()) {
			this.reverseYDirection();
			super.update();
			this.reverseYDirection();
		}
		if (atYLimits() && atXLimits()) {
			this.reverseDirection();
			super.update();
			this.reverseDirection();
		}

		for (Coin c : this.gameComponent.getCoins()) {
			if (this.overlaps(c)) {
				this.addCoin();
				gameComponent.addRemoveCoin(c);
				// System.out.println("Coins Collected: " + numCoins);

			}
		}

		for (Missile m : this.gameComponent.getMissiles()) {
			if (this.overlaps(m)) {
				die();
			}
		}

		// System.out.println("Block is at " + this.getX() + this.getWidth() + "Which is
		// greater than " + gameComponent.getWidth());
		if (this.getX() + this.getWidth() >= gameComponent.getWidth() - 1) {
			// System.out.println("Reached end");
			robot.keyPress(KeyEvent.VK_U);
			robot.delay(100);
			robot.keyRelease(KeyEvent.VK_U);
		}

		for (Barrier b : this.gameComponent.getBarriers()) {
			if (this.getBoundingBox().intersectsLine(b.getLineObj())) {
				if (b.getColor().equals(Color.ORANGE) || b.getColor().equals(Color.RED)) {
					die();
//				} else if (atYLimits()) {
//					this.reverseDirection();
//					super.update();
//					this.reverseDirection();
				} else {
					this.bounceBack();
				}
			}
			// kill condition for lasers
			if (this.overlaps(b) && b.getColor().equals(Color.RED)) {
				die();
			}
		}
	}

	public int getNumLives() {
		return numLives;
	}

	public int getNumCoins() {
		return numCoins;
	}

	public void resetState() {
		this.numLives = 6; 
		this.numCoins = 0; 
		this.setX(0); 
		this.setY(410);
	}
	
}
