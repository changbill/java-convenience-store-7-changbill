package store.model.dto;

import java.util.Objects;

public record ProductOrderDto(String productName, long quantity) {

    public static ProductOrderDto of(String productName, long quantity) {
        return new ProductOrderDto(productName, quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductOrderDto that = (ProductOrderDto) o;
        return quantity == that.quantity && Objects.equals(productName, that.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, quantity);
    }
}
