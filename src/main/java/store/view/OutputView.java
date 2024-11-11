package store.view;

import java.util.List;
import store.model.dto.ReceiptDto;
import store.util.ParseUtil;

public class OutputView {
    private static final String GREET_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String STOCK_INTRODUCTION_MESSAGE = "현재 보유하고 있는 상품입니다.";
    private static final String RECEIPT_HEADER = "===========W 편의점============="
            + "\n"
            + "상품명		수량	금액";
    private static final String RECEIPT_GIVEAWAY_PRODUCT = "===========증	정=============";
    private static final String RECEIPT_RESULT = "==============================";
    private static final String TOTAL_PRICE = "총구매액";
    private static final String EVENT_DISCOUNT = "행사할인";
    private static final String MEMBERSHIP_DISCOUNT = "멤버십할인";
    private static final String MONEY_TO_PAY = "내실돈";

    private static final String BLANK = "		";




    public void printReceipt(List<ReceiptDto> receipts, boolean isMembership) {
        StringBuilder stringBuilder = new StringBuilder();
        long totalQuantity = 0;
        long totalPrice = 0;
        long totalDiscount = 0;
        long totalMembershipDiscount = 0;

        stringBuilder.append(RECEIPT_HEADER).append("\n");
        receipts.forEach(receipt -> stringBuilder.append(receipt.toReceiptString()).append("\n"));

        if(receipts.stream().anyMatch(receipt -> receipt.resultBenefit() != 0)) {
            stringBuilder.append(RECEIPT_GIVEAWAY_PRODUCT).append("\n");
            receipts.forEach(receipt -> {
                if(receipt.resultBenefit() != 0) {
                    stringBuilder.append(receipt.toGiveawayString()).append("\n");
                }
            });
        }

        stringBuilder.append(RECEIPT_RESULT).append("\n");
        for(ReceiptDto receipt : receipts) {
            totalQuantity += receipt.orderQuantity();
            totalPrice += receipt.orderQuantity() * receipt.productPrice();
            if(receipt.resultBenefit() > 0) {
                totalDiscount += receipt.resultBenefit() * receipt.productPrice();
            } else if(isMembership && receipt.resultBenefit() == 0) {
                totalMembershipDiscount += (long) ((double) receipt.productPrice() * 0.3);
            }
        }

        if(totalMembershipDiscount > 8000) {
            totalMembershipDiscount = 8000;
        }
        
        stringBuilder.append(TOTAL_PRICE).append(BLANK)
                .append(ParseUtil.thousandDelimiter(totalQuantity)).append(BLANK)
                .append(ParseUtil.thousandDelimiter(totalPrice)).append("\n");
        stringBuilder.append(EVENT_DISCOUNT).append(BLANK).append(BLANK)
                .append(ParseUtil.thousandDelimiter(-1 * totalDiscount)).append("\n");
        stringBuilder.append(MEMBERSHIP_DISCOUNT).append(BLANK).append(BLANK)
                .append(ParseUtil.thousandDelimiter(-1 * totalMembershipDiscount)).append("\n");
        stringBuilder.append(MONEY_TO_PAY).append(BLANK).append(BLANK)
                .append(ParseUtil.thousandDelimiter(totalPrice - totalDiscount - totalMembershipDiscount))
                .append("\n");

        System.out.println(stringBuilder);
    }

    public void printIntroductionMessage(List<String> messages) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(GREET_MESSAGE).append("\n")
                                .append(STOCK_INTRODUCTION_MESSAGE).append("\n")
                .append("\n");
        for (String message : messages) {
            stringBuilder.append(message).append("\n");
        }
        System.out.println(stringBuilder);
    }
}
