package mainApp;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * Class: GameComponent
 * Purpose: holds active game state and draws/updates the game
 */
public class GameComponent extends JComponent {

	private enum GameState {
		RUNNING, PAUSED, GAME_OVER, GAME_WON
	}

	private Hero hero;
	private int numTicks;
	private BufferedImage backGroundImage;
	private JButton restartButton;
	private int currentLevel = LevelConstructor.MIN_LEVEL;
	private GameState gameState = GameState.RUNNING;
	private LevelConstructor levelConstructor;
	private ScoreManager scoreManager;
	private SoundManager soundManager;
	private DifficultyPreset difficulty;
	private boolean proceduralEnabled;

	private ArrayList<String> barriers = new ArrayList<>();
	private ArrayList<String> coins = new ArrayList<>();
	private ArrayList<String> missiles = new ArrayList<>();

	private ArrayList<Barrier> objBarriers = new ArrayList<>();
	private ArrayList<Coin> objCoins = new ArrayList<>();
	private ArrayList<Missile> objMissiles = new ArrayList<>();
	private ArrayList<Coin> coinsToRemove = new ArrayList<>();

	private static final double BACKGROUND_SCROLL_SPEED = 0.6;
	private static final int BACKGROUND_SWAY_PIXELS = 6;
	private double backgroundScrollX;

	/**
	 * ensures: constructs the GameComponent object
	 */
	public GameComponent() {
		this.difficulty = DifficultyPreset.NORMAL;
		this.proceduralEnabled = true;
		this.hero = new Hero(this, 1, 1);
		this.levelConstructor = new LevelConstructor(this);
		this.scoreManager = new ScoreManager();
		this.soundManager = new SoundManager();
		setupRestartButton();
		loadBackgroundImage();
	}

