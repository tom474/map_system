package pnm.kdtree1;

public class Map2DTest {

    public static void main(String[] args) {
        Map2D map = new Map2D();

        // Test Adding Places
        System.out.println("Testing Add Method");
        map.add(10, 20, new Place(new Point(10, 20), new ServiceType[] {ServiceType.COFFEE_SHOP}));
        map.add(15, 25, new Place(new Point(15, 25), new ServiceType[] {ServiceType.HOSPITAL}));

        // Test Adding Services
        System.out.println("\nTesting Add Service to Point");
        boolean addServiceResult = map.addServiceToPoint(10, 20, ServiceType.PHARMACY);
        System.out.println("Adding Service to existing point (expected: true): " + addServiceResult);

        // Test Removing Services
        System.out.println("\nTesting Remove Service from Point");
        boolean removeServiceResult = map.removeServiceFromPoint(10, 20, ServiceType.PHARMACY);
        System.out.println("Removing Service from existing point (expected: true): " + removeServiceResult);

        // Test Deleting Places
        System.out.println("\nTesting Delete Method");
        map.delete(10, 20);
        boolean postDeleteAddServiceResult = map.addServiceToPoint(10, 20, ServiceType.BANK);
        System.out.println("Adding Service to deleted point (expected: false): " + postDeleteAddServiceResult);

        // Test Finding Closest Points
        System.out.println("\nTesting Find K Closest Points by Service");
        map.add(1, 1, new Place(new Point(1, 1), new ServiceType[] {ServiceType.ATM}));
        map.add(2, 2, new Place(new Point(2, 2), new ServiceType[] {ServiceType.ATM}));
        map.add(3, 3, new Place(new Point(3, 3), new ServiceType[] {ServiceType.ATM}));
        int[][] closestPoints = map.findKClosestPointsByService(0, 0, 2, ServiceType.ATM);
        System.out.println("2 Closest points offering ATM (expected: [(1,1), (2,2)]):");
        for (int[] point : closestPoints) {
            System.out.println("(" + point[0] + ", " + point[1] + ")");
        }

        // Test Searching By Service Within Bounds
        System.out.println("\nTesting Search By Service Within Bounds");
        Rectangle bounds = new Rectangle(0, 0, 10, 10);
        int[][] pointsInBounds = map.searchByServiceWithinBounds(bounds, ServiceType.ATM, 3);
        System.out.println("Points offering ATM within bounds (expected: [(1,1), (2,2)]):");
        for (int[] point : pointsInBounds) {
            System.out.println("(" + point[0] + ", " + point[1] + ")");
        }
    }
}

