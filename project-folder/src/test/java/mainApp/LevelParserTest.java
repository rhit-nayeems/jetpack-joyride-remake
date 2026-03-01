package mainApp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

class LevelParserTest {

	@Test
	void parseLines_parsesValidObjects() throws InvalidLevels {
		LevelParser parser = new LevelParser();
		LevelData data = parser.parseLines(List.of("# comment", "barrier n 10 20 30 40", "coin 100 200",
				"missile tracking 50 250"));

		assertEquals(1, data.getBarriers().size());
		assertEquals(1, data.getCoins().size());
		assertEquals(1, data.getMissiles().size());
		assertEquals("barrier n 10 20 30 40", data.getBarriers().get(0));
		assertEquals("coin 100 200", data.getCoins().get(0));
		assertEquals("missile tracking 50 250", data.getMissiles().get(0));
	}

	@Test
	void parseLines_rejectsInvalidBarrierFormat() {
		LevelParser parser = new LevelParser();
		assertThrows(InvalidLevels.class, () -> parser.parseLines(List.of("barrier n 10 20 30")));
	}

	@Test
	void parseLines_rejectsUnknownObjectType() {
		LevelParser parser = new LevelParser();
		assertThrows(InvalidLevels.class, () -> parser.parseLines(List.of("portal 10 20 30")));
	}
}
