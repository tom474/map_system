package development;

public enum Service {
    ATM,
    Restaurant,
    Hospital,
    GasStation,
    CoffeeShop,
    GroceryStore,
    Pharmacy,
    Hotel,
    Bank,
    Bookstore;

    public static int size() {
        return Service.values().length;
    }
}
