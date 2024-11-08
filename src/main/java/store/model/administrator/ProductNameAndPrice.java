package store.model.administrator;

import java.util.Objects;

public class ProductNameAndPrice {
    private final String name;
    private final long price;

    private ProductNameAndPrice(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public static ProductNameAndPrice of(String name, long price) {
        return new ProductNameAndPrice(name, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductNameAndPrice that = (ProductNameAndPrice) o;
        return price == that.price && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
