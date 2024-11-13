package store.exception;

public class InputExceptionMessage {
    public static final String INPUT_PRODUCT_NAME_AND_COUNT_EXCEPTION = "올바르지 않은 형식으로 입력했습니다.";
    public static final String INPUT_PRODUCT_WRONG_NAME_EXCEPTION = "존재하지 않는 상품입니다.";
    public static final String MAX_QUANTITY_IS_100_EXCEPTION = "최대 입력치는 100 입니다.";
    public static final String INPUT_Y_OR_N_EXCEPTION = "잘못된 입력입니다.";
    public static final String INPUT_MORE_THEN_STOCK_EXCEPTION = "재고 수량을 초과하여 구매할 수 없습니다.";

    private InputExceptionMessage() {}
}
