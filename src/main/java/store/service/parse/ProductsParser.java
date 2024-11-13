package store.service.parse;

import static store.exception.ProductsFileExceptionMessage.PRODUCTS_FILE_WRONG_PROMOTION_EXCEPTION;
import static store.util.ValidationStringConstant.PROMOTION_INFORMATION_EMPTY;

import java.util.List;
import store.exception.ProductsFileException;
import store.model.stock.GeneralStock;
import store.model.ProductInformation;
import store.model.setup.PromotionInformation;
import store.model.stock.PromotionStock;
import store.repository.ProductInformationRepository;
import store.repository.PromotionInformationRepository;
import store.service.validation.ProductsValidationService;
import store.util.ParseUtil;

public class ProductsParser {
    private static final int NAME_INDEX = 0;
    private static final int PRICE_INDEX = 1;
    private static final int QUANTITY_INDEX = 2;
    private static final int PROMOTION_INDEX = 3;

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
        String name = rawProductInformation.get(NAME_INDEX);
        long price = ParseUtil.parseToLong(rawProductInformation.get(PRICE_INDEX));
        long quantity = ParseUtil.parseToLong(rawProductInformation.get(QUANTITY_INDEX));
        String rawPromotion = rawProductInformation.get(PROMOTION_INDEX);

        if(rawPromotion.equals(PROMOTION_INFORMATION_EMPTY.getValue())) {
            return productInformationRepository.addProductInformation(name, price, GeneralStock.of(quantity));
        }

        PromotionInformation promotionInformation = promotionInformationRepository.findPromotionInformation(rawPromotion);
        if(promotionInformation == null) {  // 상품 등록 시 찾는 프로모션이 없을 경우
            throw new ProductsFileException(PRODUCTS_FILE_WRONG_PROMOTION_EXCEPTION);
        }

        if(promotionInformation.isAvailablePromotion()) {   // 상품 등록 시 프로모션이 해당 날짜에 유효하다면
            return productInformationRepository.addProductInformation(
                    name,
                    price,
                    PromotionStock.of(quantity, promotionInformation)
            );
        }

        return productInformationRepository.addProductInformation(name, price, GeneralStock.of(quantity));
    }
}
