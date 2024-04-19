package pnm.quadtree1;

enum ServiceType {
    ATM, RESTAURANT, HOSPITAL, GAS_STATION, COFFEE_SHOP, GROCERY_STORE, PHARMACY, HOTEL, BANK, BOOK_STORE
}

class QuadTree {
    private static final int MAX_CAPACITY = 1000000;
    private final int level;
    private final List<Point> points;
    private final Rectangle bounds;
    private final QuadTree[] children;

    public QuadTree(int level, Rectangle bounds) {
        this.level = level;
        this.bounds = bounds;
        this.points = new ArrayList<>();
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

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            int index = getIndex(point);
            children[index].points.add(point);
        }
        points.clear();
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

    public void insert(Point point) {
        if (!bounds.contains(point.x, point.y)) {
            throw new IllegalArgumentException("Point " + point + " is out of the bounds of the quad tree");
        }

        if (points.size() < MAX_CAPACITY && children[0] == null) {
            points.add(point);
        } else {
            if (children[0] == null) {
                split();
            }
            int index = getIndex(point);
            children[index].insert(point);
        }
    }

    public boolean delete(Point point) {
        if (!bounds.contains(point.x, point.y)) {
            return false;
        }

        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).equals(point)) {
                points.removeAt(i);
                return true;
            }
        }

        if (children[0] != null) {
            int index = getIndex(point);
            return children[index].delete(point);
        }

        return false;
    }

    public List<Point> query(Rectangle range, List<Point> found) {
        if (!bounds.intersects(range)) {
            return found;
        }

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (range.contains(point.x, point.y)) {
                found.add(point);
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
    ServiceType serviceType;

    public Point(int x, int y, ServiceType serviceType) {
        this.x = x;
        this.y = y;
        this.serviceType = serviceType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point other) {
            return x == other.x && y == other.y && serviceType == other.serviceType;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return x * 31 + y * 17 + serviceType.hashCode();
    }

    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + ", serviceType=" + serviceType + '}';
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
