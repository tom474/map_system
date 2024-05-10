import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import src.*;

import java.util.Arrays;
import java.util.Random;

public class Map2DMainTest {
    private static final int NUM_PLACES = 100_000_000;
    private static final int MAX_COORDINATE = 10_000_000;
    private static Map2D map2D;
    private static final Random random = new Random();

    @BeforeAll
    public static void setUpOnce() {
        map2D = new Map2D(new Rectangle(0, MAX_COORDINATE, MAX_COORDINATE, MAX_COORDINATE));
        populateMapWithRandomPlaces();
    }

    private static void populateMapWithRandomPlaces() {
        // Service that should not be added
        int excludedServiceIndex = Service.CONVENIENCE_STORE; // Adjust this line if the method doesn't exist
        for (int i = 0; i < NUM_PLACES; i++) {
            int x = 10 + random.nextInt(MAX_COORDINATE - 10);
            int y = 10 + random.nextInt(MAX_COORDINATE - 10);
            int serviceType = generateRandomServiceExcluding(excludedServiceIndex);
            map2D.addPlace(x, y, serviceType);
        }
    }

    private static int generateRandomServiceExcluding(int excludedService) {
        int numServices = random.nextInt(Service.NUM_SERVICES - 1) + 1;
        int serviceType = 0;
        for (int j = 0; j < numServices; j++) {
            int service;
            do {
                service = random.nextInt(Service.NUM_SERVICES);
            } while (service == excludedService);
            serviceType |= 1 << service;
        }
        return serviceType;
    }

    private void printBoxedOutput(String[] messages) {
        int longestString = 0;
        for (String message : messages) {
            if (message.length() > longestString) {
                longestString = message.length();
            }
        }
        String horizontalBorder = "+" + "-".repeat(longestString + 2) + "+";

        System.out.println(horizontalBorder);
        for (String message : messages) {
            System.out.printf("| %-" + longestString + "s |\n", message);
        }
        System.out.println(horizontalBorder);
    }

    @Test
    void addPlaceWithinBounds() {
        // Test setup
        String testName = "Add Place Within Bounds";
        long startTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        // Test execution
        int x = 500;
        int y = 500;
        String[] services = new String[]{"Restaurant"};
        int encodedServices = Service.encodeService(services);
        map2D.addPlace(x, y, encodedServices);
        ArrayList<Place> results = map2D.searchPlace(x, y, 0, services, 1);

        // Test validation
        long endTime = System.nanoTime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = usedMemoryAfter - usedMemoryBefore;
        double runTime = (endTime - startTime) / 1_000_000.0; // Runtime in milliseconds

        int expectedSize = 1;
        int actualSize = results.size();
        Assertions.assertEquals(expectedSize, actualSize, "Should contain the added place");

        String[] outputMessages = {
                "Test Name: " + testName,
                "Input: x=" + x + ", y=" + y + ", services=" + String.join(", ", services),
                "Expected Output: size=" + expectedSize,
                "Actual Output: size=" + actualSize,
                "Run Time: " + runTime + " ms",
                "Memory Used: " + memoryUsed + " bytes",
                "Status: " + (expectedSize == actualSize ? "Passed" : "Failed")
        };
        printBoxedOutput(outputMessages);
    }

