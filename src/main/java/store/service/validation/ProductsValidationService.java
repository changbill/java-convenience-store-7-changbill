package store.service.validation;

import static store.exception.ProductsFileExceptionMessage.PRODUCTS_FILE_EXAMPLE;
import static store.exception.ProductsFileExceptionMessage.PRODUCTS_FILE_MAX_PRICE_EXCEPTION;
import static store.exception.ProductsFileExceptionMessage.PRODUCTS_FILE_NAME_FORMAT_EXCEPTION;

import java.util.List;
import store.exception.WrongProductsFileException;
import store.util.ValidationUtil;

public class ProductsValidationService {

    public void validateProductInformations(List<String> rawProductInformation) {
        if (rawProductInformation.size() != 4) {
            throw new WrongProductsFileException(PRODUCTS_FILE_EXAMPLE);
        }

        ValidationUtil.validateFormatHangul
                (rawProductInformation.get(0), new WrongProductsFileException(PRODUCTS_FILE_NAME_FORMAT_EXCEPTION));
        validateProductInformationPrice(rawProductInformation.get(1));
        validateProductInformationQuantity(rawProductInformation.get(2));
        validateProductInformationPromotion(rawProductInformation.get(3));
    }

    private void validateProductInformationPrice(String rawPrice) {
        ValidationUtil.validateLong(rawPrice, new WrongProductsFileException(PRODUCTS_FILE_MAX_PRICE_EXCEPTION));
    }

    private void validateProductInformationQuantity(String rawQuantity) {
        ValidationUtil.validateLong(rawQuantity, new WrongProductsFileException(PRODUCTS_FILE_MAX_PRICE_EXCEPTION));
    }

    private void validateProductInformationPromotion(String rawPromotion) {
        ValidationUtil.validateNull(rawPromotion, new WrongProductsFileException(PRODUCTS_FILE_EXAMPLE));
    }
}
