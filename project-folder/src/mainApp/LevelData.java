package mainApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class: LevelData
 * Purpose: stores parsed level objects for barriers, coins, and missiles
 */
public class LevelData {

	private final ArrayList<String> barriers;
	private final ArrayList<String> coins;
	private final ArrayList<String> missiles;

	public LevelData() {
		this.barriers = new ArrayList<>();
		this.coins = new ArrayList<>();
		this.missiles = new ArrayList<>();
	}

	public void addBarrier(String barrierLine) {
		this.barriers.add(barrierLine);
	}

	public void addCoin(String coinLine) {
		this.coins.add(coinLine);
	}

	public void addMissile(String missileLine) {
		this.missiles.add(missileLine);
	}

	public List<String> getBarriers() {
		return Collections.unmodifiableList(this.barriers);
	}

	public List<String> getCoins() {
		return Collections.unmodifiableList(this.coins);
	}

	public List<String> getMissiles() {
		return Collections.unmodifiableList(this.missiles);
	}
}
