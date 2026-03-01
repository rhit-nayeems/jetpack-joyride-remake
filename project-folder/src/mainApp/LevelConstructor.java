package mainApp;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class: InvalidLevels Purpose: implements error messages
 */
class InvalidLevels extends Exception {
	public InvalidLevels(String message) {
		super(message);
	}
}

/**
 * Class: Level Purpose: implements reading level text file and storing objects
 * into arraylists.
 */
class Level {

	private ArrayList<String> barriers = new ArrayList<>();
	private ArrayList<String> coins = new ArrayList<>();
	private ArrayList<String> missiles = new ArrayList<>();

	/**
	 * ensures: takes a file and sorts the barriers, coins, and missiles into a
	 * string list with all parameters
	 * 
	 * called by LevelConstructor.constructLevel
	 * 
	 * @param inputFileName
	 */
	public void readFile(String inputFileName) {
		barriers.clear();
		coins.clear();
		missiles.clear();
		try (Scanner scanner = new Scanner(new FileReader(inputFileName))) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty() || line.startsWith("#")) {
					continue;
				}
				String[] parts = line.split(" ");
				String objectType = parts[0];

				switch (objectType) {
				case "barrier":
					if (parts.length != 6) {
						throw new InvalidLevels("Invalid barrier format");
					}
					barriers.add(line);
					break;
				case "coin":
					if (parts.length != 3) {
						throw new InvalidLevels("Invalid coin format");
					}
					coins.add(line);
					break;
				case "missile":
					if (parts.length != 4) {
						throw new InvalidLevels("Invalid missile format");
					}
					missiles.add(line);
					break;
				default:
					throw new InvalidLevels("Unknown object type: " + objectType);

				}
			}

		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + inputFileName);

		} catch (InvalidLevels e) {
			System.err.println("Invalid level format: " + e.getMessage());

		} catch (IOException e) {
			System.err.println("I/O error: " + e.getMessage());
		}
	}

	public ArrayList<String> getBarriers() {
		return (ArrayList<String>) barriers;
	}

	public ArrayList<String> getCoins() {
		return (ArrayList<String>) coins;
	}

	public ArrayList<String> getMissiles() {
		return (ArrayList<String>) missiles;
	}
}

/**
 * Class: LevelConstructor Purpose: constructs level.
 */
public class LevelConstructor {
	private GameComponent component;

	private int levelNum;

	public LevelConstructor(GameComponent component) {
		this.component = component;

		this.levelNum = 1;
	}

	/**
	 * ensures: increases or decreases the level number by 1 calls constructLevel to
	 * construct the new level
	 * 
	 * @param levelChange <br>
	 *                    requires: levelChange is 1 or -1
	 */
	public void changeLevel(int levelChange) {
		levelNum += levelChange;
		if (levelNum > 8) {
			component.setGameWon(true);
			component.setGameOver(true);
			component.repaint();
			return;
		} else if (levelNum < 1) {
			levelNum = 1;
		}
		component.setCurrentLevel(levelNum);
		this.constructLevel("Final" + levelNum);
		System.out.println("The level is now level " + levelNum);
	}

	/**
	 * ensures: constructs a new level by reading a level file
	 * (level.readFile(levelFile)) and calls the GameComponent class to change and
	 * repaint the level
	 * 
	 * @param levelFile
	 */
	public void constructLevel(String levelFile) {

		Level level = new Level();
		level.readFile(levelFile);
		this.component.changeLevel(level.getBarriers(), level.getCoins(), level.getMissiles());

	}

}