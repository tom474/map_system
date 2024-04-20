class QuadTree {
    private static final int MAX_CAPACITY = 1000000;
    private final int level;
    private final List<Place> places;
    private final Rectangle bounds;
    private final QuadTree[] children;

    public QuadTree(int level, Rectangle bounds) {
        this.level = level;
        this.bounds = bounds;
        this.places = new ArrayList<>();
        this.children = new QuadTree[4];
    }

    private void split() {
        int subWidth = bounds.width / 2;
        int subHeight = bounds.height / 2;
        int x = bounds.x;
        int y = bounds.y;

        children[0] = new QuadTree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
        children[1] = new QuadTree(level + 1, new Rectangle(x, y, subWidth, subHeight));
        children[2] = new QuadTree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
        children[3] = new QuadTree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));

        for (int i = 0; i < places.size(); i++) {
            Place place = places.get(i);
            int index = getIndex(place.placeCoor);
            children[index].places.add(place);
        }
        places.clear();
    }

    private int getIndex(Point point) {
        double verticalMidpoint = bounds.x + bounds.width / 2.0;
        double horizontalMidpoint = bounds.y + bounds.height / 2.0;
        boolean topQuadrant = (point.y < horizontalMidpoint);
        boolean leftQuadrant = (point.x < verticalMidpoint);

        if (leftQuadrant) {
            return topQuadrant ? 1 : 2;
        } else {
            return topQuadrant ? 0 : 3;
        }
    }

    public void insert(Place place) {
        if (!bounds.contains(place.placeCoor.x, place.placeCoor.y)) {
            throw new IllegalArgumentException("Place " + place + " is out of the bounds of the quad tree");
        }

        if (places.size() < MAX_CAPACITY && children[0] == null) {
            places.add(place);
        } else {
            if (children[0] == null) {
                split();
            }
            int index = getIndex(place.placeCoor);
            children[index].insert(place);
        }
    }

    public boolean delete(Place place) {
        if (!bounds.contains(place.placeCoor.x, place.placeCoor.y)) {
            return false;
        }

        for (int i = 0; i < places.size(); i++) {
            if (places.get(i).equals(place)) {
                places.removeAt(i);
                return true;
            }
        }

        if (children[0] != null) {
            int index = getIndex(place.placeCoor);
            return children[index].delete(place);
        }

        return false;
    }

    public List<Place> query(Rectangle range, List<Place> found) {
        if (!bounds.intersects(range)) {
            return found;
        }

        for (int i = 0; i < places.size(); i++) {
            Place place = places.get(i);
            if (range.contains(place.placeCoor.x, place.placeCoor.y)) {
                found.add(place);
            }
        }

        if (children[0] != null) {
            for (int childIndex = 0; childIndex < children.length; childIndex++) {
                children[childIndex].query(range, found);
            }
        }

        return found;
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
    int x, y;
    int width, height;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean contains(int x, int y) {
        return x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height;
    }

    public boolean intersects(Rectangle other) {
        return !(other.x > x + width || other.x + other.width < x || other.y > y + height || other.y + other.height < y);
    }
}
