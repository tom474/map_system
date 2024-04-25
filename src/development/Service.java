package development;

public class Service {
    public static final int NUM_SERVICES = 10;
    public static final int ATM = 0;
    public static final int Restaurant = 1;
    public static final int Hospital = 2;
    public static final int Park = 3;
    public static final int ShoppingMall = 4;
    public static final int BusStation = 5;
    public static final int Library = 6;
    public static final int Pharmacy = 7;
    public static final int School = 8;
    public static final int ConvenienceStore = 9;

    public static int encodeService(String[] services) {
        return 1 << services.length - 1;
    }

    public static String[] decodeService(int encoded) {
        String[] result = new String[Integer.bitCount(encoded)];
        int index = 0;
        for (int i = 0; i < NUM_SERVICES; i++) {
            if ((encoded & (1 << i)) != 0) {
                result[index++] = switch (i) {
                    case ATM -> "ATM";
                    case Restaurant -> "Restaurant";
                    case Hospital -> "Hospital";
                    case Park -> "Park";
                    case ShoppingMall -> "ShoppingMall";
                    case BusStation -> "BusStation";
                    case Library -> "Library";
                    case Pharmacy -> "Pharmacy";
                    case School -> "School";
                    case ConvenienceStore -> "ConvenienceStore";
                    default -> "Unknown";
                };
            }
        }
        return result;
    }

    public static boolean contains(int services1, int services2) {
        return (services1 & services2) == services2;
    }
}
