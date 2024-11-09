package store.model.setup;

import java.util.Objects;

public class ProductName {
    private final String name;

    private ProductName(String name) {
        this.name = name;
    }

    public static ProductName of(String name) {
        return new ProductName(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductName that = (ProductName) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }
}
