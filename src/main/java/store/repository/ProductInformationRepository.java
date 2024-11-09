package store.repository;

import static store.exception.InputExceptionMessage.INPUT_MORE_THEN_STOCK_EXCEPTION;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import store.exception.WrongInputException;
import store.model.dto.ProductOrderDto;
import store.model.dto.purchaseResponse.PurchaseResponse;
import store.model.dto.purchaseResponse.PurchaseSuccessResponse;
import store.model.stock.GeneralStock;
import store.model.ProductInformation;
import store.model.stock.PromotionStock;
import store.model.stock.Stock;
import store.model.PromotionStockCalculation;
import store.model.dto.ProductDto;

public class ProductInformationRepository {
    // TODO: ProductName String 타입으로 변경
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
        if(stock.getClass() == GeneralStock.class) {
            return storeProductInformation(productName, price, (GeneralStock) stock);
        }
        return storeProductInformation(productName, price, (PromotionStock) stock);
    }

    private ProductInformation findProductInformation(String productName) {
        if (productInformationMap.containsKey(productName)) {
            return productInformationMap.get(productName);
        }
        return null;
    }

    private ProductInformation storeProductInformation(String productName, long price, PromotionStock promotionStock) {
        if(productInformationMap.containsKey(productName)) {
            return productInformationMap.get(productName).addProductInformation(promotionStock);
        }
        ProductInformation productInformation =
                new ProductInformation(price, promotionStock);
        return productInformationMap.put(productName, productInformation);
    }

    private ProductInformation storeProductInformation(String productName, long price, GeneralStock generalStock) {
        if(productInformationMap.containsKey(productName)) {
            return productInformationMap.get(productName).addProductInformation(generalStock);
        }
        ProductInformation productInformation =
                new ProductInformation(price, generalStock);
        return productInformationMap.put(productName, productInformation);
    }

    public List<PurchaseResponse> calculateProductOrders(List<ProductOrderDto> productOrderDtos) {
        List<PurchaseResponse> purchaseResponses = new ArrayList<>();
        for(ProductOrderDto productOrderDto : productOrderDtos) {
            ProductInformation productInformation = productInformationMap.get(productOrderDto.productName());
            if(productInformation.getPromotionStock() != null) {
                purchaseResponses.add(
                        PromotionStockCalculation.of(productInformation, productOrderDto)
                                .responseCalculation()
                );
            }

            long generalStockQuantity = productInformation.getGeneralStock().getQuantity();
            long orderQuantity = productOrderDto.quantity();
            if(generalStockQuantity < orderQuantity) {
                throw new WrongInputException(INPUT_MORE_THEN_STOCK_EXCEPTION);
            }

            purchaseResponses.add(PurchaseSuccessResponse.of(productOrderDto));
        }

        return purchaseResponses;
    }
}
