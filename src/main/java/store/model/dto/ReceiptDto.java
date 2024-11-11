package store.model.dto;

import store.util.ParseUtil;

public record ReceiptDto(
        String productName,
        long orderQuantity,
        long productPrice,
        long resultBenefit,
        long discountedQuantity
) {
    public String toReceiptString() {
        return productName + "		"
                + ParseUtil.thousandDelimiter(orderQuantity) + " 	"
                + ParseUtil.thousandDelimiter(productPrice * orderQuantity);
    }

    public String toGiveawayString() {
        return productName + "		"
                + ParseUtil.thousandDelimiter(resultBenefit);
    }
}
