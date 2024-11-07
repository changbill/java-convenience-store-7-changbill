package store.exception;

public class ProductsFileExceptionMessage {
    public static final String PRODUCTS_FILE_SAME_NAME_DIFFERENT_PRICE_EXCEPTION = "이미 같은 이름의 상품이 존재합니다.";
    public static final String PRODUCTS_FILE_NAME_FORMAT_EXCEPTION = "이름은 한글 형식으로 적어주세요.";
    public static final String PRODUCTS_FILE_MAX_PRICE_EXCEPTION = "1,000,000,000원 미만의 가격을 적어주세요.";
    public static final String PRODUCTS_FILE_PRICE_UNIT_EXCEPTION = "가격은 100원 단위로 적어주세요.";
    public static final String PRODUCTS_FILE_QUANTITY_RANGE_EXCEPTION = "1,000개 미만으로 등록해주세요.";
    public static final String PRODUCTS_FILE_WRONG_PROMOTION_EXCEPTION = "해당하는 프로모션이 없습니다.";
    public static final String PRODUCTS_FILE_EXAMPLE = "name,price,quantity,promotion 형식으로 된 파일을 등록해주세요. (예: 탄산수,1200,5,탄산2+1)";

    private ProductsFileExceptionMessage() {}
}
