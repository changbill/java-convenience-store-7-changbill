package store.model.dto;

import store.model.ProductInformation;
import store.model.setup.ProductName;

public record ProductDto(ProductName productName, ProductInformation productInformation) {

    public static ProductDto of(ProductName productName, ProductInformation productInformation) {
        return new ProductDto(productName, productInformation);
    }
}
