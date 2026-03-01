package mainApp;

/**
 * Class: LevelConstructor
 * Purpose: constructs levels and tracks current level
 */
public class LevelConstructor {

	public static final int MIN_LEVEL = 1;
	public static final int CAMPAIGN_MAX_LEVEL = 8;

	private final GameComponent component;
	private final LevelParser parser;
	private final ProceduralLevelGenerator proceduralGenerator;
	private int levelNum;

	public LevelConstructor(GameComponent component) {
		this.component = component;
		this.parser = new LevelParser();
		this.proceduralGenerator = new ProceduralLevelGenerator();
		this.levelNum = MIN_LEVEL;
	}

	/**
	 * ensures: changes level relative to the current level
	 *
	 * @param levelChange
	 */
	public void changeLevel(int levelChange) {
		int targetLevel = this.levelNum + levelChange;
		if (targetLevel < MIN_LEVEL) {
			targetLevel = MIN_LEVEL;
		}
		if (targetLevel > CAMPAIGN_MAX_LEVEL && !component.isProceduralEnabled()) {
			component.setGameWon(true);
			return;
		}
		loadLevel(targetLevel);
	}

	/**
	 * ensures: advances to the next level if available
	 *
	 * @return true when level was advanced, false when campaign is complete and
	 *         procedural mode is disabled
	 */
	public boolean advanceLevel() {
		int nextLevel = this.levelNum + 1;
		if (nextLevel > CAMPAIGN_MAX_LEVEL && !component.isProceduralEnabled()) {
			return false;
		}
		loadLevel(nextLevel);
		return true;
	}

	/**
	 * ensures: reloads the currently selected level
	 */
	public void reloadCurrentLevel() {
		loadLevel(this.levelNum);
	}

	/**
	 * ensures: loads a specific level and updates component state
	 *
	 * @param targetLevel
	 */
	public void loadLevel(int targetLevel) {
		this.levelNum = Math.max(MIN_LEVEL, targetLevel);
		this.component.setCurrentLevel(this.levelNum);

		LevelData levelData;
		if (this.levelNum <= CAMPAIGN_MAX_LEVEL) {
			levelData = loadCampaignLevel(this.levelNum);
		} else {
			levelData = loadProceduralLevel(this.levelNum);
		}

		this.component.changeLevel(levelData);
		System.out.println("The level is now level " + this.levelNum);
	}

	private LevelData loadCampaignLevel(int levelNumber) {
		try {
			return parser.parseFile("Final" + levelNumber);
		} catch (InvalidLevels e) {
			System.err.println("Invalid level format in Final" + levelNumber + ": " + e.getMessage());
			return new LevelData();
		} catch (IllegalStateException e) {
			System.err.println(e.getMessage());
			return new LevelData();
		}
	}

	private LevelData loadProceduralLevel(int levelNumber) {
		int width = component.getWidth() > 0 ? component.getWidth() : 1000;
		int height = component.getHeight() > 0 ? component.getHeight() : 500;
		return proceduralGenerator.generate(levelNumber, component.getDifficulty(), width, height);
	}

	public int getLevelNum() {
		return this.levelNum;
	}

	public void setLevelNum(int levelNum) {
		this.levelNum = Math.max(MIN_LEVEL, levelNum);
		this.component.setCurrentLevel(this.levelNum);
	}
}
