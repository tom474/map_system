package development;

public class Place {
    private int x;
    private int y;
    private int service;

    public Place(int x, int y, String[] services) {
        this.x = x;
        this.y = y;
        setServices(services);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String[] getServices() {
        return Service.decodeService(service);
    }

    public void setServices(String[] services) {
        service = Service.encodeService(services);
    }

    public double distanceTo(int x, int y) {
        double result = Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));

        // Return 2 digits after the decimal point
        return Math.round(result * 100.0) / 100.0;
    }
}
