package store.service.validation;

import static store.exception.InputExceptionMessage.INPUT_PRODUCT_NAME_AND_COUNT_EXCEPTION;
import static store.exception.InputExceptionMessage.MAX_QUANTITY_IS_100_EXCEPTION;

import java.util.List;
import store.exception.InputException;

public class InputValidationService {

    public void validateProductOrders(List<String> productOrders) {
        if (!productOrders.stream()
                .allMatch(order -> order.matches("\\[(\\p{IsHangul}+)-(\\d+)]"))) {
            throw new InputException(INPUT_PRODUCT_NAME_AND_COUNT_EXCEPTION);
        }
    }

    public void validateMaxQuantity(long quantity) {
        if (quantity > 100) {
            throw new InputException(MAX_QUANTITY_IS_100_EXCEPTION);
        }
    }
}
