package mainApp;

/**
 * Class: DifficultyPreset
 * Purpose: stores difficulty tuning values for gameplay
 */
public enum DifficultyPreset {

	EASY("Easy", 7, 0.85, 0.8), NORMAL("Normal", 6, 1.0, 1.0), HARD("Hard", 4, 1.25, 1.3);

	private final String displayName;
	private final int startingLives;
	private final double missileSpeedMultiplier;
	private final double obstacleDensityMultiplier;

	DifficultyPreset(String displayName, int startingLives, double missileSpeedMultiplier,
			double obstacleDensityMultiplier) {
		this.displayName = displayName;
		this.startingLives = startingLives;
		this.missileSpeedMultiplier = missileSpeedMultiplier;
		this.obstacleDensityMultiplier = obstacleDensityMultiplier;
	}

	public int getStartingLives() {
		return this.startingLives;
	}

	public double getMissileSpeedMultiplier() {
		return this.missileSpeedMultiplier;
	}

	public double getObstacleDensityMultiplier() {
		return this.obstacleDensityMultiplier;
	}

	@Override
	public String toString() {
		return this.displayName;
	}
}
