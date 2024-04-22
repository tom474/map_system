package development;

public class QuadTree {
    private static final int MAX_CAPACITY = 1000000;
    private ArrayList<Place> places;
    private Rectangle boundary;
    private QuadTree[] children;

    public QuadTree(Rectangle boundary) {
        this.boundary = boundary;
        this.places = new ArrayList();
        this.children = new QuadTree[4];
    }

    private int getSuitableLeaf(Point2D point) {
        int index = -1;
        for (int i = 0; i < 4; i++) {
            if (children[i].boundary.contains(point)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void split() {
        int x = boundary.topLeftPoint.getX();
        int y = boundary.topLeftPoint.getY();
        int w = boundary.width / 2;
        int h = boundary.height / 2;

        children[0] = new QuadTree(new Rectangle(new Point2D(x + w, y), w, h));
        children[1] = new QuadTree(new Rectangle(new Point2D(x, y), w, h));
        children[2] = new QuadTree(new Rectangle(new Point2D(x, y - h), w, h));
        children[3] = new QuadTree(new Rectangle(new Point2D(x + w, y - h), w, h));

        for (int i = 0; i < places.size(); i++) {
            Place place = places.get(i);
            int index = getSuitableLeaf(place.getLocation());
            children[index].addPlace(place);
        }
        places.clear();
    }

    public void addPlace(Place place) {
        if (!boundary.contains(place.getLocation())) {
            throw new IllegalArgumentException("Place " + place + " is out of the bounds of the quad tree");
        }

        if (places.size() < MAX_CAPACITY && children[0] == null) {
            places.add(place);
        } else {
            if (children[0] == null) {
                split();
            }
            int index = getSuitableLeaf(place.getLocation());
            children[index].addPlace(place);
        }
    }

    public boolean removePlace(Place place) {
        if (!boundary.contains(place.getLocation())) {
            return false;
        }

        if (children[0] == null) {
            return places.remove(place);
        } else {
            int index = getSuitableLeaf(place.getLocation());
            if (index != -1) {
                return children[index].removePlace(place);
            }
        }

        return false;
    }

    private Place findPlace(Point2D point) {
        if (!boundary.contains(point)) {
            return null;
        }

        if (children[0] == null) {
            for (int i = 0; i < places.size(); i++) {
                Place place = places.get(i);
                if (place.getLocation().equals(point)) {
                    return place;
                }
            }
        } else {
            int index = getSuitableLeaf(point);
            if (index != -1) {
                return children[index].findPlace(point);
            }
        }

        return null;
    }

    public boolean updatePlace(Place place) {
        return true;
    }

    // Find maximum 50 places within the boundary rectangle that offer the specified service
    // Then, display the place list in ascending order of distance from the user location
    public ArrayList<Place> searchPlace(Point2D userLocation, Rectangle boundaryRectangle, Service service) {
        ArrayList<Place> results = new ArrayList();
        searchPlace(results, boundaryRectangle, service);
        sortPlacesByDistances(results, 0, results.size() - 1, userLocation);
        return results;
    }

    private ArrayList<Place> searchPlace(ArrayList<Place> results, Rectangle boundaryRectangle, Service service) {
        if (!boundary.intersect(boundaryRectangle)) {
            return results;
        }

        if (children[0] == null) {
            for (int i = 0; i < places.size(); i++) {
                Place place = places.get(i);
                if (boundaryRectangle.contains(place.getLocation()) && place.offersService(service)) {
                    results.add(place);
                }
            }
        } else {
            for (int i = 0; i < 4; i++) {
                children[i].searchPlace(results, boundaryRectangle, service);
            }
        }

        return results;
    }

    // Quick sort algorithm to sort places based on distances from the user location
    private void sortPlacesByDistances(ArrayList<Place> places, int minIndex, int maxIndex, Point2D userLocation) {
        if (minIndex < maxIndex) {
            int pivotIndex = partition(places, minIndex, maxIndex, userLocation);
            sortPlacesByDistances(places, minIndex, pivotIndex - 1, userLocation);
            sortPlacesByDistances(places, pivotIndex + 1, maxIndex, userLocation);
        }
    }

    private int partition(ArrayList<Place> places, int minIndex, int maxIndex, Point2D userLocation) {
        Place pivot = places.get(maxIndex);
        int i = minIndex - 1;
        for (int j = minIndex; j < maxIndex; j++) {
            if (places.get(j).getLocation().distanceTo(userLocation) < pivot.getLocation().distanceTo(userLocation)) {
                i++;
                Place temp = places.get(i);
                places.set(i, places.get(j));
                places.set(j, temp);
            }
        }
        Place temp = places.get(i + 1);
        places.set(i + 1, places.get(maxIndex));
        places.set(maxIndex, temp);
        return i + 1;
    }
}
