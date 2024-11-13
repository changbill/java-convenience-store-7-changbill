package store.util;

public enum ValidationStringConstant {
    PROMOTION_INFORMATION_EMPTY("null");

    private final String value;

    ValidationStringConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
