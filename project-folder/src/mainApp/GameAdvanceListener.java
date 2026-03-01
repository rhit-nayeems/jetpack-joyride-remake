package mainApp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class: GameAdvanceListener
 * Purpose: advances the game timer and updates the game
 */
public class GameAdvanceListener implements ActionListener {

	private GameComponent gameComponent;

	/**
	 * ensures: constructs the AdvanceListener
	 * @param gameComponent
	 */
	public GameAdvanceListener(GameComponent gameComponent) {
		this.gameComponent = gameComponent;
	}

	/**
	 * ensures: advances game by one tick
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		advanceOneTick();
	}

	/**
	 * ensures: updates the game and repaints the screen
	 */
	public void advanceOneTick() {
		this.gameComponent.updateState();
		this.gameComponent.drawScreen();
	}
}
