package development;

public class Rectangle {
    private int x;
    private int y;
    private int width;
    private int height;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean contains(int x, int y) {
        return x >= this.x &&
               x <= this.x + this.width &&
               y <= this.y &&
               y >= this.y - this.height;
    }

    public boolean intersects(Rectangle anotherRect) {
        return this.x < anotherRect.x + anotherRect.width &&
               this.x + width > anotherRect.x &&
               this.y > anotherRect.y - anotherRect.height &&
               this.y - height < anotherRect.y;
    }
}
