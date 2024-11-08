package store.repository;

import static store.exception.PromotionsFileExceptionMessage.PROMOTIONS_FILE_SAME_NAME_DIFFERENT_PRICE_EXCEPTION;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import store.exception.WrongPromotionsFileException;
import store.model.administrator.GeneralStock;
import store.model.administrator.ProductInformation;
import store.model.administrator.ProductNameAndPrice;
import store.model.administrator.PromotionInformation;
import store.model.administrator.PromotionStock;
import store.model.administrator.Stock;
import store.util.ProductDto;

public class StoreRepository {
    private final LinkedHashMap<ProductNameAndPrice, ProductInformation> productInformationMap =
            new LinkedHashMap<>();
    private final HashMap<String, PromotionInformation> promotionMap = new HashMap<>();

    public List<ProductDto> findAllProducts() {
        List<ProductDto> productDtos = new ArrayList<>();
        productInformationMap.forEach(((productNameAndPrice, productInformation) ->
            productDtos.add(ProductDto.of(productNameAndPrice, productInformation))
        ));
        return productDtos;
    }

    public PromotionInformation findPromotionInformation(String rawPromotion) {
        return promotionMap.get(rawPromotion);
    }

    public PromotionInformation addPromotionInformation(String promotionName, PromotionInformation promotionInformation) {
        if(promotionMap.containsKey(promotionName)) {
            throw new WrongPromotionsFileException(PROMOTIONS_FILE_SAME_NAME_DIFFERENT_PRICE_EXCEPTION);
        }
        return promotionMap.put(promotionName, promotionInformation);
    }

    public ProductInformation addProductInformation(
            ProductNameAndPrice productNameAndPrice,
            Stock stock
    ) {
        if(stock.getClass() == GeneralStock.class) {
            return storeProductInformation(productNameAndPrice, (GeneralStock) stock);
        }
        return storeProductInformation(productNameAndPrice, (PromotionStock) stock);
    }

    private ProductInformation findProductInformation(ProductNameAndPrice productNameAndPrice) {
        if (productInformationMap.containsKey(productNameAndPrice)) {
            return productInformationMap.get(productNameAndPrice);
        }
        return null;
    }

    private ProductInformation storeProductInformation(ProductNameAndPrice productNameAndPrice, PromotionStock promotionStock) {
        if(productInformationMap.containsKey(productNameAndPrice)) {
            return productInformationMap.get(productNameAndPrice).addProductInformation(promotionStock);
        }
        ProductInformation productInformation =
                new ProductInformation(promotionStock);
        return productInformationMap.put(productNameAndPrice, productInformation);
    }

    private ProductInformation storeProductInformation(ProductNameAndPrice productNameAndPrice, GeneralStock generalStock) {
        if(productInformationMap.containsKey(productNameAndPrice)) {
            return productInformationMap.get(productNameAndPrice).addProductInformation(generalStock);
        }
        ProductInformation productInformation =
                new ProductInformation(generalStock);
        return productInformationMap.put(productNameAndPrice, productInformation);
    }
}
