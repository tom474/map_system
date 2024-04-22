package development;

public class Place {
    private String name;
    private Point2D location;
    private boolean[] services;     // Boolean array to track which services are offered

    public Place(String name, Point2D location, Service[] services) {
        this.name = name;
        this.location = location;
        this.services = new boolean[Service.size()];    // Initialize the boolean array for services
        for (Service service : services) {
            addService(service);     // Add each service to the list of offered services
        }
    }

    public String getName() {
        return name;
    }

    public Point2D getLocation() {
        return location;
    }

    public Service[] getServices() {
        Service[] offeredServices = new Service[Service.values().length];
        int count = 0;
        for (int i = 0; i < services.length; i++) {
            if (services[i]) {
                offeredServices[count] = Service.values()[i];
                count++;
            }
        }
        return offeredServices;
    }

    public boolean offersService(Service service) {
        return services[service.ordinal()];     // Return the service availability based on its ordinal
    }

    public void addService(Service service) {
        services[service.ordinal()] = true;     // Mark the service as offered
    }

    public void removeService(Service service) {
        services[service.ordinal()] = false;    // Mark the service as not offered
    }
}
