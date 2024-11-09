package store.exception;

public class InputExceptionMessage {
    public static final String INPUT_PRODUCT_NAME_AND_COUNT_EXCEPTION = "예시와 같이 입력해주세요. (예: [사이다-2],[감자칩-1])";
    public static final String INPUT_PRODUCT_WRONG_NAME_EXCEPTION = "등록되지 않은 상품명입니다.";
    public static final String MAX_QUANTITY_IS_100_EXCEPTION = "최대 입력치는 100 입니다.";
    public static final String INPUT_Y_OR_N_EXCEPTION = "Y 또는 N을 입력해주세요.";
    public static final String INPUT_MORE_THEN_STOCK_EXCEPTION = "재고보다 많은 숫자를 입력했습니다.";

    private InputExceptionMessage() {}
}
