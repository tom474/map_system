package pnm.kdtree1;

/**
 * Represents a 2D map for managing geographical locations where each location can offer various services.
 * This implementation uses a KD-Tree to efficiently handle spatial queries such as nearest neighbor searches.
 */
public class Map2D implements Map2DADT {
    private MapNode root = null;

    /**
     * Adds a new place with specified coordinates and place details to the map.
     * This is the public method that starts the recursive process of adding a new node.
     *
     * @param x     the x-coordinate of the new place.
     * @param y     the y-coordinate of the new place.
     * @param place the details of the new place including the services it offers.
     */
    public void add(int x, int y, Place place) {
        root = add(root, new Point(x, y), place, true);
    }

    /**
     * Recursively adds a new node to the KD-Tree based on x and y coordinates.
     * This method uses the 'vertical' parameter to decide the dimension (x or y) to compare at each level of the tree.
     *
     * @param node     the current node in the KD-Tree during the recursion.
     * @param point    the point containing x and y coordinates where the new place should be added.
     * @param place    the place details to be stored at the point.
     * @param vertical a boolean flag that indicates whether to compare x-coordinates (true) or y-coordinates (false).
     * @return the new node if created, or the updated subtree root after insertion.
     */
    private MapNode add(MapNode node, Point point, Place place, boolean vertical) {
        if (node == null) {
            return new MapNode(point, place);
        }

        if (vertical ? point.x < node.getPoint().x : point.y < node.getPoint().y) {
            node.left = add(node.left, point, place, !vertical);
        } else {
            node.right = add(node.right, point, place, !vertical);
        }

        return balance(node, vertical);
    }

    /**
     * Removes a place from the map at the specified coordinates.
     * This is the public method that initiates the recursive deletion process.
     *
     * @param x the x-coordinate of the place to be deleted.
     * @param y the y-coordinate of the place to be deleted.
     */
    public void delete(int x, int y) {
        root = delete(root, new Point(x, y), true);
    }

    /**
     * Recursively removes a node from the KD-Tree.
     * This method adjusts the tree structure to maintain its properties after the deletion.
     *
     * @param node     the current node being examined in the KD-Tree.
     * @param point    the point that specifies the x and y coordinates for deletion.
     * @param vertical a boolean flag that indicates whether the current depth is comparing x or y coordinates.
     * @return the new root of the subtree after the deletion.
     */
    private MapNode delete(MapNode node, Point point, boolean vertical) {
        if (node == null) {
            return null; // Nothing to delete
        }

        if (node.getPoint().isEqual(point.x, point.y)) {
            if (node.left == null || node.right == null) {
                return node.left != null ? node.left : node.right;
            } else {
                MapNode min = findMin(node.right, !vertical);
                node.setPoint(min.getPoint());
                node.setPlace(min.getPlace());
                node.right = delete(node.right, min.getPoint(), !vertical);
            }
        } else if (vertical ? point.x < node.getPoint().x : point.y < node.getPoint().y) {
            node.left = delete(node.left, point, !vertical);
        } else {
            node.right = delete(node.right, point, !vertical);
        }

        return balance(node, vertical);
    }

    private MapNode rotateRight(MapNode y) {
        MapNode x = y.left;
        MapNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.updateHeight();
        x.updateHeight();

        return x;
    }

    private MapNode rotateLeft(MapNode x) {
        MapNode y = x.right;
        MapNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.updateHeight();
        y.updateHeight();

        return y;
    }

