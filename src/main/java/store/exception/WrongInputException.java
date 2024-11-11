package store.exception;

public class WrongInputException extends IllegalArgumentException {
    public WrongInputException(String message) {
        super("[ERROR] " + message + " 다시 입력해 주세요.");
    }
}
