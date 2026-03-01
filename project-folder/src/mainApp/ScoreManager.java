package mainApp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class: ScoreManager
 * Purpose: calculate score and persist best score across runs
 */
public class ScoreManager {

	private final Path scoreFilePath;
	private int bestScore;

	public ScoreManager() {
		this(Path.of("best_score.txt"));
	}

	public ScoreManager(Path scoreFilePath) {
		this.scoreFilePath = scoreFilePath;
		this.bestScore = readBestScore();
	}

	public int calculateScore(Hero hero, int currentLevel, int numTicks) {
		int coinScore = hero.getNumCoins() * 100;
		int levelScore = Math.max(0, currentLevel - 1) * 500;
		int lifeScore = hero.getNumLives() * 250;
		int survivalScore = numTicks / 5;
		return coinScore + levelScore + lifeScore + survivalScore;
	}

	public int getBestScore() {
		return this.bestScore;
	}

	public void registerScore(int score) {
		if (score > this.bestScore) {
			this.bestScore = score;
			writeBestScore();
		}
	}

	private int readBestScore() {
		if (!Files.exists(scoreFilePath)) {
			return 0;
		}
		try {
			String content = Files.readString(scoreFilePath).trim();
			if (content.isEmpty()) {
				return 0;
			}
			return Integer.parseInt(content);
		} catch (IOException | NumberFormatException e) {
			return 0;
		}
	}

	private void writeBestScore() {
		try {
			Files.writeString(scoreFilePath, Integer.toString(this.bestScore));
		} catch (IOException e) {
			System.err.println("Unable to save best score.");
		}
	}
}
