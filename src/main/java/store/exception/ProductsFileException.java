package store.exception;

public class ProductsFileException extends IllegalArgumentException {
    public ProductsFileException(String message) {
        super("[ERROR] 상품 목록 파일의 형식이 잘못되었습니다. " + message);
    }
}
