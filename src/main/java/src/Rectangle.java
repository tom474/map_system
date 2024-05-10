package src;

/**
 * Represents a rectangle in a 2D coordinate system.
 */
public class Rectangle {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    /**
     * Constructs a Rectangle object with the specified position and size.
     *
     * @param x      The x-coordinate of the top-left corner of the rectangle.
     * @param y      The y-coordinate of the top-left corner of the rectangle.
     * @param width  The width of the rectangle.
     * @param height The height of the rectangle.
     */
    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the x-coordinate of the top-left corner of the rectangle.
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the top-left corner of the rectangle.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the width of the rectangle.
     *
     * @return The width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the rectangle.
     *
     * @return The height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Checks if the specified point is contained within this rectangle.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     * @return True if the point is contained within the rectangle, false otherwise.
     */
    public boolean contains(int x, int y) {
        return x >= this.x &&
                x <= this.x + this.width &&
                y <= this.y &&
                y >= this.y - this.height;
    }

    /**
     * Checks if this rectangle intersects with another rectangle.
     *
     * @param anotherRect The other rectangle to check intersection with.
     * @return True if there is an intersection, false otherwise.
     */
    public boolean intersects(Rectangle anotherRect) {
        return this.x < anotherRect.x + anotherRect.width &&
                this.x + width > anotherRect.x &&
                this.y > anotherRect.y - anotherRect.height &&
                this.y - height < anotherRect.y;
    }
}
