import java.util.Random;

public class QuadTreeTest {
    private static final int NUM_POINTS = 1000000;  // Reduced for practicality
    private static final int MAX_COORDINATE = 10000000; // Maximum coordinate value

    public static void main(String[] args) {
        // Initialize the QuadTree with bounds large enough to contain all points
        QuadTree quadTree = new QuadTree(0, new Rectangle(0, 0, MAX_COORDINATE, MAX_COORDINATE));
        Random random = new Random();

        // Get the Java runtime
        Runtime runtime = Runtime.getRuntime();

        // Perform a garbage collection before starting the test
        runtime.gc();

        // Memory usage before the operations
        long startMemoryUse = runtime.totalMemory() - runtime.freeMemory();

        // Generate and insert places
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < NUM_POINTS; i++) {
            int x = 10 + random.nextInt(MAX_COORDINATE - 10);
            int y = 10 + random.nextInt(MAX_COORDINATE - 10);
            ServiceType[] serviceTypes = {generateRandomService()};
            Point point = new Point(x, y);
            Place place = new Place(point, serviceTypes);
            try {
                quadTree.insert(place);
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
        Point additionalPoint = new Point(234133, 517823);
        Place additionalPlace = new Place(additionalPoint, new ServiceType[]{ServiceType.ATM});
        quadTree.insert(additionalPlace);
        long insertEndTime = System.currentTimeMillis();
        System.out.println("Insertion of 1 place completed in " + (insertEndTime - insertStartTime) + " ms");

        // Querying
        long queryStartTime = System.currentTimeMillis();
        Rectangle queryRectangle = new Rectangle(500000, 500000, 200000, 200000);
        List<Place> foundPlaces = quadTree.query(queryRectangle, new ArrayList<>());
        long queryEndTime = System.currentTimeMillis();
        System.out.println("Querying " + foundPlaces.size() + " places completed in " + (queryEndTime - queryStartTime) + " ms");
    }

    private static ServiceType generateRandomService() {
        Random random = new Random();
        return ServiceType.values()[random.nextInt(ServiceType.values().length)];
    }
}