    private MapNode balance(MapNode node, boolean vertical) {
        if (node == null) return null;

        node.updateHeight();
        int balance = node.getBalance();

        if (balance > 1) {
            if (node.left != null && node.left.getBalance() < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        } else if (balance < -1) {
            if (node.right != null && node.right.getBalance() > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

    /**
     * Finds the minimum node in a subtree based on the 'vertical' parameter.
     * If 'vertical' is true, it finds the minimum x-value; otherwise, it finds the minimum y-value.
     *
     * @param node     the subtree in which to find the minimum value.
     * @param vertical a boolean indicating whether to compare x-values or y-values.
     * @return the node with the minimum value in the specified dimension.
     */
    private MapNode findMin(MapNode node, boolean vertical) {
        MapNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    /**
     * Adds a service to a point on the map.
     *
     * @param x       the x-coordinate of the point.
     * @param y       the y-coordinate of the point.
     * @param service the service to add.
     * @return true if the service was added successfully, false otherwise.
     */
    public boolean addServiceToPoint(int x, int y, ServiceType service) {
        MapNode node = findNode(root, x, y);
        if (node != null && node.getPlace() != null) {
            node.getPlace().addService(service);
            return true;
        }
        return false;
    }

    /**
     * Removes a service from a point on the map.
     *
     * @param x       the x-coordinate of the point.
     * @param y       the y-coordinate of the point.
     * @param service the service to remove.
     * @return true if the service was removed successfully, false otherwise.
     */
    public boolean removeServiceFromPoint(int x, int y, ServiceType service) {
        MapNode node = findNode(root, x, y);
        if (node != null && node.getPlace() != null) {
            node.getPlace().removeService(service);
            return true;
        }
        return false;
    }

    /**
     * Finds the node corresponding to the given coordinates in the KD-Tree.
     *
     * @param node the current node being examined.
     * @param x    the x-coordinate of the point.
     * @param y    the y-coordinate of the point.
     * @return the node containing the specified coordinates, or null if not found.
     */
    private MapNode findNode(MapNode node, int x, int y) {
        if (node == null) {
            return null;
        }
        if (node.getPoint().isEqual(x, y)) {
            return node;
        } else if ((x < node.getPoint().x) || (x == node.getPoint().x && y < node.getPoint().y)) {
            return findNode(node.left, x, y);
        } else {
            return findNode(node.right, x, y);
        }
    }

    /**
     * Finds the k closest points to a target point on the map that offer a specific service.
     *
     * @param targetX     the x-coordinate of the target point.
     * @param targetY     the y-coordinate of the target point.
     * @param k           the number of closest points to find.
     * @param serviceType the type of service to filter by.
     * @return a 2D array containing the coordinates of the k closest points.
     */
    public int[][] findKClosestPointsByService(int targetX, int targetY, int k, ServiceType serviceType) {
        MaxHeap pq = new MaxHeap(k, targetX, targetY);
        findKClosestPointsByService(root, targetX, targetY, k, true, pq, serviceType);

        SimpleList result = new SimpleList(k);
        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }
        return sortResults(result, targetX, targetY);
    }

    /**
     * Recursively finds the k closest points to a target point on the map that offer a specific service.
     *
     * @param node        the current node being examined.
     * @param targetX     the x-coordinate of the target point.
     * @param targetY     the y-coordinate of the target point.
     * @param k           the number of closest points to find.
     * @param vertical    a boolean indicating the dimension used for comparison at this level.
     * @param pq          the max heap to store the closest points.
     * @param serviceType the type of service to filter by.
     */
    private void findKClosestPointsByService(MapNode node, int targetX, int targetY, int k, boolean vertical, MaxHeap pq, ServiceType serviceType) {
        if (node == null) return;

        if (node.getPlace() != null && node.getPlace().offersService(serviceType)) {
            int[] point = new int[]{node.getPoint().x, node.getPoint().y};
            pq.offer(point);
        }

        MapNode nextBranch = vertical ? (targetX < node.getPoint().x ? node.left : node.right) : (targetY < node.getPoint().y ? node.left : node.right);
        findKClosestPointsByService(nextBranch, targetX, targetY, k, !vertical, pq, serviceType);

        long distToLine = vertical ? Math.abs(targetX - node.getPoint().x) : Math.abs(targetY - node.getPoint().y);
        if (pq.size() < k || distToLine * distToLine < pq.distSquared(pq.peek()[0], pq.peek()[1])) {
            MapNode otherBranch = vertical ? (targetX < node.getPoint().x ? node.right : node.left) : (targetY < node.getPoint().y ? node.right : node.left);
            findKClosestPointsByService(otherBranch, targetX, targetY, k, !vertical, pq, serviceType);
        }
    }

    /**
     * Searches for points on the map that offer a specific service within a given bounding rectangle.
     *
     * @param bounds      the bounding rectangle to search within.
     * @param service     the type of service to filter by.
     * @param k           the maximum number of points to return.
     * @return a 2D array containing the coordinates of points that offer the service within the bounds.
     */
    public int[][] searchByServiceWithinBounds(Rectangle bounds, ServiceType service, int k) {
        SimpleList result = new SimpleList(k);
        searchWithinBounds(root, bounds, service, result, k, true);
        return sortResults(result, bounds.centerX(), bounds.centerY());
    }

    /**
     * Recursively searches for points on the map that offer a specific service within a given bounding rectangle.
     *
     * @param node        the current node being examined.
     * @param bounds      the bounding rectangle to search within.
     * @param service     the type of service to filter by.
     * @param results     the list to store the matching points.
     * @param k           the maximum number of points to return.
     * @param vertical    a boolean indicating the dimension used for comparison at this level.
     */
    private void searchWithinBounds(MapNode node, Rectangle bounds, ServiceType service, SimpleList results, int k, boolean vertical) {
        if (node == null || results.size() >= k) return;

        int x = node.getPoint().x;
        int y = node.getPoint().y;

        // Check if current node is within bounds and offers the service
        if (bounds.contains(x, y) && node.getPlace() != null && node.getPlace().offersService(service)) {
            results.add(new int[]{x, y});
        }

        // Recursively search the left and right children if they potentially fall within bounds
        if (vertical) {
            if (bounds.intersectsLeft(node.getPoint().x)) {
                searchWithinBounds(node.left, bounds, service, results, k, !vertical);
            }
            if (bounds.intersectsRight(node.getPoint().x)) {
                searchWithinBounds(node.right, bounds, service, results, k, !vertical);
            }
        } else {
            if (bounds.intersectsLeft(node.getPoint().y)) {
                searchWithinBounds(node.left, bounds, service, results, k, !vertical);
            }
            if (bounds.intersectsRight(node.getPoint().y)) {
                searchWithinBounds(node.right, bounds, service, results, k, !vertical);
            }
        }
    }

    /**
     * Sorts the results based on their distance to the center of the bounding box.
     *
     * @param points  the list of points to be sorted.
     * @param centerX the x-coordinate of the center of the bounding box.
     * @param centerY the y-coordinate of the center of the bounding box.
     * @return a 2D array containing the sorted coordinates of the points.
     */
    private int[][] sortResults(SimpleList points, int centerX, int centerY) {
        // Insertion sort based on distance to the center of the bounding box
        for (int i = 1; i < points.size(); i++) {
            int[] current = points.get(i);
            int j = i - 1;
            while (j >= 0 && compareDistance(points.get(j), centerX, centerY, current) > 0) {
                points.set(j + 1, points.get(j));
                j--;
            }
            points.set(j + 1, current);
        }
        return points.toArray();
    }

    /**
     * Compares the distance between two points and the center of the bounding box.
     *
     * @param a       the first point.
     * @param centerX the x-coordinate of the center of the bounding box.
     * @param centerY the y-coordinate of the center of the bounding box.
     * @param b       the second point.
     * @return 0 if both points are equidistant from the center, a positive value if point a is closer, and a negative value if point b is closer.
     */
    private int compareDistance(int[] a, int centerX, int centerY, int[] b) {
        long distA = (long) (a[0] - centerX) * (a[0] - centerX) + (long) (a[1] - centerY) * (a[1] - centerY);
        long distB = (long) (b[0] - centerX) * (b[0] - centerX) + (long) (b[1] - centerY) * (b[1] - centerY);
        return Long.compare(distA, distB);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        SimpleQueue<MapNode> queue = new SimpleQueue<>();
        if (root != null) queue.add(root);

        while (!queue.isEmpty()) {
            MapNode node = queue.poll();
            sb.append('(').append(node.getPoint().x).append(", ").append(node.getPoint().y).append("), ");
            if (node.left != null) queue.add(node.left);
            if (node.right != null) queue.add(node.right);
        }

        if (sb.length() > 2) sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}

/**
 * Represents a node in the KD-Tree.
 */
class MapNode {
    private Point coordinates;
    private Place place;
    MapNode left = null, right = null;
    private int height = 1;

    public MapNode(Point coordinates, Place place) {
        this.coordinates = coordinates;
        this.place = place;
    }

    public Point getPoint() {
        return coordinates;
    }

    public void setPoint(Point point) {
        this.coordinates = point;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public int getHeight() {
        return height;
    }

    public void updateHeight() {
        height = 1 + Math.max(height(left), height(right));
    }

    private static int height(MapNode node) {
        return node == null ? 0 : node.getHeight();
    }

    public int getBalance() {
        return height(left) - height(right);
    }

    public boolean isEqual(int x, int y) {
        return coordinates.x == x && coordinates.y == y;
    }
}

class Point {
    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isEqual(int x, int y) {
        return this.x == x && this.y == y;
    }
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

class Place {
    protected Point placeCoor;
    protected boolean[] services;  // Boolean array to track which services are offered

    public Place(Point coordinates, boolean[] services) {
        this.placeCoor = coordinates;
        this.services = services;
    }

    public Place(Point placeCoor, ServiceType[] servicesToAdd) {
        this.placeCoor = placeCoor;
        this.services = new boolean[ServiceType.size()];  // Initialize the boolean array for services
        for (ServiceType service : servicesToAdd) {
            addService(service);  // Add each service to the place
        }
    }

    public boolean offersService(ServiceType service) {
        return services[service.ordinal()];  // Return the service availability based on its ordinal
    }

    public void addService(ServiceType service) {
        services[service.ordinal()] = true;  // Mark the service as offered
    }

    public void removeService(ServiceType service) {
        services[service.ordinal()] = false;  // Mark the service as not offered
    }

    public int[][] getServices() {
        SimpleList serviceList = new SimpleList(ServiceType.size());  // Create a new simple list to collect services
        for (ServiceType service : ServiceType.values()) {
            if (offersService(service)) {
                serviceList.add(new int[] {service.ordinal()});  // Add the service ordinal to the list
            }
        }
        return serviceList.toArray();  // Convert the list of services to an array and return
    }
}

class Rectangle {
    int left, top, right, bottom;

    public Rectangle(int x, int y, int width, int height) {
        this.left = x;
        this.top = y;
        this.right = x + width;
        this.bottom = y + height;
    }

    public boolean contains(int x, int y) {
        return x >= left && x <= right && y >= top && y <= bottom;
    }

    public boolean intersectsLeft(int coordinate) {
        return left <= coordinate;
    }

    public boolean intersectsRight(int coordinate) {
        return right >= coordinate;
    }

    public int centerX() {
        return (left + right) / 2;
    }

    public int centerY() {
        return (top + bottom) / 2;
    }
}