	private void loadBackgroundImage() {
		try {
			backGroundImage = ImageIO.read(new File("backgroundCropped.png"));
			if (backGroundImage == null) {
				System.out.println("Image not found!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ensures: starts a fresh run at level 1
	 */
	public void startNewGame() {
		this.numTicks = 0;
		this.backgroundScrollX = 0;
		this.coinsToRemove.clear();
		this.hero.resetState(this.difficulty.getStartingLives());
		this.gameState = GameState.RUNNING;
		this.restartButton.setVisible(false);
		this.levelConstructor.setLevelNum(LevelConstructor.MIN_LEVEL);
		this.levelConstructor.reloadCurrentLevel();
		repaint();
	}

	/**
	 * ensures: takes information from barrier string list and creates new barriers
	 * in the objBarriers list.
	 */
	public void addBarriers() {
		for (String barrierLine : barriers) {
			String[] parts = barrierLine.split("\\s+");
			if (parts.length != 6) {
				continue;
			}
			String barrierType = parts[1];
			double x1 = Double.parseDouble(parts[2]);
			double y1 = Double.parseDouble(parts[3]);
			double x2 = Double.parseDouble(parts[4]);
			double y2 = Double.parseDouble(parts[5]);

			switch (barrierType) {
			case "e":
				this.objBarriers.add(new ElectricBarrier(this, x1, y1, x2, y2));
				break;
			case "r":
				this.objBarriers.add(new RotatingBarrier(this, x1, y1, x2, y2));
				break;
			case "laser":
				this.objBarriers.add(new Laser(this, x1, y1));
				break;
			default:
				this.objBarriers.add(new Barrier(this, x1, y1, x2, y2));
				break;
			}
		}
	}

	/**
	 * ensures: takes information from coin string list and creates new coins in the
	 * objCoins list.
	 */
	public void addCoins() {
		for (String coinLine : coins) {
			String[] parts = coinLine.split("\\s+");
			if (parts.length != 3) {
				continue;
			}
			int xLoc = Integer.parseInt(parts[1]);
			int yLoc = Integer.parseInt(parts[2]);
			this.objCoins.add(new Coin(this, xLoc, yLoc));
		}
	}

	/**
	 * ensures: takes information from missile string list and creates new missiles
	 * in the objMissiles list.
	 */
	public void addMissiles() {
		for (String missileLine : missiles) {
			String[] parts = missileLine.split("\\s+");
			if (parts.length != 4) {
				continue;
			}
			String missileType = parts[1];
			int delay = Integer.parseInt(parts[2]);
			int yLoc = Integer.parseInt(parts[3]);

			Missile missile;
			switch (missileType) {
			case "tracking":
				missile = new TrackingMissile(this, delay, yLoc);
				break;
			case "sin":
				missile = new SinMissile(this, delay, yLoc);
				break;
			case "random":
				missile = new RandomMissile(this, delay, yLoc);
				break;
			default:
				missile = new Missile(this, delay, yLoc);
				break;
			}
			missile.applyDifficulty(this.difficulty);
			this.objMissiles.add(missile);
		}
	}

	/**
	 * ensures: repaints the screen
	 */
	public void drawScreen() {
		this.repaint();
	}

	private void drawAnimatedBackground(Graphics2D g2) {
		if (backGroundImage == null) {
			return;
		}

		int panelWidth = Math.max(1, this.getWidth());
		int panelHeight = Math.max(1, this.getHeight());
		int scrollPixels = (int) Math.round(this.backgroundScrollX) % panelWidth;
		int baseX = -scrollPixels;
		int sway = (int) Math.round(Math.sin(this.numTicks * 0.02) * BACKGROUND_SWAY_PIXELS);
		int drawY = -BACKGROUND_SWAY_PIXELS + sway;
		int drawHeight = panelHeight + BACKGROUND_SWAY_PIXELS * 2;

		for (int tile = -1; tile <= 1; tile++) {
			int x = baseX + tile * panelWidth;
			g2.drawImage(backGroundImage, x, drawY, panelWidth, drawHeight, this);
		}
	}

	/**
	 * ensures: paints all game objects and overlays
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		drawAnimatedBackground(g2);

		ArrayList<GameObject> gameObjects = new ArrayList<>();
		gameObjects.add(hero);
		gameObjects.addAll(objBarriers);
		gameObjects.addAll(objCoins);
		gameObjects.addAll(objMissiles);
		for (GameObject gameObject : gameObjects) {
			gameObject.drawOn(g2);
		}

		drawHud(g2);

		if (this.gameState == GameState.PAUSED) {
			drawCenteredMessage(g2, "PAUSED", Color.YELLOW);
		} else if (this.gameState == GameState.GAME_WON) {
			drawCenteredMessage(g2, "YOU WON!", Color.GREEN);
		} else if (this.gameState == GameState.GAME_OVER) {
			drawCenteredMessage(g2, "GAME OVER", Color.RED);
		}

		if (this.restartButton.isVisible()) {
			positionRestartButton();
		}
	}

	private void drawHud(Graphics2D g2) {
		g2.setFont(new Font("Arial", Font.BOLD, 15));
		g2.setColor(Color.WHITE);
		g2.drawString("Lives: " + hero.getNumLives(), 10, 20);
		g2.drawString("Coins: " + hero.getNumCoins(), 10, 40);
		g2.drawString("Level: " + currentLevel, 10, 60);
		g2.drawString("Score: " + getCurrentScore(), 10, 80);
		g2.drawString("Best: " + scoreManager.getBestScore(), 10, 100);
		g2.drawString("Difficulty: " + this.difficulty, 10, 120);
		g2.drawString("Mode: " + (currentLevel <= LevelConstructor.CAMPAIGN_MAX_LEVEL ? "Campaign" : "Procedural"), 10,
				140);
		g2.drawString("Controls: UP fly, R restart, P pause, 1/2/3 difficulty", 10, this.getHeight() - 15);
	}

	private void drawCenteredMessage(Graphics2D g2, String text, Color color) {
		Font overlayFont = new Font("Arial", Font.BOLD, 36);
		g2.setFont(overlayFont);
		FontMetrics metrics = g2.getFontMetrics(overlayFont);
		g2.setColor(color);
		int textX = Math.max(0, (this.getWidth() - metrics.stringWidth(text)) / 2);
		int textY = Math.max(40, this.getHeight() / 2 - 25);
		g2.drawString(text, textX, textY);
	}

	private void updateBackgroundAnimation() {
		int panelWidth = Math.max(1, this.getWidth());
		this.backgroundScrollX += BACKGROUND_SCROLL_SPEED;
		if (this.backgroundScrollX >= panelWidth) {
			this.backgroundScrollX -= panelWidth;
		}
	}

	/**
	 * ensures: updates game state for one tick
	 */
	public void updateState() {
		if (this.gameState != GameState.RUNNING) {
			return;
		}

		updateBackgroundAnimation();

		for (Missile missile : objMissiles) {
			missile.update();
		}
		for (Barrier barrier : objBarriers) {
			barrier.update();
		}
		for (Coin coin : objCoins) {
			coin.update();
		}

		if (!coinsToRemove.isEmpty()) {
			this.objCoins.removeAll(coinsToRemove);
			coinsToRemove.clear();
		}

		this.hero.update();
		this.numTicks++;
	}

	public Hero getHero() {
		return this.hero;
	}

	/**
	 * ensures: updates level content and resets hero position
	 */
	public void changeLevel(LevelData levelData) {
		this.barriers = new ArrayList<>(levelData.getBarriers());
		this.coins = new ArrayList<>(levelData.getCoins());
		this.missiles = new ArrayList<>(levelData.getMissiles());

		this.objBarriers.clear();
		this.objCoins.clear();
		this.objMissiles.clear();
		this.coinsToRemove.clear();

		this.addBarriers();
		this.addCoins();
		this.addMissiles();

		this.hero.setX(0);
		this.hero.setY(410);
		this.hero.stopFlying();
	}

	public ArrayList<Barrier> getBarriers() {
		return this.objBarriers;
	}

	public ArrayList<Coin> getCoins() {
		return this.objCoins;
	}

	public ArrayList<Missile> getMissiles() {
		return this.objMissiles;
	}

	public SoundManager getSoundManager() {
		return this.soundManager;
	}

	public void addRemoveCoin(Coin coin) {
		if (!this.coinsToRemove.contains(coin)) {
			this.coinsToRemove.add(coin);
		}
	}

	public void setGameOver(boolean isGameOver) {
		if (isGameOver) {
			finishGame(GameState.GAME_OVER);
		}
	}

	public void setGameWon(boolean isGameWon) {
		if (isGameWon) {
			finishGame(GameState.GAME_WON);
		}
	}

	private void finishGame(GameState finalState) {
		if (this.gameState == GameState.GAME_OVER || this.gameState == GameState.GAME_WON) {
			return;
		}
		this.gameState = finalState;
		this.scoreManager.registerScore(getCurrentScore());
		if (finalState == GameState.GAME_OVER) {
			this.soundManager.playGameOver();
		}
		positionRestartButton();
		this.restartButton.setVisible(true);
		repaint();
	}

	private void setupRestartButton() {
		restartButton = new JButton("Restart Game");
		restartButton.setFont(new Font("Arial", Font.BOLD, 20));
		restartButton.setBounds(390, 300, 220, 50);
		restartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restartGame();
			}
		});
		restartButton.setVisible(false);
		this.setLayout(null);
		this.add(restartButton);
	}

	private void positionRestartButton() {
		int buttonWidth = 220;
		int buttonHeight = 50;
		int buttonX = Math.max(0, (this.getWidth() - buttonWidth) / 2);
		int buttonY = Math.max(0, this.getHeight() / 2 + 20);
		restartButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
	}

	public void restartGame() {
		startNewGame();
	}

	public void restartCurrentLevelAfterDeath() {
		if (this.gameState != GameState.RUNNING) {
			return;
		}
		this.levelConstructor.reloadCurrentLevel();
	}

	public void advanceToNextLevel() {
		if (this.gameState != GameState.RUNNING) {
			return;
		}
		boolean advanced = this.levelConstructor.advanceLevel();
		if (!advanced) {
			setGameWon(true);
			return;
		}
		this.soundManager.playLevelUp();
	}

	public void changeLevelDebug(int delta) {
		if (this.gameState == GameState.GAME_OVER || this.gameState == GameState.GAME_WON) {
			return;
		}
		this.levelConstructor.changeLevel(delta);
	}

	public void togglePause() {
		if (this.gameState == GameState.RUNNING) {
			this.gameState = GameState.PAUSED;
		} else if (this.gameState == GameState.PAUSED) {
			this.gameState = GameState.RUNNING;
		}
		this.soundManager.playPauseToggle();
		repaint();
	}

	public boolean isRunning() {
		return this.gameState == GameState.RUNNING;
	}

	public int getCurrentScore() {
		return this.scoreManager.calculateScore(this.hero, this.currentLevel, this.numTicks);
	}

	public int getBestScore() {
		return this.scoreManager.getBestScore();
	}

	public DifficultyPreset getDifficulty() {
		return this.difficulty;
	}

	public void setDifficulty(DifficultyPreset difficulty) {
		this.difficulty = difficulty == null ? DifficultyPreset.NORMAL : difficulty;
	}

	public boolean isProceduralEnabled() {
		return this.proceduralEnabled;
	}

	public void setProceduralEnabled(boolean proceduralEnabled) {
		this.proceduralEnabled = proceduralEnabled;
	}

	public void setCurrentLevel(int level) {
		this.currentLevel = level;
		repaint();
	}
}











