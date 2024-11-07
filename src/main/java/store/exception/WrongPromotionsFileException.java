package store.exception;

public class WrongPromotionsFileException extends IllegalArgumentException {
    public WrongPromotionsFileException(String message) {
        super("[ERROR] 프로모션 목록 파일의 형식이 잘못되었습니다. " + message);
    }
}
