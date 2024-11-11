package store.repository;

import static store.exception.InputExceptionMessage.INPUT_PRODUCT_WRONG_NAME_EXCEPTION;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import store.exception.InputException;
import store.model.dto.ProductOrderDto;
import store.model.dto.ReceiptDto;
import store.model.dto.orderCalculationResponse.OrderCalculationResponse;
import store.model.stock.GeneralStock;
import store.model.ProductInformation;
import store.model.stock.PromotionStock;
import store.model.stock.Stock;
import store.model.OrderCalculation;
import store.model.dto.ProductDto;

public class ProductInformationRepository {
    private final LinkedHashMap<String, ProductInformation> productInformationMap =
            new LinkedHashMap<>();

    public List<ProductDto> findAllProducts() {
        List<ProductDto> productDtos = new ArrayList<>();
        productInformationMap.forEach(((productName, productInformation) ->
                productDtos.add(ProductDto.of(productName, productInformation))
        ));
        return productDtos;
    }

    public ProductInformation addProductInformation(
            String productName,
            long price,
            Stock stock
    ) {
        if (stock.getClass() == GeneralStock.class) {
            return storeProductInformation(productName, price, (GeneralStock) stock);
        }
        return storeProductInformation(productName, price, (PromotionStock) stock);
    }

    public List<OrderCalculationResponse> calculateProductOrders(List<ProductOrderDto> orders) {
        List<OrderCalculationResponse> orderCalculationResponses = new ArrayList<>();
        for (ProductOrderDto order : orders) {
            ProductInformation productInformation = findProductInformation(order.productName());

            orderCalculationResponses.add(
                    OrderCalculation.of(productInformation, order).responseOrderCalculation()
            );
        }

        return orderCalculationResponses;
    }

    public void putProductOrders(List<ProductOrderDto> orders) {
        for (ProductOrderDto order : orders) {
            ProductInformation productInformation = findProductInformation(order.productName());

            ProductInformation updatedProductInformation =
                    OrderCalculation.of(productInformation, order).updateProductInformation();

            productInformationMap.put(order.productName(), updatedProductInformation);
        }
    }

    private ProductInformation findProductInformation(String productName) {
        if (productInformationMap.containsKey(productName)) {
            return productInformationMap.get(productName);
        }
        throw new InputException(INPUT_PRODUCT_WRONG_NAME_EXCEPTION);
    }

    private ProductInformation storeProductInformation(String productName, long price, PromotionStock promotionStock) {
        if (productInformationMap.containsKey(productName)) {
            return productInformationMap.get(productName).addProductInformation(promotionStock);
        }
        ProductInformation productInformation =
                new ProductInformation(price, promotionStock);
        return productInformationMap.put(productName, productInformation);
    }

    private ProductInformation storeProductInformation(String productName, long price, GeneralStock generalStock) {
        if (productInformationMap.containsKey(productName)) {
            return productInformationMap.get(productName).addProductInformation(generalStock);
        }
        ProductInformation productInformation =
                new ProductInformation(price, generalStock);
        return productInformationMap.put(productName, productInformation);
    }

    public List<ReceiptDto> calculatePrice(List<ProductOrderDto> orders) {
        return orders.stream().map(
                order -> OrderCalculation.of(findProductInformation(order.productName()), order)
                        .getReceipt()
        ).toList();
    }
}
