package src;

/**
 * Represents a place in a 2D coordinate system.
 */
public class Place {
    private final int x;
    private final int y;
    private final int services;

    /**
     * Constructs a Place object with the specified position and services.
     *
     * @param x        The x-coordinate of the place.
     * @param y        The y-coordinate of the place.
     * @param services The services available at the place.
     */
    public Place(int x, int y, int services) {
        this.x = x;
        this.y = y;
        this.services = services;
    }

    /**
     * Gets the x-coordinate of the place.
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the place.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the services available at the place.
     *
     * @return The services.
     */
    public int getServices() {
        return services;
    }

    /**
     * Calculates the distance from this place to the specified coordinates.
     *
     * @param x The x-coordinate of the destination.
     * @param y The y-coordinate of the destination.
     * @return The distance between this place and the destination.
     */
    public double distanceTo(int x, int y) {
        // Calculate Euclidean distance
        double result = Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));

        // Round to 2 decimal places
        return Math.round(result * 100.0) / 100.0;
    }
}
