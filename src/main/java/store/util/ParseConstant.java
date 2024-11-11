package store.util;

public enum ParseConstant {
    PRODUCT_ORDER_DELIMITER(","),
    NAME_AND_QUANTITY_DELIMITER("-"),
    START_OF_STOCK_MESSAGE("-"),
    THOUSAND_UNIT_COMMA_FORMAT("%,d"),
    PRICE_UNIT("원"),
    QUANTITY_UNIT("개"),
    EMPTY_STOCK("재고 없음");

    private final String value;

    ParseConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
