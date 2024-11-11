package store;

import static store.view.FileLocation.*;
import static store.view.FileLocation.PROMOTIONS;

public class Application {
    public static void main(String[] args) {
        final ConvenienceStore convenienceStore = new ConvenienceStore();
        convenienceStore.run(PROMOTIONS.getLocation(), PRODUCTS.getLocation());
    }
}
