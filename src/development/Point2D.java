package development;

public class Point2D {
    private int x;
    private int y;

    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int distanceTo(Point2D anotherPoint) {
        return (int) Math.sqrt(Math.pow(x - anotherPoint.x, 2) + Math.pow(y - anotherPoint.y, 2));
    }
}
