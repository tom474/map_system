package development;

public class Place {
    private int x;
    private int y;
    private int services;

    public Place(int x, int y, int services) {
        this.x = x;
        this.y = y;
        this.services = services;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getServices() {
        return services;
    }

    public void setServices(int services) {
        this.services = services;
    }

    public double distanceTo(int x, int y) {
        double result = Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));

        // Return 2 digits after the decimal point
        return Math.round(result * 100.0) / 100.0;
    }
}
