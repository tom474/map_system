package testcase;

import development.*;
import java.util.Random;

public class MapApplicationTest {
    private static final int NUM_POINTS = 100_000_000;
    private static final int MAX_COORDINATE = 10_000_000;
    private static final Random random = new Random();

    public static void main(String[] args) {
        // Add 100M random places to the quad tree
        QuadTree quadTree = new QuadTree(new Rectangle(0, MAX_COORDINATE, MAX_COORDINATE, MAX_COORDINATE));
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMemoryUse = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < NUM_POINTS; i++) {
            int x = random.nextInt(MAX_COORDINATE);
            int y = random.nextInt(MAX_COORDINATE);
            int serviceType = generateRandomService();
            quadTree.addPlace(x, y, serviceType);
        }
        long endMemoryUse = runtime.totalMemory() - runtime.freeMemory();
        long endTime = System.currentTimeMillis();
        System.out.println("Insertion of " + NUM_POINTS + " points completed in " + (endTime - startTime) + " ms");
        System.out.println("Memory used for insertion: " + ((endMemoryUse - startMemoryUse) / 1024 / 1024) + " MB");

        // Add an additional place
        int additionalX = 5_000_000;
        int additionalY = 5_000_000;
        int additionalService = generateRandomService();
        long additionalInsertStart = System.currentTimeMillis();
        quadTree.addPlace(additionalX, additionalY, additionalService);
        long additionalInsertEnd = System.currentTimeMillis();
        System.out.println("Insertion of additional point completed in " + (additionalInsertEnd - additionalInsertStart) + " ms");

        // Edit the additional place
        String[] editedService = {"Hospital", "Park"};
        long editStart = System.currentTimeMillis();
        quadTree.editPlace(additionalX, additionalY, editedService);
        long editEnd = System.currentTimeMillis();
        System.out.println("Editing of additional point completed in " + (editEnd - editStart) + " ms");

        // Remove the additional place
        long removeStart = System.currentTimeMillis();
        quadTree.removePlace(additionalX, additionalY);
        long removeEnd = System.currentTimeMillis();
        System.out.println("Removal of additional point completed in " + (removeEnd - removeStart) + " ms");

        // Search for the 50 nearest place
        int userX = 5_000_000;
        int userY = 5_000_000;
        int walkDistance = 10000;
        String[] searchService = {"Restaurant"};
        int k = 50;
        long searchStart = System.currentTimeMillis();
        ArrayList<Place> results = quadTree.searchPlace(userX, userY, walkDistance, searchService, k);
        long searchEnd = System.currentTimeMillis();
        System.out.println("Searching for the 50 nearest places completed in " + (searchEnd - searchStart) + " ms");
        System.out.println("Search results:");
        quadTree.displayPlaceList(results, userX, userY);
    }

    private static int generateRandomService() {
        int numServices = random.nextInt(Service.NUM_SERVICES) + 1;
        int result = 0;
        for (int i = 0; i < numServices; i++) {
            result |= 1 << random.nextInt(Service.NUM_SERVICES);
        }
        return result;
    }
}
