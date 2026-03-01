package mainApp;

import java.awt.Color;
import java.awt.Font;
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

//import game.GameObject;

public class GameComponent extends JComponent {

	// TODO: Implement GameComponent similar to practice solution inheritance design

	private Hero hero;
	private int numTicks;
	private BufferedImage backGroundImage;
	private JButton restartButton;
	private int currentLevel = 1;
	private boolean isGameOver = false;
	private boolean isGameWon = false;
	private LevelConstructor levelConstructor;


	private ArrayList<String> barriers = new ArrayList<>();
	private ArrayList<String> coins = new ArrayList<>();
	private ArrayList<String> missiles = new ArrayList<>();

	// private ArrayList<GameObject> objects = new ArrayList<>();

	private ArrayList<Barrier> objBarriers = new ArrayList<>();
	private ArrayList<Coin> objCoins = new ArrayList<>();
	private ArrayList<Missile> objMissiles = new ArrayList<>();
	// private Barrier barrier;

	private ArrayList<Coin> coinsToRemove = new ArrayList<>();

	/**
	 * ensures: constructs the GameComponent object
	 */
	public GameComponent() {

		this.hero = new Hero(this, 1, 1);
		this.levelConstructor = new LevelConstructor(this);
		setupRestartButton();

		try {
			backGroundImage = ImageIO.read(new File("backgroundCropped.png"));
			if (backGroundImage == null) {
				System.out.println("Image not found!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// this.barrier = new Barrier(this, 100, 100, 15);
		// addBarriers();
		// System.out.println(barriers);

	}

	/**
	 * ensures: takes information from barrier string list and creates new barriers
	 * in the objBarriers list.
	 */
	public void addBarriers() {
		for (String barrier : barriers) {
			String[] parts = barrier.split(" ");
//            String objectType = parts[0];
			double x1 = Double.parseDouble(parts[2]);
			double y1 = Double.parseDouble(parts[3]);
			double x2 = Double.parseDouble(parts[4]);
			double y2 = Double.parseDouble(parts[5]);
			if (barrier.charAt(8) == 'e') {// check if barrier is electric or normal
				this.objBarriers.add(new ElectricBarrier(this, x1, y1, x2, y2));
			} else if (barrier.charAt(8) == 'r') {// check if barrier is rotating
				this.objBarriers.add(new RotatingBarrier(this, x1, y1, x2, y2));
			} else if (barrier.charAt(8) == 'l') {// check if barrier is a laser
				this.objBarriers.add(new Laser(this, x1, y1));
			} else {
				this.objBarriers.add(new Barrier(this, x1, y1, x2, y2));
			}
		}
	}

	/**
	 * ensures: takes information from coin string list and creates new coins in the
	 * objCoins list.
	 */
	public void addCoins() {
		for (String coin : coins) {
			String[] parts = coin.split(" ");
			String objectType = parts[0];
			int xLoc = Integer.parseInt(parts[1]);
			int yLoc = Integer.parseInt(parts[2]);
			// objects.add(new Coin(objectType, xLoc, yLoc));
			this.objCoins.add(new Coin(this, xLoc, yLoc));

		}
	}

	/**
	 * ensures: takes information from missile string list and creates new missiles
	 * in the objMissiles list.
	 */
	public void addMissiles() {
		for (String missile : missiles) {
			String[] parts = missile.split(" ");
			// String objectType = parts[0];
			int xLoc = Integer.parseInt(parts[2]);
			int yLoc = Integer.parseInt(parts[3]);
			// this.objMissiles.add(new TrackingMissile(this, xLoc, yLoc));
			if (missile.charAt(8) == 't') {// check if missile if tracking
				this.objMissiles.add(new TrackingMissile(this, xLoc, yLoc));
			} else if (missile.charAt(8) == 's') {// check if missile is sine
				this.objMissiles.add(new SinMissile(this, xLoc, yLoc));
			} else if (missile.charAt(8) == 'r') {// check if missile is random
				this.objMissiles.add(new RandomMissile(this, xLoc, yLoc));
			} else {
				this.objMissiles.add(new Missile(this, xLoc, yLoc));
			}

		}
	}

	/**
	 * ensures: repaints the screen
	 */
	public void drawScreen() {
		this.repaint();

	}

	/**
	 * ensures: paints all components (hero, barriers, coins, missiles) onto the
	 * screen.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		if (backGroundImage != null) {
			g.drawImage(backGroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
		}

		if (!isGameOver && !isGameWon) {  // Modified: consolidated game state checks
            ArrayList<GameObject> gameObjects = new ArrayList<>();
            gameObjects.add(hero);
            gameObjects.addAll(objBarriers);
            gameObjects.addAll(objCoins);
            gameObjects.addAll(objMissiles);
            g2.setFont(new Font("Arial", Font.BOLD, 15));
            g2.setColor(Color.WHITE);
            g2.drawString("Lives: " + hero.getNumLives(), 10, 20); // Display number of lives
            g2.drawString("Coins: " + hero.getNumCoins(), 10, 40); // Display number of coins collected
            g2.drawString("Level: " + currentLevel, 10, 60);

            for (GameObject gameObject : gameObjects) {
                gameObject.drawOn(g2);
            }
        } else {
            g2.setFont(new Font("Arial", Font.BOLD, 36));
            if (isGameWon) {  // Modified: using isGameWon for win state
                g2.setColor(Color.GREEN);
                g2.drawString("YOU WON!", this.getWidth() / 2 - 90, this.getHeight() / 2);
            } else {
                g2.setColor(Color.RED);
                g2.drawString("GAME OVER", this.getWidth() / 2 - 100, this.getHeight() / 2);
            }
        }

	}

	/**
	 * ensures: updates: -hero fields -missile fields -game ticks
	 */
	public void updateState() {
		// Each is big enough to be in a helper method.
		if (isGameOver) {
            return;
        }

		for (Missile m : objMissiles) {
			m.update();
		}
		for (Barrier b : objBarriers) {
			b.update();
		}

		for (Coin c : coinsToRemove) {
			this.objCoins.remove(c);
		}
		this.hero.update();

		if (currentLevel == 8 && hero.getX() >= this.getWidth() -1) {
	        setGameWon(true); // Set game won when reaching end of level 8
	        return;
	    }

		this.numTicks++;
	}

	public Hero getHero() {
		return this.hero;
	}

	/**
	 * ensures: changes the level by 1. updating the object string lists 2. updating
	 * the object lists 3. resetting the hero's position
	 * 
	 * Called from LevelConstructor.constructLevel
	 * 
	 * @param barriers
	 * @param coins
	 * @param missiles
	 */
	public void changeLevel(ArrayList<String> barriers, ArrayList<String> coins, ArrayList<String> missiles) {
		this.barriers = barriers;
		this.coins = coins;
		this.missiles = missiles;

		this.objBarriers.clear();
		this.objCoins.clear();
		this.objMissiles.clear();

		this.addBarriers();
		this.addCoins();
		this.addMissiles();

		this.hero.setX(0);
		this.hero.setY(410);
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

	public void addRemoveCoin(Coin c) {
		this.coinsToRemove.add(c);
	}

	public void setGameOver(boolean isGameOver) {
		this.isGameOver = isGameOver;
        if (isGameOver && !isGameWon) {  // Modified: ensure restartButton visibility is based on game states
            restartButton.setVisible(true);
        } else {
            restartButton.setVisible(false);
        }
        repaint();
	}

	public void setGameWon(boolean isGameWon) {
		this.isGameWon = isGameWon;
        this.isGameOver = true;  // Ensure isGameOver is set to true when the game is won
        restartButton.setVisible(false); // Ensure restart button is hidden when the game is won
        repaint();
	}

	private void setupRestartButton() {
		restartButton = new JButton("Restart Game");
		restartButton.setFont(new Font("Arial", Font.BOLD, 20));
		restartButton.setBounds(400, 300, 200, 50); 
		restartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restartGame();
			}
		});
		restartButton.setVisible(false); 
		this.add(restartButton);
		this.setLayout(null);
	}

	private void restartGame() {
		isGameOver = false;

		 isGameWon = false;
		restartButton.setVisible(false);
		hero.resetState();
		restartLevel();
		repaint();

	}

	public void setCurrentLevel(int level) {
		this.currentLevel = level;
		repaint();
	}

	private void restartLevel() {
		currentLevel = 1; 
		LevelConstructor lc = new LevelConstructor(this);
		lc.constructLevel("Final1");

		objBarriers.clear();
		objCoins.clear();
		objMissiles.clear();

		addBarriers();
		addCoins();
		addMissiles();

		hero.setX(0);
		hero.setY(410);

		repaint(); 
	}

	

}
