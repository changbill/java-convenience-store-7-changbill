package store.repository;

import static store.exception.PromotionsFileExceptionMessage.PROMOTIONS_FILE_SAME_NAME_DIFFERENT_PRICE_EXCEPTION;

import java.util.HashMap;
import store.exception.WrongPromotionsFileException;
import store.model.administrator.GeneralStock;
import store.model.administrator.ProductInformation;
import store.model.administrator.ProductNameAndPrice;
import store.model.administrator.PromotionInformation;
import store.model.administrator.PromotionStock;
import store.model.administrator.Stock;

public class StoreRepository {
    private final HashMap<ProductNameAndPrice, ProductInformation> productInformationHashMap = new HashMap<>();
    private final HashMap<String, PromotionInformation> promotionHashMap = new HashMap<>();

    public PromotionInformation findPromotionInformation(String rawPromotion) {
        return promotionHashMap.get(rawPromotion);
    }

    public PromotionInformation addPromotionInformation(String promotionName, PromotionInformation promotionInformation) {
        if(promotionHashMap.containsKey(promotionName)) {
            throw new WrongPromotionsFileException(PROMOTIONS_FILE_SAME_NAME_DIFFERENT_PRICE_EXCEPTION);
        }
        return promotionHashMap.put(promotionName, promotionInformation);
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
        if (productInformationHashMap.containsKey(productNameAndPrice)) {
            return productInformationHashMap.get(productNameAndPrice);
        }
        return null;
    }

    private ProductInformation storeProductInformation(ProductNameAndPrice productNameAndPrice, PromotionStock promotionStock) {
        if(productInformationHashMap.containsKey(productNameAndPrice)) {
            return productInformationHashMap.get(productNameAndPrice).addProductInformation(promotionStock);
        }
        ProductInformation productInformation =
                new ProductInformation(promotionStock);
        return productInformationHashMap.put(productNameAndPrice, productInformation);
    }

    private ProductInformation storeProductInformation(ProductNameAndPrice productNameAndPrice, GeneralStock generalStock) {
        if(productInformationHashMap.containsKey(productNameAndPrice)) {
            return productInformationHashMap.get(productNameAndPrice).addProductInformation(generalStock);
        }
        ProductInformation productInformation =
                new ProductInformation(generalStock);
        return productInformationHashMap.put(productNameAndPrice, productInformation);
    }
}
