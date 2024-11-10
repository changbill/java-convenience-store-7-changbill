package store.model.dto;

import store.model.ProductInformation;

public record ProductDto(String productName, ProductInformation productInformation) {

    public static ProductDto of(String productName, ProductInformation productInformation) {
        return new ProductDto(productName, productInformation);
    }
}
