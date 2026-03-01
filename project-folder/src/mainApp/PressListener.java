package mainApp;

import java.awt.event.KeyEvent;

import java.awt.event.KeyListener;

/**
 * Class: PressListener Purpose: Detect for key presses and execute code
 * accordingly
 */
public class PressListener implements KeyListener {

	private GameComponent gameComponent;
	private LevelConstructor levelConstructor;

	/**
	 * ensures: constructs PressListener object
	 * @param gameComponent
	 * @param levelConstructor
	 */
	public PressListener(GameComponent gameComponent, LevelConstructor levelConstructor) {
		this.gameComponent = gameComponent;
		this.levelConstructor = levelConstructor;
	}
	/**
	 * ensures: constructs PressListener object
	 * @param levelConstructor
	 */
	public PressListener(LevelConstructor levelConstructor) {
		this.levelConstructor = levelConstructor;
		this.levelConstructor.toString();
	}
	/**
	 * ensures: detects if a key is typed and changes the level accordingly
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		int key = e.getKeyChar();
		if (key == 'u') {
			this.levelConstructor.changeLevel(1);
		}
		if (key == 'd') {
			this.levelConstructor.changeLevel(-1);
		}
	}
	/**
	 * ensures: check if up or R key is pressed to fly up or reset the level
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_UP) {
			this.gameComponent.getHero().flyUp();
		}
		if (key == KeyEvent.VK_R) {
			this.levelConstructor.changeLevel(0);
		}
	}
	/**
	 * ensures: checks if up key is released to stop flying
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_UP) {
			this.gameComponent.getHero().stopFlying();
		}
	}
}
