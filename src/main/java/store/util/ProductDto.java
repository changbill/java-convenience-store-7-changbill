package store.util;

import store.model.administrator.ProductInformation;
import store.model.administrator.ProductNameAndPrice;

public record ProductDto(ProductNameAndPrice productNameAndPrice, ProductInformation productInformation) {

    public static ProductDto of(ProductNameAndPrice productNameAndPrice, ProductInformation productInformation) {
        return new ProductDto(productNameAndPrice, productInformation);
    }
}
