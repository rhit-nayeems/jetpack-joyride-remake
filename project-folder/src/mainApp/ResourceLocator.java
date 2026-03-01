package mainApp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Class: ResourceLocator
 * Purpose: resolve resource files from classpath and common project locations
 */
public final class ResourceLocator {

	private static final String PROJECT_FOLDER_NAME = "project-folder";
	private static final int MAX_PARENT_DEPTH = 6;

	private ResourceLocator() {
	}

	public static InputStream openStream(String resourceName) throws IOException {
		InputStream classpathStream = openClasspathStream(resourceName);
		if (classpathStream != null) {
			return classpathStream;
		}

		Path resolved = resolvePath(resourceName);
		if (resolved != null) {
			return Files.newInputStream(resolved);
		}

		throw new IOException("Resource not found: " + resourceName);
	}

	public static Path resolvePath(String resourceName) {
		String normalizedResourceName = normalizeResourceName(resourceName);
		for (Path root : collectSearchRoots()) {
			Path candidate = root.resolve(normalizedResourceName).normalize();
			if (Files.exists(candidate)) {
				return candidate;
			}
		}
		return null;
	}

	public static Path resolveWritablePath(String fileName) {
		Path existingPath = resolvePath(fileName);
		if (existingPath != null) {
			return existingPath;
		}
		return Path.of("").toAbsolutePath().normalize().resolve(normalizeResourceName(fileName));
	}

	private static InputStream openClasspathStream(String resourceName) {
		String normalizedResourceName = normalizeResourceName(resourceName);
		ClassLoader classLoader = ResourceLocator.class.getClassLoader();
		return classLoader.getResourceAsStream(normalizedResourceName);
	}

	private static String normalizeResourceName(String resourceName) {
		String normalized = resourceName.replace('\\', '/');
		while (normalized.startsWith("/")) {
			normalized = normalized.substring(1);
		}
		return normalized;
	}

	private static Set<Path> collectSearchRoots() {
		LinkedHashSet<Path> roots = new LinkedHashSet<>();

		Path cwd = Path.of("").toAbsolutePath().normalize();
		addRootAndParents(roots, cwd);
		roots.add(cwd.resolve(PROJECT_FOLDER_NAME).normalize());

		Path classpathLocation = resolveClasspathLocation();
		if (classpathLocation != null) {
			addRootAndParents(roots, classpathLocation);
		}

		return roots;
	}

	private static Path resolveClasspathLocation() {
		try {
			Path location = Paths.get(ResourceLocator.class.getProtectionDomain().getCodeSource().getLocation().toURI())
					.toAbsolutePath().normalize();
			if (Files.isRegularFile(location)) {
				return location.getParent();
			}
			return location;
		} catch (Exception ignored) {
			return null;
		}
	}

	private static void addRootAndParents(Set<Path> roots, Path start) {
		Path current = start;
		int depth = 0;
		while (current != null && depth <= MAX_PARENT_DEPTH) {
			roots.add(current.normalize());
			roots.add(current.resolve(PROJECT_FOLDER_NAME).normalize());
			current = current.getParent();
			depth++;
		}
	}
}
