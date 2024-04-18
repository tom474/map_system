package pnm.kdtree1;

public interface Map2DADT {
    // Adds a new place with specified coordinates and place details
    void add(int x, int y, Place place);

    // Deletes a place at specified coordinates
    void delete(int x, int y);

    // Adds a service to a place at specified coordinates
    boolean addServiceToPoint(int x, int y, ServiceType service);

    // Removes a service from a place at specified coordinates
    boolean removeServiceFromPoint(int x, int y, ServiceType service);

    // Finds the k closest places by service type within a specified target area
    int[][] findKClosestPointsByService(int targetX, int targetY, int k, ServiceType serviceType);

    // Searches for places within a specified rectangle that offer a certain service
    int[][] searchByServiceWithinBounds(Rectangle bounds, ServiceType service, int k);
}
