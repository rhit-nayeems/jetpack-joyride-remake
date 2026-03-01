package mainApp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Hero extends GameObject {

	private static final double HERO_WIDTH = 30;
	private static final double HERO_HEIGHT = 40;
	private static final Color HERO_COLOR = Color.BLACK;

	private int numLives;
	private int numCoins;
	private BufferedImage barryImage;
	private double bounceTravel;
	private boolean restartQueued;
	private int animationTick;

	public Hero(GameComponent gameComponent, double xVel, double yVel) {
		super(gameComponent, 0, 410, xVel, yVel, HERO_WIDTH, HERO_HEIGHT, HERO_COLOR);
		this.bounceTravel = 0;
		this.restartQueued = false;
		this.animationTick = 0;
		resetState(gameComponent.getDifficulty().getStartingLives());
		try (InputStream imageStream = ResourceLocator.openStream("barry.png")) {
			barryImage = ImageIO.read(imageStream);
			if (barryImage == null) {
				System.err.println("Unable to load barry.png from classpath or working directory.");
			}
		} catch (IOException e) {
			System.err.println("Unable to load barry.png from classpath or working directory.");
		}
	}

	public void addCoin() {
		numCoins++;
		gameComponent.getSoundManager().playCoin();
	}

	public void die() {
		if (restartQueued || !gameComponent.isRunning()) {
			return;
		}

		numLives--;
		System.out.println("Lost Life! " + numLives + " lives remaining.");

		if (numLives <= 0) {
			numLives = 0;
			restartQueued = false;
			gameComponent.setGameOver(true);
		} else {
			this.bounceTravel = 0;
			this.xVelocity = 1;
			this.yVelocity = 1;
			this.numCoins = 0;
			restartQueued = true;
			gameComponent.getSoundManager().playHit();
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
		double bobOffset = Math.sin(animationTick * 0.2) * 2;
		double tilt = Math.max(-0.35, Math.min(0.35, -this.yVelocity * 0.12));
		AffineTransform originalTransform = g2.getTransform();
		g2.translate(getX() + getWidth() / 2.0, getY() + getHeight() / 2.0 + bobOffset);
		g2.rotate(tilt);

		if (barryImage != null) {
			g2.drawImage(barryImage, -(int) (getWidth() / 2.0), -(int) (getHeight() / 2.0), (int) getWidth(),
					(int) getHeight(), null);
		} else {
			g2.setColor(HERO_COLOR);
			g2.fillRect(-(int) (getWidth() / 2.0), -(int) (getHeight() / 2.0), (int) getWidth(), (int) getHeight());
		}

		if (this.yVelocity < 0) {
			int flamePulse = 6 + (int) (Math.abs(Math.sin(animationTick * 0.45)) * 8);
			int[] xPoints = new int[] { -(int) (getWidth() / 2.0) - 2, -(int) (getWidth() / 2.0) - flamePulse,
					-(int) (getWidth() / 2.0) - 2 };
			int[] yPoints = new int[] { -6, 0, 6 };
			Polygon flame = new Polygon(xPoints, yPoints, 3);
			g2.setColor(new Color(255, 140, 0));
			g2.fillPolygon(flame);
			g2.setColor(new Color(255, 220, 80));
			g2.drawPolygon(flame);
		}

		g2.setTransform(originalTransform);
	}

	@Override
	public void update() {
		animationTick++;
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

		for (Coin coin : this.gameComponent.getCoins()) {
			if (this.overlaps(coin)) {
				this.addCoin();
				gameComponent.addRemoveCoin(coin);
			}
		}

		for (Missile missile : this.gameComponent.getMissiles()) {
			if (this.overlaps(missile)) {
				die();
				break;
			}
		}

		if (restartQueued) {
			consumeQueuedRestart();
			return;
		}

		if (this.gameComponent.getWidth() > 0 && this.getX() + this.getWidth() >= gameComponent.getWidth() - 1) {
			gameComponent.advanceToNextLevel();
			return;
		}

		for (Barrier barrier : this.gameComponent.getBarriers()) {
			if (this.getBoundingBox().intersectsLine(barrier.getLineObj())) {
				if (barrier.getColor().equals(Color.ORANGE) || barrier.getColor().equals(Color.RED)) {
					die();
					break;
				} else {
					this.bounceBack();
				}
			}
			if (this.overlaps(barrier) && barrier.getColor().equals(Color.RED)) {
				die();
				break;
			}
		}

		if (restartQueued) {
			consumeQueuedRestart();
		}
	}

	private void consumeQueuedRestart() {
		restartQueued = false;
		gameComponent.restartCurrentLevelAfterDeath();
	}

	public int getNumLives() {
		return numLives;
	}

	public int getNumCoins() {
		return numCoins;
	}

	public void resetState(int lives) {
		this.numLives = Math.max(1, lives);
		this.numCoins = 0;
		this.setX(0);
		this.setY(410);
		this.xVelocity = 1;
		this.yVelocity = 1;
		this.bounceTravel = 0;
		this.restartQueued = false;
		this.animationTick = 0;
	}

	public void resetState() {
		resetState(6);
	}
}


