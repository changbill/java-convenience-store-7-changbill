package store.util;

public enum ValidationConstant {
    INPUT_MAX_QUANTITY(100),
    MINIMUM_PRICE_UNIT(100),
    PRODUCTS_FILE_MAX_PRICE(1_000_000_000),
    PRODUCTS_FILE_MAX_QUANTITY(1_000);

    private final long value;

    ValidationConstant(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
