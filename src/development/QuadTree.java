package development;

public class QuadTree {
    private static final int CAPACITY = 10_000;
    private final Rectangle boundary;
    private final QuadTree[] children;
    private int numOfPlaces;
    private final int[] placeXs;
    private final int[] placeYs;
    private final int[] placeServices;

    public QuadTree(Rectangle boundary) {
        this.boundary = boundary;
        children = new QuadTree[4];
        numOfPlaces = 0;
        placeXs = new int[CAPACITY];
        placeYs = new int[CAPACITY];
        placeServices = new int[CAPACITY];
    }

    private int getSuitableLeaf(int x, int y) {
        int verticalMidpoint = boundary.getX() + boundary.getWidth() / 2;
        int horizontalMidpoint = boundary.getY() - boundary.getHeight() / 2;
        boolean topQuadrant = (y >= horizontalMidpoint);
        boolean rightQuadrant = (x >= verticalMidpoint);
        if (topQuadrant) {
            return rightQuadrant ? 1 : 0;
        } else {
            return rightQuadrant ? 3 : 2;
        }
    }

    private void split() {
        int subWidth = boundary.getWidth() / 2;
        int subHeight = boundary.getHeight() / 2;
        int x = boundary.getX();
        int y = boundary.getY();

        children[0] = new QuadTree(new Rectangle(x, y, subWidth, subHeight));                                // Top left
        children[1] = new QuadTree(new Rectangle(x + subWidth, y, subWidth, subHeight));                  // Top right
        children[2] = new QuadTree(new Rectangle(x, y - subHeight, subWidth, subHeight));                 // Bottom left
        children[3] = new QuadTree(new Rectangle(x + subWidth, y - subHeight, subWidth, subHeight));   // Bottom right

        for (int i = 0; i < numOfPlaces; i++) {
            int leaf = getSuitableLeaf(placeXs[i], placeYs[i]);
            children[leaf].addPlace(placeXs[i], placeYs[i], placeServices[i]);
        }
        numOfPlaces = 0;
    }

    public void addPlace(int x, int y, int services) {
        if (children[0] != null) {
            int leaf = getSuitableLeaf(x, y);
            children[leaf].addPlace(x, y, services);
        } else {
            if (numOfPlaces < CAPACITY) {
                placeXs[numOfPlaces] = x;
                placeYs[numOfPlaces] = y;
                placeServices[numOfPlaces] = services;
                numOfPlaces++;
            } else {
                split();
                addPlace(x, y, services);
            }
        }
    }

    public boolean editPlace(int x, int y, String[] services) {
        if (children[0] != null) {
            int leaf = getSuitableLeaf(x, y);
            children[leaf].editPlace(x, y, services);
        } else {
            for (int i = 0; i < numOfPlaces; i++) {
                if (placeXs[i] == x && placeYs[i] == y) {
                    placeServices[i] = Service.encodeService(services);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removePlace(int x, int y) {
        if (children[0] != null) {
            int leaf = getSuitableLeaf(x, y);
            return children[leaf].removePlace(x, y);
        } else {
            for (int i = 0; i < numOfPlaces; i++) {
                if (placeXs[i] == x && placeYs[i] == y) {
                    placeXs[i] = placeXs[numOfPlaces - i - 1];
                    placeYs[i] = placeYs[numOfPlaces - i - 1];
                    placeServices[i] = placeServices[numOfPlaces - i - 1];
                    numOfPlaces--;
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<Place> searchPlace(int userX, int userY, int walkDistance, String[] services, int k) {
        Rectangle boundaryRect = new Rectangle(userX - walkDistance, userY + walkDistance, walkDistance * 2, walkDistance * 2);
        ArrayList<Place> results = new ArrayList<>();
        searchPlace(boundaryRect, services, results);

        sortPlaceList(results, 0, results.size() - 1, userX, userY);
        ArrayList<Place> kResults = new ArrayList<>();
        for (int i = 0; i < k && i < results.size(); i++) {
            kResults.add(results.get(i));
        }
        return kResults;
    }

    private void searchPlace(Rectangle boundaryRect, String[] services, ArrayList<Place> results) {
        if (!boundaryRect.intersects(boundary)) {
            return;
        }
        for (int i = 0; i < numOfPlaces; i++) {
            if (boundaryRect.contains(placeXs[i], placeYs[i]) && Service.contains(placeServices[i], Service.encodeService(services))) {
                results.add(new Place(placeXs[i], placeYs[i], placeServices[i]));
            }
        }
        if (children[0] != null) {
            for (QuadTree child : children) {
                child.searchPlace(boundaryRect, services, results);
            }
        }
    }

    private void sortPlaceList(ArrayList<Place> places, int low, int high, int userX, int userY) {
        if (low < high) {
            int pivot = partition(places, low, high, userX, userY);
            sortPlaceList(places, low, pivot - 1, userX, userY);
            sortPlaceList(places, pivot + 1, high, userX, userY);
        }
    }

    private int partition(ArrayList<Place> places, int low, int high, int userX, int userY) {
        Place pivot = places.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (places.get(j).distanceTo(userX, userY) < pivot.distanceTo(userX, userY)) {
                i++;
                Place temp = places.get(i);
                places.set(i, places.get(j));
                places.set(j, temp);
            }
        }
        Place temp = places.get(i + 1);
        places.set(i + 1, places.get(high));
        places.set(high, temp);
        return i + 1;
    }

    public void displayPlaceList(ArrayList<Place> places, int userX, int userY) {
        for (int i = 0; i < places.size(); i++) {
            Place place = places.get(i);
            System.out.println("Place " + (i + 1) + " at (" + place.getX() + ", " + place.getY() + ") is " + place.distanceTo(userX, userY) + " units away");
        }
    }
}
