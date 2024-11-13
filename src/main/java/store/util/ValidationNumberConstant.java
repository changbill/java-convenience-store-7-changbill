package store.util;

public enum ValidationNumberConstant {
    INPUT_MAX_QUANTITY(100),
    PRICE_UNIT(100),
    PRODUCTS_FILE_MAX_PRICE(1_000_000_000),
    PRODUCTS_FILE_MAX_QUANTITY(1_000);

    private final long value;

    ValidationNumberConstant(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
