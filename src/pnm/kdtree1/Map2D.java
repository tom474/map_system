package pnm.kdtree1;

public class Map2D implements Map2DADT {
    private MapNode root = null;

    public void add(int x, int y, Place place) {
        root = add(root, new Point(x, y), place, true);
    }

    private MapNode add(MapNode node, Point point, Place place, boolean vertical) {
        if (node == null) {
            return new MapNode(point, place);
        }
        if (vertical ? point.x < node.getPoint().x : point.y < node.getPoint().y) {
            node.left = add(node.left, point, place, !vertical);
        } else {
            node.right = add(node.right, point, place, !vertical);
        }
        return node;
    }

    public void delete(int x, int y) {
        root = delete(root, new Point(x, y), true);
    }

    private MapNode delete(MapNode node, Point point, boolean vertical) {
        if (node == null) {
            throw new RuntimeException("Point not found!");
        }
        if (node.getPoint().isEqual(point.x, point.y)) {
            if (node.hasTwoChildren()) {
                MapNode min = findMin(node.right, vertical);
                node.setPoint(min.getPoint());
                node.right = delete(node.right, min.getPoint(), !vertical);
                return node;
            } else {
                return node.left != null ? node.left : node.right;
            }
        } else if (vertical ? point.x < node.getPoint().x : point.y < node.getPoint().y) {
            node.left = delete(node.left, point, !vertical);
        } else {
            node.right = delete(node.right, point, !vertical);
        }
        return node;
    }

    private MapNode findMin(MapNode node, boolean vertical) {
        MapNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    public boolean addServiceToPoint(int x, int y, ServiceType service) {
        MapNode node = findNode(root, x, y);
        if (node != null && node.getPlace() != null) {
            node.getPlace().addService(service);
            return true;
        }
        return false;
    }

    public boolean removeServiceFromPoint(int x, int y, ServiceType service) {
        MapNode node = findNode(root, x, y);
        if (node != null && node.getPlace() != null) {
            node.getPlace().removeService(service);
            return true;
        }
        return false;
    }

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

    public int[][] findKClosestPointsByService(int targetX, int targetY, int k, ServiceType serviceType) {
        MaxHeap pq = new MaxHeap(k, targetX, targetY);
        findKClosestPointsByService(root, targetX, targetY, k, true, pq, serviceType);

        SimpleList result = new SimpleList(k);
        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }
        return sortResults(result, targetX, targetY);
    }

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

    public int[][] searchByServiceWithinBounds(Rectangle bounds, ServiceType service, int k) {
        SimpleList result = new SimpleList(k);
        searchWithinBounds(root, bounds, service, result, k, true);
        return sortResults(result, bounds.centerX(), bounds.centerY());
    }

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

class MapNode {
    private Point coordinates;
    private Place place;
    MapNode left = null, right = null;

    public MapNode(Point coordinates) {
        this.coordinates = coordinates;
    }

    public MapNode(Point coordinates, Place place) {
        this.coordinates = coordinates;
        this.place = place;
    }

    public Point getPoint() {
        return coordinates;
    }

    public Place getPlace() {
        return place;
    }

    public void setPoint(Point point) {
        this.coordinates = point;
    }

    public boolean isEqual(int x, int y) {
        return coordinates.x == x && coordinates.y == y;
    }

    public boolean hasTwoChildren() {
        return left != null && right != null;
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
