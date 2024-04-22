package testcase;

import development.*;

import java.util.Random;

public class MapApplicationWith100MPlaces {
    private static final int NUM_POINTS = 70000000;  // Reduced for practicality
    private static final int MAX_COORDINATE = 10000000; // Maximum coordinate value
    private static final Random random = new Random();

    public static void main(String[] args) {
        // Initialize the QuadTree with bounds large enough to contain all points
        QuadTree quadTree = new QuadTree(new Rectangle(new Point2D(0, MAX_COORDINATE), MAX_COORDINATE, MAX_COORDINATE));

        // Get the Java runtime
        Runtime runtime = Runtime.getRuntime();

        // Perform a garbage collection before starting the test
        runtime.gc();

        // Memory usage before the operations
        long startMemoryUse = runtime.totalMemory() - runtime.freeMemory();

        // Generate and insert places with unique coordinates
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < NUM_POINTS; i++) {
            int x = 10 + random.nextInt(MAX_COORDINATE - 10);
            int y = 10 + random.nextInt(MAX_COORDINATE - 10);
            Service[] serviceTypes = {generateRandomService()};
            Point2D point = new Point2D(x, y);
            Place place = new Place("Place " + i, point, serviceTypes);
            try {
                quadTree.addPlace(place);
            } catch (IllegalArgumentException e) {
                System.err.println("Failed to insert place: " + place);
            }
        }
        long endTime = System.currentTimeMillis();
        long endMemoryUse = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("Insertion of " + NUM_POINTS + " places completed in " + (endTime - startTime) + " ms");
        System.out.println("Memory used for insertion: " + ((endMemoryUse - startMemoryUse) / 1024 / 1024) + " MB");

        // Insertion of an additional place
        long insertStartTime = System.currentTimeMillis();
        Point2D additionalPoint = new Point2D(234133, 517823);
        Place additionalPlace = new Place("Additional Place", additionalPoint, new Service[]{Service.ATM});
        quadTree.addPlace(additionalPlace);
        long insertEndTime = System.currentTimeMillis();
        System.out.println("Insertion of 1 place completed in " + (insertEndTime - insertStartTime) + " ms");

        // Querying
        long queryStartTime = System.currentTimeMillis();
        Rectangle queryRectangle = new Rectangle(new Point2D(500000, 500000), 200000, 200000);
        ArrayList<Place> foundPlaces = quadTree.searchPlace(new Point2D(600000, 650000), queryRectangle, Service.ATM);
        long queryEndTime = System.currentTimeMillis();
        System.out.println("Querying " + foundPlaces.size() + " places completed in " + (queryEndTime - queryStartTime) + " ms");
    }

    private static Service generateRandomService() {
        return Service.values()[random.nextInt(Service.values().length)];
    }
}
