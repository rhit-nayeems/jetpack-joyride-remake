package mainApp;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Class: PressListener
 * Purpose: detect key presses and execute controls
 */
public class PressListener implements KeyListener {

	private GameComponent gameComponent;

	/**
	 * ensures: constructs PressListener object
	 *
	 * @param gameComponent
	 */
	public PressListener(GameComponent gameComponent) {
		this.gameComponent = gameComponent;
	}

	/**
	 * ensures: compatibility constructor for existing call sites
	 *
	 * @param gameComponent
	 * @param levelConstructor
	 */
	public PressListener(GameComponent gameComponent, LevelConstructor levelConstructor) {
		this(gameComponent);
	}

	/**
	 * ensures: debug level skip controls
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		char key = e.getKeyChar();
		if (key == 'u') {
			this.gameComponent.changeLevelDebug(1);
		}
		if (key == 'd') {
			this.gameComponent.changeLevelDebug(-1);
		}
	}

	/**
	 * ensures: control movement and game state
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_UP && this.gameComponent.isRunning()) {
			this.gameComponent.getHero().flyUp();
		}
		if (key == KeyEvent.VK_R) {
			this.gameComponent.restartGame();
		}
		if (key == KeyEvent.VK_P) {
			this.gameComponent.togglePause();
		}
		if (key == KeyEvent.VK_1) {
			this.gameComponent.setDifficulty(DifficultyPreset.EASY);
			this.gameComponent.restartGame();
		}
		if (key == KeyEvent.VK_2) {
			this.gameComponent.setDifficulty(DifficultyPreset.NORMAL);
			this.gameComponent.restartGame();
		}
		if (key == KeyEvent.VK_3) {
			this.gameComponent.setDifficulty(DifficultyPreset.HARD);
			this.gameComponent.restartGame();
		}
	}

	/**
	 * ensures: stop flying when up key is released
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_UP) {
			this.gameComponent.getHero().stopFlying();
		}
	}
}
