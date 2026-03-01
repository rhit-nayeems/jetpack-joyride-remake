package mainApp;

/**
 * Class: InvalidLevels
 * Purpose: checked exception for invalid level data format
 */
public class InvalidLevels extends Exception {

	public InvalidLevels(String message) {
		super(message);
	}
}
