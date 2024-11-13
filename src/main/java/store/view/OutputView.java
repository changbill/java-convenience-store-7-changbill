package store.view;

import static store.util.ReceiptConstant.EVENT_DISCOUNT;
import static store.util.ReceiptConstant.GREET_MESSAGE;
import static store.util.ReceiptConstant.MEMBERSHIP_DISCOUNT;
import static store.util.ReceiptConstant.MONEY_TO_PAY;
import static store.util.ReceiptConstant.RECEIPT_BLANK;
import static store.util.ReceiptConstant.RECEIPT_GIVEAWAY_PRODUCT;
import static store.util.ReceiptConstant.RECEIPT_HEADER;
import static store.util.ReceiptConstant.RECEIPT_RESULT;
import static store.util.ReceiptConstant.STOCK_INTRODUCTION_MESSAGE;
import static store.util.ReceiptConstant.TOTAL_PRICE;

import java.util.List;
import store.model.dto.ReceiptDto;
import store.util.ParseUtil;

public class OutputView {

    public void printReceipt(List<ReceiptDto> receipts, boolean isMembership) {
        StringBuilder stringBuilder = new StringBuilder();
        long totalQuantity = 0;
        long totalPrice = 0;
        long totalDiscount = 0;
        long totalMembershipDiscount = 0;

        stringBuilder.append(RECEIPT_HEADER.getMessage()).append("\n");
        receipts.forEach(receipt -> stringBuilder.append(receipt.toReceiptString()).append("\n"));

        if(receipts.stream().anyMatch(receipt -> receipt.resultBenefit() != 0)) {
            stringBuilder.append(RECEIPT_GIVEAWAY_PRODUCT.getMessage()).append("\n");
            receipts.forEach(receipt -> {
                if(receipt.resultBenefit() != 0) {
                    stringBuilder.append(receipt.toGiveawayString()).append("\n");
                }
            });
        }

        stringBuilder.append(RECEIPT_RESULT.getMessage()).append("\n");
        for(ReceiptDto receipt : receipts) {
            totalQuantity += receipt.orderQuantity();
            totalPrice += receipt.orderQuantity() * receipt.productPrice();
            if(receipt.resultBenefit() > 0) {
                totalDiscount += receipt.resultBenefit() * receipt.productPrice();
            }

            if(isMembership) {
                totalMembershipDiscount += (long) ((double) (receipt.orderQuantity() - receipt.discountedQuantity()) * (double) receipt.productPrice() * 0.3);
            }
        }

        if(totalMembershipDiscount > 8000) {
            totalMembershipDiscount = 8000;
        }

        stringBuilder.append(
                TOTAL_PRICE.getMessage()
                        + RECEIPT_BLANK.getMessage()
                        + ParseUtil.thousandDelimiter(totalQuantity)
                        + RECEIPT_BLANK.getMessage()
                        + ParseUtil.thousandDelimiter(totalPrice)
                        + "\n"
        );
        stringBuilder.append(
                EVENT_DISCOUNT.getMessage()
                        + RECEIPT_BLANK.getMessage()
                        + RECEIPT_BLANK.getMessage()
                        + ParseUtil.thousandDelimiter(-1 * totalDiscount)
                        + "\n"
        );
        stringBuilder.append(
                MEMBERSHIP_DISCOUNT.getMessage()
                        + RECEIPT_BLANK.getMessage()
                        + RECEIPT_BLANK.getMessage()
                        + ParseUtil.thousandDelimiter(-1 * totalMembershipDiscount)
                        + "\n"
        );
        stringBuilder.append(
                MONEY_TO_PAY.getMessage()
                        + RECEIPT_BLANK.getMessage()
                        + RECEIPT_BLANK.getMessage()
                        + ParseUtil.thousandDelimiter(totalPrice - totalDiscount - totalMembershipDiscount)
                        + "\n"
        );

        System.out.println(stringBuilder);
    }

    public void printIntroductionMessage(List<String> messages) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(GREET_MESSAGE.getMessage() + "\n"
                        + STOCK_INTRODUCTION_MESSAGE.getMessage() + "\n" + "\n");
        for (String message : messages) {
            stringBuilder.append(message + "\n");
        }
        System.out.println(stringBuilder);
    }
}