    @Test
    void addPlaceOutOfBounds() {
        // Test setup
        String testName = "Add Place Out of Bounds";
        long startTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        // Test execution
        int x = 11_000_000;
        int y = 11_000_000;
        String[] services = new String[]{"Restaurant"};
        int encodedServices = Service.encodeService(services);

        // Test validation
        long endTime = System.nanoTime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = usedMemoryAfter - usedMemoryBefore;
        double runTime = (endTime - startTime) / 1_000_000.0; // Runtime in milliseconds

        Exception expectedException = new IllegalArgumentException();
        Exception actualException = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            map2D.addPlace(x, y, encodedServices);
        }, "Should throw IllegalArgumentException for out-of-bounds coordinates");

        String[] outputMessages = {
                "Test Name: " + testName,
                "Input: x=" + x + ", y=" + y + ", services=" + String.join(", ", services),
                "Expected Exception: " + expectedException.getClass().getSimpleName(),
                "Actual Exception: " + actualException.getClass().getSimpleName(),
                "Run Time: " + runTime + " ms",
                "Memory Used: " + memoryUsed + " bytes",
                "Status: " + (expectedException.getClass().equals(actualException.getClass()) ? "Passed" : "Failed")
        };
        printBoxedOutput(outputMessages);
    }

    @Test
    void removeExistingPlace() {
        String testName = "Remove Existing Place";
        int x = 250;
        int y = 250;
        String[] services = new String[]{"Pharmacy"};
        map2D.addPlace(x, y, Service.encodeService(services));

        boolean removed = map2D.removePlace(x, y);
        ArrayList<Place> results = map2D.searchPlace(x, y, 0, services, 1);
        int expectedResultsSize = 0;
        int actualResultsSize = results.size();

        Assertions.assertTrue(removed, "Should successfully remove the place");
        Assertions.assertEquals(expectedResultsSize, actualResultsSize, "Should not return any places after removal");

        String[] outputMessages = {
                "Test Name: " + testName,
                "Input: x=" + x + ", y=" + y + ", services=" + String.join(", ", services),
                "Expected Outcome: Removal successful, size=" + expectedResultsSize,
                "Actual Outcome: Removal status=" + removed + ", size=" + actualResultsSize,
                "Status: " + (removed && expectedResultsSize == actualResultsSize ? "Passed" : "Failed")
        };
        printBoxedOutput(outputMessages);
    }

    @Test
    void removeNonExistingPlace() {
        String testName = "Remove Non-Existing Place";
        int x = 250;
        int y = 250;
        String[] services = new String[]{"Pharmacy"};
        map2D.addPlace(x, y, Service.encodeService(services));

        boolean removed = map2D.removePlace(x + 1, y + 1);
        ArrayList<Place> results = map2D.searchPlace(x, y, 0, services, 1);
        boolean isPlaceStillThere = !results.isEmpty();

        Assertions.assertFalse(removed, "Should not remove a place that does not exist");
        Assertions.assertTrue(isPlaceStillThere, "Should return the place that was not removed");

        String[] outputMessages = {
                "Test Name: " + testName,
                "Input: x=" + (x+1) + ", y=" + (y+1) + ", services=" + String.join(", ", services),
                "Expected Outcome: Removal failed, place still exists",
                "Actual Outcome: Removal status=" + removed + ", place still exists=" + isPlaceStillThere,
                "Status: " + (!removed && isPlaceStillThere ? "Passed" : "Failed")
        };
        printBoxedOutput(outputMessages);
    }

    @Test
    void removePropagation() {
        String testName = "Remove Propagation";
        int x1 = 3, y1 = 3, x2 = 4, y2 = 4;
        String[] services1 = {"Pharmacy"}, services2 = {"Restaurant"};
        map2D.addPlace(x1, y1, Service.encodeService(services1));
        map2D.addPlace(x2, y2, Service.encodeService(services2));

        boolean removed = map2D.removePlace(x1, y1);
        ArrayList<Place> results1 = map2D.searchPlace(x1, y1, 0, services1, 1);
        ArrayList<Place> results2 = map2D.searchPlace(x2, y2, 0, services2, 1);

        Assertions.assertTrue(removed, "Should successfully remove the first place");
        Assertions.assertTrue(results1.isEmpty(), "The first place should no longer be found in the map");
        Assertions.assertFalse(results2.isEmpty(), "The second place should still exist and remain unaffected");
        Assertions.assertEquals(1, results2.size(), "There should be exactly one entry for the second place.");

        String[] outputMessages = {
                "Test Name: " + testName,
                "Input: x1=" + x1 + ", y1=" + y1 + ", services1=" + String.join(", ", services1),
                "Input2: x2=" + x2 + ", y2=" + y2 + ", services2=" + String.join(", ", services2),
                "Expected Outcome: First place removed, second place unaffected",
                "Actual Outcome: First place status=" + removed + ", first place size=" + results1.size() + ", second place size=" + results2.size(),
                "Status: " + (removed && results1.isEmpty() && results2.size() == 1 ? "Passed" : "Failed")
        };
        printBoxedOutput(outputMessages);
    }

    @Test
    void editExistingPlace() {
        String testName = "Edit Existing Place";
        int x = 9;
        int y = 9;
        String[] initialServices = {"Pharmacy"};
        map2D.addPlace(x, y, Service.encodeService(initialServices));

        String[] newServices = {"Hospital", "Pharmacy"};
        boolean edited = map2D.editPlace(x, y, newServices);
        ArrayList<Place> results = map2D.searchPlace(x, y, 0, newServices, 1);

        Assertions.assertTrue(edited, "Should successfully edit the place");
        Assertions.assertFalse(results.isEmpty(), "The place should list the new services");

        String[] outputMessages = {
                "Test Name: " + testName,
                "Input: x=" + x + ", y=" + y + ", initial services=" + String.join(", ", initialServices),
                "Edited Services: " + String.join(", ", newServices),
                "Expected Outcome: Place edited successfully, results not empty",
                "Actual Outcome: Edit status=" + edited + ", result size=" + results.size(),
                "Status: " + (edited && !results.isEmpty() ? "Passed" : "Failed")
        };
        printBoxedOutput(outputMessages);
    }

    @Test
    void editNonExistingPlace() {
        String testName = "Edit Non-Existing Place";
        int x = 750;
        int y = 750;
        String[] newServices = {"Hospital", "Pharmacy"};

        boolean edited = map2D.editPlace(x, y, newServices);
        ArrayList<Place> results = map2D.searchPlace(x, y, 0, newServices, 1);

        Assertions.assertFalse(edited, "Should not edit a place that does not exist");
        Assertions.assertTrue(results.isEmpty(), "Should not return any places after an unsuccessful edit");

        String[] outputMessages = {
                "Test Name: " + testName,
                "Input: x=" + x + ", y=" + y + ", attempted services=" + String.join(", ", newServices),
                "Expected Outcome: Edit failed, no results",
                "Actual Outcome: Edit status=" + edited + ", result size=" + results.size(),
                "Status: " + (!edited && results.isEmpty() ? "Passed" : "Failed")
        };
        printBoxedOutput(outputMessages);
    }

    @Test
    void editPropagation() {
        String testName = "Edit Propagation";
        int x1 = 1, y1 = 1, x2 = 2, y2 = 2;
        String[] initialServices = {"Library"}, services2 = {"Restaurant"}, newServices = {"Hospital", "Pharmacy"};
        map2D.addPlace(x1, y1, Service.encodeService(initialServices));
        map2D.addPlace(x2, y2, Service.encodeService(services2));

        boolean edited = map2D.editPlace(x1, y1, newServices);
        ArrayList<Place> results1 = map2D.searchPlace(x1, y1, 0, newServices, 1);
        ArrayList<Place> results2 = map2D.searchPlace(x2, y2, 0, services2, 1);

        Assertions.assertTrue(edited, "Should successfully edit the first place");
        Assertions.assertFalse(results1.isEmpty(), "The first place should now list the updated services");
        Assertions.assertFalse(results2.isEmpty(), "The second place should remain unaffected");

        String[] outputMessages = {
                "Test Name: " + testName,
                "Input1: x=" + x1 + ", y=" + y1 + ", initial services=" + String.join(", ", initialServices),
                "Input2: x=" + x2 + ", y=" + y2 + ", services2=" + String.join(", ", services2),
                "Edited Services: " + String.join(", ", newServices),
                "Expected Outcome: First place edited, second place unaffected",
                "Actual Outcome: First place edit status=" + edited + ", first place size=" + results1.size() + ", second place size=" + results2.size(),
                "Status: " + (edited && !results1.isEmpty() && !results2.isEmpty() ? "Passed" : "Failed")
        };
        printBoxedOutput(outputMessages);
    }

    @Test
    void basicSearch() {
        // Test Setup
        String testName = "Basic Search";
        boolean passed = true;
        long startTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        int userX = 5_000_000;
        int userY = 5_000_000;
        int walkDistance = 1000;
        String[] services = {"Restaurant"};
        int searchService = Service.encodeService(services);
        ArrayList<Place> results = map2D.searchPlace(userX, userY, walkDistance, services, 50);

        try {
            for (Place place : results) {
                Assertions.assertTrue((place.getServices() & searchService) != 0, "Place should offer at least one of the specified services");
            }
            Assertions.assertTrue(results.size() <= 50, "Should not return more than 50 places");
        } catch (AssertionError e) {
            passed = false;
        }

        // Test Validation
        long endTime = System.nanoTime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = usedMemoryAfter - usedMemoryBefore;
        double runTime = (endTime - startTime) / 1_000_000.0; // Runtime in milliseconds

        // Display results with test metadata
        displayPlaceList(results, userX, userY, services, testName,
                "User position: (" + userX + ", " + userY + "), Walk distance: " + walkDistance + ", Services: Restaurant, Max results: 50",
                "At most 50 places offering at least one specified service",
                "Returned " + results.size() + " places",
                (long) runTime, // Example runtime in ms
                memoryUsed / 1024, // Convert bytes to kilobytes
                passed ? "Passed" : "Failed");
    }

    @Test
    void boundarySearch() {
        // Test Setup
        String testName = "Boundary Search";
        boolean passed = true;
        long startTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        int userX = 5_000_000;
        int userY = 5_000_000;
        int walkDistance = 100;
        String[] services = {"Restaurant"};
        int searchService = Service.encodeService(services);
        ArrayList<Place> results = map2D.searchPlace(userX, userY, walkDistance, services, 50);

        try {
            for (Place place : results) {
                Assertions.assertTrue((place.getServices() & searchService) != 0, "Place should offer at least one of the specified services");
            }
            Assertions.assertTrue(results.size() <= 50, "Should not return more than 50 places");
        } catch (AssertionError e) {
            passed = false;
        }

        // Test Validation
        long endTime = System.nanoTime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = usedMemoryAfter - usedMemoryBefore;
        double runTime = (endTime - startTime) / 1_000_000.0; // Runtime in milliseconds

        // Display results with test metadata
        displayPlaceList(results, userX, userY, services, testName,
                "User position: (" + userX + ", " + userY + "), Walk distance: " + walkDistance + ", Services: Restaurant, Max results: 50",
                "At most 50 places offering at least one specified service",
                "Returned " + results.size() + " places",
                (long) runTime, // Runtime in ms
                memoryUsed / 1024, // Memory usage in KB (convert from bytes)
                passed ? "Passed" : "Failed");
    }

    @Test
    void emptySearch() {
        // Test Setup
        String testName = "Empty Search";
        boolean passed = true;
        long startTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        int userX = 250;
        int userY = 250;
        int walkDistance = 100;
        String[] services = {"ConvenienceStore"};
        ArrayList<Place> results = map2D.searchPlace(userX, userY, walkDistance, services, 50);

        try {
            Assertions.assertTrue(results.isEmpty(), "Should not return any places for the specified service");
        } catch (AssertionError e) {
            passed = false;
        }

        // Test Validation
        long endTime = System.nanoTime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = usedMemoryAfter - usedMemoryBefore;
        double runTime = (endTime - startTime) / 1_000_000.0; // Runtime in milliseconds

        System.out.printf("Test '%s' - Status: %s, Runtime: %.2f ms, Memory Usage: %d KB%n",
                testName, passed ? "Passed" : "Failed", runTime, memoryUsed / 1024);
    }

    @Test
    void searchWith100x100Boundary() {
        // Test Setup
        String testName = "Search with 100x100 Boundary";
        boolean passed = true;
        long startTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        int userX = 5_000_000;
        int userY = 5_000_000;
        int walkDistance = 100;

        String[] services = {"Hospital", "School"};
        int encodedServices = Service.encodeService(services);
        Place predefinedPlace = new Place(userX - walkDistance + 10, userY - walkDistance + 10,encodedServices);
        map2D.addPlace(predefinedPlace.getX(), predefinedPlace.getY(), encodedServices);

        ArrayList<Place> results = map2D.searchPlace(userX, userY, walkDistance, services, 50);

        boolean isFoundPredefined = false;
        try {
            for (Place place : results) {
                if (place.getX() == predefinedPlace.getX() && place.getY() == predefinedPlace.getY()) {
                    isFoundPredefined = true;
                }
                Assertions.assertTrue((place.getServices() & Service.encodeService(services)) != 0, "Place should offer at least one of the specified services");
            }
            Assertions.assertTrue(isFoundPredefined, "Should include the predefine place in the result");
            Assertions.assertTrue(results.size() <= 50, "Should not return more than 50 places");
        } catch (AssertionError e) {
            passed = false;
        }

        // Test Validation
        long endTime = System.nanoTime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = usedMemoryAfter - usedMemoryBefore;
        double runTime = (endTime - startTime) / 1_000_000.0; // Runtime in milliseconds

        displayPlaceList(results, userX, userY, services, testName,
                "User position: (" + userX + ", " + userY + "), Walk distance: " + walkDistance + ", Services: Restaurant, Max results: 50",
                "At most 50 places offering at least one specified service and including the predefined place",
                "Returned " + results.size() + " places",
                (long) runTime, // Runtime in ms
                memoryUsed / 1024, // Memory usage in KB (convert from bytes)
                passed ? "Passed" : "Failed");
    }

    @Test
    void searchWith1000x1000Boundary() {
        // Test Setup
        String testName = "Search with 1.000x1.000 Boundary";
        boolean passed = true;
        long startTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        int userX = 5_000_000;
        int userY = 5_000_000;
        int walkDistance = 1000;
        String[] services = {"Hospital", "School"};
        int encodedServices = Service.encodeService(services);
        Place predefinedPlace = new Place(userX - walkDistance + 10, userY - walkDistance + 10,encodedServices);
        map2D.addPlace(predefinedPlace.getX(), predefinedPlace.getY(), encodedServices);

        ArrayList<Place> results = map2D.searchPlace(userX, userY, walkDistance, services, 50);

        boolean isFoundPredefined = false;
        try {
            for (Place place : results) {
                if (place.getX() == predefinedPlace.getX() && place.getY() == predefinedPlace.getY()) {
                    isFoundPredefined = true;
                }
                Assertions.assertTrue((place.getServices() & Service.encodeService(services)) != 0, "Place should offer at least one of the specified services");
            }
            Assertions.assertTrue(isFoundPredefined, "Should include the predefine place in the result");
            Assertions.assertTrue(results.size() <= 50, "Should not return more than 50 places");
        } catch (AssertionError e) {
            passed = false;
        }

        // Test Validation
        long endTime = System.nanoTime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = usedMemoryAfter - usedMemoryBefore;
        double runTime = (endTime - startTime) / 1_000_000.0; // Runtime in milliseconds

        displayPlaceList(results, userX, userY, services, testName,
                "User position: (" + userX + ", " + userY + "), Walk distance: " + walkDistance + ", Services: Restaurant, Max results: 50",
                "At most 50 places offering at least one specified service and including the predefined place",
                "Returned " + results.size() + " places",
                (long) runTime, // Runtime in ms
                memoryUsed / 1024, // Memory usage in KB (convert from bytes)
                passed ? "Passed" : "Failed");
    }


    @Test
    void searchWith10000x10000Boundary() {
        // Test Setup
        String testName = "Search with 10.000x10.000 Boundary";
        boolean passed = true;
        long startTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        int userX = 5_000_000;
        int userY = 5_000_000;
        int walkDistance = 10_000;
        String[] services = {"Hospital", "School"};
        int encodedServices = Service.encodeService(services);
        Place predefinedPlace = new Place(userX, userY,encodedServices);
        System.out.println(predefinedPlace.getX() + " " + predefinedPlace.getY());
        map2D.addPlace(predefinedPlace.getX(), predefinedPlace.getY(), encodedServices);

        ArrayList<Place> results = map2D.searchPlace(userX, userY, walkDistance, services, 50);

        boolean isFoundPredefined = false;
        try {
            for (Place place : results) {
                if (place.getX() == predefinedPlace.getX() && place.getY() == predefinedPlace.getY()) {
                    isFoundPredefined = true;
                }
                Assertions.assertTrue((place.getServices() & Service.encodeService(services)) != 0, "Place should offer at least one of the specified services");
            }
            Assertions.assertTrue(isFoundPredefined, "Should include the predefine place in the result");
            Assertions.assertTrue(results.size() <= 50, "Should not return more than 50 places");
        } catch (AssertionError e) {
            passed = false;
        }

        // Test Validation
        long endTime = System.nanoTime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = usedMemoryAfter - usedMemoryBefore;
        double runTime = (endTime - startTime) / 1_000_000.0; // Runtime in milliseconds

        displayPlaceList(results, userX, userY, services, testName,
                "User position: (" + userX + ", " + userY + "), Walk distance: " + walkDistance + ", Services: Restaurant, Max results: 50",
                "At most 50 places offering at least one specified service and including the predefined place",
                "Returned " + results.size() + " places",
                (long) runTime, // Runtime in ms
                memoryUsed / 1024, // Memory usage in KB (convert from bytes)
                passed ? "Passed" : "Failed");
    }

    @Test
    void searchWithOutOfMap() {
        // Test Setup
        String testName = "Search With Out of Map Coordinates";
        boolean passed = true;
        long startTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        int userX = 11_000_000;
        int userY = 11_000_000;
        int walkDistance = 100;
        String[] services = {"Restaurant", "School"};

        ArrayList<Place> results = map2D.searchPlace(userX, userY, walkDistance, services, 50);

        try {
            Assertions.assertEquals(0, results.size(), "Should not return any places for out-of-map coordinates");
        } catch (AssertionError e) {
            passed = false;
        }

        // Test Validation
        long endTime = System.nanoTime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = usedMemoryAfter - usedMemoryBefore;
        double runTime = (endTime - startTime) / 1_000_000.0; // Runtime in milliseconds

        displayPlaceList(results, userX, userY, services, testName,
                "User position: (" + userX + ", " + userY + "), Walk distance: " + walkDistance + ", Services: Restaurant, Max results: 50",
                "Nothing should return since the input is out of the map",
                "Returned " + results.size() + " places",
                (long) runTime, // Runtime in ms
                memoryUsed / 1024, // Memory usage in KB (convert from bytes)
                passed ? "Passed" : "Failed");
    }

    @Test
    void searchPerformance() {
        // Test Setup
        String testName = "Search Performance";
        boolean passed = true;
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        int userX = 5_000_000;
        int userY = 5_000_000;
        int walkDistance = 5000;
        String[] services = {"Restaurant", "Hospital"};
        long startTime = System.nanoTime();

        ArrayList<Place> results = map2D.searchPlace(userX, userY, walkDistance, services, 50);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000; // Duration in milliseconds
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = usedMemoryAfter - usedMemoryBefore;

        try {
            Assertions.assertTrue(duration < 200, "Search should complete within 200 milliseconds");
            Assertions.assertFalse(results.isEmpty(), "Should return at least one result");
            Assertions.assertTrue(results.size() <= 50, "Should not return more than 50 places");
        } catch (AssertionError e) {
            passed = false;
        }

        String[] outputMessages = {
                "Test Name: " + testName,
                "Input: x=" + userX + ", y=" + userY + ", services=" + String.join(", ", services),
                "Expected Output: duration= lower than " + 200 + "ms",
                "Actual Output: duration=" + duration + "ms",
                "Run Time: " + duration + " ms",
                "Memory Used: " + memoryUsed / 1000 + " KB",
                "Status: " + (passed ? "Passed" : "Failed")
        };
        printBoxedOutput(outputMessages);
    }

    public void displayPlaceList(ArrayList<Place> places, int userX, int userY, String[] searchedServices,
                                 String testName, String inputs, String expectedOutcome, String actualOutcome,
                                 long runtime, long memoryUsage, String status) {
        int metadataWidth = 144; // Total width of the table.

        // Display Headers
        System.out.println("_".repeat(metadataWidth));
        System.out.printf("| %-140s |%n", "Test Metadata");
        System.out.println("|" + "-".repeat(metadataWidth - 2) + "|");

        // Dynamically adjust the width for each metadata line
        String testLine = String.format("Test Name: %s", testName);
        String inputLine = String.format("Inputs: %s", inputs);
        String expectedLine = String.format("Expected Outcome: %s", expectedOutcome);
        String actualLine = String.format("Actual Outcome: %s", actualOutcome);
        String runtimeLine = String.format("Runtime: %s ms", runtime);
        String memoryLine = String.format("Memory Usage: %s KB", memoryUsage);
        String statusLine = String.format("Status: %s", status);

        System.out.printf("| %-140s |%n", testLine);
        System.out.printf("| %-140s |%n", inputLine);
        System.out.printf("| %-140s |%n", expectedLine);
        System.out.printf("| %-140s |%n", actualLine);
        System.out.printf("| %-140s |%n", runtimeLine);
        System.out.printf("| %-140s |%n", memoryLine);
        System.out.printf("| %-140s |%n", statusLine);
        System.out.println("|" + "_".repeat(metadataWidth - 2) + "|");

        int placeColumnWidth = 5; // Width of the Place column
        int distanceColumnWidth = 16; // Width of the Distance(units) column
        // Display Headers
        System.out.println("_".repeat(140));
        System.out.println("| " + String.format("%" + placeColumnWidth + "s", "Place") + " | " + String.format("%-24s", "Coordinates") + " | " + String.format("%" + distanceColumnWidth + "s", "Distance(units)") + " | " + String.format("%-30s", "Matched Services") + " | " + String.format("%-50s", "Other Services") + " |");
        System.out.println("|" + "-".repeat(placeColumnWidth + 2) + "|" + "-".repeat(26) + "|" + "-".repeat(distanceColumnWidth + 2) + "|" + "-".repeat(32) + "|" + "-".repeat(52) + "|");
        // Split Services into Matched Services and Other Services.
        for (int i = 0; i < places.size(); i++) {
            Place place = places.get(i);
            String coordinates = "(x: " + place.getX() + ", y: " + place.getY() + ")";
            String distance = String.format("%.2f", place.distanceTo(userX, userY));
            String allServices = String.join(", ", Service.decodeService(place.getServices()));
            String[] services = allServices.split(", ");
            String[] matchedServices = new String[searchedServices.length];
            String[] otherServices = new String[services.length];

            Arrays.fill(matchedServices, "");
            Arrays.fill(otherServices, "");

            for (String service : services) {
                boolean matched = false;
                for (String searchedService : searchedServices) {
                    if (service.equals(searchedService)) {
                        matched = true;
                        break;
                    }
                }
                if (matched) {
                    for (int j = 0; j < matchedServices.length; j++) {
                        if (matchedServices[j].isEmpty()) {
                            matchedServices[j] = service;
                            break;
                        }
                    }
                } else {
                    for (int j = 0; j < otherServices.length; j++) {
                        if (otherServices[j].isEmpty()) {
                            otherServices[j] = service;
                            break;
                        }
                    }
                }
            }

            matchedServices = Arrays.stream(matchedServices).filter(s -> !s.isEmpty()).toArray(String[]::new);
            otherServices = Arrays.stream(otherServices).filter(s -> !s.isEmpty()).toArray(String[]::new);

            String matchedServicesStr = String.join(", ", matchedServices);
            String otherServicesStr = String.join(", ", otherServices);

            String placeNumber = String.format("%" + placeColumnWidth + "d", i + 1);
            // Display Place
            System.out.println("| " + placeNumber + " | " + String.format("%-24s", coordinates) + " | " + String.format("%" + distanceColumnWidth + "s", distance) + " | " + String.format("%-30s", matchedServicesStr) + " | " + String.format("%-50s", otherServicesStr) + " |");
        }
        // Display Footer
        System.out.println("|" + "_".repeat(placeColumnWidth + 2) + "|" + "_".repeat(26) + "|" + "_".repeat(distanceColumnWidth + 2) + "|" + "_".repeat(32) + "|" + "_".repeat(52) + "|");
    }
}
