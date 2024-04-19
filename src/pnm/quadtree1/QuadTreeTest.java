package pnm.quadtree1;

import java.util.Random;

public class QuadTreeTest {
    private static final int NUM_POINTS = 100000000; // 10 million points
    private static final int MAX_COORDINATE = 10000000; // Maximum coordinate value

    public static void main(String[] args) {
        // Initialize the QuadTree with bounds large enough to contain all points
        QuadTree quadTree = new QuadTree(0, new Rectangle(0, 0, MAX_COORDINATE, MAX_COORDINATE));
        Random random = new Random();

        // Generate and insert points
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < NUM_POINTS; i++) {
            int x = 10 + random.nextInt(MAX_COORDINATE - 10);
            int y = 10 + random.nextInt(MAX_COORDINATE - 10);
            ServiceType serviceType = generateRandomService();
            Point point = new Point(x, y, serviceType);
            try {
                quadTree.insert(point);
            } catch (IllegalArgumentException e) {
                System.err.println("Failed to insert point: " + point);
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Insertion of " + NUM_POINTS + " points completed in " + (endTime - startTime) + " ms");

        // Optionally, you can also test querying or deleting some points here
        long queryStartTime = System.currentTimeMillis();
        Rectangle queryRectangle = new Rectangle(500000, 500000, 200000, 200000);
        List<Point> foundPoints = quadTree.query(queryRectangle, new ArrayList<>());
        long queryEndTime = System.currentTimeMillis();
        System.out.println("Querying " + foundPoints.size() + " points completed in " + (queryEndTime - queryStartTime) + " ms");
    }

    private static ServiceType generateRandomService() {
        Random random = new Random();
        return ServiceType.values()[random.nextInt(ServiceType.values().length)];
    }

}
