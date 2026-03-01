package mainApp;

import java.util.Random;

/**
 * Class: ProceduralLevelGenerator
 * Purpose: build deterministic procedural levels after campaign levels
 */
public class ProceduralLevelGenerator {

	private static final int MIN_Y = 30;

	public LevelData generate(int level, DifficultyPreset difficulty, int width, int height) {
		LevelData data = new LevelData();
		long seed = 31L * level + difficulty.ordinal() * 997L;
		Random random = new Random(seed);

		int safeWidth = Math.max(900, width);
		int safeHeight = Math.max(450, height);
		double density = difficulty.getObstacleDensityMultiplier();

		int barrierCount = (int) Math.round((5 + level / 2.0) * density);
		int missileCount = (int) Math.round((2 + level / 4.0) * density);
		int coinClusters = 4 + level / 3;

		for (int i = 0; i < barrierCount; i++) {
			String barrierType = pickBarrierType(random, level);
			if ("laser".equals(barrierType)) {
				int delay = 40 + random.nextInt(420);
				int y = MIN_Y + random.nextInt(Math.max(60, safeHeight - 60));
				data.addBarrier("barrier laser " + delay + " " + y + " 0 0");
				continue;
			}

			int x1 = 120 + random.nextInt(Math.max(200, safeWidth - 220));
			int y1 = MIN_Y + random.nextInt(Math.max(60, safeHeight - 60));
			int length = 60 + random.nextInt(140);
			double angle = random.nextDouble() * Math.PI * 2;
			int x2 = clamp((int) Math.round(x1 + length * Math.cos(angle)), 60, safeWidth - 40);
			int y2 = clamp((int) Math.round(y1 + length * Math.sin(angle)), MIN_Y, safeHeight - 20);
			data.addBarrier("barrier " + barrierType + " " + x1 + " " + y1 + " " + x2 + " " + y2);
		}

		for (int i = 0; i < missileCount; i++) {
			String missileType = pickMissileType(random);
			int delay = 20 + random.nextInt(300);
			int y = MIN_Y + random.nextInt(Math.max(60, safeHeight - 80));
			data.addMissile("missile " + missileType + " " + delay + " " + y);
		}

		for (int cluster = 0; cluster < coinClusters; cluster++) {
			int centerX = 150 + random.nextInt(Math.max(200, safeWidth - 200));
			int centerY = 60 + random.nextInt(Math.max(80, safeHeight - 120));
			int coinCount = 4 + random.nextInt(5);
			for (int i = 0; i < coinCount; i++) {
				double theta = (2 * Math.PI * i) / coinCount;
				int radius = 20 + random.nextInt(45);
				int x = clamp((int) Math.round(centerX + radius * Math.cos(theta)), 40, safeWidth - 40);
				int y = clamp((int) Math.round(centerY + radius * Math.sin(theta)), MIN_Y, safeHeight - 30);
				data.addCoin("coin " + x + " " + y);
			}
		}

		return data;
	}

	private String pickBarrierType(Random random, int level) {
		int roll = random.nextInt(100);
		if (level >= 10 && roll < 22) {
			return "laser";
		}
		if (roll < 20) {
			return "e";
		}
		if (roll < 34) {
			return "r";
		}
		return "n";
	}

	private String pickMissileType(Random random) {
		int roll = random.nextInt(100);
		if (roll < 35) {
			return "non-tracking";
		}
		if (roll < 60) {
			return "tracking";
		}
		if (roll < 82) {
			return "sin";
		}
		return "random";
	}

	private int clamp(int value, int min, int max) {
		return Math.max(min, Math.min(max, value));
	}
}
