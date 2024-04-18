package pnm.quadtree1;

import java.util.ArrayList;
import java.util.List;

class QuadTree {

    private final int MAX_CAPACITY = 10;
    private int level;
    private List<Point> points;
    private Rectangle bounds;
    private QuadTree[] children;

    public QuadTree(int level, Rectangle bounds) {
        this.level = level;
        this.bounds = bounds;
        this.points = new ArrayList<>();
        this.children = new QuadTree[4];
    }

    // Subdivides the current node into 4 children
    private void split() {
        int subWidth = (int)(bounds.width / 2);
        int subHeight = (int)(bounds.height / 2);
        int x = bounds.x;
        int y = bounds.y;

        children[0] = new QuadTree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
        children[1] = new QuadTree(level + 1, new Rectangle(x, y, subWidth, subHeight));
        children[2] = new QuadTree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
        children[3] = new QuadTree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
    }

    // Determines the index of the child node that contains the point
    private int getIndex(Point point) {
        int index = -1;
        double verticalMidpoint = bounds.x + (bounds.width / 2);
        double horizontalMidpoint = bounds.y + (bounds.height / 2);

        // Check top quadrants
        boolean topQuadrant = (point.y < horizontalMidpoint);
        // Check bottom quadrants
        boolean bottomQuadrant = (point.y > horizontalMidpoint);

        // Check if the point is in the left quadrants
        if (point.x < verticalMidpoint) {
            if (topQuadrant) {
                index = 1;
            } else if (bottomQuadrant) {
                index = 2;
            }
        }
        // Check if the point is in the right quadrants
        else if (point.x > verticalMidpoint) {
            if (topQuadrant) {
                index = 0;
            } else if (bottomQuadrant) {
                index = 3;
            }
        }
        return index;
    }

    // Insert a point into the QuadTree
    public void insert(Point point) {
        if (!bounds.contains(point.x, point.y)) {
            return;
        }

        if (points.isEmpty()) {
            points.add(point);
        } else {
            if (children[0] == null) {
                split();
                // Re-insert existing point(s) into correct child node(s)
                Point existing = points.remove(0); // Remove the existing point
                insert(existing); // Re-insert it to ensure it goes into the correct child
            }

            int index = getIndex(point);
            if (index != -1) {
                children[index].insert(point);
            } else {
                // This else block should rarely be reached since points should either
                // fit into a child or the current node should be capable of holding it.
                points.add(point);
            }
        }
    }

    // Query the QuadTree for points within a certain range
    public List<Point> query(Rectangle range, List<Point> found) {
        if (!bounds.intersects(range)) {
            return found;
        }

        for (Point point : points) {
            if (range.contains(point.x, point.y)) {
                found.add(point);
            }
        }

        if (children[0] != null) {
            for (int i = 0; i < children.length; i++) {
                children[i].query(range, found);
            }
        }

        return found;
    }

    public void print(String indent) {
        System.out.println(indent + "Node Bounds: " + bounds + " | Points: " + points.size());
        for (Point point : points) {
            System.out.println(indent + "  Point: " + point);
        }
        if (children[0] != null) {
            for (int i = 0; i < children.length; i++) {
                children[i].print(indent + "  ");
            }
        }
    }

    public static void main(String[] args) {
        Rectangle bounds = new Rectangle(0, 0, 10000000, 10000000);
        QuadTree quadTree = new QuadTree(0, bounds);

        // Example: Insert some points
        quadTree.insert(new Point(10, 10, "P1"));
        quadTree.insert(new Point(700000, 800000, "P2"));
        quadTree.insert(new Point(20, 20, "P3"));
        quadTree.insert(new Point(729038, 900000, "P4"));
        quadTree.insert(new Point(30, 30, "P5"));
        quadTree.insert(new Point(312390, 301323, "P6"));
        quadTree.insert(new Point(70, 70, "P7"));
        quadTree.insert(new Point(651120, 687956, "P8"));
        quadTree.print("");

        // Example: Query points within a rectangle
        Rectangle searchArea = new Rectangle(500000, 500000, 400000, 400000);
        List<Point> foundPoints = quadTree.query(searchArea, new ArrayList<>());
        System.out.println("Found points: " + foundPoints.size());
        for (Point p : foundPoints) {
            System.out.println(p);
        }
    }
}

class Point {
    int x, y;
    String serviceType;

    public Point(int x, int y, String serviceType) {
        this.x = x;
        this.y = y;
        this.serviceType = serviceType;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", serviceType='" + serviceType + '\'' +
                '}';
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

