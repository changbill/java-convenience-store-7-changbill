package store.service.parse;

import java.util.List;
import store.model.administrator.GeneralStock;
import store.model.administrator.ProductInformation;
import store.model.administrator.ProductNameAndPrice;
import store.model.administrator.PromotionInformation;
import store.model.administrator.PromotionStock;
import store.repository.StoreRepository;
import store.service.validation.ProductsValidationService;
import store.util.ParseUtil;

public class ProductsParser {
    private final StoreRepository storeRepository;
    private final ProductsValidationService productsValidationService;

    public ProductsParser(StoreRepository storeRepository, ProductsValidationService productsValidationService) {
        this.storeRepository = storeRepository;
        this.productsValidationService = productsValidationService;
    }

    public ProductInformation parseToProductInformation(List<String> rawProductInformation) {
        productsValidationService.validateProductInformations(rawProductInformation);
        String name = rawProductInformation.get(0);
        long price = ParseUtil.parseToLong(rawProductInformation.get(1));
        long quantity = ParseUtil.parseToLong(rawProductInformation.get(2));
        String rawPromotion = rawProductInformation.get(3);

        // TODO: 만약 상품명, 가격, stock 종류까지 같은 경우 중복 오류 잡아낼 것
        ProductNameAndPrice productNameAndPrice = ProductNameAndPrice.of(name, price);
        if(rawPromotion.equals("null")) {
            return storeRepository.addProductInformation(productNameAndPrice, GeneralStock.of(quantity));
        }

        PromotionInformation promotionInformation = storeRepository.findPromotionInformation(rawPromotion);
        return storeRepository.addProductInformation(productNameAndPrice, PromotionStock.of(quantity, promotionInformation));
    }
}
