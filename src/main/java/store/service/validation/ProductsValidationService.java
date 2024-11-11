package store.service.validation;

import static store.exception.ProductsFileExceptionMessage.PRODUCTS_FILE_EXAMPLE;
import static store.exception.ProductsFileExceptionMessage.PRODUCTS_FILE_MAX_PRICE_EXCEPTION;
import static store.exception.ProductsFileExceptionMessage.PRODUCTS_FILE_NAME_FORMAT_EXCEPTION;
import static store.exception.ProductsFileExceptionMessage.PRODUCTS_FILE_PRICE_UNIT_EXCEPTION;
import static store.exception.ProductsFileExceptionMessage.PRODUCTS_FILE_QUANTITY_RANGE_EXCEPTION;
import static store.exception.ProductsFileExceptionMessage.PRODUCTS_FILE_WRONG_PROMOTION_EXCEPTION;

import java.util.List;
import store.exception.ProductsFileException;
import store.repository.PromotionInformationRepository;
import store.util.ValidationUtil;

public class ProductsValidationService {
    private final PromotionInformationRepository promotionInformationRepository;

    public ProductsValidationService(PromotionInformationRepository promotionInformationRepository) {
        this.promotionInformationRepository = promotionInformationRepository;
    }

    public void validateProductInformations(List<String> rawProductInformation) {
        if (rawProductInformation.size() != 4) {
            throw new ProductsFileException(PRODUCTS_FILE_EXAMPLE);
        }

        ValidationUtil.validateFormatHangul
                (rawProductInformation.get(0), new ProductsFileException(PRODUCTS_FILE_NAME_FORMAT_EXCEPTION));
        validateProductInformationPrice(rawProductInformation.get(1));
        validateProductInformationQuantity(rawProductInformation.get(2));
        validateProductInformationPromotion(rawProductInformation.get(3));
    }

    private void validateProductInformationPrice(String rawPrice) {
        ValidationUtil.validateNull(rawPrice, new ProductsFileException(PRODUCTS_FILE_EXAMPLE));
        try {
            long price = Long.parseLong(rawPrice);
            if (price >= 1_000_000_000) {
                throw new ProductsFileException(PRODUCTS_FILE_MAX_PRICE_EXCEPTION);
            }
            if (price % 100 != 0) {
                throw new ProductsFileException(PRODUCTS_FILE_PRICE_UNIT_EXCEPTION);
            }
        } catch (NumberFormatException e) {
            throw new ProductsFileException(PRODUCTS_FILE_EXAMPLE);
        }
    }

    private void validateProductInformationQuantity(String rawQuantity) {
        ValidationUtil.validateNull(rawQuantity, new ProductsFileException(PRODUCTS_FILE_EXAMPLE));
        try {
            long quantity = Long.parseLong(rawQuantity);
            if (quantity >= 1_000) {
                throw new ProductsFileException(PRODUCTS_FILE_QUANTITY_RANGE_EXCEPTION);
            }
        } catch (NumberFormatException e) {
            throw new ProductsFileException(PRODUCTS_FILE_EXAMPLE);
        }
    }

    private void validateProductInformationPromotion(String rawPromotion) {
        ValidationUtil.validateNull(rawPromotion, new ProductsFileException(PRODUCTS_FILE_EXAMPLE));
        if (promotionInformationRepository.findPromotionInformation(rawPromotion) == null &&
                !rawPromotion.equals("null")) {
            throw new ProductsFileException(PRODUCTS_FILE_WRONG_PROMOTION_EXCEPTION);
        }
    }
}
