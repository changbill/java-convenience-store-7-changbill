package store.exception;

public class PromotionsFileExceptionMessage {
    public static final String PROMOTIONS_FILE_SAME_NAME_DIFFERENT_PRICE_EXCEPTION = "이미 같은 이름의 행사가 존재합니다.";
    public static final String PROMOTIONS_FILE_EXAMPLE = "name,buy,get,start_date,end_date 형식으로 된 파일을 등록해주세요. (예: 탄산2+1,2,1,2024-01-01,2024-12-31)";
    public static final String PROMOTIONS_NAME_FORMAT_EXCEPTION = "'{상품명}2+1', '{상품명}1+1', 'MD추천상품', '반짝할인'과 같은 행사명을 등록해주세요.";
    public static final String PROMOTIONS_WRONG_BUY_GET_EXCEPTION = "buy와 get을 올바르게 등록해주세요.";

    private PromotionsFileExceptionMessage() {}
}
