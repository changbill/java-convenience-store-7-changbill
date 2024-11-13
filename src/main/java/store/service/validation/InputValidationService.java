package store.service.validation;

import static store.exception.InputExceptionMessage.INPUT_PRODUCT_NAME_AND_COUNT_EXCEPTION;
import static store.exception.InputExceptionMessage.MAX_QUANTITY_IS_100_EXCEPTION;
import static store.util.ValidationNumberConstant.INPUT_MAX_QUANTITY;
import static store.util.ValidationRegexConstant.HANGUL_AND_NUMBER;

import java.util.List;
import store.exception.InputException;

public class InputValidationService {

    public void validateProductOrders(List<String> productOrders) {
        if (!productOrders.stream()
                .allMatch(order -> order.matches(HANGUL_AND_NUMBER.getRegex()))) {
            throw new InputException(INPUT_PRODUCT_NAME_AND_COUNT_EXCEPTION);
        }
    }

    public void validateMaxQuantity(long quantity) {
        if (quantity > INPUT_MAX_QUANTITY.getValue()) {
            throw new InputException(MAX_QUANTITY_IS_100_EXCEPTION);
        }
    }
}
