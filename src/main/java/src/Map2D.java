package src;

import java.util.Arrays;

/**
 * Represents a 2D map with the ability to add, edit, and search places within a boundary.
 */
public class Map2D {
    private static final int CAPACITY = 100_000;
    private final Rectangle boundary;
    private final Map2D[] children;
    private int numOfPlaces;
    private final int[] placeXs;
    private final int[] placeYs;
    private final int[] placeServices;

    /**
     * Constructs a Map2D object with the given boundary.
     *
     * @param boundary The boundary of the map.
     */
    public Map2D(Rectangle boundary) {
        this.boundary = boundary;
        children = new Map2D[4];
        numOfPlaces = 0;
        placeXs = new int[CAPACITY];
        placeYs = new int[CAPACITY];
        placeServices = new int[CAPACITY];
    }

    /**
     * Determines the suitable leaf for placing coordinates (x, y) based on the boundary's quadrants.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The index of the suitable leaf.
     */
    private int getSuitableLeaf(int x, int y) {
        int verticalMidpoint = boundary.getX() + boundary.getWidth() / 2;
        int horizontalMidpoint = boundary.getY() - boundary.getHeight() / 2;
        boolean topQuadrant = (y >= horizontalMidpoint);
        boolean rightQuadrant = (x >= verticalMidpoint);
        if (topQuadrant) {
            return rightQuadrant ? 1 : 0; // Top right or top left
        } else {
            return rightQuadrant ? 3 : 2; // Bottom right or bottom left
        }
    }

    /**
     * Splits the map into four quadrants.
     */
    private void split() {
        int subWidth = boundary.getWidth() / 2;
        int subHeight = boundary.getHeight() / 2;
        int x = boundary.getX();
        int y = boundary.getY();

        children[0] = new Map2D(new Rectangle(x, y, subWidth, subHeight));                                // Top left
        children[1] = new Map2D(new Rectangle(x + subWidth, y, subWidth, subHeight));                  // Top right
        children[2] = new Map2D(new Rectangle(x, y - subHeight, subWidth, subHeight));                 // Bottom left
        children[3] = new Map2D(new Rectangle(x + subWidth, y - subHeight, subWidth, subHeight));   // Bottom right

        for (int i = 0; i < numOfPlaces; i++) {
            int leaf = getSuitableLeaf(placeXs[i], placeYs[i]);
            children[leaf].addPlace(placeXs[i], placeYs[i], placeServices[i]);
        }
        numOfPlaces = 0;
    }

    /**
     * Adds a place to the map.
     *
     * @param x        The x-coordinate of the place.
     * @param y        The y-coordinate of the place.
     * @param services The services available at the place.
     */
    public void addPlace(int x, int y, int services) {
        if (!boundary.contains(x, y)) {
            throw new IllegalArgumentException("Place is out of boundary.");
        }
        if (children[0] != null) {
            int leaf = getSuitableLeaf(x, y);
            children[leaf].addPlace(x, y, services);
        } else {
            if (numOfPlaces < CAPACITY) {
                placeXs[numOfPlaces] = x;
                placeYs[numOfPlaces] = y;
                placeServices[numOfPlaces] = services;
                numOfPlaces++;
            } else {
                split();
                addPlace(x, y, services);
            }
        }
    }

