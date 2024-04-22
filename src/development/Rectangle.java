package development;

public class Rectangle {
    Point2D topLeftPoint;
    int width;
    int height;

    public Rectangle(Point2D topLeftPoint, int width, int height) {
        this.topLeftPoint = topLeftPoint;
        this.width = width;
        this.height = height;
    }

    public boolean contains(Point2D point) {
        return point.getX() >= topLeftPoint.getX() &&
               point.getX() <= topLeftPoint.getX() + width &&
               point.getY() <= topLeftPoint.getY() &&
               point.getY() >= topLeftPoint.getY() - height;
    }

    public boolean intersect(Rectangle anotherRect) {
        return topLeftPoint.getX() < anotherRect.topLeftPoint.getX() + anotherRect.width &&
               topLeftPoint.getX() + width > anotherRect.topLeftPoint.getX() &&
               topLeftPoint.getY() > anotherRect.topLeftPoint.getY() - anotherRect.height &&
               topLeftPoint.getY() - height < anotherRect.topLeftPoint.getY();
    }

    public static void main(String[] args) {
        // Test contains method
        Rectangle rect1 = new Rectangle(new Point2D(5, 20), 10, 10);
        Point2D point1 = new Point2D(7, 12);
        Point2D point2 = new Point2D(20, 30);
        System.out.println(rect1.contains(point1)); // true
        System.out.println(rect1.contains(point2)); // false

        // Test intersect method
        Rectangle rect2 = new Rectangle(new Point2D(10, 15), 10, 10);
        Rectangle rect3 = new Rectangle(new Point2D(15, 20), 10, 10);
        Rectangle rect4 = new Rectangle(new Point2D(50, 100), 10, 10);
        System.out.println(rect2.intersect(rect3)); // true
        System.out.println(rect2.intersect(rect4)); // false
    }
}
