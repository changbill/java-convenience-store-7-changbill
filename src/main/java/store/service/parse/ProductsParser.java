package store.service.parse;

import java.util.List;
import store.model.stock.GeneralStock;
import store.model.ProductInformation;
import store.model.setup.PromotionInformation;
import store.model.stock.PromotionStock;
import store.repository.ProductInformationRepository;
import store.repository.PromotionInformationRepository;
import store.service.validation.ProductsValidationService;
import store.util.ParseUtil;

public class ProductsParser {
    private final ProductInformationRepository productInformationRepository;
    private final PromotionInformationRepository promotionInformationRepository;
    private final ProductsValidationService productsValidationService;

    public ProductsParser(
            ProductInformationRepository productInformationRepository,
            PromotionInformationRepository promotionInformationRepository,
            ProductsValidationService productsValidationService
    ) {
        this.productInformationRepository = productInformationRepository;
        this.promotionInformationRepository = promotionInformationRepository;
        this.productsValidationService = productsValidationService;
    }

    public ProductInformation parseToProductInformation(List<String> rawProductInformation) {
        productsValidationService.validateProductInformations(rawProductInformation);
        String name = rawProductInformation.get(0);
        long price = ParseUtil.parseToLong(rawProductInformation.get(1));
        long quantity = ParseUtil.parseToLong(rawProductInformation.get(2));
        String rawPromotion = rawProductInformation.get(3);

        // TODO: 만약 상품명, 가격, stock 종류까지 같은 경우 중복 오류 잡아낼 것
        if(rawPromotion.equals("null")) {
            return productInformationRepository.addProductInformation(name, price, GeneralStock.of(quantity));
        }

        PromotionInformation promotionInformation = promotionInformationRepository.findPromotionInformation(rawPromotion);
        return productInformationRepository.addProductInformation(name, price, PromotionStock.of(quantity, promotionInformation));
    }
}
