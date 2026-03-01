package mainApp;

/**
 * Class: MainApp Purpose: Starts the game
 */
public class MainApp {

	public static final int DELAY = 10;

	public static void main(String[] args) {
		GameComponent component = new GameComponent();
		new GameMenu(component);
	}
}
