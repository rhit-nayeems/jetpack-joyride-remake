package mainApp;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class: LevelParser
 * Purpose: parse text level files into validated LevelData
 */
public class LevelParser {

	public LevelData parseFile(String inputFileName) throws InvalidLevels {
		ArrayList<String> lines = new ArrayList<>();
		try (Scanner scanner = new Scanner(new FileReader(inputFileName))) {
			while (scanner.hasNextLine()) {
				lines.add(scanner.nextLine());
			}
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("File not found: " + inputFileName, e);
		}
		return parseLines(lines);
	}

	public LevelData parseLines(List<String> lines) throws InvalidLevels {
		LevelData data = new LevelData();
		for (String rawLine : lines) {
			String line = rawLine.trim();
			if (line.isEmpty() || line.startsWith("#")) {
				continue;
			}
			String[] parts = line.split("\\s+");
			String objectType = parts[0];

			switch (objectType) {
			case "barrier":
				validateBarrier(parts, line);
				data.addBarrier(line);
				break;
			case "coin":
				validateCoin(parts, line);
				data.addCoin(line);
				break;
			case "missile":
				validateMissile(parts, line);
				data.addMissile(line);
				break;
			default:
				throw new InvalidLevels("Unknown object type: " + objectType);
			}
		}
		return data;
	}

	private void validateBarrier(String[] parts, String line) throws InvalidLevels {
		if (parts.length != 6) {
			throw new InvalidLevels("Invalid barrier format: " + line);
		}
		String type = parts[1];
		if (!("n".equals(type) || "e".equals(type) || "r".equals(type) || "laser".equals(type))) {
			throw new InvalidLevels("Unknown barrier type: " + type);
		}
		parseNumber(parts[2], "barrier x1");
		parseNumber(parts[3], "barrier y1");
		parseNumber(parts[4], "barrier x2");
		parseNumber(parts[5], "barrier y2");
	}

	private void validateCoin(String[] parts, String line) throws InvalidLevels {
		if (parts.length != 3) {
			throw new InvalidLevels("Invalid coin format: " + line);
		}
		parseNumber(parts[1], "coin x");
		parseNumber(parts[2], "coin y");
	}

	private void validateMissile(String[] parts, String line) throws InvalidLevels {
		if (parts.length != 4) {
			throw new InvalidLevels("Invalid missile format: " + line);
		}
		String type = parts[1];
		if (!("non-tracking".equals(type) || "tracking".equals(type) || "sin".equals(type)
				|| "random".equals(type))) {
			throw new InvalidLevels("Unknown missile type: " + type);
		}
		parseNumber(parts[2], "missile delay");
		parseNumber(parts[3], "missile y");
	}

	private double parseNumber(String token, String fieldName) throws InvalidLevels {
		try {
			return Double.parseDouble(token);
		} catch (NumberFormatException e) {
			throw new InvalidLevels("Invalid number in " + fieldName + ": " + token);
		}
	}
}
