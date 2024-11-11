package store.util;

public enum ReceiptConstant {
    RECEIPT_BLANK("		");

    private final String message;

    ReceiptConstant(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
