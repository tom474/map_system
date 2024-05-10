import src.*;

import java.util.Random;

public class Main {
    private static final int NUM_POINTS = 100_000_000;
    private static final int MAX_COORDINATE = 10_000_000;
    private static final Random random = new Random();

    public static void main(String[] args) {
        // Add 100M random places to the quad tree
        Map2D map2D = new Map2D(new Rectangle(0, MAX_COORDINATE, MAX_COORDINATE, MAX_COORDINATE));
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMemoryUse = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < NUM_POINTS; i++) {
            int x = random.nextInt(MAX_COORDINATE);
            int y = random.nextInt(MAX_COORDINATE);
            int serviceType = generateRandomService();
            map2D.addPlace(x, y, serviceType);
        }
        long endMemoryUse = runtime.totalMemory() - runtime.freeMemory();
        long endTime = System.currentTimeMillis();
        System.out.println("Insertion of " + NUM_POINTS + " points completed in " + (endTime - startTime) + " ms");
        System.out.println("Memory used for insertion: " + ((endMemoryUse - startMemoryUse) / 1024 / 1024) + " MB");

        // Add a place
        int additionalX = 5_000_000;
        int additionalY = 5_000_000;
        int additionalService = generateRandomService();
        long additionalInsertStart = System.currentTimeMillis();
        map2D.addPlace(additionalX, additionalY, additionalService);
        long additionalInsertEnd = System.currentTimeMillis();
        System.out.println("Insertion of additional point completed in " + (additionalInsertEnd - additionalInsertStart) + " ms");

        // Edit the additional place
        String[] editedService = {"Restaurant", "Park"};
        long editStart = System.currentTimeMillis();
        map2D.editPlace(additionalX, additionalY, editedService);
        long editEnd = System.currentTimeMillis();
        System.out.println("Editing of additional point completed in " + (editEnd - editStart) + " ms");

        // Remove the additional place
        long removeStart = System.currentTimeMillis();
        map2D.removePlace(additionalX, additionalY);
        long removeEnd = System.currentTimeMillis();
        System.out.println("Removal of additional point completed in " + (removeEnd - removeStart) + " ms");

        // Search for the 50 nearest place
        System.out.println("-".repeat(138));
        // input search method
        int userX = 5_000_000;
        int userY = 5_000_000;
        int walkDistance = 50000;
        String[] searchService = {"Restaurant", "School"};

        // Display search input
        Rectangle boundaryRect = new Rectangle(userX - walkDistance, userY + walkDistance, walkDistance * 2, walkDistance * 2);
        System.out.println("User's coordinate: (x: " + userX + "," + " y: " + userY + ")");
        System.out.println("The longest distance that user willing to go: " + walkDistance + " units");
        System.out.println("Services are searched: " + String.join(", ", searchService));
        System.out.println("Bounding rectangle size: " + (boundaryRect.getX() + boundaryRect.getWidth() - boundaryRect.getX()) + " x " + (boundaryRect.getX() + boundaryRect.getHeight() - boundaryRect.getX()));
        int k = 50;

        // Output for search method
        System.out.println();
        long searchStart = System.currentTimeMillis();
        ArrayList<Place> results = map2D.searchPlace(userX, userY, walkDistance, searchService, k);
        long searchEnd = System.currentTimeMillis();

        // Display search output
        System.out.println("Searching for the 50 nearest places completed in " + (searchEnd - searchStart) + " ms");
        System.out.println("There are " + results.size() + " places has been found");
        System.out.println("Search results:");

        if (results.size() == 0) {
            System.out.println("This coordinate is out-of-map");
        } else {
            map2D.displayPlaceList(results, userX, userY, searchService);
        }

    }

    private static int generateRandomService() {
        int numServices = random.nextInt(5) + 1;
        int result = 0;
        for (int i = 0; i < numServices; i++) {
            result |= 1 << random.nextInt(Service.NUM_SERVICES);
        }
        return result;
    }
}
