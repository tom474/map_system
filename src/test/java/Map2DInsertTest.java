import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Assertions;
import src.*;

import java.util.Random;

public class Map2DInsertTest {
    private static final int MAX_COORDINATE = 10_000_000;
    private static Map2D map2D;
    private static final Random random = new Random();

    @BeforeAll
    public static void setUpOnce() {
        map2D = new Map2D(new Rectangle(0, MAX_COORDINATE, MAX_COORDINATE, MAX_COORDINATE));
        System.out.println("| Num of Places   | Pass/Fail | Runtime (ms) | Memory Usage (MB) |");
        System.out.println("|-----------------|-----------|--------------|-------------------|");
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 100, 1_000, 10_000, 100_000, 1_000_000, 10_000_000, 100_000_000})
    void insertVariousNumberOfPlaces(int numberOfPlaces) {
        // Ensure the system is ready for measurement
        Runtime runtime = Runtime.getRuntime();
        System.gc();
        System.gc(); // Call twice to increase the likelihood of cleanup
        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();

        // Insert places with random coordinates and services
        for (int i = 0; i < numberOfPlaces; i++) {
            int x = random.nextInt(MAX_COORDINATE);
            int y = random.nextInt(MAX_COORDINATE);
            int serviceType = random.nextInt(); // Simulate random service encoding
            map2D.addPlace(x, y, serviceType);
        }

        long endTime = System.nanoTime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = (usedMemoryAfter - usedMemoryBefore) / (1024 * 1024); // Convert bytes to MB

        double duration = (endTime - startTime) / 1_000_000.0; // Convert ns to ms
        int actualNumberOfPlaces = map2D.countPlaces();
        boolean passed = actualNumberOfPlaces == numberOfPlaces;

        System.out.printf("| %-15d | %-9s | %12.3f | %17d |\n",
                numberOfPlaces, passed ? "Passed" : "Failed", duration, memoryUsed);

        // Reset the map for next test
        map2D.clear();
    }
}
