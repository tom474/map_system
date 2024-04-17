package pnm.kdtree1;

public class Map2D {
}

enum ServiceType {
    ATM, RESTAURANT, HOSPITAL, GAS_STATION, COFFEE_SHOP, GROCERY_STORE, PHARMACY, HOTEL, BANK, BOOK_STORE;

    // Getting a hash code directly from enum ordinal
    public int getHashCode() {
        return this.ordinal();
    }

    public static int size() {
        return ServiceType.values().length;
    }
}

/**
 * Represents a geographical location (Place) on a map that can offer various services.
 * This class manages the services offered at a specific (x, y) coordinate without using Java Collections Framework.
 */
class Place {
    private int x;  // X-coordinate of the place
    private int y;  // Y-coordinate of the place
    private boolean[] services;  // Array to track which services are offered by this place

    /**
     * Constructs a new Place with specified coordinates.
     * Initializes the services array to track the services offered by this place.
     *
     * @param x The x-coordinate of the place
     * @param y The y-coordinate of the place
     */
    public Place(int x, int y) {
        this.x = x;
        this.y = y;
        this.services = new boolean[ServiceType.size()];  // Initialize the service availability array
    }

    /**
     * Checks whether the place offers a specific service.
     *
     * @param service The service type to check
     * @return true if the service is offered, false otherwise
     */
    public boolean offersService(ServiceType service) {
        return services[service.ordinal()];  // Return the service availability based on its ordinal
    }

    /**
     * Adds a service to the place.
     *
     * @param service The service type to add
     */
    public void addService(ServiceType service) {
        services[service.ordinal()] = true;  // Mark the service as offered
    }

    /**
     * Removes a service from the place.
     *
     * @param service The service type to remove
     */
    public void removeService(ServiceType service) {
        services[service.ordinal()] = false;  // Mark the service as not offered
    }

    /**
     * Returns all services offered by the place as an array.
     * Each service is represented by an array with a single integer (its ordinal).
     *
     * @return A 2D array where each sub-array contains the ordinal of a service offered by the place
     */
    public int[][] getServices() {
        SimpleList serviceList = new SimpleList(ServiceType.size());  // Create a new simple list to collect services
        for (ServiceType service : ServiceType.values()) {  // Iterate over all possible services
            if (offersService(service)) {  // Check if the service is offered
                serviceList.add(new int[] {service.ordinal()});  // Add the service ordinal to the list
            }
        }
        return serviceList.toArray();  // Convert the list of services to an array and return
    }

    /**
     * Gets the x-coordinate of the place.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the place.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }
}
