package store.model.dto;

import store.util.ParseUtil;

import static store.util.ReceiptConstant.RECEIPT_BLANK;

public record ReceiptDto(
        String productName,
        long orderQuantity,
        long productPrice,
        long resultBenefit,
        long discountedQuantity
) {
    public String toReceiptString() {
        return productName + RECEIPT_BLANK.getMessage()
                + ParseUtil.thousandDelimiter(orderQuantity) + RECEIPT_BLANK.getMessage()
                + ParseUtil.thousandDelimiter(productPrice * orderQuantity);
    }

    public String toGiveawayString() {
        return productName + RECEIPT_BLANK.getMessage()
                + ParseUtil.thousandDelimiter(resultBenefit);
    }
}
