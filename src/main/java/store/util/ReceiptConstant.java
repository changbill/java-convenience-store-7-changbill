package store.util;

public enum ReceiptConstant {
    GREET_MESSAGE("안녕하세요. W편의점입니다."),
    STOCK_INTRODUCTION_MESSAGE("현재 보유하고 있는 상품입니다."),
    RECEIPT_HEADER("===========W 편의점============="
            + "\n"
            + "상품명		수량	금액"),
    RECEIPT_GIVEAWAY_PRODUCT("===========증	정============="),
    RECEIPT_RESULT("=============================="),
    TOTAL_PRICE("총구매액"),
    EVENT_DISCOUNT("행사할인"),
    MEMBERSHIP_DISCOUNT("멤버십할인"),
    MONEY_TO_PAY("내실돈"),
    RECEIPT_BLANK("		");

    private final String message;

    ReceiptConstant(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
