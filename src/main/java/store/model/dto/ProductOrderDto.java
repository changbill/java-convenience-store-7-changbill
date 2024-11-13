package store.model.dto;

public record ProductOrderDto(String productName, long quantity) {

    public static ProductOrderDto of(String productName, long quantity) {
        return new ProductOrderDto(productName, quantity);
    }
}
