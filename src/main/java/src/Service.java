package src;

/**
 * Represents services available at a place.
 */
public class Service {
    public static final int NUM_SERVICES = 10;
    public static final int ATM = 0;
    public static final int RESTAURANT = 1;
    public static final int HOSPITAL = 2;
    public static final int PARK = 3;
    public static final int SHOPPING_MALL = 4;
    public static final int BUS_STATION = 5;
    public static final int LIBRARY = 6;
    public static final int PHARMACY = 7;
    public static final int SCHOOL = 8;
    public static final int CONVENIENCE_STORE = 9;

    /**
     * Encodes an array of services into an integer representation.
     *
     * @param services The array of services to encode.
     * @return The encoded integer representing the services.
     */
    public static int encodeService(String[] services) {
        int encoded = 0;
        for (String service : services) {
            switch (service) {
                case "ATM" -> encoded |= 1 << ATM;
                case "Restaurant" -> encoded |= 1 << RESTAURANT;
                case "Hospital" -> encoded |= 1 << HOSPITAL;
                case "Park" -> encoded |= 1 << PARK;
                case "ShoppingMall" -> encoded |= 1 << SHOPPING_MALL;
                case "BusStation" -> encoded |= 1 << BUS_STATION;
                case "Library" -> encoded |= 1 << LIBRARY;
                case "Pharmacy" -> encoded |= 1 << PHARMACY;
                case "School" -> encoded |= 1 << SCHOOL;
                case "ConvenienceStore" -> encoded |= 1 << CONVENIENCE_STORE;
                default -> throw new IllegalArgumentException("Unknown service: " + service);
            }
        }
        return encoded;
    }

    /**
     * Decodes an integer representing services into an array of service names.
     *
     * @param encoded The encoded integer representing the services.
     * @return An array of service names.
     */
    public static String[] decodeService(int encoded) {
        String[] result = new String[Integer.bitCount(encoded)];
        int index = 0;
        for (int i = 0; i < NUM_SERVICES; i++) {
            if ((encoded & (1 << i)) != 0) {
                result[index++] = switch (i) {
                    case ATM -> "ATM";
                    case RESTAURANT -> "Restaurant";
                    case HOSPITAL -> "Hospital";
                    case PARK -> "Park";
                    case SHOPPING_MALL -> "ShoppingMall";
                    case BUS_STATION -> "BusStation";
                    case LIBRARY -> "Library";
                    case PHARMACY -> "Pharmacy";
                    case SCHOOL -> "School";
                    case CONVENIENCE_STORE -> "ConvenienceStore";
                    default -> "Unknown";
                };
            }
        }
        return result;
    }

    /**
     * Checks if services2 are contained within services1.
     *
     * @param services1 The services to check against.
     * @param services2 The services to check for containment.
     * @return True if services2 are contained within services1, false otherwise.
     */
    public static boolean contains(int services1, int services2) {
        return (services1 & services2) == services2;
    }
}