    /**
     * Edits services available at a place.
     *
     * @param x        The x-coordinate of the place.
     * @param y        The y-coordinate of the place.
     * @param services The new services available.
     * @return True if the place is found and edited, false otherwise.
     */
    public boolean editPlace(int x, int y, String[] services) {
        if (children[0] != null) {
            int leaf = getSuitableLeaf(x, y);
            return children[leaf].editPlace(x, y, services);  // Added return here
        } else {
            for (int i = 0; i < numOfPlaces; i++) {
                if (placeXs[i] == x && placeYs[i] == y) {
                    placeServices[i] = Service.encodeService(services);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes a place from the map.
     *
     * @param x The x-coordinate of the place.
     * @param y The y-coordinate of the place.
     * @return True if the place is found and removed, false otherwise.
     */
    public boolean removePlace(int x, int y) {
        if (children[0] != null) {
            int leaf = getSuitableLeaf(x, y);
            return children[leaf].removePlace(x, y);
        } else {
            for (int i = 0; i < numOfPlaces; i++) {
                if (placeXs[i] == x && placeYs[i] == y) {
                    // Correctly shifting remaining places
                    for (int j = i; j < numOfPlaces - 1; j++) {
                        placeXs[j] = placeXs[j + 1];
                        placeYs[j] = placeYs[j + 1];
                        placeServices[j] = placeServices[j + 1];
                    }
                    numOfPlaces--;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Searches for places within a certain distance from a given point with specified services.
     *
     * @param userX        The x-coordinate of the user's position.
     * @param userY        The y-coordinate of the user's position.
     * @param walkDistance The maximum walking distance from the user.
     * @param services     The services to search for.
     * @param k            The maximum number of results to return.
     * @return An ArrayList of places matching the search criteria.
     */
    public ArrayList<Place> searchPlace(int userX, int userY, int walkDistance, String[] services, int k) {
        Rectangle boundaryRect = new Rectangle(userX - walkDistance, userY + walkDistance, walkDistance * 2, walkDistance * 2);
        ArrayList<Place> results = new ArrayList<>();
        searchPlace(boundaryRect, services, results);

        // Call to merge sort
        if (!results.isEmpty()) {
            mergeSortPlaceList(results, 0, results.size() - 1, userX, userY);
        }

        ArrayList<Place> kResults = new ArrayList<>();
        for (int i = 0; i < k && i < results.size(); i++) {
            kResults.add(results.get(i));
        }
        return kResults;
    }

    /**
     * Recursively searches for places within a boundary with specified services.
     *
     * @param boundaryRect The boundary to search within.
     * @param services     The services to search for.
     * @param results      The ArrayList to store the results.
     */
    private void searchPlace(Rectangle boundaryRect, String[] services, ArrayList<Place> results) {
        if (!boundaryRect.intersects(boundary)) {
            return;
        }
        if (children[0] != null) {
            for (Map2D child : children) {
                child.searchPlace(boundaryRect, services, results);
            }
        }
        for (int i = 0; i < numOfPlaces; i++) {
            if (boundaryRect.contains(placeXs[i], placeYs[i]) && Service.contains(placeServices[i], Service.encodeService(services))) {
                results.add(new Place(placeXs[i], placeYs[i], placeServices[i]));
            }
        }
    }

    /**
     * Merges two halves of a place list during merge sort.
     *
     * @param places The ArrayList of places to merge.
     * @param left   The left index.
     * @param mid    The middle index.
     * @param right  The right index.
     * @param userX  The x-coordinate of the user's position.
     * @param userY  The y-coordinate of the user's position.
     */
    private void merge(ArrayList<Place> places, int left, int mid, int right, int userX, int userY) {
        // Temporary ArrayLists to hold the two halves
        ArrayList<Place> leftSublist = new ArrayList<>();
        ArrayList<Place> rightSublist = new ArrayList<>();

        // Copy data to temporary sublists
        for (int i = 0; i <= mid - left; i++) {
            leftSublist.add(places.get(left + i));
        }
        for (int j = 0; j < right - mid; j++) {
            rightSublist.add(places.get(mid + 1 + j));
        }

        int i = 0, j = 0;
        int k = left; // Start merging into the original array starting from left index

        // Merge back to original list
        while (i < leftSublist.size() && j < rightSublist.size()) {
            if (leftSublist.get(i).distanceTo(userX, userY) <= rightSublist.get(j).distanceTo(userX, userY)) {
                places.set(k++, leftSublist.get(i++));
            } else {
                places.set(k++, rightSublist.get(j++));
            }
        }

        // Copy the remaining elements of leftSublist, if any
        while (i < leftSublist.size()) {
            places.set(k++, leftSublist.get(i++));
        }

        // Copy the remaining elements of rightSublist, if any
        while (j < rightSublist.size()) {
            places.set(k++, rightSublist.get(j++));
        }
    }

    /**
     * Sorts places using merge sort.
     *
     * @param places The ArrayList of places to sort.
     * @param left   The left index of the subarray.
     * @param right  The right index of the subarray.
     * @param userX  The x-coordinate of the user's position.
     * @param userY  The y-coordinate of the user's position.
     */
    private void mergeSortPlaceList(ArrayList<Place> places, int left, int right, int userX, int userY) {
        if (left < right) {
            int mid = (left + right) / 2; // Find the middle point

            // Sort first and second halves
            mergeSortPlaceList(places, left, mid, userX, userY);
            mergeSortPlaceList(places, mid + 1, right, userX, userY);

            // Merge the sorted halves
            merge(places, left, mid, right, userX, userY);
        }
    }

    /**
     * Displays the list of places with relevant information.
     *
     * @param places          The ArrayList of places to display.
     * @param userX           The x-coordinate of the user's position.
     * @param userY           The y-coordinate of the user's position.
     * @param searchedServices The services searched by the user.
     */
    public void displayPlaceList(ArrayList<Place> places, int userX, int userY, String[] searchedServices) {
        int placeColumnWidth = 5; // Width of the Place column
        int distanceColumnWidth = 16; // Width of the Distance(units) column
        // Display Headers
        System.out.println("_".repeat(140));
        System.out.println("| " + String.format("%" + placeColumnWidth + "s", "Place") + " | " + String.format("%-24s", "Coordinates") + " | " + String.format("%" + distanceColumnWidth + "s", "Distance(units)") + " | " + String.format("%-30s", "Matched Services") + " | " + String.format("%-50s", "Other Services") + " |");
        System.out.println("|" + "-".repeat(placeColumnWidth + 2) + "|" + "-".repeat(26) + "|" + "-".repeat(distanceColumnWidth + 2) + "|" + "-".repeat(32) + "|" + "-".repeat(52) + "|");
        // Split Services into Matched Services and Other Services.
        for (int i = 0; i < places.size(); i++) {
            Place place = places.get(i);
            String coordinates = "(x: " + place.getX() + ", y: " + place.getY() + ")";
            String distance = String.format("%.2f", place.distanceTo(userX, userY));
            String allServices = String.join(", ", Service.decodeService(place.getServices()));
            String[] services = allServices.split(", ");
            String[] matchedServices = new String[searchedServices.length];
            String[] otherServices = new String[services.length];

            Arrays.fill(matchedServices, "");
            Arrays.fill(otherServices, "");

            for (String service : services) {
                boolean matched = false;
                for (String searchedService : searchedServices) {
                    if (service.equals(searchedService)) {
                        matched = true;
                        break;
                    }
                }
                if (matched) {
                    for (int j = 0; j < matchedServices.length; j++) {
                        if (matchedServices[j].isEmpty()) {
                            matchedServices[j] = service;
                            break;
                        }
                    }
                } else {
                    for (int j = 0; j < otherServices.length; j++) {
                        if (otherServices[j].isEmpty()) {
                            otherServices[j] = service;
                            break;
                        }
                    }
                }
            }

            matchedServices = Arrays.stream(matchedServices).filter(s -> !s.isEmpty()).toArray(String[]::new);
            otherServices = Arrays.stream(otherServices).filter(s -> !s.isEmpty()).toArray(String[]::new);

            String matchedServicesStr = String.join(", ", matchedServices);
            String otherServicesStr = String.join(", ", otherServices);

            String placeNumber = String.format("%" + placeColumnWidth + "d", i + 1);
            // Display Place
            System.out.println("| " + placeNumber + " | " + String.format("%-24s", coordinates) + " | " + String.format("%" + distanceColumnWidth + "s", distance) + " | " + String.format("%-30s", matchedServicesStr) + " | " + String.format("%-50s", otherServicesStr) + " |");
        }
        // Display Footer
        System.out.println("|" + "_".repeat(placeColumnWidth + 2) + "|" + "_".repeat(26) + "|" + "_".repeat(distanceColumnWidth + 2) + "|" + "_".repeat(32) + "|" + "_".repeat(52) + "|");
    }

    public int countPlaces() {
        int count = numOfPlaces;
        if (children[0] != null) {
            for (Map2D child : children) {
                count += child.countPlaces();
            }
        }
        return count;
    }

    public void clear() {
        numOfPlaces = 0;
        for (int i = 0; i < children.length; i++) {
            children[i] = null;
        }
    }
}
