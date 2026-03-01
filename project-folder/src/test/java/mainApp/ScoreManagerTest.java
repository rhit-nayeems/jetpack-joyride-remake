package mainApp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ScoreManagerTest {

	@TempDir
	Path tempDir;

	@Test
	void calculateScore_usesCoinsLivesLevelAndTicks() throws Exception {
		GameComponent component = new GameComponent();
		Hero hero = component.getHero();
		setPrivateIntField(hero, "numCoins", 3);
		setPrivateIntField(hero, "numLives", 4);

		ScoreManager scoreManager = new ScoreManager(tempDir.resolve("best_score_test.txt"));
		int score = scoreManager.calculateScore(hero, 3, 250);

		assertEquals(2350, score);
	}

	@Test
	void registerScore_persistsHighestValue() {
		Path scoreFile = tempDir.resolve("persisted_score.txt");
		ScoreManager scoreManager = new ScoreManager(scoreFile);
		scoreManager.registerScore(900);
		scoreManager.registerScore(850);
		scoreManager.registerScore(1200);

		ScoreManager reloaded = new ScoreManager(scoreFile);
		assertEquals(1200, reloaded.getBestScore());
	}

	private void setPrivateIntField(Object target, String fieldName, int value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.setInt(target, value);
	}
}
