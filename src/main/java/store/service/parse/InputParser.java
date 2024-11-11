package store.service.parse;

import static store.exception.InputExceptionMessage.INPUT_PRODUCT_NAME_AND_COUNT_EXCEPTION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import store.exception.InputException;
import store.repository.ProductInformationRepository;
import store.service.validation.InputValidationService;
import store.model.dto.ProductOrderDto;
import store.util.ValidationUtil;

public class InputParser {
    private final ProductInformationRepository productInformationRepository;
    private final InputValidationService inputValidationService;

    public InputParser(ProductInformationRepository productInformationRepository, InputValidationService inputValidationService) {
        this.productInformationRepository = productInformationRepository;
        this.inputValidationService = inputValidationService;
    }

    public List<ProductOrderDto> parseToProductOrderDto(String rawPurchaseOrder) {
        List<String> splitedPurchaseOrder = getSplitedPurchaseOrder(rawPurchaseOrder);

        return getProductOrders(
                splitedPurchaseOrder.stream()
                        .map(text -> text.substring(1, text.length() - 1))
                        .toList()
        );
    }

    private List<String> getSplitedPurchaseOrder(String rawPurchaseOrder) {
        ValidationUtil.validateNull(rawPurchaseOrder, new InputException(INPUT_PRODUCT_NAME_AND_COUNT_EXCEPTION));

        List<String> productOrders = Arrays.stream(rawPurchaseOrder.split(",")).toList();
        inputValidationService.validateProductOrders(productOrders);
        return productOrders;
    }

    private List<ProductOrderDto> getProductOrders(List<String> productTrimmedOrders) {
        List<ProductOrderDto> productOrders = new ArrayList<>();
        for (String productTrimmedText : productTrimmedOrders) {
            List<String> nameAndQuantity = Arrays.stream(productTrimmedText.split("-")).toList();
            String name = getName(nameAndQuantity);
            long quantity = getQuantity(nameAndQuantity);

            productOrders.add(ProductOrderDto.of(name, quantity));
        }
        return productOrders;
    }

    private static String getName(List<String> nameAndQuantity) {
        ValidationUtil.validateFormatHangul
                (nameAndQuantity.get(0), new InputException(INPUT_PRODUCT_NAME_AND_COUNT_EXCEPTION));
        String name = nameAndQuantity.get(0);
        return name;
    }

    private long getQuantity(List<String> nameAndQuantity) {
        ValidationUtil.validateLong(nameAndQuantity.get(1), new InputException(
                INPUT_PRODUCT_NAME_AND_COUNT_EXCEPTION));
        long quantity = Long.parseLong(nameAndQuantity.get(1));
        inputValidationService.validateMaxQuantity(quantity);
        return quantity;
    }
}
