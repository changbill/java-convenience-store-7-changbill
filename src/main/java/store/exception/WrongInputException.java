package store.exception;

public class WrongInputException extends IllegalArgumentException {
    public WrongInputException(String message) {
        super("[ERROR] 올바르지 않은 입력값입니다. " + message);
    }
}
