package pnm.kdtree1;

import java.util.Random;

public class Map2DTest {

    public static void main(String[] args) {
        Map2D map = new Map2D();
        Random random = new Random();
        long PLACES = 10000000; // Number of places to add to the map

        // Test Adding Places
        testAdd(map, random, PLACES);

//        // Test Adding Services
//        System.out.println("\nTesting Add Service to Point");
//        boolean addServiceResult = map.addServiceToPoint(10, 20, ServiceType.PHARMACY);
//        System.out.println("Adding Service to existing point (expected: true): " + addServiceResult);
//
//        // Test Removing Services
//        System.out.println("\nTesting Remove Service from Point");
//        boolean removeServiceResult = map.removeServiceFromPoint(10, 20, ServiceType.PHARMACY);
//        System.out.println("Removing Service from existing point (expected: true): " + removeServiceResult);
//
//        // Test Deleting Places
//        System.out.println("\nTesting Delete Method");
//        map.delete(10, 20);
//        boolean postDeleteAddServiceResult = map.addServiceToPoint(10, 20, ServiceType.BANK);
//        System.out.println("Adding Service to deleted point (expected: false): " + postDeleteAddServiceResult);
//
//        // Test Finding Closest Points
//        System.out.println("\nTesting Find K Closest Points by Service");
//        map.add(1, 1, new Place(new Point(1, 1), new ServiceType[] {ServiceType.ATM}));
//        map.add(2, 2, new Place(new Point(2, 2), new ServiceType[] {ServiceType.ATM}));
//        map.add(3, 3, new Place(new Point(3, 3), new ServiceType[] {ServiceType.ATM}));
//        int[][] closestPoints = map.findKClosestPointsByService(0, 0, 2, ServiceType.ATM);
//        System.out.println("2 Closest points offering ATM (expected: [(1,1), (2,2)]):");
//        for (int[] point : closestPoints) {
//            System.out.println("(" + point[0] + ", " + point[1] + ")");
//        }
//
//        // Test Searching By Service Within Bounds
//        System.out.println("\nTesting Search By Service Within Bounds");
//        Rectangle bounds = new Rectangle(0, 0, 10, 10);
//        int[][] pointsInBounds = map.searchByServiceWithinBounds(bounds, ServiceType.ATM, 3);
//        System.out.println("Points offering ATM within bounds (expected: [(1,1), (2,2)]):");
//        for (int[] point : pointsInBounds) {
//            System.out.println("(" + point[0] + ", " + point[1] + ")");
//        }
    }

    // Method to generate a random array of ServiceType
    private static ServiceType[] generateRandomServices() {
        Random random = new Random();
        int numServices = random.nextInt(ServiceType.values().length) + 1; // Random number of services (1 to total number of services)
        ServiceType[] services = new ServiceType[numServices];
        for (int i = 0; i < numServices; i++) {
            services[i] = ServiceType.values()[random.nextInt(ServiceType.values().length)];
        }
        return services;
    }

    private static void testAdd(Map2D map, Random random, long PLACES) {
        long startTime = System.nanoTime(); // Record start time
        System.out.println("Testing Add Method");
        for (int i = 0; i < PLACES; i++) {
            int x = random.nextInt(10000000); // Generate random x-coordinate (0 to 99)
            int y = random.nextInt(10000000); // Generate random y-coordinate (0 to 99)
            map.add(x, y, new Place(new Point(x, y), generateRandomServices()));
        }
        long endTime = System.nanoTime(); // Record end time
        System.out.println("Time taken to add " + PLACES + " places: " + (endTime - startTime) / 1e9 + " seconds");

        long operationTime = System.nanoTime(); // Record start time
        int a = random.nextInt(10000000);
        int b = random.nextInt(10000000);
        map.add(a, b, new Place(new Point(a, b), generateRandomServices()));
        long operationEndTime = System.nanoTime(); // Record end time
        System.out.println("Time taken to add a place: " + (operationEndTime - operationTime) / 1e9 + " seconds");
    }
}

